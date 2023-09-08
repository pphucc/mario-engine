package editor;

import imgui.ImGui;
import jade.GameObject;
import jade.MouseListener;
import renderer.PickingTexture;
import scenes.Scene;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class PropertiesWindow {
    private GameObject activeGameobject = null, currentActive = null;
    private PickingTexture pickingTexture;

    private float deboundce = 0.2f;

    public PropertiesWindow(PickingTexture pickingTexture) {
        this.pickingTexture = pickingTexture;
    }

    public void update(float dt, Scene currentScene) {
        deboundce -= dt;
        if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && deboundce < 0.0f) {
            int x = (int) MouseListener.getScreenX();
            int y = (int) MouseListener.getScreenY();
            int gameObjectId = pickingTexture.readPixel(x, y);
            activeGameobject = currentScene.getGameObject(gameObjectId);
            this.deboundce = 0.2f;
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
