package com.computerjohney.my_mystic.tiled;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.computerjohney.my_mystic.asset.AssetService;
import com.computerjohney.my_mystic.asset.MapAsset;

import java.util.function.Consumer;

public class TiledService {

    private final AssetService assetService;
    private TiledMap currentMap;

    private Consumer<TiledMap> mapChangeConsumer;
    private Consumer<TiledMapTileMapObject> loadObjectConsumer;

    public  TiledService(AssetService assetService) {
        this.assetService = assetService;
        this.mapChangeConsumer = null;
        this.loadObjectConsumer = null;
        this.currentMap = null;
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

    private void loadMapObjects(TiledMap tiledMap) {
        //
        for (MapLayer layer: tiledMap.getLayers()) {
            if("objects".equals(layer.getName())) {
                loadObjectLayer(layer);
            }
            // later branch for trigger etc.
        }

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


}
