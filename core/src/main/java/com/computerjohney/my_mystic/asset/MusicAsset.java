package com.computerjohney.my_mystic.asset;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Music;

public enum MusicAsset implements Asset<Music> {

    TOWN("town.ogg");

    private final AssetDescriptor<Music> descriptor;

    MusicAsset(String musicFile) {
        this.descriptor = new AssetDescriptor<>("my_maps2/audio/" + musicFile, Music.class);
    }

    public AssetDescriptor<Music> getDescriptor() {
        return descriptor;
    }
}
