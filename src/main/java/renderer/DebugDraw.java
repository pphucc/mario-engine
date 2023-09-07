package renderer;

import jade.Window;
import org.joml.Vector2f;
import org.joml.Vector3f;
import util.AssetPool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class DebugDraw {
    private static int MAX_LINES = 500;

    private static List<Line2D> lines = new ArrayList<>();
    // 6 floats per vertex, 2 vertices per line

    private static float[] vertexArray = new float[MAX_LINES * 6 * 2];
    private static Shader shader = AssetPool.getShader("assets/shaders/debugLine2D.glsl");

    private static int vaoId, vboId;

    private static boolean started = false;

    public static void start() {
        // Generate the vao

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        // Create the vbo and buffer some memory
        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertexArray.length * Float.BYTES, GL_DYNAMIC_DRAW);

        //Enable the vertex array
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        // TODO: SET LINE WIDTH
        glLineWidth(2.0f);

    }

    public static void beginFrame() {
        if (!started) {
            start();
            started = true;
        }

        // Remove dead lines
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).beginFrame() < 0) {
                lines.remove(i);
                i--;
            }
        }
    }

    public static void draw() {
        if (lines.size() <= 0) return;

        int index = 0;
        for (Line2D line : lines) {
            for (int i = 0; i < 2; i++) {
                Vector2f position = i == 0 ? line.getFrom() : line.getTo();
                Vector3f color = line.getColor();

                // Load position
                vertexArray[index] = position.x;
                vertexArray[index + 1] = position.y;
                vertexArray[index + 2] = -10.0f;

                // Load the color
                vertexArray[index + 3] = color.x;
                vertexArray[index + 4] = color.y;
                vertexArray[index + 5] = color.z;
                index += 6;
            }
        }

        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferSubData(GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(vertexArray, 0, lines.size() * 6 * 2));


        // Use our shader
        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().camera.getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().camera.getViewMatrix());

        //Bind the vao
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);


        //Draw the batch
        glDrawArrays(GL_LINES, 0, lines.size());

        // Disable Location
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        //Unbind shader
        shader.detach();

    }

    // ========================================================================
    // Add line2D methods
    // ========================================================================

    public static void addLine2D(Vector2f from, Vector2f to) {
        // TODO: ADD CONSTANTS FOR COMMON COLORS
        addLine2D(from, to, new Vector3f(0, 1, 0), 1);
    }

    public static void addLine2D(Vector2f from, Vector2f to, Vector3f color) {
        // TODO: ADD CONSTANTS FOR COMMON COLORS
        addLine2D(from, to, color, 1);
    }


    public static void addLine2D(Vector2f from, Vector2f to, Vector3f color, int lifetime) {
        if (lines.size() >= MAX_LINES) return;
        DebugDraw.lines.add(new Line2D(from, to, color, lifetime));
    }

    // ========================================================================
    // Add Box2D methods
    // ========================================================================
    public static void addBox2D(Vector2f center, Vector2f dimensions, float rotation) {
        addBox2D(center, dimensions, rotation, new Vector3f(0, 1, 0), 1);
    }

    ;

    public static void addBox2D(Vector2f center, Vector2f dimensions, float rotation, Vector3f color) {
        addBox2D(center, dimensions, rotation, color, 1);
    }

    ;

    public static void addBox2D(Vector2f center, Vector2f dimensions, float rotation, Vector3f color, int lifetime) {
        Vector2f min = new Vector2f(center).sub(new Vector2f(dimensions).div(2.0f));
        Vector2f max = new Vector2f(center).add(new Vector2f(dimensions).div(2.0f));

        Vector2f[] vertices = {
                new Vector2f(min.x, min.y),
                new Vector2f(min.x, max.y),
                new Vector2f(max.x, max.y),
                new Vector2f(max.x, min.y)
        };

        if (rotation != 0.0f) {
            for (Vector2f vert : vertices) {
                rotate(vert, rotation, center);
            }
        }
        addLine2D(vertices[0], vertices[1], color, lifetime);
        addLine2D(vertices[0], vertices[3], color, lifetime);
        addLine2D(vertices[1], vertices[2], color, lifetime);
        addLine2D(vertices[2], vertices[3], color, lifetime);
    }

    // ========================================================================
    // Add Circle2D methods
    // ========================================================================
    public static void addCircle(Vector2f center, float radius) {
        addCircle(center, radius, new Vector3f(0, 1, 0), 1);
    }

    public static void addCircle(Vector2f center, float radius, Vector3f color) {
        addCircle(center, radius, color, 1);

    }

    public static void addCircle(Vector2f center, float radius, Vector3f color, int lifetime) {
        int segments = 50; //The number of sides of the polygon - What make circle "CIRCLE"!
        Vector2f[] vertices = new Vector2f[segments];
        // Calculate vertices for the circle
        float angleIncrement = 2 * (float) Math.PI / segments;
        float currentAngle = 0.0f;

        for (int i = 0; i < segments; i++) {
            float x = center.x + radius * (float) Math.cos(currentAngle);
            float y = center.y + radius * (float) Math.sin(currentAngle);
            vertices[i] = new Vector2f(x, y);
            currentAngle += angleIncrement;
        }

        // Add lines to connect the circle vertices
        for (int i = 0; i < segments - 1; i++) {
            addLine2D(vertices[i], vertices[i + 1], color, lifetime);
        }

        // Connect the last vertex to the first to close the circle
        addLine2D(vertices[segments - 1], vertices[0], color, lifetime);
    }


    public static Vector2f rotate(Vector2f vector, float angle, Vector2f center) {
        float cos = (float) Math.cos(90 + angle);
        float sin = (float) Math.sin(90 + angle);

        // Translate the vector to the origin
        vector.sub(center);

        // Perform the rotation
        float newX = vector.x * cos - vector.y * sin;
        float newY = vector.x * sin + vector.y * cos;

        // Translate the vector back to its original position
        vector.set(newX, newY).add(center);

        return vector;

    }


}
