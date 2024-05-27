package com.hat_quest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Input.Keys;

// Class representing the Start Menu screen
public class StartMenuScreen implements Screen {
    private final MainGame game; // Reference to the main game class
    private final SpriteBatch batch; // SpriteBatch used for drawing
    private final BitmapFont titleFont; // Font for the title
    private final BitmapFont buttonFont; // Font for the buttons
    private int selectedIndex; // Index of the selected menu item

    private final Rectangle playButtonBounds; // Bounds for the Play button
    private final Rectangle settingsButtonBounds; // Bounds for the Settings button
    private final Rectangle exitButtonBounds; // Bounds for the Exit button

    // Constructor to initialize the Start Menu screen
    public StartMenuScreen(final MainGame game) {
        this.game = game;
        this.batch = game.getBatch();
        this.titleFont = new BitmapFont();
        this.buttonFont = new BitmapFont();
        this.selectedIndex = 0;

        // Initialize button bounds
        this.playButtonBounds = new Rectangle(350, 180, 100, 30);
        this.settingsButtonBounds = new Rectangle(350, 130, 100, 30);
        this.exitButtonBounds = new Rectangle(350, 80, 100, 30);

        setupInputProcessor(); // Set up input handling
    }

    // Set up input handling for keyboard and touch input
    private void setupInputProcessor() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                handleKeyInput(keycode);
                return true;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                handleTouchInput(screenX, screenY);
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
            case Keys.ENTER:
                selectButton(); // Select the current button
                break;
            default:
                break;
        }
    }

    // Handle touch input
    private void handleTouchInput(int screenX, int screenY) {
        Vector2 touch = new Vector2(screenX, Gdx.graphics.getHeight() - screenY);
        if (playButtonBounds.contains(touch)) {
            selectedIndex = 0; // Play button selected
        } else if (settingsButtonBounds.contains(touch)) {
            selectedIndex = 1; // Settings button selected
        } else if (exitButtonBounds.contains(touch)) {
            selectedIndex = 2; // Exit button selected
        }
        selectButton(); // Select the current button
    }

    // Perform action based on the selected button
    private void selectButton() {
        switch (selectedIndex) {
            case 0:
                game.setScreen(new MainWork(game)); // Start the main game
                break;
            case 1:
                game.setScreen(new SettingsScreen(game)); // Open settings screen
                break;
            case 2:
                Gdx.app.exit(); // Exit the game
                break;
            default:
                break;
        }
    }

    @Override
    public void show() {
        // Any setup code for the screen can go here
    }

    @Override
    public void render(float delta) {
        clearScreen(); // Clear the screen
        renderMenu(); // Render the menu
    }

    // Clear the screen with a dark blue color
    private void clearScreen() {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    // Render the start menu
    private void renderMenu() {
        batch.begin();
        drawTitle(); // Draw the title
        drawButtons(); // Draw the buttons
        batch.end();
    }

    // Draw the title text
    private void drawTitle() {
        titleFont.getData().setScale(2);
        titleFont.draw(batch, "Bucket Jump Adventure!", 200, 300);
    }

    // Draw the buttons
    private void drawButtons() {
        drawButton("Play", playButtonBounds, 0);
        drawButton("Settings", settingsButtonBounds, 1);
        drawButton("Exit", exitButtonBounds, 2);
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
        titleFont.dispose();
        buttonFont.dispose();
    }
}
