package project_game.AP;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Slingshot {
    private Sprite sprite;
    public Slingshot(Texture texture){
        sprite=new Sprite(texture);
    }
    public void setPosition(float x, float y) {
        sprite.setPosition(x, y);
    }
    public void setSize(float x,float y){
        sprite.setSize(x,y);
    }
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

}
