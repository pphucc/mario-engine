package editor;

import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import jade.GameObject;
import jade.Window;

import java.util.List;

public class SceneHierarchyWindow {

    private static String payloadDragDropType = "SceneHierarchy";

    public void imgui() {
        ImGui.begin("Scene Hierarchy");
        int index = 0;
        List<GameObject> gameObjects = Window.getScene().getGameObjects();
        for (GameObject go : gameObjects) {
            if (!go.doSerialization()) {
                continue;
            }
            boolean treeNodeOpen = doTreeNode(go, index);

            if (treeNodeOpen) {
                ImGui.treePop();
            }
            index++;
        }
        ImGui.end();
    }

    private boolean doTreeNode(GameObject go, int index) {
        ImGui.pushID(index);
        boolean treeNodeOpen = ImGui.treeNodeEx(
                go.name,
                ImGuiTreeNodeFlags.DefaultOpen |
                        ImGuiTreeNodeFlags.FramePadding |
                        ImGuiTreeNodeFlags.OpenOnArrow |
                        ImGuiTreeNodeFlags.SpanAvailWidth,
                go.name
        );
        ImGui.popID();

        if(ImGui.beginDragDropSource()){
            ImGui.setDragDropPayload(payloadDragDropType, go);
            // Put anything here it will appear along with the object when dragging
            ImGui.text(go.name);
            ImGui.endDragDropSource();
        }

        if(ImGui.beginDragDropTarget()){

            Object payloadObj = ImGui.acceptDragDropPayload(payloadDragDropType);
            if(payloadObj != null){
                if(payloadObj.getClass().isAssignableFrom(GameObject.class)){

                    GameObject playerGameObj = (GameObject) payloadObj;

                    //Add code here for the hierarchy logic(parent, children, etc...)
                    System.out.println("Payload accepted '" + playerGameObj.name +"'");
                }
            }
            ImGui.endDragDropTarget();
        }

        return treeNodeOpen;
    }
}
