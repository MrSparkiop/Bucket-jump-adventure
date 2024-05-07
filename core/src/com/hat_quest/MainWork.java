package com.hat_quest;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class MainWork extends ApplicationAdapter {
    // Textures and Sounds
    private Texture dropImage; // Texture for raindrops
    private Texture bucketImage; // Texture for the bucket
    private Sound dropSound; // Sound played when a raindrop is caught
    private Music rainMusic; // Background music

    // Camera and Rendering
    private OrthographicCamera camera; // Camera for the game
    private SpriteBatch batch; // Renders 2D images

    // Game Objects
    private Rectangle bucket; // Represents the bucket
    private Array<Rectangle> raindrops; // Stores raindrops
    private long lastDropTime; // Time of last raindrop spawn

    @Override
    public void create() {
        // Load resources and initialize game objects
        loadResources();
        setupCamera();
        createBucket();
        initializeRaindrops();
    }

    // Load textures and sounds
    private void loadResources() {
        dropImage = new Texture(Gdx.files.internal("droplet.png"));
        bucketImage = new Texture(Gdx.files.internal("bucket.png"));
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        rainMusic.setLooping(true);
        rainMusic.play();
    }

    // Setup camera for rendering
    private void setupCamera() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();
    }

    // Create the bucket object
    private void createBucket() {
        bucket = new Rectangle();
        bucket.x = 800 / 2 - 64 / 2;
        bucket.y = 20;
        bucket.width = 64;
        bucket.height = 64;
    }

    // Initialize raindrop array and spawn first raindrop
    private void initializeRaindrops() {
        raindrops = new Array<Rectangle>();
        spawnRaindrop();
    }

    @Override
    public void render() {
        // Update game state and render graphics
        handleInput();
        ScreenUtils.clear(0, 0, 0.2f, 1);
        updateCamera();
        renderBucket();
        renderRaindrops();
    }

    // Handle user input for moving the bucket
    private void handleInput() {
        if (Gdx.input.isKeyPressed(Keys.A))
            bucket.x -= 300 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Keys.D))
            bucket.x += 300 * Gdx.graphics.getDeltaTime();

        // Ensure the bucket stays within the screen bounds
        if (bucket.x < 0)
            bucket.x = 0;
        if (bucket.x > 800 - 64)
            bucket.x = 800 - 64;
    }

    // Update camera projection matrix
    private void updateCamera() {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
    }

    // Render the bucket
    private void renderBucket() {
        batch.begin();
        batch.draw(bucketImage, bucket.x, bucket.y);
        batch.end();
    }

    // Spawn a new raindrop
    private void spawnRaindrop() {
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, 800 - 64);
        raindrop.y = 480;
        raindrop.width = 64;
        raindrop.height = 64;
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }

    // Render raindrops and handle collisions
    private void renderRaindrops() {
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000) {
            spawnRaindrop();
        }

        for (Iterator<Rectangle> iter = raindrops.iterator(); iter.hasNext();) {
            Rectangle raindrop = iter.next();
            raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
            if (raindrop.y + 64 < 0) {
                iter.remove();
            }

            if (raindrop.overlaps(bucket)) {
                dropSound.play();
                iter.remove();
            }
        }

        batch.begin();
        for (Rectangle raindrop : raindrops) {
            batch.draw(dropImage, raindrop.x, raindrop.y);
        }
        batch.end();
    }

    // Dispose of resources when the game is closed
    @Override
    public void dispose() {
        dropImage.dispose();
        bucketImage.dispose();
        dropSound.dispose();
        rainMusic.dispose();
        batch.dispose();
    }
}
