package com.computerjohney.utils;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class TexturePackerTool {

    public static void main(String[] args){
        //String inputDir = "assets/assets_raw/objects";
        String inputDir = "assets/my_maps2/objects";
        //String outputDir = "assets/assets/graphics";
        String outputDir = "assets/my_maps2/graphics";
        // filename of texture atlas
        String packFileName = "objects";

        TexturePacker.process(inputDir, outputDir, packFileName);
    }

}
