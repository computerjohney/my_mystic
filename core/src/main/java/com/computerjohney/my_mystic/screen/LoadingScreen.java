package com.computerjohney.my_mystic.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.computerjohney.my_mystic.GdxGame;
import com.computerjohney.my_mystic.asset.AssetService;
import com.computerjohney.my_mystic.asset.AtlasAsset;
import com.computerjohney.my_mystic.asset.SoundAsset;

public class LoadingScreen extends ScreenAdapter {

    // needs access to...
    private final GdxGame game;
    private final AssetService assetService;

    public LoadingScreen(GdxGame game, AssetService assetService) {
        this.game = game;
        this.assetService = assetService;
    }

    // iterate over assets...
    public void show() {
        for (AtlasAsset atlas : AtlasAsset.values()) {
            // async...
            assetService.queue(atlas);
        }
        for (SoundAsset sound : SoundAsset.values()) {
            assetService.queue(sound);
        }
    }

    public void render(float delta) {
        if (this.assetService.update()) {
            // here should get a value to tell how much of loading is already done from update method and getProgress()
            Gdx.app.debug("LoadingScreen", "Finished asset loading");
            createScreens();
            this.game.removeScreen(this);
            this.dispose();
            //this.game.setScreen(UIGameScreen.class);
            //this.game.setScreen(GameScreen.class);
            this.game.setScreen(UIGameScreen.class);
        }
    }

    private void createScreens() {

        //addScreen(new LoadingScreen(this, assetService));
        //this.game.addScreen(new UIGameScreen(this.game));
        //this.game.addScreen(new GameScreen(this.game));
        this.game.addScreen(new UIGameScreen(this.game));
    }

}
