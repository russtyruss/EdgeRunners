package com.Azcuna.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music; // Import Music class
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

public class MainMenuScreen implements Screen {
    private final PlatformerGame game;
    private final Stage stage;
    private final Skin skin;
    private Music backgroundMusic; // Add Music instance

    // Textures - added clicked state textures
    private Texture startNormal, startHover, startClicked;
    private Texture settingsNormal, settingsHover, settingsClicked;
    private Texture exitNormal, exitHover, exitClicked;

    private AnimatedBackground animatedBackground;

    public MainMenuScreen(PlatformerGame game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Load sound effects
        SoundManager.load();
        SoundManager.playMenuSoundtrack();

        loadTextures();
        setupUI();

        Gdx.input.setInputProcessor(stage);
    }

    private void loadTextures() {
        // Load normal state textures
        startNormal = new Texture(Gdx.files.internal("menu/normal/start_normal_button.png"));
        settingsNormal = new Texture(Gdx.files.internal("menu/normal/settings_normal_button.png"));
        exitNormal = new Texture(Gdx.files.internal("menu/normal/exit_normal_button.png"));

        // Load hover state textures
        startHover = new Texture(Gdx.files.internal("menu/hover/start_hover_button.png"));
        settingsHover = new Texture(Gdx.files.internal("menu/hover/settings_hover_button.png"));
        exitHover = new Texture(Gdx.files.internal("menu/hover/exit_hover_button.png"));

        // Load clicked state textures
        startClicked = new Texture(Gdx.files.internal("menu/clicked/start_clicked_button.png"));
        settingsClicked = new Texture(Gdx.files.internal("menu/clicked/settings_clicked_button.png"));
        exitClicked = new Texture(Gdx.files.internal("menu/clicked/exit_clicked_button.png"));

        Array<Texture> backgroundFrames = new Array<>();
        for (int i = 1; i <= 30; i++) { // Assuming you have 30 frames
            backgroundFrames.add(new Texture(Gdx.files.internal("menu/animated_bg/frame_" + i + ".png")));
        }

        // Create animated background (0.04f = 25fps animation)
        animatedBackground = new AnimatedBackground(backgroundFrames, 0.04f, true);
    }

    private void setupUI() {
        Table table = new Table();
        table.setFillParent(true);


        // Load background texture

        Texture backgroundTexture1 = new Texture(Gdx.files.internal("menu/pane_box.png"));
        Image background1 = new Image(new TextureRegionDrawable(new TextureRegion(backgroundTexture1)));

        // Get stage dimensions
                float stageWidth = stage.getWidth();
                float stageHeight = stage.getHeight();

        float offsetXx = 0; // Adjust this value to move more or less to the right
        table.setPosition(410, 20);

        // Get image dimensions
                float imageWidth = backgroundTexture1.getWidth();
                float imageHeight = backgroundTexture1.getHeight();

        // Adjust this value to move more or less to the right
        background1.setPosition(925, -40);



        float scaleFactor = 1.2f; // Adjust this value as needed
            background1.setSize(imageWidth * scaleFactor, imageHeight * scaleFactor);

//        Texture backgroundTexture = new Texture(Gdx.files.internal("menu/menu_background.png"));
//        Image background = new Image(new TextureRegionDrawable(new TextureRegion(backgroundTexture)));
//        background.setFillParent(true);



        // Create buttons
        ImageButton startButton = createButton(startNormal, startHover, startClicked);
        ImageButton settingsButton = createButton(settingsNormal, settingsHover, settingsClicked);
        ImageButton exitButton = createButton(exitNormal, exitHover, exitClicked);

        // Add button listeners
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //game.setScreen(new CharacterSelectScreen(game));
                game.setScreen(new CharacterSelect());
            }
        });



        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        // Add buttons to table
        table.add(startButton).size(650, 70).pad(5);
        table.row();
        table.add(settingsButton).size(650, 70).pad(5);
        table.row();
        table.add(exitButton).size(650, 70).pad(5);


        // Add background first, then UI elements
        //stage.addActor(background);
        stage.addActor(animatedBackground);
        stage.addActor(background1);
        stage.addActor(table);
    }

    private ImageButton createButton(Texture normalTex, Texture hoverTex, Texture clickedTex) {
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = new TextureRegionDrawable(new TextureRegion(normalTex));
        style.imageOver = new TextureRegionDrawable(new TextureRegion(hoverTex));
        style.imageDown = new TextureRegionDrawable(new TextureRegion(clickedTex)); // Clicked state

        ImageButton button = new ImageButton(style);

        // Add hover and click effects
        button.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                button.addAction(Actions.scaleTo(1.1f, 1.1f, 0.1f));
                SoundManager.playHoverSound();
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                button.addAction(Actions.scaleTo(1f, 1f, 0.1f));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Additional click effect (optional)
                event.getListenerActor().addAction(Actions.sequence(
                    Actions.scaleTo(0.95f, 0.95f, 0.05f),
                    Actions.scaleTo(1.1f, 1.1f, 0.05f)
                ));
                return true;
            }
        });

        return button;
    }



    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        SoundManager.playMenuSoundtrack();
    }

    @Override
    public void hide() {
        SoundManager.stopMenuSoundtrack();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        animatedBackground.dispose();
        backgroundMusic.dispose(); // Clean up music

        // Dispose all textures
        startNormal.dispose();
        startHover.dispose();
        startClicked.dispose();
        settingsNormal.dispose();
        settingsHover.dispose();
        settingsClicked.dispose();
        exitNormal.dispose();
        exitHover.dispose();
        exitClicked.dispose();
        SoundManager.stopMenuSoundtrack();
        SoundManager.dispose();
    }

    // Other required Screen methods
    @Override public void pause() {}
    @Override public void resume() {}
}
