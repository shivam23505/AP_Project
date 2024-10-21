package project_game.AP;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameScreen implements Screen {
    final Structure game;
    private Texture background;
    private FreeTypeFontGenerator fontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    private BitmapFont font;
    OrthographicCamera camera;
    private Stage stage;

    public GameScreen(final Structure game) {
        this.game = game;
        background = new Texture("Angry Birds background.jpg");
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("BronacoDemoRegular.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 40;
        fontParameter.borderWidth = 3;
        fontParameter.borderColor = Color.YELLOW;
        fontParameter.color = Color.BLACK;
        font = fontGenerator.generateFont(fontParameter);
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage); // Set input processor for the stage
        createButtons();
    }

    private void createButtons() {
        Texture buttonTexture = new Texture("button.png");
        TextureRegionDrawable drawable = new TextureRegionDrawable(buttonTexture);
        Table table = new Table();
        table.setFillParent(true); // Center the table on the screen
        table.setDebug(true); // Enable debug to see button boundaries
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = drawable; // Set the button's appearance
        textButtonStyle.font = font;
        TextButton levelButton = new TextButton("Levels", textButtonStyle);
        TextButton settingsButton = new TextButton("Settings", textButtonStyle);
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(game.settings);
            }
        });
        TextButton quitButton = new TextButton("Quit", textButtonStyle);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        levelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new Level2(game));
                dispose();
            }
        });

        table.add(levelButton).padBottom(20).width(200).height(75).row();
        table.add(settingsButton).padBottom(20).width(200).height(75).row();
        table.add(quitButton).width(200).height(75);
        stage.addActor(table);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(background, 0, 0, 800, 480);
        game.batch.end();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        if (background!=null){background.dispose();}
        if (font!=null){font.dispose();}
        if (fontGenerator!=null){fontGenerator.dispose();}
        stage.dispose();
    }
}
