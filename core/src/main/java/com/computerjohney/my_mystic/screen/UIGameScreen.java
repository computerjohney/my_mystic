package com.computerjohney.my_mystic.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.computerjohney.my_mystic.GdxGame;
import com.computerjohney.my_mystic.asset.AssetService;
import com.computerjohney.my_mystic.asset.MapAsset;
import com.computerjohney.my_mystic.input.Command;
import com.computerjohney.my_mystic.input.ControllerState;
import com.computerjohney.my_mystic.input.GameControllerState;
import com.computerjohney.my_mystic.input.KeyboardController;
import com.computerjohney.my_mystic.system.AnimationSystem;
import com.computerjohney.my_mystic.system.CameraSystem;
import com.computerjohney.my_mystic.system.ControllerSystem;
import com.computerjohney.my_mystic.system.FacingSystem;
import com.computerjohney.my_mystic.system.FsmSystem;
import com.computerjohney.my_mystic.system.PhysicMoveSystem;
import com.computerjohney.my_mystic.system.PhysicDebugRenderSystem;
import com.computerjohney.my_mystic.system.PhysicSystem;
import com.computerjohney.my_mystic.system.RenderSystem;
import com.computerjohney.my_mystic.tiled.TiledAshleyConfigurator;
import com.computerjohney.my_mystic.tiled.TiledService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class UIGameScreen extends ScreenAdapter {

    // every gamescreen gets 1 of these...
    private final GdxGame game;
    //    private final Batch batch;
//    private final AssetService assetService;
//    private final Viewport viewport;
//    private final OrthographicCamera camera;
    private final Engine engine;
    private final TiledService tiledService;
    private final TiledAshleyConfigurator tiledAshleyConfigurator;
    private final KeyboardController keyboardController;
    private final World physicWorld;

    //private final Stage stage;
    private final Viewport uiViewport;
    private final Camera uiCamera;
    //private final Batch uiBatch;
    //private final Skin skin;
    private final Stage uiStage;



    public UIGameScreen(GdxGame game) {
        this.game = game;
        //this.batch = game.getBatch();
//        this.assetService = game.getAssetService();
//        this.viewport = game.getViewport();
//        this.camera = game.getCamera();

        this.engine = new Engine();
        this.physicWorld = new World(Vector2.Zero, true);
        this.physicWorld.setAutoClearForces(false);  // cleared after step calls in step time loop
        this.tiledService = new TiledService(game.getAssetService(),physicWorld);
        this.tiledAshleyConfigurator = new TiledAshleyConfigurator(this.engine, game.getAssetService(), physicWorld);
        this.keyboardController = new KeyboardController(GameControllerState.class, engine);


        // 1st system executed for every...
        this.engine.addSystem(new ControllerSystem(game));
        this.engine.addSystem(new PhysicMoveSystem());
        // add systems as needed eg. moveSystem, animationSystem, heal, damage etc.
        this.engine.addSystem(new FsmSystem());
        this.engine.addSystem(new FacingSystem());
        this.engine.addSystem(new PhysicSystem(physicWorld, 1/60f));        // our fixed time step!
        this.engine.addSystem(new AnimationSystem(game.getAssetService()));
        this.engine.addSystem(new CameraSystem(game.getCamera()));
        this.engine.addSystem(new RenderSystem(game.getBatch(), game.getViewport(), game.getCamera()));     // sets viewport.apply
        // that updates current game view
        // this must be after Render... expect viewport applied correctly...
        this.engine.addSystem(new PhysicDebugRenderSystem(physicWorld, game.getCamera()));

//        hudStage = new Stage(new FitViewport(200, 200));
//        Skin skin = new Skin(Gdx.files.internal("assets/my_maps2/ui/skin.json")); // Load your UI skin
//
//        TextButton textButton = new TextButton("Hello", skin);
//        //scoreLabel = new Label("Score: 0", skin);
//        textButton.setPosition(10, Gdx.graphics.getHeight() - scoreLabel.getHeight() - 10); // Top-left
//        hudStage.addActor(textButton);
        this.uiCamera = new OrthographicCamera();
        this.uiViewport = new FitViewport(500, 300, uiCamera);
        //this.uiBatch = new SpriteBatch();

        //this.stage = new Stage(uiViewport, game.getBatch());
        this.uiStage = new Stage(uiViewport, game.getBatch());
        //Gdx.input.setInputProcessor(uiStage); // Essential to process input

        // camera debuggin use this zoom...
        //game.getCamera().zoom = 2f;
    }

    @Override
    public void resize(int width, int height) {
        uiViewport.update(width, height, true);
    }


    public void show() {
        //this.engine.getSystem(RenderSystem.class).setMap(this.assetService.get(MapAsset.MAIN));

        // going to set this for keyboard and on screen buttons
        game.setInputProcessors(keyboardController, uiStage);
        keyboardController.setActiveState(GameControllerState.class);

        // use the consumers...
        // now setMap is consumer
        Consumer<TiledMap> renderConsumer = this.engine.getSystem(RenderSystem.class)::setMap;
        Consumer<TiledMap> cameraConsumer = this.engine.getSystem(CameraSystem.class)::setMap;
        this.tiledService.setMapChangeConsumer(renderConsumer.andThen(cameraConsumer));
        // later can add to renderConsumer with .andThen

        this.tiledService.setLoadObjectConsumer(this.tiledAshleyConfigurator::onLoadObject);
        this.tiledService.setLoadTileConsumer(tiledAshleyConfigurator::onLoadTile);

        TiledMap tiledMap = this.tiledService.loadMap(MapAsset.MAIN);
        this.tiledService.setMap(tiledMap);

        //
        //
        // Movement buttons ____________________________________________________________________________________________________
        //
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        Gdx.app.log("Screen Dimensions", "Width: " + screenWidth + ", Height: " + screenHeight);

        float xOffset = 450;
        float yOffset = 90;

        Skin buttonSkin = new Skin(Gdx.files.internal("my_maps2/buttons/buttons.json"));

        ImageButton image_button_left = new ImageButton(buttonSkin.get("button_left-style", ImageButton.ImageButtonStyle.class));
        ImageButton image_button_right = new ImageButton(buttonSkin.get("button_right-style", ImageButton.ImageButtonStyle.class));
        ImageButton image_button_up = new ImageButton(buttonSkin.get("button_up-style", ImageButton.ImageButtonStyle.class));
        ImageButton image_button_down = new ImageButton(buttonSkin.get("button_down-style", ImageButton.ImageButtonStyle.class));

        // think its meant to work by new screen with table filling screen

        Table table1 = new Table();
        Table table2 = new Table();
        Table table3 = new Table();
        Table table4 = new Table();
        table1.setFillParent(false); // Make the table fill the entire stage
        table2.setFillParent(false);
        table3.setFillParent(false);
        table4.setFillParent(false);
        // Add actors to create a grid
        table1.add(image_button_up).minWidth(30).minHeight(30).expand().fill();
        table1.setPosition(xOffset, yOffset);

        table2.add(image_button_left).minWidth(30).minHeight(30).expand().fill();
        table2.setPosition(xOffset-30, yOffset-30);

        table3.add(image_button_right).minWidth(30).minHeight(30).expand().fill();
        table3.setPosition(xOffset+30, yOffset-30);

        table4.add(image_button_down).minWidth(30).minHeight(30).expand().fill();
        table4.setPosition(xOffset, yOffset-60);

        uiStage.addActor(table1);
        uiStage.addActor(table2);
        uiStage.addActor(table3);
        uiStage.addActor(table4);

        // Optional: Enable debug lines to see cell boundaries
//        table1.debug();
//        table2.debug();
//        table3.debug();
//        table4.debug();

//        image_button_left.setPosition( 320, yOffset );
//        image_button_right.setPosition( 360, yOffset );
//        image_button_up.setPosition( xOffset , yOffset +20);
//        image_button_down.setPosition( xOffset , yOffset -20 );
        // This worked ...
        //this.stage.addActor(new Button());
        // Load the PNG texture
        //Texture aTexture = new Texture(Gdx.files.internal("my_maps2/objects/house/house.png"));

        // Create an Image actor from the texture
        //Image aImage = new Image(aTexture);

        // Set position and size (optional, here it fills the screen)
        //aImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //image_button_down.setPosition(300, 50); // Positions the actor at (100, 200) relative to its parent

        //really need a
        //ButtonGroup<ImageButton> buttonGroup = new ButtonGroup<>(image_button_left, image_button_right, image_button_up, image_button_down);
        // or maybe
//        HashMap<String, ImageButton> buttonMap = new HashMap<>();
//        buttonMap.put("button_left", image_button_left);
//        buttonMap.put("button_right", image_button_right);
//        buttonMap.put("button_up", image_button_up);
//        buttonMap.put("button_down", image_button_down);

        //Quills is...
//        private static final Map<Integer, Command> KEY_MAPPING = Map.ofEntries(
//            Map.entry(Input.Keys.W, Command.UP),
//            Map.entry(Input.Keys.S, Command.DOWN),
//            Map.entry(Input.Keys.A, Command.LEFT),
//            Map.entry(Input.Keys.D, Command.RIGHT),
//            Map.entry(Input.Keys.SPACE, Command.SELECT),
//            Map.entry(Input.Keys.ESCAPE, Command.CANCEL)
//        );

        Map<ImageButton, Integer> buttonMap = Map.ofEntries(
            Map.entry(image_button_left, Input.Keys.A),     //"Command.LEFT"
            Map.entry(image_button_right, Input.Keys.D),    //"Command.RIGHT"
            Map.entry(image_button_up, Input.Keys.W),       //"Command.UP"
            Map.entry(image_button_down, Input.Keys.S)      //"Command.DOWN"
        );

        for (Map.Entry<ImageButton, Integer> entry : buttonMap.entrySet()) {
            //String key = entry.getKey();
            ImageButton btn = entry.getKey();
            Integer key = entry.getValue();

            btn.addListener(new InputListener() {

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    // This method is called when a touch/mouse button is pressed down on the actor
                    System.out.println("Touch down on "+ key + " at: " + x + ", " + y);
                    keyboardController.keyDown(key);
                    return true; // Return true to indicate the event was handled
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    // This method is called when a touch/mouse button is released on the actor
                    System.out.println("Touch up on "+ key + " at: " + x + ", " + y);
                    keyboardController.keyUp(key);
                }

            });
        }


    }

    public void hide() {
        this.engine.removeAllEntities();
    }

    public void render(float delta) {
        // clamp delta (taking too long)
        delta = Math.min(delta, 1 / 30f);
        this.engine.update(delta);

        // super simple approach...
//        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
//            System.out.println("W was just pressed");
//        }

        uiViewport.apply();

        uiStage.act(delta);
        uiStage.draw();


    }

    public void dispose() {


        uiStage.dispose();
        for (EntitySystem system : this.engine.getSystems()) {
            if (system instanceof Disposable disposableSystem) {
                disposableSystem.dispose();
            }
        }
        this.physicWorld.dispose();
    }


}
