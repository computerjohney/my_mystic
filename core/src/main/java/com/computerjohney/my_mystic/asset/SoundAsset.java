package com.computerjohney.my_mystic.asset;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Sound;

public enum SoundAsset implements Asset<Sound> {


        SWORD_HIT("sword_hit.wav"),
        LIFE_REG("life_reg.wav"),
        TRAP("trap.wav"),
        SWING("swing.wav");

        private final AssetDescriptor<Sound> descriptor;

        SoundAsset(String musicFile) {
            this.descriptor = new AssetDescriptor<>("my_maps2/audio/" + musicFile, Sound.class);
        }

        @Override
        public AssetDescriptor<Sound> getDescriptor() {
            return descriptor;
        }
}
