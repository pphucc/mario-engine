package editor;

import components.NonPickable;
import imgui.ImGui;
import jade.GameObject;
import jade.MouseListener;
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
            if(pickedObj != null && pickedObj.getComponent(NonPickable.class) == null){
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
            activeGameobject.imgui();
            ImGui.end();
        }
    }

    public GameObject getActiveGameobject(){
        return this.activeGameobject;
    }



}
