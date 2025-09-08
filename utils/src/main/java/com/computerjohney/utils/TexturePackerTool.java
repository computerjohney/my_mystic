package com.computerjohney.utils;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class TexturePackerTool {

    public static void main(String[] args){
        String inputDir = "assets_raw/objects";
        String outputDir = "assets/graphics";
        // filename of texture atlas
        String packFileName = "objects";

        TexturePacker.process(inputDir, outputDir, packFileName);
    }

}
