package com.computerjohney.my_mystic;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.computerjohney.my_mystic.asset.AssetService;

import java.util.HashMap;
import java.util.Map;

public class GdxGame extends Game {

    public static final float WORLD_WIDTH = 16f;//16f;
    public static final float WORLD_HEIGHT = 9f;//9f;
    public static final float UNIT_SCALE = 1f / 16f;

    private Batch batch;
    private OrthographicCamera camera;
    private Viewport viewport;
    private AssetService assetService;
    private GLProfiler glProfiler;
    private FPSLogger fpsLogger;

    private final Map<Class<? extends Screen>, Screen> screenCache = new HashMap<>();

    public void create() {
        // initialize...
        //
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        //batch together the different render calls to AGP
        // render it all at once efficiently
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        this.assetService = new AssetService(new InternalFileHandleResolver());
        // profiler get gfx context passed in
        this.glProfiler = new GLProfiler(Gdx.graphics);
        this.glProfiler.enable();
        this.fpsLogger = new FPSLogger();
        // Cache of screens
        // addScreen(new MyFirstScreen());
        //setScreen( MyFirstScreen.class);

        //passing this for cache and assets
        addScreen(new GameScreen(this));
        setScreen( GameScreen.class);

    }

    public void addScreen(Screen screen) {
        screenCache.put(screen.getClass(), screen);
    }

    public void setScreen(Class<? extends Screen> screenClass) {
        Screen screen = screenCache.get(screenClass);
        if(screen == null) {
            throw new GdxRuntimeException("No screen with class " + screenClass + " found in screen cache HashMap!");
        }
        super.setScreen(screen);
    }

    public void render() {

        glProfiler.reset();

        Gdx.gl.glClearColor(0f, 0f,0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.render();

        Gdx.graphics.setTitle("Mystic Tutorial - Draw Calls = " + glProfiler.getDrawCalls());
        fpsLogger.log();
    }


    public void resize(int width, int height){
        viewport.update(width, height, true);
        super.resize(width, height);

    }

    public void dispose() {
        screenCache.values().forEach(Screen::dispose);
        screenCache.clear();

        this.batch.dispose();
        //super.dispose();
        this.assetService.debugDiagnostics();
        this.assetService.dispose();
    }

    public Batch getBatch() {
        return batch;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public AssetService getAssetService() {
        return assetService;
    }
}
