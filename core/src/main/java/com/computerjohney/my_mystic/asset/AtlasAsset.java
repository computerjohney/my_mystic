package com.computerjohney.my_mystic.asset;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public enum AtlasAsset implements Asset<TextureAtlas> {
    OBJECTS("objects.atlas");
    // our asset service "can load such things"

    private final AssetDescriptor<TextureAtlas> descriptor;

    AtlasAsset(String atlasName) {
        this.descriptor = new AssetDescriptor<>("my_maps2/graphics/" + atlasName, TextureAtlas.class);
    }

    @Override
    public AssetDescriptor<TextureAtlas> getDescriptor() {
        return descriptor;
    }
}
