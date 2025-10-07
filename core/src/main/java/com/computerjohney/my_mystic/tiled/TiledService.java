package com.computerjohney.my_mystic.tiled;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.computerjohney.my_mystic.GdxGame;
import com.computerjohney.my_mystic.asset.AssetService;
import com.computerjohney.my_mystic.asset.MapAsset;

import java.util.function.Consumer;

public class TiledService {

    private final AssetService assetService;
    private final World physicWorld;
    private TiledMap currentMap;

    private Consumer<TiledMap> mapChangeConsumer;
    private Consumer<TiledMapTileMapObject> loadObjectConsumer;
    private LoadTileConsumer loadTileConsumer;

    public  TiledService(AssetService assetService, World physicWorld) {
        this.assetService = assetService;
        this.physicWorld = physicWorld;
        this.mapChangeConsumer = null;
        this.loadObjectConsumer = null;
        this.currentMap = null;
        this.loadTileConsumer = null;
    }

    public TiledMap loadMap(MapAsset mapAsset) {
        // when we load a map (take this line out of GameScreen.show())
        //TiledMap tiledMap = this.assetService.load(MapAsset.MAIN);
        // wait, not the hardcoded MAIN asset...
        TiledMap tiledMap = this.assetService.load(mapAsset);

        // dumb tiledMap from libGDX, but can see from debugger key/val pairs so can store stuff
        // store enums ("mapAsset") in TiledMap with this specific key...
        tiledMap.getProperties().put("mapAsset", mapAsset);

        return tiledMap;
    }

    public void setMap(TiledMap map) {
        if (this.currentMap != null) {
            this.assetService.unload(this.currentMap.getProperties().get("mapAsset", MapAsset.class));
            //
            // here really want to clean up entities, save state of entities remaining on current map etc.
            //
        }
        this.currentMap = map;
        // load tilemap objects
        loadMapObjects(map);
        // if class needs to react to map changes like the render system
        // call the map change method of these different consumers
        if (this.mapChangeConsumer != null) {
            this.mapChangeConsumer.accept(map);
        }

    }

//    private void loadMapObjects(TiledMap tiledMap) {
//        //
//        for (MapLayer layer: tiledMap.getLayers()) {
//            if("objects".equals(layer.getName())) {
//                loadObjectLayer(layer);
//            }
//            // later branch for trigger etc.
//        }
//
//    }

    /**
     * Loads all map objects from different layers and creates map collision boundaries.
     */
    public void loadMapObjects(TiledMap tiledMap) {
        for (MapLayer layer : tiledMap.getLayers()) {
            if (layer instanceof TiledMapTileLayer tileLayer) {
                loadTileLayer(tileLayer);
            } else if ("objects".equals(layer.getName())) {
                loadObjectLayer(layer);
//            } else if ("trigger".equals(layer.getName())) {
//                loadTriggerLayer(layer);
            }
        }

        spawnMapBoundary(tiledMap);
    }


    private void loadObjectLayer(MapLayer objectLayer) {
        if (loadObjectConsumer == null) return;

        for (MapObject mapObject : objectLayer.getObjects()) {
            if(mapObject instanceof TiledMapTileMapObject tileMapObject) {
                loadObjectConsumer.accept(tileMapObject);
            } else {
                throw new GdxRuntimeException("Unsupported object: " + mapObject.getClass().getSimpleName());
                // could even be a text object
            }
        }
    }

    public void setMapChangeConsumer(Consumer<TiledMap> mapChangeConsumer) {
        this.mapChangeConsumer = mapChangeConsumer;
    }

    public void setLoadObjectConsumer(Consumer<TiledMapTileMapObject> loadObjectConsumer) {
        this.loadObjectConsumer = loadObjectConsumer;
    }

    public void setLoadTileConsumer(LoadTileConsumer loadTileConsumer) {
        this.loadTileConsumer = loadTileConsumer;
    }



    /**
     * Creates physics boundaries around the map edges.
     */
    private void spawnMapBoundary(TiledMap tiledMap) {
        int width = tiledMap.getProperties().get("width", 0, Integer.class);
        int tileW = tiledMap.getProperties().get("tilewidth", 0, Integer.class);
        int height = tiledMap.getProperties().get("height", 0, Integer.class);
        int tileH = tiledMap.getProperties().get("tileheight", 0, Integer.class);
        float mapW = width * tileW * GdxGame.UNIT_SCALE;
        float mapH = height * tileH * GdxGame.UNIT_SCALE;
        float halfW = mapW * 0.5f;
        float halfH = mapH * 0.5f;
        float boxThickness = 0.5f;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.setZero();
        bodyDef.fixedRotation = true;
        Body body = physicWorld.createBody(bodyDef);
        body.setUserData("environment");

        // left edge
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(boxThickness, halfH, new Vector2(-boxThickness, halfH), 0f);
        body.createFixture(shape, 0f).setFriction(0f);
        shape.dispose();
        // right edge
        shape = new PolygonShape();
        shape.setAsBox(boxThickness, halfH, new Vector2(mapW + boxThickness, halfH), 0f);
        body.createFixture(shape, 0f).setFriction(0f);
        shape.dispose();
        // bottom edge
        shape = new PolygonShape();
        shape.setAsBox(halfW, boxThickness, new Vector2(halfW, -boxThickness), 0f);
        body.createFixture(shape, 0f).setFriction(0f);
        shape.dispose();
        // top edge
        shape = new PolygonShape();
        shape.setAsBox(halfW, boxThickness, new Vector2(halfW, mapH + boxThickness), 0f);
        body.createFixture(shape, 0f).setFriction(0f);
        shape.dispose();
    }

    private void loadTileLayer(TiledMapTileLayer tileLayer) {
        if (loadTileConsumer == null) return;

        for (int y = 0; y < tileLayer.getHeight(); y++) {
            for (int x = 0; x < tileLayer.getWidth(); x++) {
                TiledMapTileLayer.Cell cell = tileLayer.getCell(x, y);
                if (cell == null) continue;

                loadTileConsumer.accept(cell.getTile(), x, y);
            }
        }
    }

    @FunctionalInterface
    public interface LoadTileConsumer {
        void accept(TiledMapTile tile, float x, float y);
    }
}
