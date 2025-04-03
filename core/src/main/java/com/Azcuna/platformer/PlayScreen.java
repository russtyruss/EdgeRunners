package com.Azcuna.platformer;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.ArrayList;
import java.util.List;

public class PlayScreen implements Screen {
    private PlatformerGame game;
    private float playerX, playerY;
    private float speed = 170f;
    private boolean isJumping = false;
    private float gravity = -1300f;
    private float jumpVelocity = 500f;
    private float velocityY = 0f;
    private float spawnX, spawnY;
    private float deathHeight = -50f;
    private List<Platform> platforms;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private boolean isPaused;
    private Stage pauseStage;
    private Skin skin;
    private Color playerColor;
    private String selectedCharacter;
    private boolean canDoubleJump = false;
    private boolean doubleJumpUsed = false;
    private float playerWidth = 25, playerHeight = 40;
    private Enemy enemy;

    public PlayScreen(PlatformerGame game, String selectedCharacter) {
        this.game = game;
        this.selectedCharacter = selectedCharacter;
        this.playerX = 150;
        this.playerY = 150;
        this.spawnX = 150;
        this.spawnY = 150;

        switch (selectedCharacter.toLowerCase()) {
            case "david":
                playerColor = new Color(0, 0, 1, 1);
                canDoubleJump = true;
                break;
            case "lucy":
                playerColor = new Color(1, 1, 1, 1);
                break;
            case "rebecca":
                playerColor = new Color(1, 0.5f, 0.8f, 1);
                break;
            default:
                playerColor = new Color(1, 1, 1, 1);
                break;
        }

        platforms = new ArrayList<>();
        platforms.add(new Platform(900, 150, 200, 20));
        platforms.add(new Platform(450, 150, 150, 20));
        platforms.add(new Platform(600, 150, 200, 20));
        platforms.add(new Platform(1200, 100, 150, 20));
        platforms.add(new Platform(50, 50, 1920, 20));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1600, 900);
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        isPaused = false;

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        pauseStage = new Stage(new ScreenViewport());

        TextButton mainMenuButton = new TextButton("Back to Main Menu", skin);
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        mainMenuButton.setPosition(700, 400);
        pauseStage.addActor(mainMenuButton);

        enemy = new Enemy(spawnX, spawnY);
        enemy.setPlatforms(platforms);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            isPaused = !isPaused;
            Gdx.input.setInputProcessor(isPaused ? pauseStage : null);
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);

        if (isPaused) {
            pauseStage.act(delta);
            pauseStage.draw();
        } else {
            handleInput(delta);
            applyGravity(delta);
            checkCollisions();
            enemy.update(delta, playerX, playerY);
            checkRespawn();
            updateCamera();

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(playerColor);
            shapeRenderer.rect(playerX, playerY, playerWidth, playerHeight);
            shapeRenderer.setColor(0, 1, 0, 1);
            for (Platform platform : platforms) {
                Rectangle bounds = platform.getBounds();
                shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
            }
            enemy.draw(shapeRenderer);
            shapeRenderer.end();
        }
    }

    private void handleInput(float delta) {
        float newX = playerX;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            newX -= speed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            newX += speed * delta;
        }
        if (!checkHorizontalCollision(newX)) {
            playerX = newX;
        }

        // Jump Logic
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (!isJumping) {
                velocityY = jumpVelocity;
                isJumping = true;
            } else if (canDoubleJump) {  // Allow double jump
                velocityY = jumpVelocity;
                canDoubleJump = false; // Consume the double jump
            }
        }
    }

    private void applyGravity(float delta) {
        velocityY += gravity * delta;
        playerY += velocityY * delta;
    }

    private void checkCollisions() {
        boolean onGround = false;
        float delta = Gdx.graphics.getDeltaTime();

        Rectangle futurePlayerBounds = new Rectangle(playerX, playerY + velocityY * delta, playerWidth, playerHeight);

        for (Platform platform : platforms) {
            Rectangle platformBounds = platform.getBounds();

            if (futurePlayerBounds.overlaps(platformBounds)) {
                if (playerY >= platformBounds.y + platformBounds.height - 5 && velocityY < 0) {
                    //  Landing on a platform
                    playerY = platformBounds.y + platformBounds.height;
                    velocityY = 0;
                    isJumping = false;
                    canDoubleJump = true; //  Reset double jump here
                    onGround = true;
                }
                else if (playerY + playerHeight > platformBounds.y && velocityY > 0 && playerY < platformBounds.y + platformBounds.height) {
                    //  Hitting the bottom of a platform
                    playerY = platformBounds.y - playerHeight;
                    velocityY = 0;
                }
            }
        }

        if (!onGround) {
            isJumping = true;
        }
    }

    private boolean checkHorizontalCollision(float newX) {
        Rectangle futurePlayerBounds = new Rectangle(newX, playerY, playerWidth, playerHeight);
        for (Platform platform : platforms) {
            if (futurePlayerBounds.overlaps(platform.getBounds())) {
                return true;
            }
        }
        return false;
    }

    private void checkRespawn() {
        if (playerY < deathHeight || enemy.collidesWithPlayer(playerX, playerY, playerWidth, playerHeight)) {
            playerX = spawnX;
            playerY = spawnY;
            velocityY = 0;
            isJumping = false;
            enemy.resetPosition(spawnX, spawnY);
        }
    }

    private void updateCamera() {
        camera.position.set(playerX + 200, camera.viewportHeight / 2, 0);
        camera.update();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void show() {}

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        pauseStage.dispose();
        skin.dispose();
    }
}
