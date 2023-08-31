package jade;

import components.Sprite;
import components.SpriteRenderer;
import components.Spritesheet;
import imgui.internal.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.AssetPool;

import static org.lwjgl.glfw.GLFW.*;

public class LevelEditorScene extends Scene {

    GameObject obj1, obj2;
    Spritesheet sprites;

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        loadResources();

        this.camera = new Camera(new Vector2f());

        sprites = AssetPool.getSpritesheet("assets/images/spritesheet.png");

        obj1 = new GameObject("O1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)), 2);
//        obj1.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/images/blendImage1.png"))));
        obj1.addComponent(new SpriteRenderer(new Vector4f(1, 0, 0, 1)));

        this.addGameObjectToScene(obj1);
        this.activeGameobject = obj1;
        obj2 = new GameObject("O2", new Transform(new Vector2f(300, 100), new Vector2f(256, 256)), 3);
        obj2.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/images/blendImage2.png"))));
        this.addGameObjectToScene(obj2);
    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpritesheet("assets/images/spritesheet.png",
                new Spritesheet(AssetPool.getTexture("assets/images/spritesheet.png"),
                        16, 16, 26, 0));

    }

    @Override
    public void update(float dt) {

        if (KeyListener.isKeyPressed(GLFW_KEY_UP)) {
            camera.position.y += 1;
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_DOWN)) {
            camera.position.y -= 1;
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_LEFT)) {
            camera.position.x -= 1;
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_RIGHT)) {
            camera.position.x += 1;
        }

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }
        this.renderer.render();
    }

    @Override
    public void imgui(){
        ImGui.begin("Test window");
        ImGui.text("Random text");
        ImGui.button("Random button");
        ImGui.end();

    }
}
