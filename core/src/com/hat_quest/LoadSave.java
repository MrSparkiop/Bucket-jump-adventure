package com.hat_quest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class LoadSave {

    public static final String PLAYER_ATLAS = "player_atlas.png"; // might have to adjust this
    public static final String LEVEL_ATLAS = null; // Fix later

    public static Texture getTexture(String fileName) {
        try {
            return new Texture(Gdx.files.internal(fileName));
        } catch (GdxRuntimeException e) {
            e.printStackTrace();
            return null; // return null if texture loading fails
        }
    }
}
