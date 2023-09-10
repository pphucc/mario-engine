package editor;

import components.NonPickable;
import components.Rigidbody;
import imgui.ImGui;
import jade.GameObject;
import jade.MouseListener;
import physics2d.components.Box2DCollider;
import physics2d.components.CircleCollider;
import physics2d.components.Rigidbody2D;
import renderer.PickingTexture;
import scenes.Scene;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class PropertiesWindow {
    private GameObject activeGameobject = null, currentActive = null;
    private PickingTexture pickingTexture;

    private float debounce = 0.5f;

    public PropertiesWindow(PickingTexture pickingTexture) {
        this.pickingTexture = pickingTexture;
    }

    public void update(float dt, Scene currentScene) {
        debounce -= dt;
        if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && debounce < 0.0f) {
            int x = (int) MouseListener.getScreenX();
            int y = (int) MouseListener.getScreenY();
            int gameObjectId = pickingTexture.readPixel(x, y);

            GameObject pickedObj = currentScene.getGameObject(gameObjectId);
            if (pickedObj != null && pickedObj.getComponent(NonPickable.class) == null) {
                activeGameobject = pickedObj;
            } else if (pickedObj == null && !MouseListener.isDragging()) {
                activeGameobject = null;
            }
            this.debounce = 0.5f;
        }

    }

    public void imgui() {
        if (activeGameobject != null) {
            ImGui.begin("Properties");

            if (ImGui.beginPopupContextWindow("ComponentAdder")) {
                if (ImGui.menuItem("Add Rigidbody")) {
                    if (activeGameobject.getComponent(Rigidbody2D.class) == null) {
                        activeGameobject.addComponent(new Rigidbody2D());
                    }
                }

                if (ImGui.menuItem("Add Box Collider")) {
                    if (activeGameobject.getComponent(Box2DCollider.class) == null &&
                        activeGameobject.getComponent(CircleCollider.class) == null) {
                        activeGameobject.addComponent(new Box2DCollider());
                    }
                }

                if (ImGui.menuItem("Add Circle Collider")) {
                    if (activeGameobject.getComponent(CircleCollider.class) == null &&
                        activeGameobject.getComponent(Box2DCollider.class) == null) {
                        activeGameobject.addComponent(new CircleCollider());
                    }
                }

                ImGui.endPopup();
            }


            activeGameobject.imgui();
            ImGui.end();
        }
    }

    public GameObject getActiveGameobject() {
        return this.activeGameobject;
    }


}
