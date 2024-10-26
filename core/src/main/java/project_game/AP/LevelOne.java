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

import java.util.Objects;

public class LevelOne implements Screen {
    final Structure game;
    private Texture background;
    private OrthographicCamera camera;
    private Viewport viewport;
    private BitmapFont font;

    private FreeTypeFontGenerator fontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    private Stage stage;

    // Tiled map variables
    private TmxMapLoader mapLoader;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer renderer;

    // Box2d Variables
    private World world;
    private Box2DDebugRenderer b2dr;

    //Overlay
    private Stage overlayStage;
    private boolean showOverlay;

    // Constant for tile size (adjust according to your tile size)
    private static final float TILE_SIZE = 16.0f;
    private float elapsed=0;
    private boolean background_changedw=false;
    private boolean background_changedl=false;
    private SpriteBatch batch;
    Pigs pig1;
    Pigs pig2;
    Pigs pig3;
    Birds redBird,yellowBird,blackBird;
    private Texture pig1Texture,pig2Texture,pig3Texture;
    private Texture redTexture,yellowTexture,blackTexture;
    public LevelOne(Structure game) {
        this.game = game;
        background = new Texture("Level_Two_bg.jpg");
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 480, camera);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        overlayStage = new Stage(new ScreenViewport());
        showOverlay = false;

        pig1Texture=new Texture("pig1-removebg-preview.png");
        pig2Texture=new Texture("pig2-removebg-preview.png");
        pig3Texture=new Texture("pig1-removebg-preview.png");
        redTexture = new Texture("redBird.png");
        yellowTexture = new Texture("yellowBird.png");
        blackTexture = new Texture("blackBird.png");
        pig1=new Pigs(pig1Texture);
        pig2=new Pigs(pig2Texture);
        pig3=new Pigs(pig3Texture);
        redBird=new Birds(redTexture);
        yellowBird=new Birds(yellowTexture);
        blackBird=new Birds(blackTexture);


        batch=new SpriteBatch();
        pig1.setPosition(600,140);
        pig1.setSize(40,40);
        pig2.setPosition(655,255);
        pig2.setSize(50,50);
        pig3.setPosition(710,220);
        pig3.setSize(40,40);
        redBird.setSize(50,50);
        redBird.setPosition(90,105);
        yellowBird.setSize(50,50);
        yellowBird.setPosition(45,100);
        blackBird.setSize(50,50);
        blackBird.setPosition(5,105);

        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Arial.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 40;
        fontParameter.borderWidth = 2;
        fontParameter.color = Color.BLACK;
        font = fontGenerator.generateFont(fontParameter);

        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture("pauseButton.png")));
        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.imageUp = drawable;
        buttonStyle.imageDown = new TextureRegionDrawable(new TextureRegion(new Texture("pauseButton_down.png")));
//
        ImageButton pauseButton = new ImageButton(buttonStyle);
//
        pauseButton.setSize(50, 50);

        Table table = new Table();
        table.setFillParent(true);
        table.top().left();
        table.add(pauseButton).size(100,100);
        stage.addActor(table);
        pauseButton.setZIndex(stage.getActors().size-1);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Button clicked");
                showOverlay = true;
                showPauseMenu();
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
    public void showPauseMenu(){
        Texture overlayImageTexture = new Texture(Gdx.files.internal("menubg3.png"));
        Drawable overlayImageDrawable = new TextureRegionDrawable(overlayImageTexture);
        Image overlayImage = new Image(overlayImageDrawable);
        overlayImage.setPosition(100, 0);
        overlayStage.addActor(overlayImage);

        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture("continue_button.png")));
        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.imageUp = drawable;
        buttonStyle.imageDown = new TextureRegionDrawable(new TextureRegion(new Texture("continue_button_down.png")));

        ImageButton continueButton = new ImageButton(buttonStyle);
        continueButton.setSize(90, 90);
//        TextButton button1 = new TextButton("RESUME", skin);
//        button1.setPosition(150, 100);
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showOverlay = false;
            }
        });
//        overlayStage.addActor(continueButton);

        drawable = new TextureRegionDrawable(new TextureRegion(new Texture("exit_button.png")));
        ImageButton.ImageButtonStyle buttonStyle2 = new ImageButton.ImageButtonStyle();
        buttonStyle2.imageUp = drawable;
        buttonStyle2.imageDown = new TextureRegionDrawable(new TextureRegion(new Texture("exit_button_down.png")));
        ImageButton exitButton = new ImageButton(buttonStyle2);
        exitButton.setSize(90, 90);

//        TextButton button2 = new TextButton("QUIT", skin);
//        button2.setPosition(250, 100);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Button clicked");
                game.setScreen(new LevelSelector(game));
                dispose();
            }
        });
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        Label label = new Label("PAUSED",labelStyle);
//        overlayStage.addActor(exitButton);
        Table table2 = new Table();
        table2.setFillParent(true);
//        table2.center();
//        table2.row();
        table2.add(label).padLeft(40).padTop(-90);
        table2.row();
        table2.add(continueButton).padTop(65).padLeft(40).padBottom(25);
        table2.row();
        table2.add(exitButton).center().padLeft(40);

        overlayStage.addActor(table2);
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
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        pig1.render(batch);
        pig2.render(batch);
        pig3.render(batch);
        redBird.render(batch);blackBird.render(batch);yellowBird.render(batch);
        batch.end();
        // Draw UI elements on the stage
//        stage.act();
//        stage.draw();
        stage.act(Gdx.graphics.getDeltaTime()); // Update the stage
        stage.draw();
        if (showOverlay) {
            Gdx.input.setInputProcessor(overlayStage);

            overlayStage.act(Gdx.graphics.getDeltaTime());
            overlayStage.draw();
        }
        else{
            Gdx.input.setInputProcessor(stage);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            if (!background_changedw) {
                LevelSelector.level1complete = true;
                background = new Texture("LevelComplete.jpg");  // Change background
                elapsed = 0;  // Reset the timer
                background_changedw = true;  // Set flag to avoid resetting on every frame
            }
        }

        if (background_changedw) {
            elapsed += delta;
            game.batch.begin();
            game.batch.draw(background, 0, 0, 800, 480);  // Ensure the new background is drawn
            game.batch.end();
            if (elapsed >= 2) {
                game.setScreen(new LevelSelector(game));// Transition to LevelSelector screen
                dispose();
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            if (!background_changedl) {
                background = new Texture("levelfailed.jpeg");  // Change background
                elapsed = 0;  // Reset the timer
                background_changedl = true;  // Set flag to avoid resetting on every frame
            }
        }

        if (background_changedl) {
            elapsed += delta;
            game.batch.begin();
            game.batch.draw(background, 0, 0, 800, 480);  // Ensure the new background is drawn
            game.batch.end();
            if (elapsed >= 2) {
                game.setScreen(new LevelSelector(game));  // Transition to LevelSelector screen
                dispose();
            }
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
        pig1Texture.dispose();
        pig2Texture.dispose();
        pig3Texture.dispose();
        overlayStage.dispose();
        batch.dispose();
    }
}
