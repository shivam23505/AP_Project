package project_game.AP;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class Pigs implements Health{
    protected Body body;
    protected float width;
    protected float height;
    protected int currHealth;
    protected boolean MarkForRemoval;

    private Sprite sprite;
    public Pigs(Ellipse ellipse, Body body){
        this.body = body;
        this.width = ellipse.width;
        this.height = ellipse.height;

        CircleShape shape = new CircleShape();
        shape.setRadius(width/2);
        FixtureDef fdef = createFixtureDef(shape);
        body.createFixture(fdef);
        shape.dispose();
        MarkForRemoval = false;
    }


    public void destroyMe(){
        MarkForRemoval = true;
    }

    protected abstract FixtureDef createFixtureDef(CircleShape shape);
    public Body getBody() {
        return body;
    }
    public float getWidth() {
        return width;
    }
    public float getHeight() {
        return height;
    }
    public void decreaseHealth(int damage){
        currHealth-=damage;
    }

}
