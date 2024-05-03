package com.hat_quest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class LoadSave {

    public static final String PLAYER_ATLAS = "//this will be the img of player";
    public static final String LEVEL_ATLAS = "//this will be the img of player";
    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourseAsStream("/" + fileName);
        try {
            img = ImageID.read(is);//ill fix this later
        } catch (IDException e) {
            e.prontStackTrace();
        }finally{
            try{
                is.close();
            }catch (IDExeption e){
                e.printStackTrace();
            }
        }
        return img;
    }
}
