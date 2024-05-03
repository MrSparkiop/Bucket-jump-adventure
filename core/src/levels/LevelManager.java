package levels;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.hat_quest.LoadSave;

public class LevelManager {
    private Game game;
    private Texture levelSprite;

    public LevelManager(Game game){
        this.game = game;
        levelSprite = LoadSave.getTexture(LoadSave.LEVEL_ATLAS);
        if (levelSprite == null) {
            // Handle failed loading
            throw new GdxRuntimeException("Failed to load level atlas.");
        }
    }

    public void draw(SpriteBatch batch){
        batch.draw(levelSprite, 0, 0);
    }

    public void update(){
        // Update logic for the level
    }
}