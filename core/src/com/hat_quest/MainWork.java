package com.hat_quest;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

// Main game screen class
public class MainWork implements Screen {
    private final Game game;
    private final OrthographicCamera camera;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final Sound dropSound;
    private static Music rainMusic = null;
    private final Texture dropImage, bucketImage, enemyDropImage, shieldImage;
    private final Bucket bucket;
    private final RaindropManager raindropManager;
    private final EnemyDropManager enemyDropManager;
    private final BonusSystem bonusSystem;
    private final Shield shield;
    private final ScoreBoard scoreBoard;

    private boolean showDeathScreen = false; // Flag to show the death screen
    private boolean isPaused = false; // Flag to check if the game is paused
    private int lives = 3; // Player's lives

    private long lastDropTime; // Last time a raindrop was spawned
    private long lastEnemyDropTime; // Last time an enemy drop was spawned

    public MainWork(Game game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 800, 480); // Set camera dimensions
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        this.rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        this.dropImage = new Texture(Gdx.files.internal("droplet.png"));
        this.bucketImage = new Texture(Gdx.files.internal("bucket.png"));
        this.enemyDropImage = new Texture(Gdx.files.internal("enemy_droplet.png"));
        this.shieldImage = new Texture(Gdx.files.internal("shield.png"));
        this.bucket = new Bucket(bucketImage);
        this.raindropManager = new RaindropManager(dropImage, dropSound, bucket, this::onRaindropCaught);
        this.enemyDropManager = new EnemyDropManager(enemyDropImage, bucket, this::onEnemyDropCaught);
        this.bonusSystem = new BonusSystem("bonus.png");
        this.shield = new Shield();
        this.scoreBoard = new ScoreBoard();

