package project_game.AP;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import org.junit.jupiter.api.Test;
import org.w3c.dom.css.Rect;

public class Testing {
    // Initialize Box2D World
    World world = new World(new Vector2(0, -9.81f), false);


    @Test
    void PigsTesting() {
        // Create a dynamic body definition
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(10, 10); // Center position of the ellipse (adjust as needed)

        // Create the body in the Box2D world
        Body body = world.createBody(bdef);

        // Create an ellipse-like body using a CircleShape
        CircleShape shape = new CircleShape();
        shape.setRadius(15f); // Radius for a circular approximation

        // Define a fixture and attach the shape to the body
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.density = 1.0f;
        body.createFixture(fdef);

        // Dispose of the shape after creating the fixture
        shape.dispose();

        // Test `Pigs` class with the created body
        Pigs pig = new Pigs(body) {
            private int health = 100; // Example health value

            @Override
            protected FixtureDef createFixtureDef(CircleShape shape) {
                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.shape = shape;
                fixtureDef.density = 1.0f;
                return fixtureDef;
            }

            @Override
            public void setHealth() {
                health = 50;
            }

            @Override
            public int getHealth() {
                return health;
            }
        };
        assert pig.getHealth() > 0;
        assert body.getPosition().x == 10; // Example position check
    }
    @Test
    void BlockTesting(){
        Rectangle rect=new Rectangle(10,10,25,25);
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set((rect.getX() + rect.getWidth() / 2) , (rect.getY() + rect.getHeight() / 2) );

        // Create the body in the Box2D world
        Body body = world.createBody(bdef);
        Block block=new Block(rect,body) {
            @Override
            protected FixtureDef createFixtureDef(PolygonShape shape) {
                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.shape = shape;
                fixtureDef.density = 1.0f;
                return fixtureDef;
            }

            @Override
            public void setHealth() {
                currHealth=10;
            }

            @Override
            public int getHealth() {
                return currHealth;
            }
        };
        assert block.currHealth>=0;
        assert !block.MarkForRemoval;
    }
}
