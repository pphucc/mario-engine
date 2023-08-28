package jade;


import components.Sprite;
import components.SpriteRenderer;
import components.Spritesheet;
import org.joml.Vector2f;
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


        obj1 = new GameObject("O1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
        obj1.addComponent(new SpriteRenderer(sprites.getSprite(0)));
        this.addGameObjectToScene(obj1);

        obj2 = new GameObject("O2", new Transform(new Vector2f(400, 100), new Vector2f(128, 128)));
        obj2.addComponent(new SpriteRenderer(sprites.getSprite(15)));
        this.addGameObjectToScene(obj2);


    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpritesheet("assets/images/spritesheet.png",
                new Spritesheet(AssetPool.getTexture("assets/images/spritesheet.png"),
                        16, 16, 26, 0));

    }

    private int spriteIndex = 0;
    private float spriteFlipTime = 0.2f;
    private float spriteFlipTimeLeft = 0.0f;

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
            spriteFlipTimeLeft -= dt;
            if(spriteFlipTimeLeft <= 0) {
                spriteFlipTimeLeft = spriteFlipTime;
                spriteIndex++;
                if(spriteIndex > 4){
                    spriteIndex = 0;
                }
                obj1.getComponent(SpriteRenderer.class).setSprite(sprites.getSprite(spriteIndex));
            }
            obj1.transform.position.x += 1;
        }

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }
        this.renderer.render();
    }
}
