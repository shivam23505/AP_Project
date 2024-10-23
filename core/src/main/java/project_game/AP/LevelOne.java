package project_game.AP;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Objects;

public class LevelOne implements Screen {
    final Structure game;
    private Texture background;
    private OrthographicCamera camera;
    private Viewport viewport;

    private FreeTypeFontGenerator fontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    private BitmapFont font;
    private Stage stage;
    private Skin skin;
    private Texture pauseButton;

    // Tiled map variables
    private TmxMapLoader mapLoader;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer renderer;

    // Box2d Variables
    private World world;
    private Box2DDebugRenderer b2dr;

    // Constant for tile size (adjust according to your tile size)
    private static final float TILE_SIZE = 16.0f;

    public LevelOne(Structure game) {
        this.game = game;
        background = new Texture("Level_Two_bg.jpg");
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 480, camera);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Arial.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 40;
        fontParameter.borderWidth = 2;
        fontParameter.color = Color.BLACK;
        font = fontGenerator.generateFont(fontParameter);

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        pauseButton = new Texture("pauseButton.png");

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new TextureRegionDrawable(new TextureRegion(pauseButton));

        ImageButton btn = new ImageButton(style);
        btn.setSize(btn.getWidth(), btn.getHeight());

        Table table = new Table();
        table.setFillParent(true);
        table.top();
        table.add(btn).right();
        stage.addActor(table);

        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
            }
        });

        // Load the tilemap and set the renderer with the correct scale
        mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load("Level1at4.tmx");
        renderer = new OrthogonalTiledMapRenderer(tiledMap);

        world = new World(new Vector2(0, 0), true);
        b2dr = new Box2DDebugRenderer();
        System.out.println("Number of layers: " + tiledMap.getLayers().getCount());
        // Iterate over objects in the second layer (adjust layer if necessary)
        for (int layerIndex = 1; layerIndex <= 2; layerIndex++) {
            for (MapObject object : tiledMap.getLayers().get(layerIndex).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();

                // Define the body as a static body
                BodyDef bdef = new BodyDef();
                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((rect.getX() + rect.getWidth() / 2) , (rect.getY() + rect.getHeight() / 2) );

                // Create the body in the Box2D world
                Body body = world.createBody(bdef);

                // Define the shape of the body as a rectangle
                PolygonShape shape = new PolygonShape();
                shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);

                // Fixture definition
                FixtureDef fdef = new FixtureDef();
                fdef.shape = shape;
                body.createFixture(fdef);

                // Dispose the shape after using it
                shape.dispose();
            }
        }
    }

    @Override
    public void show() {
    }

    public void update(float delta) {
        camera.update();
        renderer.setView(camera);
    }

    @Override
    public void render(float delta) {
        // Clear the screen with a black color
        ScreenUtils.clear(0, 0, 0, 1);

        // Step 1: Draw the background using game.batch
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(background, 0, 0, 800, 480);
        game.batch.end();

        // Step 2: Set the camera and update its position
        camera.position.set(400, 240, 0);
        camera.update();

        // Step 3: Render the tile map
        renderer.setView(camera);
        renderer.render();

        // Render Box2D debug lines
        b2dr.render(world, camera.combined);

        // Draw UI elements on the stage
//        stage.act();
//        stage.draw();
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                LevelSelector.level1complete=true;
                game.setScreen(game.levelSelector);
            }
        }


    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        if(background!=null){background.dispose();}
        if(tiledMap!=null){tiledMap.dispose();}
        if(renderer!=null){renderer.dispose();}
        if(world!=null){world.dispose();}
        if(b2dr!=null){b2dr.dispose();}
    }
}