        rainMusic.setLooping(true); // Loop background music
        rainMusic.play();
    }

    // Callback when a raindrop is caught
    private void onRaindropCaught() {
        scoreBoard.addScore(1);
    }

    // Callback when an enemy drop is caught
    private void onEnemyDropCaught() {
        if (shield.isActive()) {
            return;
        }
        lives--;
        if (lives <= 0) {
            showDeathScreen = true;
            rainMusic.stop();
        }
    }

    @Override
    public void show() {
        // Initialization is done in the constructor
    }

    @Override
    public void render(float delta) {
        if (isPaused) {
            renderPausedScreen();
            if (Gdx.input.isKeyJustPressed(Keys.P)) {
                isPaused = false;
                rainMusic.play();
            }
            return;
        }

        handleInput();
        updateGameObjects(delta);
        checkAndSpawnDrops(); // Handle time-based drop spawning
        renderGameObjects();
    }

    // Render the paused screen
    private void renderPausedScreen() {
        ScreenUtils.clear(0, 0, 0.2f, 1); // Clear screen with blue color
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font.draw(batch, "Paused", 350, 240);
        font.draw(batch, "Press 'P' to Resume", 320, 200);
        batch.end();
    }

    // Handle user input
    private void handleInput() {
        if (showDeathScreen) {
            if (Gdx.input.isKeyPressed(Keys.R)) {
                restartGame();
            }
            if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
                game.setScreen(new StartMenuScreen((MainGame) game));
                rainMusic.stop();
            }
        } else {
            bucket.handleInput();
            if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
                game.setScreen(new StartMenuScreen((MainGame) game));
                rainMusic.stop();
            }
            if (Gdx.input.isKeyJustPressed(Keys.P)) {
                isPaused = true;
                rainMusic.pause();
            }
            if (Gdx.input.isKeyJustPressed(Keys.F) && shield.canActivate()) {
                shield.activate();
            }
        }
    }

    // Update game objects
    private void updateGameObjects(float delta) {
        if (showDeathScreen) {
            return;
        }
        bucket.update(delta);
        raindropManager.update(delta);
        enemyDropManager.update(delta);
        shield.update();
        bonusSystem.updateBonusDrops(delta, bucket.getRectangle(), this);
    }

    // Check and spawn drops based on time intervals
    private void checkAndSpawnDrops() {
        long timeNow = TimeUtils.nanoTime();
        if (timeNow - lastDropTime > 1000000000) { // 1 second for normal drops
            raindropManager.spawnRaindrop();
            lastDropTime = timeNow;
        }
        if (timeNow - lastEnemyDropTime > 1500000000) { // 1.5 seconds for enemy drops
            if (MathUtils.randomBoolean(0.5f)) { // 50% chance to spawn an enemy drop
                enemyDropManager.spawnEnemyDrop();
            }
            lastEnemyDropTime = timeNow;
        }
        if (MathUtils.randomBoolean(0.001f)) { // Chance to spawn a bonus drop
            bonusSystem.spawnBonusDrop();
        }
    }

    // Render game objects
    private void renderGameObjects() {
        ScreenUtils.clear(0, 0, 0.2f, 1); // Clear screen with blue color
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        if (!showDeathScreen) {
            bucket.render(batch);
            raindropManager.render(batch);
            enemyDropManager.render(batch);
            bonusSystem.renderBonusDrops(batch);
            if (shield.isActive()) {
                bucket.renderShield(batch, shieldImage);
            }
        } else {
            font.draw(batch, "Game Over!", 350, 240);
            font.draw(batch, "Press 'R' to Restart", 320, 200);
            font.draw(batch, "Esc for Main Menu", 320, 160);
        }
        renderUI(batch);
        batch.end();
    }

    // Render the UI elements
    private void renderUI(SpriteBatch batch) {
        int screenWidth = Gdx.graphics.getWidth();
        font.draw(batch, "Lives: " + lives, screenWidth - 100, 460);
        scoreBoard.draw(batch);
        if (!shield.canActivate()) {
            long cooldownRemaining = shield.getCooldownRemaining();
            font.draw(batch, "Shield Cooldown: " + cooldownRemaining + "s", screenWidth - 200, 440);
        } else {
            font.draw(batch, "Shield Ready!", screenWidth - 200, 440);
        }
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
        dropImage.dispose();
        bucketImage.dispose();
        dropSound.dispose();
        rainMusic.dispose();
        batch.dispose();
        scoreBoard.dispose();
        bonusSystem.dispose();
    }

    // Restart the game
    private void restartGame() {
        raindropManager.clear();
        enemyDropManager.clear();
        bucket.resetPosition();
        showDeathScreen = false;
        scoreBoard.resetScore();
        lives = 3;
        rainMusic.play();
    }

    // Add a life to the player
    public void addLife() {
        if (lives < 3) {
            lives++;
        }
    }

    // Update the volume of the background music
    public static void updateVolume(float volume) {
        rainMusic.setVolume(volume);
    }
}

// Class representing the bucket
class Bucket {
    private final Rectangle bucketRect;
    private final Texture texture;
    private float jumpVelocity;
    private int jumpCount;
    private static final float GRAVITY = 1000; // Gravity constant
    private static final float JUMP_HEIGHT = 600; // Jump height constant

    public Bucket(Texture texture) {
        this.texture = texture;
        this.bucketRect = new Rectangle(368, 20, 64, 64); // Initial bucket position
    }

    // Handle user input for moving the bucket
    public void handleInput() {
        if (Gdx.input.isKeyPressed(Keys.A))
            bucketRect.x -= 300 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Keys.D))
            bucketRect.x += 300 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyJustPressed(Keys.SPACE) && jumpCount < 2) {
            jumpVelocity = JUMP_HEIGHT;
            jumpCount++;
        }
    }

    // Update the bucket's position and state
    public void update(float delta) {
        bucketRect.y += jumpVelocity * delta;
        jumpVelocity -= GRAVITY * delta;
        if (bucketRect.y < 0) {
            bucketRect.y = 0;
            jumpVelocity = 0;
            jumpCount = 0;
        }
        if (bucketRect.x < 0)
            bucketRect.x = 0;
        if (bucketRect.x > 736)
            bucketRect.x = 736;
    }

    // Render the bucket
    public void render(SpriteBatch batch) {
        batch.draw(texture, bucketRect.x, bucketRect.y);
    }

    // Render the shield around the bucket
    public void renderShield(SpriteBatch batch, Texture shieldImage) {
        batch.draw(shieldImage, bucketRect.x - 10, bucketRect.y - 10, bucketRect.width + 20, bucketRect.height + 20);
    }

    // Reset the bucket's position
    public void resetPosition() {
        bucketRect.x = 368;
        bucketRect.y = 20;
        jumpVelocity = 0;
        jumpCount = 0;
    }

    // Get the rectangle representing the bucket
    public Rectangle getRectangle() {
        return bucketRect;
    }
}

