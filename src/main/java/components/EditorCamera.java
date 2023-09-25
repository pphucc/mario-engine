package components;

import jade.Camera;
import jade.KeyListener;
import jade.MouseListener;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class EditorCamera extends Component {

    private float dragDebounce = 0.032f;

    private Camera levelEditorCamera;
    private Vector2f clickOrigin;
    private boolean reset = false;

    private float lerpTime = 0.0f;

    public float getDragSensitivity() {
        return dragSensitivity;
    }

    public void setDragSensitivity(float dragSensitivity) {
        this.dragSensitivity = dragSensitivity;
    }

    private float dragSensitivity = 10.0f;
    private float scrollSensitivity = 0.1f;

    public EditorCamera(Camera levelEditorCamera) {
        this.levelEditorCamera = levelEditorCamera;
        this.clickOrigin = new Vector2f();
    }


    private boolean isStay = false;

    @Override
    public void editorUpdate(float dt) {


        if (KeyListener.isKeyPressed(GLFW_KEY_LEFT_CONTROL)) {
            isStay = true;
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            isStay = false;
        }

        if (isStay) {
            setDragSensitivity(0);
        } else {
            setDragSensitivity(10.0f);
        }


        if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && dragDebounce > 0) {
            this.clickOrigin = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
            dragDebounce -= dt;
            return;
        } else if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            Vector2f mousePos = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
            Vector2f delta = new Vector2f(mousePos).sub(this.clickOrigin);
            levelEditorCamera.position.sub(delta.mul(dt).mul(dragSensitivity));
            this.clickOrigin.lerp(mousePos, dt);
        }
        if (dragDebounce <= 0.0f && !MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            dragDebounce = 0.032f;
        }

        if (MouseListener.getScrollY() != 0.0f) {
            float addValue = (float) Math.pow(Math.abs(MouseListener.getScrollY() * scrollSensitivity)
                    , 1 / levelEditorCamera.getZoom());
            addValue *= -Math.signum(MouseListener.getScrollY());
            levelEditorCamera.addZoom(addValue / 2);
        }

        if (KeyListener.isKeyPressed(GLFW_KEY_KP_DECIMAL)) {
            reset = true;
        }

        if (reset) {
            levelEditorCamera.position.lerp(new Vector2f(), lerpTime);
            levelEditorCamera.setZoom(this.levelEditorCamera.getZoom()
                    + ((1.0f - levelEditorCamera.getZoom()) * lerpTime));
            this.lerpTime += 0.1f * dt;
            if (Math.abs(levelEditorCamera.position.x) <= 5.0f && Math.abs(levelEditorCamera.position.y) <= 5) {
                this.lerpTime = 0.0f;
                levelEditorCamera.position.set(0.0f, 0.0f);
                this.levelEditorCamera.setZoom(1.0f);
                reset = false;
            }
        }
    }

}
