package com.hat_quest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Input.Keys;

// Class representing the Settings screen
public class SettingsScreen implements Screen {
    private final MainGame game; // Reference to the main game class
    private final SpriteBatch batch; // SpriteBatch used for drawing
    private final BitmapFont font; // Font for the title
    private final BitmapFont buttonFont; // Font for the buttons
    private int selectedIndex; // Index of the selected menu item
    private float volume = 0.5f; // Default volume value
    private boolean isFullscreen = false; // Default fullscreen value

    private final Rectangle volumeButtonBounds; // Bounds for the Volume button
    private final Rectangle fullscreenButtonBounds; // Bounds for the Fullscreen button
    private final Rectangle backButtonBounds; // Bounds for the Back button

    // Constructor to initialize the Settings screen
    public SettingsScreen(final MainGame game) {
        this.game = game;
        this.batch = game.getBatch();
        this.font = new BitmapFont();
        this.buttonFont = new BitmapFont();
        this.selectedIndex = 0;

        // Initialize button bounds
        this.volumeButtonBounds = new Rectangle(350, 180, 200, 30);
        this.fullscreenButtonBounds = new Rectangle(350, 130, 200, 30);
        this.backButtonBounds = new Rectangle(350, 80, 100, 30);

        setupInputProcessor(); // Set up input handling
    }

    // Set up input handling for keyboard input
    private void setupInputProcessor() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                handleKeyInput(keycode);
                return true;
            }
        });
    }

    // Handle keyboard input
    private void handleKeyInput(int keycode) {
        switch (keycode) {
            case Keys.UP:
                selectedIndex = (selectedIndex + 2) % 3; // Navigate up
                break;
            case Keys.DOWN:
                selectedIndex = (selectedIndex + 1) % 3; // Navigate down
                break;
            case Keys.LEFT:
                if (selectedIndex == 0) {
                    adjustVolume(-0.1f); // Decrease volume
                }
                break;
            case Keys.RIGHT:
                if (selectedIndex == 0) {
                    adjustVolume(0.1f); // Increase volume
                }
                break;
            case Keys.ENTER:
                selectButton(); // Select the current button
                break;
            default:
                break;
        }
    }

    // Adjust the volume
    private void adjustVolume(float adjustment) {
        volume = Math.max(0, Math.min(1, volume + adjustment));
        MainWork.updateVolume(volume); // Update volume in MainWork
    }

    // Perform action based on the selected button
    private void selectButton() {
        switch (selectedIndex) {
            case 0:
                break; // Volume adjustment is handled with LEFT/RIGHT keys
            case 1:
                toggleFullscreen();
                break;
            case 2:
                navigateBack();
                break;
            default:
                break;
        }
    }

    // Toggle fullscreen mode
    private void toggleFullscreen() {
        isFullscreen = !isFullscreen;
        try {
            if (isFullscreen) {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            } else {
                Gdx.graphics.setWindowedMode(800, 480);
            }
        } catch (Exception e) {
            // Log the exception and handle it appropriately
            System.err.println("Error toggling fullscreen mode: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Navigate back to the start menu
    private void navigateBack() {
        game.setScreen(new StartMenuScreen(game));
    }

    @Override
    public void show() {
        // Any setup code for the screen can go here
    }

    @Override
    public void render(float delta) {
        try {
            clearScreen(); // Clear the screen
            renderSettings(); // Render the settings menu
        } catch (Exception e) {
            // Log the exception and handle it appropriately
            System.err.println("Error during rendering: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Clear the screen with a dark blue color
    private void clearScreen() {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    // Render the settings menu
    private void renderSettings() {
        batch.begin();
        drawTitle(); // Draw the title
        drawButtons(); // Draw the buttons
        batch.end();
    }

    // Draw the title text
    private void drawTitle() {
        font.getData().setScale(2);
        font.draw(batch, "Settings", 350, 300);
    }

    // Draw the buttons
    private void drawButtons() {
        drawButton("Volume: " + (int) (volume * 100), volumeButtonBounds, 0);
        drawButton("Toggle Fullscreen", fullscreenButtonBounds, 1);
        drawButton("Back", backButtonBounds, 2);
    }

    // Draw a single button
    private void drawButton(String text, Rectangle bounds, int index) {
        buttonFont.getData().setScale(1.5f);
        buttonFont.setColor(selectedIndex == index ? 1 : 0.7f, selectedIndex == index ? 1 : 0.7f, selectedIndex == index ? 1 : 0.7f, 1);
        buttonFont.draw(batch, text, bounds.x, bounds.y);
    }

    @Override
    public void resize(int width, int height) {
        // Handle screen resizing if necessary
    }

    @Override
    public void pause() {
        // Handle screen pause if necessary
    }

    @Override
    public void resume() {
        // Handle screen resume if necessary
    }

    @Override
    public void hide() {
        // Handle screen hide if necessary
    }

    @Override
    public void dispose() {
        try {
            font.dispose();
            buttonFont.dispose();
        } catch (Exception e) {
            // Log the exception and handle it appropriately
            System.err.println("Error during dispose: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
