package project_game.AP;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Objects;

public class Level2 implements Screen {
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

    //Tiled map variables
    private TmxMapLoader mapLoader;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d Variables
    private World world;
    private Box2DDebugRenderer b2dr;

    public Level2(Structure game) {
        this.game = game;
        background = new Texture("Level_Two_bg.jpg");
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
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
        btn.setSize(btn.getWidth(),btn.getHeight());

        Table table = new Table();
        table.setFillParent(true);

        table.top();
        table.add(btn).right();
        stage.addActor(table);

        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
//                showPauseMenu();
                dispose();
            }
        });
        mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load("Level1at1.tmx");
        renderer = new OrthogonalTiledMapRenderer(tiledMap);

        world = new World(new Vector2(0,0),true);
        b2dr = new Box2DDebugRenderer();

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        for (MapObject object : tiledMap.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(rect.getX() + rect.getWidth() / 2, rect.y + rect.getHeight() / 2);
            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
            fdef.shape = shape;
            body.createFixture(fdef);
        }
    }

    @Override
    public void show() {

    }
    public void update(float delta){
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
        game.batch.draw(background, 0, 0, 800, 480);  // Draw background at (0, 0)
        game.batch.end();  // End the background batch

//        // Step 2: Set the camera and translate the map rendering position
        camera.position.set(400, 240, 0);  // Move camera to (200, 100) over the original position
        camera.update();  // Update the camera with the new position

        // Step 3: Render the tile map at the new camera position
        renderer.setView(camera);  // Set camera view for the renderer
        renderer.render();  // Render the tile map (no need to manually call begin/end on the renderer's batch)

        b2dr.render(world, camera.combined);
    }



    @Override
    public void resize(int i, int i1) {
        viewport.update(i,i1);
//        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0); // Center the camera

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
        background.dispose();
        tiledMap.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
    }
}
