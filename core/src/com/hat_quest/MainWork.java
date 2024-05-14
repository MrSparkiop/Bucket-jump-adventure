    package com.hat_quest;

    import com.badlogic.gdx.ApplicationAdapter;
    import com.badlogic.gdx.Gdx;
    import com.badlogic.gdx.audio.Music;
    import com.badlogic.gdx.audio.Sound;
    import com.badlogic.gdx.graphics.Texture;
    import com.badlogic.gdx.graphics.g2d.BitmapFont;
    import com.badlogic.gdx.math.MathUtils;
    import com.badlogic.gdx.utils.Array;
    import com.badlogic.gdx.utils.ScreenUtils;
    import com.badlogic.gdx.graphics.g2d.SpriteBatch;
    import com.badlogic.gdx.graphics.OrthographicCamera;
    import com.badlogic.gdx.math.Rectangle;

    import com.badlogic.gdx.Input;
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
        private Texture enemyDropImage; // Texture for enemy drops
        private Array<Rectangle> enemyDrops;
        private long lastEnemyDropTime; // Time of last enemy drop spawn
        private boolean showDeathScreen = false;
        private BitmapFont font;
        private ScoreBoard scoreBoard;
        private float gravity = 1000; // Gravity force
        private float jumpVelocity = 250; // Variable for controlling jump height
        private float jumpHeight = 600; // Jump height




        @Override
        public void create() {
            // Load resources and initialize game objects
            loadResources();
            setupCamera();
            createBucket();
            initializeRaindrops();
            enemyDrops = new Array<Rectangle>();
            lastEnemyDropTime = TimeUtils.nanoTime();
            font = new BitmapFont();
            scoreBoard = new ScoreBoard();
        }

        // Load textures and sounds
        private void loadResources() {
            dropImage = new Texture(Gdx.files.internal("droplet.png"));
            bucketImage = new Texture(Gdx.files.internal("bucket.png"));
            dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
            rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
            enemyDropImage = new Texture(Gdx.files.internal("enemy_droplet.png"));
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
            update(Gdx.graphics.getDeltaTime());
            ScreenUtils.clear(0, 0, 0.2f, 1);
            updateCamera();

            if (!showDeathScreen) {
                batch.begin();
                batch.draw(bucketImage, bucket.x, bucket.y);
                for (Rectangle raindrop : raindrops) {
                    batch.draw(dropImage, raindrop.x, raindrop.y);
                }
                for (Rectangle enemyDrop : enemyDrops) {
                    batch.draw(enemyDropImage, enemyDrop.x, enemyDrop.y);
                }
                batch.end();

                updateDrops();

            }

            long timeNow = TimeUtils.nanoTime();
            if (timeNow - lastDropTime > 1000000000) { // 1 second for normal drops
                spawnRaindrop();
                lastDropTime = timeNow;
            }
            if (timeNow - lastEnemyDropTime > 1500000000) { // 1.5 seconds for enemy drops
                if (MathUtils.randomBoolean(0.5f)) { // 50% chance to spawn an enemy drop
                    spawnEnemyDrop();
                }
                lastEnemyDropTime = timeNow;
            }

            if (showDeathScreen) {
                batch.begin();
                font.draw(batch, "Game Over!", 350, 240);
                font.draw(batch, "Press 'R' to Restart", 320, 200);
                batch.end();
            }

            batch.begin();
            scoreBoard.draw(batch);
            // Main logic to draw
            batch.end();
        }

        private void updateDrops() {
            Iterator<Rectangle> iter = raindrops.iterator();
            while (iter.hasNext()) {
                Rectangle raindrop = iter.next();
                raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
                if (raindrop.y + 64 < 0) {
                    iter.remove();
                } else if (raindrop.overlaps(bucket)) {
                    dropSound.play();
                    scoreBoard.addScore(1);  // Adding points to the ScoreBoard when collecting a drop
                    iter.remove();
                }
            }

            Iterator<Rectangle> enemyIter = enemyDrops.iterator();
            while (enemyIter.hasNext()) {
                Rectangle enemyDrop = enemyIter.next();
                enemyDrop.y -= 200 * Gdx.graphics.getDeltaTime();
                if (enemyDrop.y + 64 < 0) {
                    enemyIter.remove();
                } else if (enemyDrop.overlaps(bucket)) {
                    showDeathScreen = true;
                    rainMusic.stop();
                    break;
                }
            }

        }

        // Handle user input for moving the bucket
        private void handleInput() {
            if (!showDeathScreen) {
                if (Gdx.input.isKeyPressed(Keys.A))
                    bucket.x -= 350 * Gdx.graphics.getDeltaTime();
                if (Gdx.input.isKeyPressed(Keys.D))
                    bucket.x += 350 * Gdx.graphics.getDeltaTime();

                if (bucket.x < 0)
                    bucket.x = 0;
                if (bucket.x > 800 - 64)
                    bucket.x = 800 - 64;
            }
            if (Gdx.input.isKeyPressed(Keys.R) && showDeathScreen) {
                restartGame();
            }

            if (Gdx.input.isKeyJustPressed(Keys.SPACE) && bucket.y == 0) {
                jumpVelocity = jumpHeight;
            }
        }

        private void update(float delta) {
            // Update bucket position based on jump velocity
            bucket.y += jumpVelocity * delta;

            // Apply gravity to jump velocity to simulate falling
            jumpVelocity -= gravity * delta;

            // Check if bucket reaches bottom of the screen
            if (bucket.y < 0) {
                bucket.y = 0;
                jumpVelocity = 0; // Reset jump velocity
            }
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

            for (Iterator<Rectangle> iter = enemyDrops.iterator(); iter.hasNext();) {
                Rectangle enemyDrop = iter.next();
                enemyDrop.y -= 200 * Gdx.graphics.getDeltaTime();
                if (enemyDrop.y + 64 < 0) {
                    iter.remove();
                }
                if (enemyDrop.overlaps(bucket)) {
                    System.out.println("Game is over");
                    restartGame(); // Game restart
                    return;
                }
            }

            batch.begin();
            for (Rectangle raindrop : raindrops) {
                batch.draw(dropImage, raindrop.x, raindrop.y);
            }
            batch.end();
        }

        // Dispose of resources when the game is closed.
        @Override
        public void dispose() {
            dropImage.dispose();
            bucketImage.dispose();
            dropSound.dispose();
            rainMusic.dispose();
            batch.dispose();
            scoreBoard.dispose();
        }

        private void spawnEnemyDrop() {
            Rectangle enemyDrop = new Rectangle();
            enemyDrop.x = MathUtils.random(0, 800 - 64);
            enemyDrop.y = 480;
            enemyDrop.width = 64;
            enemyDrop.height = 64;
            enemyDrops.add(enemyDrop);
        }

        private void restartGame() {
            raindrops.clear();
            enemyDrops.clear();
            createBucket();
            lastDropTime = TimeUtils.nanoTime();
            showDeathScreen = false;
            scoreBoard.resetScore();
            rainMusic.play();
        }


    }