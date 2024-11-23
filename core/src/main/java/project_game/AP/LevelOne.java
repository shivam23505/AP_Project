package project_game.AP;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
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
import com.badlogic.gdx.math.MathUtils;
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

import java.util.ArrayList;
import java.util.Objects;

public class LevelOne implements Screen {
    private Box2DDebugRenderer debugRenderer;
    final Structure game;
    private Texture background;
    private OrthographicCamera camera;
    private Viewport viewport;
    private BitmapFont font;
    private SpriteBatch batch;

    //Font Varibales
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

    //PIGS BIRD SLIGNSHOT TEXTURES
    Pigs pig1;
    Pigs pig2;
    Pigs pig3;
    Birds redBird,yellowBird,blackBird;
    Slingshot slingshot;
    private Texture pig1Texture,pig2Texture,pig3Texture;
    private Sprite redTexture;
    private Sprite woodTexture,concreteTexture;
    private Texture yellowTexture,blackTexture;
    private Texture sling;

    private Drawable overlayDrawable;

    private Texture wood_texture,concrete_texture;

    //WORLD BODIES ARRAY LISTS
    ArrayList<Wood> wood=new ArrayList<Wood>();
    ArrayList<Float> wood_width=new ArrayList<Float>();
    ArrayList<Float> wood_height=new ArrayList<Float>();
    ArrayList<Concrete> concrete=new ArrayList<Concrete>();
    ArrayList<Float> concrete_width=new ArrayList<Float>();
    ArrayList<Float> concrete_height=new ArrayList<Float>();

    //MOVIG PROJECTILE BODY VARIABLES
    Body projectileBody,movingbody;
    private static final float PPM = 100f; // Pixels per meter
    private Vector2 startPoint = new Vector2(150,180); // Drag start
    private Vector2 endPoint = new Vector2();   // Drag end
    private boolean isDragging = false;

    public LevelOne(Structure game) {
        this.game = game;
        background = new Texture("Level_Two_bg.jpg");
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 480, camera);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        overlayStage = new Stage(new ScreenViewport());
        showOverlay = false;

        //PIGS BIRD SLINGSHOT INITIALIZATION
        pig1Texture=new Texture("pig1-removebg-preview.png");
        pig2Texture=new Texture("pig2-removebg-preview.png");
        pig3Texture=new Texture("pig1-removebg-preview.png");
        redTexture = new Sprite(new Texture("redBird.png"));
        redTexture.setSize(30 * 2 , 30 * 2 );
        yellowTexture = new Texture("yellowBird.png");
        blackTexture = new Texture("blackBird.png");
        wood_texture=new Texture("wooden_textureAB.png");
        woodTexture = new Sprite(new Texture("wooden_textureAB.png"));
        concrete_texture=new Texture("concrete_blockAB.jpeg");
        concreteTexture=new Sprite(new Texture("concrete_blockAB.jpeg"));
        sling =new Texture("slingshot.png");
        pig1=new Pigs(pig1Texture);
        pig2=new Pigs(pig2Texture);
        pig3=new Pigs(pig3Texture);
        redBird=new Birds(redTexture.getTexture());
        yellowBird=new Birds(yellowTexture);
        blackBird=new Birds(blackTexture);
        slingshot = new Slingshot(sling);

        //PIGS BIRD SLINGSHOT POSITION SIZE SETTING
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
        slingshot.setSize(100,100);
        slingshot.setPosition(130,105);

        //STAGE BUTTON
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Arial.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 40;
        fontParameter.borderWidth = 2;
        fontParameter.color = Color.WHITE;
        font = fontGenerator.generateFont(fontParameter);

        Texture transparentPixel = createTransparentPixel(0f, 0f, 0f, 0.5f);
        overlayDrawable = new TextureRegionDrawable(new TextureRegion(transparentPixel));

        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture("pauseButton.png")));
        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.imageUp = drawable;
        buttonStyle.imageDown = new TextureRegionDrawable(new TextureRegion(new Texture("pauseButton_down.png")));
        ImageButton pauseButton = new ImageButton(buttonStyle);
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

        //TILE MAP AND BOX2D WORLD CODE
        // Load the tilemap and set the renderer with the correct scale
        mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load("Level1at4.tmx");
        renderer = new OrthogonalTiledMapRenderer(tiledMap);

        world = new World(new Vector2(0, -9.81f), false);
        b2dr = new Box2DDebugRenderer();
        WorldUtils.createGround(world);