// Class managing raindrops
class RaindropManager {
    private final Texture dropTexture;
    private final Sound dropSound;
    private final Bucket bucket;
    private final Array<Rectangle> raindrops;
    private final Runnable onRaindropCaught;
    private long lastDropTime;

    public RaindropManager(Texture dropTexture, Sound dropSound, Bucket bucket, Runnable onRaindropCaught) {
        this.dropTexture = dropTexture;
        this.dropSound = dropSound;
        this.bucket = bucket;
        this.raindrops = new Array<>();
        this.onRaindropCaught = onRaindropCaught;
        spawnRaindrop();
    }

    // Update the position and state of raindrops
    public void update(float delta) {
        for (Iterator<Rectangle> iter = raindrops.iterator(); iter.hasNext(); ) {
            Rectangle raindrop = iter.next();
            raindrop.y -= 200 * delta;
            if (raindrop.y + 64 < 0) {
                iter.remove();
            } else if (raindrop.overlaps(bucket.getRectangle())) {
                dropSound.play();
                onRaindropCaught.run();
                iter.remove();
            }
        }
    }

    // Render the raindrops
    public void render(SpriteBatch batch) {
        for (Rectangle raindrop : raindrops) {
            batch.draw(dropTexture, raindrop.x, raindrop.y);
        }
    }

    // Spawn a new raindrop
    public void spawnRaindrop() {
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, 736);
        raindrop.y = 480;
        raindrop.width = 64;
        raindrop.height = 64;
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }

    // Clear all raindrops
    public void clear() {
        raindrops.clear();
    }
}

// Class managing enemy drops
class EnemyDropManager {
    private final Texture enemyDropTexture;
    private final Bucket bucket;
    private final Array<Rectangle> enemyDrops;
    private final Runnable onEnemyDropCaught;
    private long lastEnemyDropTime;

    public EnemyDropManager(Texture enemyDropTexture, Bucket bucket, Runnable onEnemyDropCaught) {
        this.enemyDropTexture = enemyDropTexture;
        this.bucket = bucket;
        this.enemyDrops = new Array<>();
        this.onEnemyDropCaught = onEnemyDropCaught;
        spawnEnemyDrop();
    }

    // Update the position and state of enemy drops
    public void update(float delta) {
        for (Iterator<Rectangle> iter = enemyDrops.iterator(); iter.hasNext(); ) {
            Rectangle enemyDrop = iter.next();
            enemyDrop.y -= 200 * delta;
            if (enemyDrop.y + 64 < 0) {
                iter.remove();
            } else if (enemyDrop.overlaps(bucket.getRectangle())) {
                onEnemyDropCaught.run();
                iter.remove();
            }
        }
    }

    // Render the enemy drops
    public void render(SpriteBatch batch) {
        for (Rectangle enemyDrop : enemyDrops) {
            batch.draw(enemyDropTexture, enemyDrop.x, enemyDrop.y);
        }
    }

    // Spawn a new enemy drop
    public void spawnEnemyDrop() {
        Rectangle enemyDrop = new Rectangle();
        enemyDrop.x = MathUtils.random(0, 736);
        enemyDrop.y = 480;
        enemyDrop.width = 64;
        enemyDrop.height = 64;
        enemyDrops.add(enemyDrop);
        lastEnemyDropTime = TimeUtils.nanoTime();
    }

    // Clear all enemy drops
    public void clear() {
        enemyDrops.clear();
    }
}
