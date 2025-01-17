package project_game.AP;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Objects;

public class LoadGame implements Screen {
    final Structure game;
    OrthographicCamera camera;
    private Texture background,buttonImage,menubg,buttondown;
    private Texture menuPlay,menuBack;
    private Stage stage;
    private Skin skin;
    private BitmapFont font,btnFont;
    private FreeTypeFontGenerator fontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    private FreeTypeFontGenerator buttonFontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter buttonFontParameter;
    private Texture levelComplete,levelCantAttempt;
    private ImageTextButton levelOne,levelTwo,levelThree;
    public static boolean game1available=false;
    public static boolean game2available=false;
    public static boolean game3available=false;

    public LoadGame(Structure game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        background = new Texture("background.png");
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        font = new BitmapFont();
        buttonImage = new Texture("button.png");
        menubg = new Texture("menubg3.png");
        menuPlay = new Texture("menu_play_btn.png");
        menuBack = new Texture("menu_back_btn.png");
        buttondown = new Texture("button_down.png");

        if (Gdx.files.local("savedGame1.ser").exists()){
            game1available = true;
        }
        if (Gdx.files.local("savedGame2.ser").exists()){
            game2available = true;
        }
        if (Gdx.files.local("savedGame3.ser").exists()){
            game3available = true;
        }

        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("BronacoDemoRegular.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 40;
        fontParameter.borderWidth = 3;
        fontParameter.borderColor = Color.YELLOW;
        fontParameter.color = Color.BLACK;
        font = fontGenerator.generateFont(fontParameter);

        buttonFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Arial.ttf"));
        buttonFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        buttonFontParameter.size = 20;
        buttonFontParameter.color = Color.BLACK;
        btnFont = buttonFontGenerator.generateFont(buttonFontParameter);

        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture("backScreen.png")));
        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.imageUp = drawable;
        buttonStyle.imageDown = new TextureRegionDrawable(new TextureRegion(new Texture("backScreen_down.png")));
//
        ImageButton backScreen = new ImageButton(buttonStyle);
        backScreen.setSize(50,50);
        backScreen.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
                dispose();
            }
        });
        Table mytable = new Table();
        mytable.setFillParent(true);
        mytable.bottom().left().add(backScreen).size(50,50);
        stage.addActor(mytable);

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        ImageTextButton.ImageTextButtonStyle style = new ImageTextButton.ImageTextButtonStyle();
        style.up = new TextureRegionDrawable(new TextureRegion(buttonImage)); // Set button background
        style.down = new TextureRegionDrawable(new TextureRegion(buttondown));
        style.font = btnFont;

        levelComplete = new Texture("levelcompletedgreen.jpeg");
        levelCantAttempt = new Texture("levelcompletedgrey.jpeg");
        levelOne = new ImageTextButton("Game 1", style);
        levelTwo = new ImageTextButton("Game 2", style);
        levelThree = new ImageTextButton("Game 3", style);

        float buttonWidth = 100f;
        float buttonHeight = 50f;
        levelOne.setSize(buttonWidth, buttonHeight);
        levelOne.getLabelCell().minSize(buttonWidth, buttonHeight);
        levelOne.getLabelCell().prefSize(buttonWidth, buttonHeight);

        levelTwo.setSize(buttonWidth, buttonHeight);
        levelTwo.getLabelCell().minSize(buttonWidth, buttonHeight);
        levelTwo.getLabelCell().prefSize(buttonWidth, buttonHeight);

        levelThree.setSize(buttonWidth, buttonHeight);
        levelThree.getLabelCell().minSize(buttonWidth, buttonHeight);
        levelThree.getLabelCell().prefSize(buttonWidth, buttonHeight);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        Label selectLevelLabel = new Label("Saved Games", labelStyle);

        Table table = new Table();
        table.setFillParent(true);

        table.top();
        table.add(selectLevelLabel).padTop(-100).padBottom(100).colspan(3).center();  // Center the label at the top and add some padding
        table.row();
        table.add(levelOne).size(100, 50).pad(10);
        table.add(levelTwo).size(100, 50).pad(10);
        table.add(levelThree).size(100, 50).pad(10);
        table.center();
        table.invalidate();

        stage.addActor(table);

        levelOne.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game1available){
                    showMenuPopup("Level 1");
                }
            }
        });
        levelTwo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game2available){
                    showMenuPopup("Level 2");
                }
            }
        });
        levelThree.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game3available){
                    showMenuPopup("Level 3");
                }
            }
        });
    }

    private void showMenuPopup(String levelName) {
        Dialog dialog = new Dialog("", skin) {
            @Override
            public void result(Object obj) {
            }
        };
        dialog.setBackground(new TextureRegionDrawable(new TextureRegion(menubg)));

        Skin popupskin = new Skin();
        popupskin.add("button1Image", new TextureRegion(menuPlay));
        popupskin.add("button2Image", new TextureRegion(menuBack));

        TextButton.TextButtonStyle topStyle = new TextButton.TextButtonStyle();
        topStyle.font = btnFont;
        topStyle.up = new TextureRegionDrawable(new TextureRegion(buttonImage));
//        topStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture("button_down.png")));


        TextButton btn = new TextButton(levelName, topStyle);
        btn.setSize(btn.getWidth(), btn.getHeight());

        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture("menu_play_btn.png")));

        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.imageUp = drawable;
        buttonStyle.imageDown = new TextureRegionDrawable(new TextureRegion(new Texture("menu_play_btn_down.png")));

        drawable = new TextureRegionDrawable(new TextureRegion(new Texture("menu_back_btn.png")));

        ImageButton.ImageButtonStyle buttonStyle2 = new ImageButton.ImageButtonStyle();
        buttonStyle2 = new ImageButton.ImageButtonStyle();
        buttonStyle2.imageUp = drawable;
        buttonStyle2.imageDown = new TextureRegionDrawable(new TextureRegion(new Texture("menu_back_btn_down.png")));

