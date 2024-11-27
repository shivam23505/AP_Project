package project_game.AP;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class Wood extends Block implements Serializable {
    public String spriteTexturePath="wooden_textureAB.png";// Store the texture path for serialization
    transient public Sprite sprite=new Sprite(new Texture(spriteTexturePath));
    public static int GiveDamage;
    public Wood(Rectangle rect,Body body1){
        super(rect,body1);
        initializeSprite();
        GiveDamage = 1;
        setHealth();
    }
    private void initializeSprite() {
        sprite = new Sprite(new Texture(spriteTexturePath));
        sprite.setSize(width, height);
        sprite.setOriginCenter();
    }

    @Override
    protected FixtureDef createFixtureDef(PolygonShape shape){
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.density=1.5f;
        fdef.friction=0.4f;
        fdef.restitution=0f;
        return fdef;
    }

    @Override
    public void setHealth() {
        currHealth = 25;
    }

    @Override
    public int getHealth() {
        return currHealth;
    }

    static public void drawMe(ArrayList<Wood>wood,World world, SpriteBatch batch){
        Iterator<Wood> iterator = wood.iterator();
        while (iterator.hasNext()) {
            Wood b = iterator.next();
            if(b.sprite==null){
                b.sprite=new Sprite(new Texture("wooden_TextureAB.png"));
                b.sprite.setSize(b.width, b.height);
                b.sprite.setOriginCenter();
            }
            if (b.MarkForRemoval) {
                iterator.remove();
                world.destroyBody(b.getBody());
                continue;
            }
            float screenRight = 800;
            if (b.getBody().getPosition().x + b.getWidth() / 2 > screenRight) {
                b.getBody().setTransform(screenRight - b.getWidth() / 2, b.getBody().getPosition().y, b.getBody().getAngle());
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
