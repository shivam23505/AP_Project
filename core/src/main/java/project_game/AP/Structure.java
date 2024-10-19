package project_game.AP;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Structure extends Game {
    public SpriteBatch batch;
    public BitmapFont font;
    public GameScreen gameScreen;
    public Settings settings;
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont(); // use libGDX's default Arial font
        gameScreen=new GameScreen(this);
        settings=new Settings(this);
        this.setScreen(new MainMenuScreen(this));
    }
    public void render() {
        super.render(); // important!
    }

    public void dispose() {
        if (gameScreen!=null){gameScreen.dispose();}
        settings.dispose();
        if(font!=null){font.dispose();}
        batch.dispose();
    }

}
