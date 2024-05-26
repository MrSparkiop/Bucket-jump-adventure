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

public class SettingsScreen implements Screen {
    private MainGame game;
    private SpriteBatch batch;
    private BitmapFont font;
    private BitmapFont buttonFont;
    private int selectedIndex;
    private float volume = 0.5f; // Default volume value

    private Rectangle volumeButtonBounds;
    private Rectangle backButtonBounds;

    public SettingsScreen(final MainGame game) {
        this.game = game;
        this.batch = game.batch;
        font = new BitmapFont();
        buttonFont = new BitmapFont();
        selectedIndex = 0;

        volumeButtonBounds = new Rectangle(350, 180, 200, 30);
        backButtonBounds = new Rectangle(350, 130, 100, 30);

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                switch (keycode) {
                    case Keys.UP:
                        selectedIndex = (selectedIndex + 1) % 2;
                        break;
                    case Keys.DOWN:
                        selectedIndex = (selectedIndex + 1) % 2;
                        break;
                    case Keys.LEFT:
                        if (selectedIndex == 0) {
                            volume = Math.max(0, volume - 0.1f);
                            MainWork.updateVolume(volume); // Update volume in MainWork
                        }
                        break;
                    case Keys.RIGHT:
                        if (selectedIndex == 0) {
                            volume = Math.min(1, volume + 0.1f);
                            MainWork.updateVolume(volume); // Update volume in MainWork
                        }
                        break;
                    case Keys.ENTER:
                        selectButton();
                        break;
                }
                return true;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector2 touch = new Vector2(screenX, Gdx.graphics.getHeight() - screenY);
                if (volumeButtonBounds.contains(touch)) {
                    selectedIndex = 0; // Потребителят е натиснал бутона за звука
                } else if (backButtonBounds.contains(touch)) {
                    selectedIndex = 1; // Потребителят е натиснал бутона за връщане
                    selectButton();
                }
                return true;
            }
        });
    }

    private void selectButton() {
        if (selectedIndex == 1) {
            game.setScreen(new StartMenuScreen(game));
        }
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        font.getData().setScale(2);
        font.draw(batch, "Settings", 350, 300);

        buttonFont.getData().setScale(1.5f);
        buttonFont.setColor(selectedIndex == 0 ? 1 : 0.7f, selectedIndex == 0 ? 1 : 0.7f, selectedIndex == 0 ? 1 : 0.7f, 1);
        font.draw(batch, "Volume: " + (int) (volume * 100), 350, 250);
        buttonFont.setColor(selectedIndex == 1 ? 1 : 0.7f, selectedIndex == 1 ? 1 : 0.7f, selectedIndex == 1 ? 1 : 0.7f, 1);
        buttonFont.draw(batch, "Back", backButtonBounds.x, backButtonBounds.y);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        font.dispose();
        buttonFont.dispose();
    }
}
