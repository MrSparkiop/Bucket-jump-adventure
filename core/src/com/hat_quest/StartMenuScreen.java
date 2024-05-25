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

public class StartMenuScreen implements Screen {
    private MainGame game;
    private SpriteBatch batch;
    private BitmapFont font;
    private BitmapFont buttonFont;
    private int selectedIndex;

    private Rectangle playButtonBounds;
    private Rectangle settingsButtonBounds;
    private Rectangle exitButtonBounds;

    public StartMenuScreen(final MainGame game) {
        this.game = game;
        this.batch = game.batch;
        font = new BitmapFont();
        buttonFont = new BitmapFont();
        selectedIndex = 0;

        playButtonBounds = new Rectangle(350, 180, 100, 30);
        settingsButtonBounds = new Rectangle(350, 130, 100, 30);
        exitButtonBounds = new Rectangle(350, 80, 100, 30);

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                switch (keycode) {
                    case Keys.UP:
                        selectedIndex = (selectedIndex + 2) % 3;
                        break;
                    case Keys.DOWN:
                        selectedIndex = (selectedIndex + 1) % 3;
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
                if (playButtonBounds.contains(touch)) {
                    selectedIndex = 0;
                    selectButton();
                } else if (settingsButtonBounds.contains(touch)) {
                    selectedIndex = 1;
                    selectButton();
                } else if (exitButtonBounds.contains(touch)) {
                    selectedIndex = 2;
                    selectButton();
                }
                return true;
            }
        });
    }

    private void selectButton() {
        if (selectedIndex == 0) {
            game.setScreen(new MainWork(game));
        } else if (selectedIndex == 1) {
            game.setScreen(new SettingsScreen(game));
        } else if (selectedIndex == 2) {
            Gdx.app.exit();
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
        font.draw(batch, "Bucket Jump Adventure!", 200, 300);

        buttonFont.getData().setScale(1.5f);
        buttonFont.setColor(selectedIndex == 0 ? 1 : 0.7f, selectedIndex == 0 ? 1 : 0.7f, selectedIndex == 0 ? 1 : 0.7f, 1);
        buttonFont.draw(batch, "Play", playButtonBounds.x, playButtonBounds.y);
        buttonFont.setColor(selectedIndex == 1 ? 1 : 0.7f, selectedIndex == 1 ? 1 : 0.7f, selectedIndex == 1 ? 1 : 0.7f, 1);
        buttonFont.draw(batch, "Settings", settingsButtonBounds.x, settingsButtonBounds.y);
        buttonFont.setColor(selectedIndex == 2 ? 1 : 0.7f, selectedIndex == 2 ? 1 : 0.7f, selectedIndex == 2 ? 1 : 0.7f, 1);
        buttonFont.draw(batch, "Exit", exitButtonBounds.x, exitButtonBounds.y);

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
