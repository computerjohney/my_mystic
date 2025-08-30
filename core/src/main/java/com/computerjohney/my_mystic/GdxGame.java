package com.computerjohney.my_mystic;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.HashMap;
import java.util.Map;

public class GdxGame extends Game {

    public static final float WORLD_WIDTH = 16f;
    public static final float WORLD_HEIGHT = 9f;

    private Batch batch;
    private OrthographicCamera camera;
    private Viewport viewport;

    private final Map<Class<? extends Screen>, Screen> screenCache = new HashMap<>();

        public void create() {
            // initialize...


            //batch together the different render calls to AGP
            // render it all at once efficiently
            this.batch = new SpriteBatch();
            this.camera = new OrthographicCamera();
            this.viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

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

        public void resize(int width, int height){
            viewport.update(width, height, true);
            super.resize(width, height);

        }

        public void dispose() {
            screenCache.values().forEach(Screen::dispose);
            screenCache.clear();

            this.batch.dispose();
            //super.dispose();

        }
}
