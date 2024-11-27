package project_game.AP;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.math.Ellipse;

import java.io.Serializable;

public abstract class Pigs implements Health, Serializable {
    private static final long serialVersionUID = 1L; // Added for safe serialization

    protected transient Body body; // Marked as transient
    protected float width;
    protected float height;
    protected int currHealth;
    protected boolean MarkForRemoval;

    // Store position for recreating the body
    protected float posX;
    protected float posY;
    protected float angle;

    public Pigs(Ellipse ellipse, Body body) {
        this.posX = ellipse.x;  // Store the X position of the ellipse
        this.posY = ellipse.y;  // Store the Y position of the ellipse
        this.body = body;
        this.width = ellipse.width;
        this.height = ellipse.height;

        if (body != null) { // Initialize the physics body if provided
            CircleShape shape = new CircleShape();
            shape.setRadius(width / 2);
            FixtureDef fdef = createFixtureDef(shape);
            body.createFixture(fdef);
            shape.dispose();
        }
        MarkForRemoval = false;
    }

    public void destroyMe() {
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

    public void decreaseHealth(int damage) {
        currHealth -= damage;
    }

    // Getter for position
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
            CircleShape shape = new CircleShape();
            shape.setRadius(width / 2);
            FixtureDef fdef = createFixtureDef(shape);
            newBody.createFixture(fdef);
            shape.dispose();
        }
    }
    public void setAngle(float angle) {
        this.angle=angle;
    }
}
