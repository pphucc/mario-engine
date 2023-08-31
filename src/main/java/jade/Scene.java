package jade;

import imgui.ImGui;
import renderer.Renderer;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected GameObject activeGameobject = null;


    public Scene() {

    }

    public void init() {

    }
    public void start(){
        for(GameObject go: gameObjects){
            go.start();
            this.renderer.add(go);
        }
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject go) {
        if(!isRunning){
            gameObjects.add(go);

        } else {
            gameObjects.add(go);
            go.start();
            this.renderer.add(go);
        }
    }

    public abstract void update(float dt);

    public Camera camera(){
        return this.camera;
    }

    public void sceneImgui(){
        if(activeGameobject != null){
            ImGui.begin("Inspector");
            activeGameobject.imgui();
            ImGui.end();
        }
        imgui();
    }

    public void imgui(){

    }
}