//        System.out.println("Number of layers: " + tiledMap.getLayers().getCount());
        // Iterate over objects in the second layer (adjust layer if necessary)
        for (int layerIndex = 1; layerIndex <= 2; layerIndex++) {
            for (MapObject object : tiledMap.getLayers().get(layerIndex).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                BodyDef bdef = new BodyDef();
                bdef.type = BodyDef.BodyType.DynamicBody;
                bdef.position.set((rect.getX() + rect.getWidth() / 2) , (rect.getY() + rect.getHeight() / 2) );

                // Create the body in the Box2D world
                Body body = world.createBody(bdef);

//                 Define the shape of the body as a rectangle
//                PolygonShape shape = new PolygonShape();
//                shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
                body.setAwake(true);
//                 Fixture definition
                if(layerIndex==1){
//                    FixtureDef fdef = new FixtureDef();
//                    fdef.shape = shape;
//                    fdef.density=1f;
//                    fdef.friction=0.4f;
//                    fdef.restitution=0f;
//                    body.createFixture(fdef);

                    // Dispose the shape after using it

                    wood.add(new Wood(rect,body));
//                    wood_width.add(rect.getWidth());
//                    wood_height.add(rect.getHeight());
                }
                if(layerIndex==2){
//                    FixtureDef fdef = new FixtureDef();
//                    fdef.shape = shape;
//                    fdef.density=2f;
//                    fdef.friction=0.4f;
//                    fdef.restitution=0f;
//                    body.createFixture(fdef);
//
//                    // Dispose the shape after using it
//                    shape.dispose();


                    concrete.add(new Concrete(rect,body));
//                    concrete_height.add(rect.getHeight());
//                    concrete_width.add(rect.getWidth());
                }
            }
        }
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(90,105);

        projectileBody = world.createBody(bodyDef);
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(30f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.3f;
        projectileBody.createFixture(fixtureDef);
        projectileBody.setAwake(false);
        circleShape.dispose();

    }
    public void showPauseMenu(){
//        Texture overlayImageTexture = new Texture(Gdx.files.internal("menubg3.png"));
//        Drawable overlayImageDrawable = new TextureRegionDrawable(overlayImageTexture);
//        Image overlayImage = new Image(overlayImageDrawable);
//        overlayImage.setPosition(100, 0);
//        overlayStage.addActor(overlayImage);

        Table backgroundTable = new Table();
        backgroundTable.setFillParent(true);
        backgroundTable.setBackground(overlayDrawable);  // Set the transparent background
        overlayStage.addActor(backgroundTable);

        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture("continue_button.png")));
        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.imageUp = drawable;
        buttonStyle.imageDown = new TextureRegionDrawable(new TextureRegion(new Texture("continue_button_down.png")));

        ImageButton continueButton = new ImageButton(buttonStyle);
        continueButton.setSize(90, 90);
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showOverlay = false;
            }
        });
