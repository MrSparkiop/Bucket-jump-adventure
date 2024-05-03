package com.hat_quest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class LoadSave {

    public static final String PLAYER_ATLAS = "player_atlas.png"; // Adjust file path as needed

    public static Texture getTexture(String fileName) {
        try {
            return new Texture(Gdx.files.internal(fileName));
        } catch (GdxRuntimeException e) {
            e.printStackTrace();
            return null; // Return null if texture loading fails
        }
    }
}
