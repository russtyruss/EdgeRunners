package com.Azcuna.platformer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import java.util.List;

public class Enemy {
    private float x, y;
    private float width = 25, height = 40;
    private float speed = 80f;
    private float gravity = -1300f;
    private float velocityY = 0f;
    private boolean isFalling = true;
    private List<Platform> platforms;

    public Enemy(float spawnX, float spawnY) {
        this.x = spawnX + 500; // Spawns at the other side of the platform
        this.y = spawnY;
    }

    public void update(float delta, float playerX, float playerY) {
        float direction = playerX > x ? 1 : -1;
        x += direction * speed * delta;

        applyGravity(delta);
        checkPlatformCollision();
    }

    private void applyGravity(float delta) {
        if (isFalling) {
            velocityY += gravity * delta;
            y += velocityY * delta;
        }
    }

    private void checkPlatformCollision() {
        boolean onGround = false;
        Rectangle enemyBounds = new Rectangle(x, y + velocityY * 1/60f, width, height);

        if (platforms != null) {
            for (Platform platform : platforms) {
                if (enemyBounds.overlaps(platform.getBounds()) && y >= platform.getBounds().y + platform.getBounds().height - 5) {
                    y = platform.getBounds().y + platform.getBounds().height;
                    velocityY = 0;
                    isFalling = false;
                    onGround = true;
                    break;
                }
            }
        }

        if (!onGround) {
            isFalling = true;
        }
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(x, y, width, height);
    }

    public boolean collidesWithPlayer(float playerX, float playerY, float playerWidth, float playerHeight) {
        Rectangle enemyBounds = new Rectangle(x, y, width, height);
        Rectangle playerBounds = new Rectangle(playerX, playerY, playerWidth, playerHeight);
        return enemyBounds.overlaps(playerBounds);
    }

    public void resetPosition(float spawnX, float spawnY) {
        x = spawnX + 500;
        y = spawnY;
        velocityY = 0;
        isFalling = true;
    }

    public void setPlatforms(List<Platform> platforms) {
        this.platforms = platforms;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public float getY(){
        return y;
    }

    public float getX(){
        return x;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

}
