package project_game.AP;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;


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
    public void drawTrajectory(float x, float y,Vector2 startPoint,float PPM,World world,ShapeRenderer shapeRenderer) {
        float stx = startPoint.x - x;
        float sty = startPoint.y - y;
        Vector2 initialVelocity = new Vector2((startPoint.x + 50 - x) * 7 / PPM,
            (startPoint.y + 50 - y) * 3 / PPM);

        Vector2 position = new Vector2(x / PPM, y / PPM);

        float timeStep = 0.1f;

        Vector2 gravity = world.getGravity().scl(1 / PPM);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);

        for (int i = 0; i < 30; i++) {
            shapeRenderer.circle(position.x * PPM, position.y * PPM, 5);
            position.x += initialVelocity.x * timeStep;
            position.y += initialVelocity.y * timeStep;
            initialVelocity.add(gravity.x * timeStep, gravity.y * timeStep);
        }
        shapeRenderer.end();
    }

}
