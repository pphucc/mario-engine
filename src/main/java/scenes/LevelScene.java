package scenes;

import jade.Window;
import scenes.Scene;

public class LevelScene extends Scene {

    public LevelScene() {
        System.out.println("Insde Level Scene");
        Window.get().r = 1.0f;
        Window.get().g = 1.0f;
        Window.get().b = 1.0f;
    }

    @Override
    public void update(float dt) {
    }

}