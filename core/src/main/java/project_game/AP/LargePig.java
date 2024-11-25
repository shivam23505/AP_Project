package project_game.AP;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class LargePig extends Pigs {
    public Sprite sprite;
    public static int GiveDamage;
    public LargePig(Ellipse ellipse, Body body1){
        super(ellipse,body1);
        sprite = new Sprite(new Texture("pig2-removebg-preview.png"));
        sprite.setSize(width,height);
        sprite.setOriginCenter();
        GiveDamage = 1;
        setHealth();
    }

    @Override
    protected FixtureDef createFixtureDef(CircleShape shape){
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.density=1.75f;
        fdef.friction=0.4f;
        fdef.restitution=0f;
        return fdef;
    }

    @Override
    public void setHealth() {
        currHealth = 7;
    }

    @Override
    public int getHealth() {
        return currHealth;
    }


}
