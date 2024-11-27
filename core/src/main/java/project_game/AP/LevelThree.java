package project_game.AP;

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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.*;

public class LevelThree implements Screen, Serializable {
    transient private Box2DDebugRenderer debugRenderer;
    transient final Structure game;
    transient private Texture background;
    transient private OrthographicCamera camera;
    transient private Viewport viewport;
    transient private BitmapFont font;
    transient private SpriteBatch batch;

    //Font Varibales
    transient private FreeTypeFontGenerator fontGenerator;
    transient private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    transient private Stage stage;

    // Tiled map variables
    transient private TmxMapLoader mapLoader;
    transient private TiledMap tiledMap;
    transient private OrthogonalTiledMapRenderer renderer;

    // Box2d Variables
    transient private World world;
    transient private Box2DDebugRenderer b2dr;

    //Overlay
    transient private Stage overlayStage;
    private boolean showOverlay = false;

    // Constant for tile size (adjust according to your tile size)
    private static final float TILE_SIZE = 16.0f;
    private float elapsed=0;
    private boolean background_changedw=false;
    private boolean background_changedl=false;
    //PIGS BIRD SLIGNSHOT TEXTURES

    Birds redBird,yellowBird,blackBird;
    Slingshot slingshot;
    transient private Texture pig1Texture,pig2Texture,pig3Texture;
    transient private Texture redTexture;
    transient private Sprite woodTexture,concreteTexture;
    transient private Texture yellowTexture,blackTexture;

    transient private Sprite BlackSitting,YellowSitting,RedSitting;
    transient private Texture sling;

    transient private Drawable overlayDrawable;

    transient private Texture wood_texture,concrete_texture;

    transient private ShapeRenderer shapeRenderer;
    transient private ListenerClass CollisionHandler;

    //WORLD BODIES ARRAY LISTS
    static ArrayList<Wood> wood=new ArrayList<Wood>();
    static ArrayList<Concrete> concrete=new ArrayList<Concrete>();
    static ArrayList<SmallPig> smallpig = new ArrayList<SmallPig>();
    static ArrayList<LargePig> largepig = new ArrayList<LargePig>();

    //MOVING PROJECTILE BODY VARIABLES
    transient Body projectileBody;
    private static final float PPM = 16f; // Pixels per meter
    static Vector2 startPoint = new Vector2(150,180); // Drag start
    private boolean isDragging = false;

    private int BirdCount = 0;
    private boolean levelFlag=false;
    private boolean trajectoryFlag = false;
    transient private Vector2 currPoint = new Vector2();


    public LevelThree(Structure game) {
        this.game = game;

        background = new Texture("Level_Two_bg.jpg");
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 480, camera);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        overlayStage = new Stage(new ScreenViewport());
        showOverlay = false;
        shapeRenderer = new ShapeRenderer();
        //PIGS BIRD SLINGSHOT INITIALIZATION
        pig1Texture=new Texture("pig1-removebg-preview.png");
        pig2Texture=new Texture("pig2-removebg-preview.png");
        pig3Texture=new Texture("pig1-removebg-preview.png");

        yellowTexture = new Texture("yellowBird.png");
        YellowSitting = new Sprite(new Texture("yellowBird.png"));
        blackTexture = new Texture("blackBird.png");
        BlackSitting = new Sprite(new Texture("blackBird.png"));
        wood_texture=new Texture("wooden_textureAB.png");
        woodTexture = new Sprite(new Texture("wooden_textureAB.png"));
        woodTexture.setOriginCenter();
        concrete_texture=new Texture("concrete_blockAB.jpeg");
        concreteTexture=new Sprite(new Texture("concrete_blockAB.jpeg"));
        concreteTexture.setOriginCenter();
        sling =new Texture("slingshot.png");

        redTexture = new Texture("redBird.png");
        redBird=new Birds(redTexture,4);
        slingshot = new Slingshot(sling);

        //PIGS BIRD SLINGSHOT POSITION SIZE SETTING
        batch=new SpriteBatch();
        redBird.setSize(50,50);
        redBird.setPosition(90,105);
        YellowSitting.setSize(50,50);
        YellowSitting.setPosition(45,100);
        BlackSitting.setSize(50,50);
        BlackSitting.setPosition(5,105);
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
        CollisionHandler = new ListenerClass();
        world.setContactListener(CollisionHandler);

