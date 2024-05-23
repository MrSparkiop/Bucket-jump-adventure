package com.hat_quest;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Iterator;

public class BonusSystem {
    private Texture bonusDropImage; // Texture for bonus drops
    private Array<Rectangle> bonusDrops; // Stores bonus drops

    public BonusSystem(String bonusImagePath) {
        bonusDropImage = new Texture(bonusImagePath);
        bonusDrops = new Array<Rectangle>();
    }

    // Spawn a new bonus drop
    public void spawnBonusDrop() {
        Rectangle bonusDrop = new Rectangle();
        bonusDrop.x = MathUtils.random(0, 800 - 64);
        bonusDrop.y = 480;
        bonusDrop.width = 64;
        bonusDrop.height = 64;
        bonusDrops.add(bonusDrop);
    }

    // Update bonus drops and handle collisions
    public void updateBonusDrops(float delta, Rectangle bucket, MainWork game) {
        for (Iterator<Rectangle> iter = bonusDrops.iterator(); iter.hasNext();) {
            Rectangle bonusDrop = iter.next();
            bonusDrop.y -= 200 * delta; // Update bonus drop position
            if (bonusDrop.y + 64 < 0) {
                iter.remove(); // Remove bonus drop if it's below the screen
            } else if (bonusDrop.overlaps(bucket)) {
                game.addLife(); // Increase lives if bucket catches bonus drop
                iter.remove();
            }
        }
    }

    // Render bonus drops
    public void renderBonusDrops(SpriteBatch batch) {
        for (Rectangle bonusDrop : bonusDrops) {
            batch.draw(bonusDropImage, bonusDrop.x, bonusDrop.y);
        }
    }

    // Dispose of resources
    public void dispose() {
        bonusDropImage.dispose();
    }
}
