package project_game.AP;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;

public class Birds{
    protected Sprite sprite;
    public static int damage;
    public Birds(Texture texture , int damage){
        sprite=new Sprite(texture);
        Birds.damage=damage;
        sprite.setOriginCenter();
        sprite.setSize(60,60);
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
    public Texture getTexture(){
        return sprite.getTexture();
    }
    public void setTexture(Texture texture, int damage){
        Texture t = getTexture();
        if(t!=texture){
        sprite.setTexture(texture);
        sprite.setSize(60,60);
        sprite.setOriginCenter();
        Birds.damage=damage;
        t.dispose();}
    }
    public int getDamage(){
        return Birds.damage;
    }


}
