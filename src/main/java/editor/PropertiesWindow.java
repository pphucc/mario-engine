package editor;

import components.SpriteRenderer;
import imgui.ImGui;
import jade.GameObject;
import org.joml.Vector4f;
import physics2d.components.Box2DCollider;
import physics2d.components.CircleCollider;
import physics2d.components.Rigidbody2D;
import renderer.PickingTexture;


import java.util.ArrayList;
import java.util.List;


public class PropertiesWindow {
    private List<GameObject> activeGameObjects;
    private List<Vector4f> activeGameObjectOgColor;
    private GameObject activeGameobject = null, currentActive = null;
    private PickingTexture pickingTexture;


    public PropertiesWindow(PickingTexture pickingTexture) {
        this.activeGameObjects = new ArrayList<>();
        this.pickingTexture = pickingTexture;
        this.activeGameObjectOgColor = new ArrayList<>();
    }

    public void imgui() {
        if (activeGameObjects.size() == 1 && activeGameObjects.get(0) != null) {
            activeGameobject = activeGameObjects.get(0);
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
        return activeGameObjects.size() == 1 ? this.activeGameObjects.get(0) : null;
    }

    public List<GameObject> getActiveGameObjects() {
        return this.activeGameObjects;
    }

    public void clearSelected() {
        if(activeGameObjectOgColor.size() > 0){
            int i = 0;
            for(GameObject go : activeGameObjects){
                SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
                if(spr !=null ){
                    spr.setColor(activeGameObjectOgColor.get(i));

                }
                i++;
            }
        }
        this.activeGameObjects.clear();
        this.activeGameObjectOgColor.clear();
    }

    public void setActiveGameObject(GameObject go) {
        if (go != null) {
            clearSelected();
            this.activeGameObjects.add(go);
        }
    }

    public void addActiveGameObject(GameObject go) {
        SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
        if(spr != null){
            this.activeGameObjectOgColor.add(new Vector4f(spr.getColor()));
            spr.setColor(new Vector4f(0.8f, 0.8f, 0.8f, 0.5f));
        }
        else {
            this.activeGameObjectOgColor.add(new Vector4f());
        }

        this.activeGameObjects.add(go);
    }

    public PickingTexture getPickingTexture() {
        return this.pickingTexture;
    }
}
