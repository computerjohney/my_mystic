package com.computerjohney.my_mystic.asset;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

// gives a predefined set of maps (well, you can get a descriptor)
public enum MapAsset implements Asset<TiledMap> {

    MAIN("mainmap.tmx");

    private final AssetDescriptor<TiledMap> descriptor;

    MapAsset(String mapName) {
        TmxMapLoader.Parameters parameters = new TmxMapLoader.Parameters();
        parameters.projectFilePath = "maps/mystic.tiled-project";
        this.descriptor = new AssetDescriptor<>("maps/" + mapName, TiledMap.class, parameters);
    }

    public AssetDescriptor<TiledMap> getDescriptor() {
        return this.descriptor;
    }
}
