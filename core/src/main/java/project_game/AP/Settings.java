package project_game.AP;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class Settings implements Screen {
    final Structure game;
    private Stage stage;
    Texture background;
    Texture button;
    private Texture buttonOnTexture, buttonOffTexture;
    private ImageButton onOffButton;
    private boolean isOn = false;
    private FreeTypeFontGenerator fontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    private BitmapFont font;
    Texture settingsBG;
    OrthographicCamera camera;
    public Settings(final Structure game){
        this.game=game;
        stage=new Stage(new ScreenViewport());
        background=new Texture("background.png");
        button=new Texture("button.png");
        settingsBG=new Texture("settings page.jpeg");
        buttonOnTexture = new Texture("onbutton.jpg");
        buttonOffTexture = new Texture("offbutton.png");
        onOffButton = new ImageButton(
            new TextureRegionDrawable(buttonOffTexture),
            new TextureRegionDrawable(buttonOnTexture)
        );
        onOffButton.setSize(75,30);
        onOffButton.setPosition(350, 200);

        onOffButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isOn = !isOn;
                if (isOn) {
                    onOffButton.getStyle().imageUp = new TextureRegionDrawable(buttonOnTexture);  // Show "on" texture
                } else {
                    onOffButton.getStyle().imageUp = new TextureRegionDrawable(buttonOffTexture);  // Show "off" texture
                }
            }
        });

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        Gdx.input.setInputProcessor(stage);
        fontGenerator=new FreeTypeFontGenerator(Gdx.files.internal("BronacoDemoRegular.ttf"));
        fontParameter=new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size=40;
        fontParameter.borderWidth=3;
        fontParameter.borderColor= Color.YELLOW;
        fontParameter.color=Color.BLACK;
        font=fontGenerator.generateFont(fontParameter);
        stage.addActor(onOffButton);
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
        game.batch.draw(background,0,0,800,480);
        game.batch.draw(settingsBG,50,50,700,380);
        float backButtonX=350;
        float backButtonY=100;
        game.batch.draw(button,backButtonX,backButtonY,200,75);
        font.draw(game.batch,"Sound",200,235);
        font.draw(game.batch, "Back", backButtonX-50,backButtonY+50);
        if (Gdx.input.isTouched()) {
            Vector3 touchPos=new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(),0);
            camera.unproject(touchPos);
            if(touchPos.x>350 && touchPos.x<550 && touchPos.y>100 && touchPos.y<175){
                game.setScreen(new GameScreen(game));
//                dispose();
            }
        }

        stage.act();
        stage.draw();
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

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
        background.dispose();
        fontGenerator.dispose();
        stage.dispose();
    }
}
