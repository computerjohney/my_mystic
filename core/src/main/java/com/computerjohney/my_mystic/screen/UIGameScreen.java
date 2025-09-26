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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.computerjohney.my_mystic.GdxGame;
import com.computerjohney.my_mystic.asset.AssetService;
import com.computerjohney.my_mystic.asset.MapAsset;
import com.computerjohney.my_mystic.input.ControllerState;
import com.computerjohney.my_mystic.input.GameControllerState;
import com.computerjohney.my_mystic.input.KeyboardController;
import com.computerjohney.my_mystic.system.AnimationSystem;
import com.computerjohney.my_mystic.system.ControllerSystem;
import com.computerjohney.my_mystic.system.FacingSystem;
import com.computerjohney.my_mystic.system.FsmSystem;
import com.computerjohney.my_mystic.system.MoveSystem;
import com.computerjohney.my_mystic.system.RenderSystem;
import com.computerjohney.my_mystic.tiled.TiledAshleyConfigurator;
import com.computerjohney.my_mystic.tiled.TiledService;

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
        this.tiledService = new TiledService(game.getAssetService());
        this.engine = new Engine();
        this.tiledAshleyConfigurator = new TiledAshleyConfigurator(this.engine, game.getAssetService());
        this.keyboardController = new KeyboardController(GameControllerState.class, engine);

        // 1st system executed for every...
        this.engine.addSystem(new ControllerSystem(game));
        this.engine.addSystem(new MoveSystem());
        // add systems as needed eg. moveSystem, animationSystem, heal, damage etc.
        this.engine.addSystem(new FsmSystem());
        this.engine.addSystem(new FacingSystem());
        this.engine.addSystem(new AnimationSystem(game.getAssetService()));
        this.engine.addSystem(new RenderSystem(game.getBatch(), game.getViewport(), game.getCamera()));

//        hudStage = new Stage(new FitViewport(200, 200));
//        Skin skin = new Skin(Gdx.files.internal("assets/my_maps2/ui/skin.json")); // Load your UI skin
//
//        TextButton textButton = new TextButton("Hello", skin);
//        //scoreLabel = new Label("Score: 0", skin);
//        textButton.setPosition(10, Gdx.graphics.getHeight() - scoreLabel.getHeight() - 10); // Top-left
//        hudStage.addActor(textButton);
        this.uiCamera = new OrthographicCamera();
        this.uiViewport = new FitViewport(400, 300, uiCamera);
        //this.uiBatch = new SpriteBatch();

        //this.stage = new Stage(uiViewport, game.getBatch());
        this.uiStage = new Stage(uiViewport, game.getBatch());
        Gdx.input.setInputProcessor(uiStage); // Essential to process input
    }

    @Override
    public void resize(int width, int height) {
        uiViewport.update(width, height, true);
    }


    public void show() {
        //this.engine.getSystem(RenderSystem.class).setMap(this.assetService.get(MapAsset.MAIN));
        game.setInputProcessors(keyboardController);
        keyboardController.setActiveState(GameControllerState.class);

        // now setMap is consumer
        Consumer<TiledMap> renderConsumer = this.engine.getSystem(RenderSystem.class)::setMap;
        this.tiledService.setMapChangeConsumer(renderConsumer);
        // later can add to renderConsumer with .andThen

        this.tiledService.setLoadObjectConsumer(this.tiledAshleyConfigurator::onLoadObject);

        TiledMap tiledMap = this.tiledService.loadMap(MapAsset.MAIN);
        this.tiledService.setMap(tiledMap);

        Skin buttonSkin = new Skin(Gdx.files.internal("my_maps2/buttons/buttons.json"));
        ImageButton image_button_down = new ImageButton(buttonSkin.get("button_down-style", ImageButton.ImageButtonStyle.class));
        // Create the ImageButton style

        // This worked ...
        //this.stage.addActor(new Button());
        // Load the PNG texture
        //Texture aTexture = new Texture(Gdx.files.internal("my_maps2/objects/house/house.png"));

        // Create an Image actor from the texture
        //Image aImage = new Image(aTexture);

        // Set position and size (optional, here it fills the screen)
        //aImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        image_button_down.setPosition(300, 50); // Positions the actor at (100, 200) relative to its parent

        // Add a ClickListener to the button
        image_button_down.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ImageButton", "Button clicked!");
                // Perform your desired action here
            }
        });

        // Add the Image actor to the stage
        uiStage.addActor(image_button_down);


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

    }


}
