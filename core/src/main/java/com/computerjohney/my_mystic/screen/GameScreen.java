package com.computerjohney.my_mystic.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Disposable;
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

public class GameScreen extends ScreenAdapter {

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

    public GameScreen(GdxGame game) {
        this.game = game;
//        this.batch = game.getBatch();
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


    }

    public void dispose() {

        for (EntitySystem system : this.engine.getSystems()) {
            if (system instanceof Disposable disposableSystem) {
                disposableSystem.dispose();
            }
        }

    }


}
