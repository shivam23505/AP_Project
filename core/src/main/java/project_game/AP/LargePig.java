package project_game.AP;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

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
        currHealth = 15;
    }

    @Override
    public int getHealth() {
        return currHealth;
    }
    static public void drawMe(ArrayList<LargePig>largepig, World world, SpriteBatch batch){

        Iterator<LargePig> iterator4 = largepig.iterator();
        while (iterator4.hasNext()) {
            LargePig b = iterator4.next();
            if(b.sprite==null){
                b.sprite=new Sprite(new Texture("pig1-removebg-preview.png"));
                b.sprite.setSize(b.width, b.height);
                b.sprite.setOriginCenter();

            }
            if (b.MarkForRemoval) {
                iterator4.remove();
                world.destroyBody(b.getBody());
                continue;
            }
            if (b.getBody().getPosition().x + b.getWidth() / 2 > 800) {
                b.getBody().setTransform(800 - b.getWidth() / 2, b.getBody().getPosition().y, b.getBody().getAngle());
            }
            b.sprite.setOriginCenter();
            b.sprite.setPosition(
                b.getBody().getPosition().x - b.getWidth() / 2,
                b.getBody().getPosition().y - b.getHeight() / 2
            );
            b.posX=b.sprite.getX();
            b.posY=b.sprite.getY();
            b.setAngle((float) Math.toDegrees(b.getBody().getAngle()));
            b.sprite.setRotation((float) Math.toDegrees(b.getBody().getAngle()));
            b.sprite.draw(batch);
        }
    }
}
