package com.computerjohney.my_mystic.asset;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

// gives a predefined set of maps (well, you can get a descriptor)
public enum MapAsset implements Asset<TiledMap> {

    MAIN("my_map.tmx");

    private final AssetDescriptor<TiledMap> descriptor;

    MapAsset(String mapName) {
        TmxMapLoader.Parameters parameters = new TmxMapLoader.Parameters();
        parameters.projectFilePath = "my_maps2/my_maps2.tiled-project";
        this.descriptor = new AssetDescriptor<>("my_maps2/" + mapName, TiledMap.class, parameters);
    }

    public AssetDescriptor<TiledMap> getDescriptor() {
        return this.descriptor;
    }
}