//
        ImageButton button1 = new ImageButton(buttonStyle);
        ImageButton button2 = new ImageButton(buttonStyle2);

        button1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Objects.equals(levelName, "Level 1") && game1available){
                    SavedLevel savedLevel=null;
                    try {
                        FileInputStream fis = new FileInputStream("savedGame1.ser");
                        ObjectInputStream ois = new ObjectInputStream(fis);
                        savedLevel = (SavedLevel) ois.readObject();
                    }
                    catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (savedLevel.getLevelNo() == 1){
                        LevelOne inGameScreen = new LevelOne(savedLevel, game);
                        dispose();
                        game.setScreen(inGameScreen);
                    }
                    else if (savedLevel.getLevelNo() == 2){
                        LevelTwo inGameScreen = new LevelTwo(savedLevel, game);
                        dispose();
                        game.setScreen(inGameScreen);
                    }
                    else if (savedLevel.getLevelNo() == 3){
                        LevelThree inGameScreen = new LevelThree(savedLevel, game);
                        dispose();
                        game.setScreen(inGameScreen);
                    }

                }
                else if (Objects.equals(levelName, "Level 2") && game2available){
                    SavedLevel savedLevel=null;
                    try {
                        FileInputStream fis = new FileInputStream("savedGame2.ser");
                        ObjectInputStream ois = new ObjectInputStream(fis);
                        savedLevel = (SavedLevel) ois.readObject();
                    }
                    catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (savedLevel.getLevelNo() == 1){
                        LevelOne inGameScreen = new LevelOne(savedLevel, game);
                        dispose();
                        game.setScreen(inGameScreen);
                    }
                    else if (savedLevel.getLevelNo() == 2){
                        LevelTwo inGameScreen = new LevelTwo(savedLevel, game);
                        dispose();
                        game.setScreen(inGameScreen);
                    }
                    else if (savedLevel.getLevelNo() == 3){
                        LevelThree inGameScreen = new LevelThree(savedLevel, game);
                        dispose();
                        game.setScreen(inGameScreen);
                    }

                }
                else if (Objects.equals(levelName, "Level 3") && game3available){
                    SavedLevel savedLevel=null;
                    try {
                        FileInputStream fis = new FileInputStream("savedGame3.ser");
                        ObjectInputStream ois = new ObjectInputStream(fis);
                        savedLevel = (SavedLevel) ois.readObject();
                    }
                    catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (savedLevel.getLevelNo() == 1){
                        LevelOne inGameScreen = new LevelOne(savedLevel, game);
                        dispose();
                        game.setScreen(inGameScreen);
                    }
                    else if (savedLevel.getLevelNo() == 2){
                        LevelTwo inGameScreen = new LevelTwo(savedLevel, game);
                        dispose();
                        game.setScreen(inGameScreen);
                    }
                    else if (savedLevel.getLevelNo() == 3){
                        LevelThree inGameScreen = new LevelThree(savedLevel, game);
                        dispose();
                        game.setScreen(inGameScreen);
                    }
                }

            }
        });

        button2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Button 2 clicked!");
                dialog.hide();
            }
        });

        Table table2 = new Table();
        table2.setFillParent(true);
        table2.add(btn).align(Align.top).colspan(2).padBottom(80).padTop(-10);
        table2.row();
        table2.add(button1).padBottom(10);
        table2.row();
        table2.add(button2).padTop(10);

        dialog.getContentTable().add(table2).fill().expand();

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
        if (skin!=null){skin.dispose();}
        if (background!=null){background.dispose();}
        if (buttonImage!=null){buttonImage.dispose();}
        if (menuPlay!=null){menuPlay.dispose();}
        if (menuBack!=null){menuBack.dispose();}
        if (menubg!=null){menubg.dispose();}
        if (btnFont!=null){btnFont.dispose();}
        if (fontGenerator!=null){fontGenerator.dispose();}
        if (buttonFontGenerator!=null){buttonFontGenerator.dispose();}
        if (font!=null){font.dispose();}
        if (stage!=null){stage.dispose();}
    }
}
