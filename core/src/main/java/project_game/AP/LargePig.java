package project_game.AP;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import java.io.IOException;
import java.io.Serializable;

public class LargePig extends Pigs implements Serializable {
    private static final long serialVersionUID = 1L;

    transient public Sprite sprite;
    public String spriteTexturePath;
    public static int GiveDamage;

    public LargePig(Ellipse ellipse, Body body1) {
        super(ellipse, body1);
        this.spriteTexturePath = "pig2-removebg-preview.png";
        initializeSprite(); // Initialize the sprite
        GiveDamage = 1;
        setHealth();
    }

    private void initializeSprite() {
        sprite = new Sprite(new Texture(spriteTexturePath));
        sprite.setSize(width, height);
        sprite.setOriginCenter();
    }

    @Override
    protected FixtureDef createFixtureDef(CircleShape shape) {
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.density = 1.75f;
        fdef.friction = 0.4f;
        fdef.restitution = 0f;
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