        for (int layerIndex = 1; layerIndex <= 2; layerIndex++) {
            for (MapObject object : tiledMap.getLayers().get(layerIndex).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                BodyDef bdef = new BodyDef();
                bdef.type = BodyDef.BodyType.DynamicBody;
                bdef.position.set((rect.getX() + rect.getWidth() / 2) , (rect.getY() + rect.getHeight() / 2) );

                // Create the body in the Box2D world
                Body body = world.createBody(bdef);
                body.setAwake(true);

                if(layerIndex==1){
                    Wood NewWood = new Wood(rect,body);
                    body.setUserData(NewWood);
                    wood.add(NewWood);
                }
                if(layerIndex==2){
                    Concrete newConcrete = new Concrete(rect,body);
                    body.setUserData(newConcrete);
                    concrete.add(newConcrete);
                }
            }
        }
        for (int layerIndex = 3; layerIndex <= 4; layerIndex++) {
            for (MapObject object : tiledMap.getLayers().get(layerIndex).getObjects().getByType(EllipseMapObject.class)) {
                Ellipse ellipse = ((EllipseMapObject) object).getEllipse();
                BodyDef bdef = new BodyDef();
                bdef.type = BodyDef.BodyType.DynamicBody;
                bdef.position.set((ellipse.x + ellipse.width/ 2) , (ellipse.y + ellipse.height / 2));

                // Create the body in the Box2D world
                Body body = world.createBody(bdef);
                body.setAwake(true);

//                 Fixture definition
                if(layerIndex==3){
                    SmallPig NewPig = new SmallPig(ellipse,body);
                    body.setUserData(NewPig);
                    smallpig.add(NewPig);
                }
                if(layerIndex==4){
                    LargePig NewPig = new LargePig(ellipse,body);
                    body.setUserData(NewPig);
                    largepig.add(NewPig);
                }
            }
        }
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(90,105);

        projectileBody = world.createBody(bodyDef);
        projectileBody.setUserData(redBird);
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(30f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = 1.5f;
        fixtureDef.friction = 0.3f;
        projectileBody.createFixture(fixtureDef);
        projectileBody.setAwake(false);
        circleShape.dispose();

    }
    public LevelThree(SavedLevel level, Structure game) {
        this.game = game;

        // Load basic level settings
        BirdCount = level.getBirdCount();
        smallpig = level.getSmallPigs();
        largepig = level.getLargePigs();
        wood = level.getWood();
        concrete = level.getConcrete();

        // Initialize other properties
        background = new Texture("Level_Two_bg.jpg");
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 480, camera);
        stage = new Stage();
        overlayStage = new Stage(new ScreenViewport());
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load("Level1at4.tmx");
        renderer = new OrthogonalTiledMapRenderer(tiledMap);

        // Initialize bird and slingshot assets
        redTexture = new Texture("redBird.png");
        redBird = new Birds(redTexture, 4);
        slingshot = new Slingshot(new Texture("slingshot.png"));
        slingshot.setSize(100,100);
        slingshot.setPosition(130,105);
        pig1Texture=new Texture("pig1-removebg-preview.png");
        pig2Texture=new Texture("pig2-removebg-preview.png");
        pig3Texture=new Texture("pig1-removebg-preview.png");

        yellowTexture = new Texture("yellowBird.png");
        YellowSitting = new Sprite(new Texture("yellowBird.png"));
        blackTexture = new Texture("blackBird.png");
        BlackSitting = new Sprite(new Texture("blackBird.png"));
        YellowSitting.setSize(50,50);
        YellowSitting.setPosition(45,100);
        BlackSitting.setSize(50,50);
        BlackSitting.setPosition(5,105);

        // Initialize the Box2D world
        world = new World(new Vector2(0, -9.81f), false);
        b2dr = new Box2DDebugRenderer();
        WorldUtils.createGround(world);
        CollisionHandler = new ListenerClass();
        world.setContactListener(CollisionHandler);
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


        // Create saved wood bodies
        for (Wood savedWood : wood) {
            BodyDef bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.DynamicBody;
            bdef.position.set(savedWood.getPosX(),savedWood.getPosY());
            Body body = world.createBody(bdef);
            body.setUserData(savedWood);
            savedWood.recreateBody(body);
        }

        // Create saved concrete bodies
        for (Concrete savedConcrete : concrete) {
            BodyDef bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.DynamicBody;
            bdef.position.set(savedConcrete.getPosX(),savedConcrete.getPosY());
            Body body = world.createBody(bdef);
            body.setUserData(savedConcrete);
            savedConcrete.recreateBody(body);
        }

        // Create saved small pigs
        for (SmallPig savedPig : smallpig) {
            BodyDef bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.DynamicBody;
            bdef.position.set(savedPig.getPosX(),savedPig.getPosY());
            Body body = world.createBody(bdef);
            body.setUserData(savedPig);
            savedPig.recreateBody(body);
        }

