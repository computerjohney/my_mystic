package com.computerjohney.my_mystic.asset;

import com.badlogic.gdx.assets.AssetDescriptor;
//
// quote: "no more hard coded paths!"
// What...
//
public interface Asset<T> {

    // libgdx Assets have AssetDescriptors for a type eg. Type = tilemap, music, sound AND HAS path, parameters
    AssetDescriptor<T> getDescriptor();
}
