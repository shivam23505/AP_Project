package project_game.AP;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
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
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import javax.swing.*;
import java.util.Objects;

public class LevelTwo implements Screen {
    final Structure game;
    private Texture background;
    private OrthographicCamera camera;
    private Viewport viewport;
    private FreeTypeFontGenerator fontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    private BitmapFont font;
    private Stage stage;
    private Skin skin;

    //Tiled map variables
    private TmxMapLoader mapLoader;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d Variables
    private World world;
    private Box2DDebugRenderer b2dr;

    //Overlay
    private Stage overlayStage;
    private boolean showOverlay;
    private float elapsed=0;
    private boolean background_changedw=false;
    private boolean background_changedl=false;
    private SpriteBatch batch;
    Pigs pig1;
    Pigs pig2;
    Birds redBird;
    Birds blackBird;
    Slingshot slingshot;
    private Texture pig1Texture,pig2Texture,pig3Texture;
    private Texture redTexture,blackTexture;
    private Texture sling;
    private Drawable overlayDrawable;
    public LevelTwo(Structure game) {
        this.game = game;
        background = new Texture("Level_Two_bg.jpg");
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        viewport = new FitViewport(800, 480, camera);
        pig1Texture=new Texture("pig1-removebg-preview.png");
        pig2Texture=new Texture("pig2-removebg-preview.png");
        pig3Texture=new Texture("pig3-removebg-preview.png");
        redTexture = new Texture("redBird.png");
        blackTexture = new Texture("blackBird.png");
        sling = new Texture("slingshot.png");
        batch=new SpriteBatch();
        pig1=new Pigs(pig3Texture);
        pig2=new Pigs(pig1Texture);
        redBird = new Birds(redTexture);
        blackBird = new Birds(blackTexture);
        slingshot = new Slingshot(sling);
        redBird.setSize(50,50);
        redBird.setPosition(10,105);
        blackBird.setSize(50,50);
        blackBird.setPosition(70,105);
        pig1.setSize(70,70);
        pig1.setPosition(670,105);
        pig2.setSize(40,40);
        pig2.setPosition(680,205);
        slingshot.setSize(100,100);
        slingshot.setPosition(120,105);

        overlayStage = new Stage(new ScreenViewport());
        showOverlay = false;

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Texture transparentPixel = createTransparentPixel(0f, 0f, 0f, 0.5f);
        overlayDrawable = new TextureRegionDrawable(new TextureRegion(transparentPixel));

        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Arial.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 40;
        fontParameter.borderWidth = 2;
        fontParameter.color = Color.WHITE;
        font = fontGenerator.generateFont(fontParameter);

//        buttonImage = new Texture("button.png");
        skin = new Skin(Gdx.files.internal("ui/uiskin.json")); // Load a skin if you have one
//        menuPlay = new Texture("menu_play_btn.png");
////
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
        mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load("level_two_map.tmx");
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
//        TextButton button1 = new TextButton("RESUME", skin);
//        button1.setPosition(150, 100);
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

    }
    public void update(float delta){
        camera.update();
        renderer.setView(camera);
    }
    @Override
    public void render(float delta) {
        // Clear the screen with a black color
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(background, 0, 0, 800, 480);  // Draw background at (0, 0)
        game.batch.end();  // End the background batch
        camera.position.set(400,240,0);
        renderer.setView(camera);
        renderer.render();

        b2dr.render(world, camera.combined);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        pig1.render(batch);
        pig2.render(batch);
        redBird.render(batch);
        blackBird.render(batch);
        slingshot.render(batch);
        batch.end();
//        System.out.println(stage.getActors());
        stage.act(Gdx.graphics.getDeltaTime()); // Update the stage
//        System.out.println("Number of actors in stage: " + stage.getActors().size); // Check if the stage has actors
        stage.draw();
//        stage.setDebugAll(true);
        if (showOverlay) {
            Gdx.input.setInputProcessor(overlayStage);

            overlayStage.act(Gdx.graphics.getDeltaTime());
            overlayStage.draw();
        }
        else{
            Gdx.input.setInputProcessor(stage);
            overlayStage.clear();
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
    public void resize(int i, int i1) {
        viewport.update(i,i1);
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
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
        fontGenerator.dispose();
        font.dispose();
        skin.dispose();
        stage.dispose();
        pig1Texture.dispose();
        pig2Texture.dispose();
        pig3Texture.dispose();
        redTexture.dispose();
        blackTexture.dispose();
        sling.dispose();
        batch.dispose();
    }
}
