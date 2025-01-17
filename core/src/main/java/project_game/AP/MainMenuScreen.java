package project_game.AP;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MainMenuScreen implements Screen {
    final Structure game;
    OrthographicCamera camera;
    private Texture Background;
    private Texture play;
    static Music music;
    public MainMenuScreen(final Structure game) {
        this.game = game;
        Background=new Texture("AngryBirdsBG2.jpg");
        play=new Texture("play.png");
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        if (Settings.isOn){
            playMusic();
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        //We use game's spriteBatch
        game.batch.begin();
        game.batch.draw(Background,0,0,800,480);
        game.batch.draw(play,300,50,200,75);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            Vector3 touchPos=new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(),0);
            camera.unproject(touchPos);
            if(touchPos.x>300 && touchPos.x<500 && touchPos.y>50 && touchPos.y<125){
                game.setScreen(new GameScreen(game));
                dispose();
            }
        }
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
        if (Background!=null){Background.dispose();}
        if (play!=null){play.dispose();}
    }
    public static void playMusic(){
        music.setLooping(true);
        music.setVolume(.5f);
        music.play();
    }
    public static void stopMusic(){
        music.stop();
    }
}
