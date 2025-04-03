package com.Azcuna.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class CharacterSelectScreen implements Screen {
    private PlatformerGame game;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private Stage stage;

    public CharacterSelectScreen(PlatformerGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1600, 900);
        shapeRenderer = new ShapeRenderer();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // Clickable areas for character selection
        createCharacterButton(400, 400, "David");   // Left
        createCharacterButton(800, 400, "Lucy");    // Middle
        createCharacterButton(1200, 400, "Rebecca");// Right
    }

    private void createCharacterButton(float x, float y, final String characterName) {
        Actor characterBox = new Actor();
        characterBox.setBounds(x, y, 50, 80); // Character box size
        characterBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PlayScreen(game, characterName));
            }
        });
        stage.addActor(characterBox);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Draw characters as colored boxes
        shapeRenderer.setColor(0, 0, 1, 1); // Blue for David
        shapeRenderer.rect(400, 400, 50, 80);

        shapeRenderer.setColor(1, 1, 1, 1); // White for Lucy
        shapeRenderer.rect(800, 400, 50, 80);

        shapeRenderer.setColor(1, 0.5f, 0.8f, 1); // Pink for Rebecca
        shapeRenderer.rect(1200, 400, 50, 80);

        shapeRenderer.end();

        stage.act(delta);
        stage.draw();
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
    public void show() {}

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        stage.dispose();
    }
}
