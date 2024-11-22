package project_game.AP;
import com.badlogic.gdx.physics.box2d.*;
public class WorldUtils {
    public static void createGround(World world) {
        BodyDef groundDef = new BodyDef();
        groundDef.type = BodyDef.BodyType.StaticBody;
        groundDef.position.set(0, 1);

        Body groundBody = world.createBody(groundDef);

        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(800, 100);

        FixtureDef groundFixture = new FixtureDef();
        groundFixture.shape = groundShape;
        groundBody.createFixture(groundFixture);

        groundShape.dispose();

    }
    public static void createBird(World world) {
        BodyDef birdDef = new BodyDef();
        birdDef.type = BodyDef.BodyType.StaticBody;
        birdDef.position.set(100, 150);

        Body birdBody = world.createBody(birdDef);

        CircleShape birdShape = new CircleShape();
        birdShape.setRadius(100f);

        FixtureDef groundFixture = new FixtureDef();
        groundFixture.shape = birdShape;
        birdBody.createFixture(groundFixture);

        birdShape.dispose();

    }

}