        // Create saved large pigs
        for (LargePig savedPig : largepig) {
            BodyDef bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.DynamicBody;
            bdef.position.set(savedPig.getPosX(),savedPig.getPosY());
            Body body = world.createBody(bdef);
            body.setUserData(savedPig);
            savedPig.recreateBody(body);
        }

        // Create the bird's projectile body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(90, 105);
        projectileBody = world.createBody(bodyDef);
        projectileBody.setUserData(redBird);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(30f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = 1.5f;
        fixtureDef.friction = 0.3f;
        projectileBody.createFixture(fixtureDef);
        projectileBody.setAwake(false);
        circleShape.dispose();
    }
    public void showPauseMenu(){
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

        drawable = new TextureRegionDrawable(new TextureRegion(new Texture("menu_save_btn.png")));
        ImageButton.ImageButtonStyle buttonStyle3 = new ImageButton.ImageButtonStyle();
        buttonStyle3.imageUp = drawable;
        ImageButton saveButton = new ImageButton(buttonStyle3);
        saveButton.setSize(90, 90);

        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SavedLevel savedLevel=new SavedLevel(BirdCount,LevelSelector.level1complete,LevelSelector.level2complete
                ,LevelSelector.level3complete,smallpig,new ArrayList<MediumPig>(),largepig,wood,concrete,new ArrayList<Glass>(),3);
                try{
                    // if savedGame1.ser exists, try to save to savedGame2.ser, else save to savedGame3.ser else save to savedGame4.ser else save to savedGame1.ser
                    FileOutputStream fileOut = null;
                    if (Gdx.files.local("savedGame1.ser").exists()) {
                        if (Gdx.files.local("savedGame2.ser").exists()) {
                            if (Gdx.files.local("savedGame3.ser").exists()) {
                                fileOut = new FileOutputStream("savedGame1.ser");
                            } else {
                                fileOut = new FileOutputStream("savedGame3.ser");
                                LoadGame.game3available = true;
                            }
                        } else {
                            fileOut = new FileOutputStream("savedGame2.ser");
                            LoadGame.game2available = true;
                        }
                    } else {
                        fileOut = new FileOutputStream("savedGame1.ser");
                        LoadGame.game1available = true;
                    }
                    ObjectOutputStream out = new ObjectOutputStream(fileOut);
                    out.writeObject(savedLevel);
                    out.close();
                    fileOut.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Clicked! GAME STATE SAVED!!");
                cleanup();
                dispose();
                game.setScreen(new MainMenuScreen(game));
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
                game.setScreen(new LevelSelector(game));
                cleanup();
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
        world.step(1/120f,6,2);
        renderer.setView(camera);
        batch.setProjectionMatrix(camera.combined);
    }



    public void winGame(float delta){
        if(smallpig.size()==0 && largepig.size()==0){
            if(levelFlag==false){
                elapsed=0;
                levelFlag=true;
            }
            elapsed+=delta;
            if(elapsed>=4){
                LevelSelector.level3complete=true;
                background = new Texture("LevelComplete.jpg");
                elapsed = 0;
                background_changedw = true;}
        }
    }
    public void loseGame(float delta){
        if((smallpig.size()!=0 || largepig.size()!=0) && BirdCount>=3){
            if(levelFlag==false){
                elapsed=0;
                levelFlag=true;
            }
            elapsed+=delta;
            if(elapsed>=12 && (smallpig.size()!=0 || largepig.size()!=0)){
                LevelSelector.level3complete=false;
                background = new Texture("levelfailed.jpeg");  // Change background
                elapsed = 0;  // Reset the timer
                background_changedl = true;}  // Set flag to avoid resetting on every frame
        }
    }
    public void cleanup(){
        Iterator<Wood> iterator = wood.iterator();
        while (iterator.hasNext()) {
            Wood b = iterator.next();
            b.sprite.getTexture().dispose();
            iterator.remove();
            world.destroyBody(b.getBody());
        }

        Iterator<Concrete> iterator2 = concrete.iterator();
        while (iterator2.hasNext()) {
            Concrete b = iterator2.next();
            b.sprite.getTexture().dispose();
            iterator2.remove();
            world.destroyBody(b.getBody());
        }
        Iterator<SmallPig> iterator3 = smallpig.iterator();
        while (iterator3.hasNext()) {
            SmallPig b = iterator3.next();
            b.sprite.getTexture().dispose();
            iterator3.remove();
            world.destroyBody(b.getBody());
        }

        Iterator<LargePig> iterator4 = largepig.iterator();

        while (iterator4.hasNext()) {
            LargePig b = iterator4.next();
            b.sprite.getTexture().dispose();
            iterator4.remove();
            world.destroyBody(b.getBody());
        }
    }

    @Override
    public void render(float delta) {
        if (!showOverlay){
            update(delta);
        }
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

        if (BirdCount < 2){
            YellowSitting.draw(batch);
        }
        if (BirdCount < 3){
            BlackSitting.draw(batch);
        }
        slingshot.render(batch);

        Wood.drawMe(wood,world,batch);
        Concrete.drawMe(concrete,world,batch);
        SmallPig.drawMe(smallpig,world,batch);
        LargePig.drawMe(largepig,world,batch);

        batch.end();
        //-------------------------------------------------------------------

        debugRenderer.render(world, camera.combined);

        //PAUSE BUTTON INPUT PROCESSOR HANDLE -----------------

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)){
            showOverlay = true;
            showPauseMenu();
        }
        if (showOverlay){
            Gdx.input.setInputProcessor(overlayStage);
            overlayStage.act(Gdx.graphics.getDeltaTime());
            overlayStage.draw();
        }
        //---------------------------------------------------
        if (!showOverlay){

            overlayStage.clear();
            //LAUNCH BIRD INPUT DRAGGING HANDLING------------------------------------
            Gdx.input.setInputProcessor(new InputAdapter() {
                @Override
                public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                    if (BirdCount == 1){
                        redBird.setTexture(yellowTexture,6);
                    }
                    else if (BirdCount == 2){
                        redBird.setTexture(blackTexture,8);
                    }
                    projectileBody.setAwake(false);
                    projectileBody.setTransform(startPoint.x,startPoint.y,0);
                    return true;
                }

                @Override
                public boolean touchDragged(int screenX, int screenY, int pointer) {

                    int maxDistance = 80; // radius of the circle
                    float centerX = startPoint.x;  // x-coordinate of the circle center
                    float centerY = startPoint.y;  // y-coordinate of the circle center
                    isDragging = true;
                    if (isDragging) {
                        trajectoryFlag = true;
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
                        currPoint.x = worldCoords.x;
                        currPoint.y = worldCoords.y;
                    }

                    return true;
                }
                @Override
                public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                    if (isDragging) {
                        trajectoryFlag = false;
                        BirdCount++;
                        isDragging = false;
                        projectileBody.setAwake(true);
                        Vector2 position = new Vector2(projectileBody.getPosition());
                        float angle = projectileBody.getAngle();
                        projectileBody.setTransform(position.x, position.y, angle);
                        Vector2 velocity = new Vector2((startPoint.x+50-projectileBody.getPosition().x)*700, (startPoint.y+50-projectileBody.getPosition().y)*300);
                        projectileBody.setLinearVelocity(velocity);
                        return true;
                    }
                    return true;
                }
            });
        }
        //--------------------------------------------------------------------------
        if (trajectoryFlag){
            slingshot.drawTrajectory(currPoint.x, currPoint.y,startPoint,PPM,world,shapeRenderer);
        }
        //DRAWING THE BIRD ON THE PROJECTILE BODY-----------
        batch.begin();

        redBird.sprite.setOriginCenter();
        Vector2 bodyPosition = projectileBody.getPosition(); // Get the Box2D body position
        redBird.sprite.setPosition(bodyPosition.x - redBird.sprite.getWidth() / 2,
            bodyPosition.y - redBird.sprite.getHeight() / 2); // Center the texture
        redBird.sprite.setRotation((float) Math.toDegrees(projectileBody.getAngle())); // Set rotation
        redBird.sprite.draw(batch); // Draw the texture

        batch.end();
        //---------------------------------------------------

        stage.act(Gdx.graphics.getDeltaTime());

        //WINNING AND LOSING SCREEN HANDLING
        winGame(delta);
        loseGame(delta);

        if (background_changedw) {
            elapsed += delta;
            game.batch.begin();
            game.batch.draw(background, 0, 0, 800, 480);  // Ensure the new background is drawn
            game.batch.end();
            if (elapsed >= 2) {
                game.setScreen(new LevelSelector(game));// Transition to LevelSelector screen
                cleanup();
                dispose();
            }
        }
        if (background_changedl) {
            elapsed += delta;
            game.batch.begin();
            game.batch.draw(background, 0, 0, 800, 480);  // Ensure the new background is drawn
            game.batch.end();
            if (elapsed >= 2) {
                game.setScreen(new LevelSelector(game));  // Transition to LevelSelector screen
                cleanup();
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
        redTexture.dispose();
        blackTexture.dispose();
        overlayStage.dispose();
        batch.dispose();
    }
}
