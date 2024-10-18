package project_game.AP;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;


public class LevelSelector implements Screen {
    final Structure game;
    OrthographicCamera camera;
    private Texture background,buttonImage,menubg;
    private Texture menuPlay,menuBack;
    private Stage stage;
    private Skin skin;
    private BitmapFont font;

    public LevelSelector(Structure game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        background = new Texture("background.png");
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        font = new BitmapFont();
        buttonImage = new Texture("wooden_button.png");
        menubg = new Texture("menubg3.png");
        menuPlay = new Texture("menu_play_btn.png");
        menuBack = new Texture("menu_back_btn.png");
//        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("arial.ttf"));
//        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
//        parameter.size = 36;  // Set the font size for the buttons
//        font = generator.generateFont(parameter);
//        generator.dispose();  // Dispose the generator when done

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle();
        style.up = new TextureRegionDrawable(new TextureRegion(buttonImage)); // Set button background
        style.font = font;

        ImageTextButton levelOne = new ImageTextButton("1", style);
        ImageTextButton levelTwo = new ImageTextButton("2", style);
        ImageTextButton levelThree = new ImageTextButton("3", style);

        float buttonWidth = 150f;
        float buttonHeight = 100f;
        levelOne.setSize(buttonWidth, buttonHeight);
        levelTwo.setSize(buttonWidth, buttonHeight);
        levelThree.setSize(buttonWidth, buttonHeight);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;  // Use the same font or create a new one for the label
        Label selectLevelLabel = new Label("Select Level", labelStyle);

        // Create a table to arrange the label and buttons
        Table table = new Table();
        table.setFillParent(true);  // Make the table fill the screen

        // Add the "Select Level" label at the top
        table.top();  // Align table content to the top
        table.add(selectLevelLabel).padTop(-100).padBottom(100).colspan(3).center();  // Center the label at the top and add some padding
        table.row();  // Move to the next row

        // Add buttons in the next row, evenly spaced
        table.add(levelOne).size(150, 100).pad(10);
        table.add(levelTwo).size(150, 100).pad(10);
        table.add(levelThree).size(150, 100).pad(10);
        table.center();  // Center the table on the screen

        // Add the table to the stage
        stage.addActor(table);

        levelOne.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showMenuPopup();
            }
        });
        levelTwo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showMenuPopup();
            }
        });
        levelThree.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showMenuPopup();
            }
        });
    }

    private void showMenuPopup() {
        Dialog dialog = new Dialog("", skin) {
            @Override
            public void result(Object obj) {
            }
        };
        dialog.setBackground(new TextureRegionDrawable(new TextureRegion(menubg)));

        // Create buttons from images
        Skin skin = new Skin();
        skin.add("button1Image", new TextureRegion(menuPlay));
        skin.add("button2Image", new TextureRegion(menuBack));

        ImageButton button1 = new ImageButton(skin.getDrawable("button1Image"));
        ImageButton button2 = new ImageButton(skin.getDrawable("button2Image"));

        button1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Button 1 clicked!");
            }
        });

        button2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Button 2 clicked!");
            }
        });

        // Add buttons to the dialog
        dialog.getContentTable().add(button1).padTop(80).padBottom(10).row();
        dialog.getContentTable().add(button2).padBottom(10);

        // Set dialog size and position
        dialog.setSize(300, 200);  // Adjust as needed
        dialog.setPosition(Gdx.graphics.getWidth() / 2 - dialog.getWidth() / 2,
            Gdx.graphics.getHeight() / 2 - dialog.getHeight() / 2);

        // Show the dialog on the stage
        dialog.show(stage);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(background, 0, 0, 800, 480);
        game.batch.end();

        stage.act(Gdx.graphics.getDeltaTime()); // Update the stage
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {

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
        stage.dispose();
        skin.dispose();
        background.dispose();
        buttonImage.dispose();
        menuPlay.dispose();
        menuBack.dispose();
        menubg.dispose();
        font.dispose();
    }
}
