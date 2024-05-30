package com.hat_quest;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// Main game class that initializes and manages the game screens
public class MainGame extends Game {
    private SpriteBatch batch; // SpriteBatch used for drawing 2D textures

    @Override
    public void create() {
        try {
            batch = new SpriteBatch(); // Initialize the SpriteBatch
            setScreen(new StartMenuScreen(this)); // Set the initial screen to the Start Menu
        } catch (Exception e) {
            // Log the exception and handle it appropriately
            System.err.println("Error during game creation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void render() {
        try {
            super.render(); // Delegate the rendering to the current screen
        } catch (Exception e) {
            // Log the exception and handle it appropriately
            System.err.println("Error during rendering: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void dispose() {
        try {
            // Dispose of the SpriteBatch when the game is closed
            if (batch != null) {
                batch.dispose();
            }
        } catch (Exception e) {
            // Log the exception and handle it appropriately
            System.err.println("Error during dispose: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Getter for the SpriteBatch
    public SpriteBatch getBatch() {
        return batch;
    }
}