//        overlayStage.addActor(continueButton);

        drawable = new TextureRegionDrawable(new TextureRegion(new Texture("menu_save_btn.png")));
        ImageButton.ImageButtonStyle buttonStyle3 = new ImageButton.ImageButtonStyle();
        buttonStyle3.imageUp = drawable;
        ImageButton saveButton = new ImageButton(buttonStyle3);
        saveButton.setSize(90, 90);

        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Clicked! GAME STATE SAVED!!");
            }
        });

        drawable = new TextureRegionDrawable(new TextureRegion(new Texture("exit_button.png")));
        ImageButton.ImageButtonStyle buttonStyle2 = new ImageButton.ImageButtonStyle();
        buttonStyle2.imageUp = drawable;
        buttonStyle2.imageDown = new TextureRegionDrawable(new TextureRegion(new Texture("exit_button_down.png")));
        ImageButton exitButton = new ImageButton(buttonStyle2);
        exitButton.setSize(90, 90);

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

        Table table2 = new Table();
        table2.setFillParent(true);

        table2.add(label).padLeft(40).padTop(-10);
        table2.row();
        table2.add(continueButton).padTop(65).padLeft(40).padBottom(25);
        table2.row();
        table2.add(saveButton).center().padLeft(40).padBottom(25);
        table2.row();
        table2.add(exitButton).center().padLeft(40);

        overlayStage.addActor(table2);
    }
    private Texture createTransparentPixel(float r, float g, float b, float alpha) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(r, g, b, alpha);  // Set RGBA for transparency
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    @Override
    public void show() {
        debugRenderer = new Box2DDebugRenderer();
    }

    public void update(float delta) {
        camera.update();
        world.step(1/60f,6,2);
        renderer.setView(camera);
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void render(float delta) {
        update(delta);
        // Clear the screen with a black color
        ScreenUtils.clear(0, 0, 0, 1);


        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(background, 0, 0, 800, 480);
        game.batch.end();

        // Step 2: Set the camera and update its position
        camera.position.set(400, 240, 0);
        camera.update();

        // Render the tile map
        renderer.setView(camera);
        renderer.render();

        // RENDER BOX2D BODY LINES -------------------------------------------
        b2dr.render(world, camera.combined);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        pig1.render(batch);
        pig2.render(batch);
        pig3.render(batch);
        blackBird.render(batch);yellowBird.render(batch);
        slingshot.render(batch);
        int count=0;
        for(Body b:wood){
            woodTexture.setSize(wood_width.get(count),wood_height.get(count));
            woodTexture.setPosition((b.getPosition().x)-(wood_width.get(count))/2, b.getPosition().y-(wood_height.get(count))/2);
            woodTexture.setRotation((float) Math.toDegrees(b.getAngle()));
            woodTexture.draw(batch);
            count++;
        }
        count=0;
        for(Body b:concrete){
            concreteTexture.setSize(concrete_width.get(count),concrete_height.get(count));
            concreteTexture.setPosition((b.getPosition().x)-(concrete_width.get(count))/2, b.getPosition().y-(concrete_height.get(count))/2);
            concreteTexture.setRotation((float) Math.toDegrees(b.getAngle()));
            concreteTexture.draw(batch);
            count++;
        }
        batch.end();
        //-------------------------------------------------------------------

        debugRenderer.render(world, camera.combined);

        //PAUSE BUTTON INPUT PROCESSOR HANDLE -----------------
        if (showOverlay) {
            Gdx.input.setInputProcessor(overlayStage);

            overlayStage.act(Gdx.graphics.getDeltaTime());
            overlayStage.draw();
        }
        else{
            Gdx.input.setInputProcessor(stage);
            overlayStage.clear();
        }
        //---------------------------------------------------

        //LAUNCH BIRD INPUT DRAGGING HANDLING------------------------------------
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                projectileBody.setAwake(false);
                projectileBody.setTransform(startPoint.x,startPoint.y,0);
                isDragging = true;
                return true;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {

                int maxDistance = 50; // radius of the circle
                float centerX = startPoint.x;  // x-coordinate of the circle center
                float centerY = startPoint.y;  // y-coordinate of the circle center
                if (isDragging) {
                    Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));
                    float dx = worldCoords.x - centerX;
                    float dy = worldCoords.y - centerY;
                    float distance = (float) Math.sqrt(dx * dx + dy * dy);
                    if (distance > maxDistance) {
                        float scale = maxDistance / distance; // Scale factor to bring the point within the circle
                        worldCoords.x = centerX + dx * scale;
                        worldCoords.y = centerY + dy * scale;
                    }
                    projectileBody.setTransform(worldCoords.x, worldCoords.y, 0);
                }
                return true;
            }
//
            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if (isDragging) {
                    isDragging = false;
                    projectileBody.setAwake(true);
                    Vector2 position = new Vector2(projectileBody.getPosition()); // Get current position
                    float angle = projectileBody.getAngle();         // Get current rotation
                    projectileBody.setTransform(position.x, position.y, angle);
//                    Vector2 velocity = new Vector2(startPoint).sub(position).scl(5f); // Scale the speed
                    Vector2 velocity = new Vector2(400f, 50f);
                    projectileBody.setLinearVelocity(velocity);
                    return true;
                }
                return true;
            }
        });
        //--------------------------------------------------------------------------

        //DRAWING THE BIRD ON THE PROJECTILE BODY-----------
        batch.begin();

        Vector2 bodyPosition = projectileBody.getPosition(); // Get the Box2D body position
        redTexture.setPosition(bodyPosition.x - redTexture.getWidth() / 2,
            bodyPosition.y - redTexture.getHeight() / 2); // Center the texture
        redTexture.setRotation((float) Math.toDegrees(projectileBody.getAngle())); // Set rotation
        redTexture.draw(batch); // Draw the texture
//        System.out.println("Texture position: " + redTexture.getX() + ", " + redTexture.getY());
        batch.end();
        //---------------------------------------------------


        stage.act(Gdx.graphics.getDeltaTime()); // Update the stage
        stage.draw();

        //WINNING AND LOSING SCREEN HANDLING
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
        yellowTexture.dispose();
        redTexture.getTexture().dispose();
        blackTexture.dispose();
        overlayStage.dispose();

        batch.dispose();
    }
}
