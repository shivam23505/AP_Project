package project_game.AP;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import java.io.Serializable;

public abstract class Block implements Health, Serializable {
    private static final long serialVersionUID = 1L; // Added for safe serialization

    protected transient Body body; // Marked as transient
    protected float width;
    protected float height;
    protected int currHealth;
    protected boolean MarkForRemoval;
    protected float angle;

    // Store position for recreating the body
    protected float posX;
    protected float posY;

    public Block(Rectangle rect, Body body) {
        this.posX = rect.getX();
        this.posY = rect.getY();
        this.body = body;
        this.width = rect.getWidth();
        this.height = rect.getHeight();

        if (body != null) { // Initialize the physics body if provided
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(width / 2, height / 2);
            FixtureDef fdef = createFixtureDef(shape);
            body.createFixture(fdef);
            shape.dispose();
        }
        MarkForRemoval = false;
    }

    public void destroyMe() {
        MarkForRemoval = true;
    }

    protected abstract FixtureDef createFixtureDef(PolygonShape shape);

    public Body getBody() {
        return body;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void decreaseHealth(int damage) {
        currHealth -= damage;
    }
    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    // Method to recreate the body
    public void recreateBody(Body newBody) {
        this.body = newBody;
        if (newBody != null) {
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(width / 2, height / 2);
            FixtureDef fdef = createFixtureDef(shape);
            newBody.createFixture(fdef);
            shape.dispose();
        }
    }
    public void setAngle(float angle) {
        this.angle=angle;
    }
}
