package com.mygdx.game.box2d;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.box2d.entity.components.Box2DBodyComponent;
import com.mygdx.game.box2d.entity.components.BulletComponent;
import com.mygdx.game.box2d.entity.components.CollisionComponent;
import com.mygdx.game.box2d.entity.components.EnemyComponent;
import com.mygdx.game.box2d.entity.components.PlayerComponent;
import com.mygdx.game.box2d.entity.components.StateComponent;
import com.mygdx.game.box2d.entity.components.TextureComponent;
import com.mygdx.game.box2d.entity.components.TransformComponent;
import com.mygdx.game.box2d.entity.components.TypeComponent;
import com.mygdx.game.box2d.entity.components.WallComponent;
import com.mygdx.game.box2d.entity.components.WaterFloorComponent;
import com.mygdx.game.box2d.entity.systems.RenderingSystem;
import com.mygdx.game.box2d.simplexnoise.SimplexNoise;

public class LevelFactory {
    private static final float PLATFORM_WIDTH = 1.5f;
    private static final float BOUNCE_PLATFORM_WIDTH = 0.5f;
    private static final float PLATFORM_HEIGHT = 0.2f;
    private static final float ENEMY_RADIUS = .5f;
    private static final int PLAYER_RADIUS = 1;
    private static final float BULLET_RADIUS = 0.2f;
    private final TextureRegion platformTex;
    public World world;
    public int currentLevel = 0;
    private BodyFactory bodyFactory;
    private PooledEngine engine;
    private SimplexNoise sim;
    private TextureRegion floorTex;
    private TextureRegion enemyTex;
    private TextureRegion bulletTex;

    LevelFactory(PooledEngine en, TextureRegion floorTexture) {
        engine = en;
        floorTex = floorTexture;
        world = new World(new Vector2(0, -10f), true);
        world.setContactListener(new Box2DContactListener());
        bodyFactory = BodyFactory.getInstance(world);
        // create a new SimplexNoise (size,roughness,seed)
        sim = new SimplexNoise(512, 0.85f, 1);
        floorTex = Utils.makeTextureRegion(40 * RenderingSystem.PIXELS_PER_METRE, 0.5f * RenderingSystem.PIXELS_PER_METRE, "111111FF");
        enemyTex = Utils.makeTextureRegion(1 * RenderingSystem.PIXELS_PER_METRE, 1 * RenderingSystem.PIXELS_PER_METRE, "331111FF");
        bulletTex = Utils.makeTextureRegion(1 * RenderingSystem.PIXELS_PER_METRE, 1 * RenderingSystem.PIXELS_PER_METRE, "331111FF");
        platformTex = Utils.makeTextureRegion(2 * RenderingSystem.PIXELS_PER_METRE, 0.1f * RenderingSystem.PIXELS_PER_METRE, "221122FF");
    }


    /** Creates a pair of platforms per level up to yLevel
     * @param ylevel
     */
    public void generateLevel(int ylevel) {
        while (ylevel > currentLevel) {
            // get noise      sim.getNoise(xpos,ypos,zpos) 3D noise

            float width = RenderingSystem.getScreenSizeInMeters().x;
            double noise1 = Math.random(); // (float)sim.getNoise(1, currentLevel, 0);  // platform 1 should exist?
            double noise2 = Math.random(); //(float)sim.getNoise(1, currentLevel, 100);	// if plat 1 exists where on x axis
            double noise3 = Math.random(); //(float)sim.getNoise(1, currentLevel, 200);	// platform 2 exists?
            double noise4 = Math.random(); //(float)sim.getNoise(1, currentLevel, 300);	// if 2 exists where on x axis ?
            double noise5 = Math.random(); //(float)sim.getNoise(1, currentLevel ,300);	// should spring exist on p1?
            double noise6 = Math.random(); //(float)sim.getNoise(1, currentLevel ,300);	// should spring exists on p2?
            double noise7 = Math.random(); //(float)sim.getNoise(1, currentLevel, 600);	// should enemy exist?
            double noise8 = Math.random(); //(float)sim.getNoise(1, currentLevel, 700);	// platform 1 or 2?
            int y = currentLevel * 2;
            if (noise1 > 0.5) {
                float x = (float) ((width - PLATFORM_WIDTH) * noise2);
                createPlatform(x, y);
                if (noise5 > 0.5) createBouncyPlatform(x, y);
                if (noise7 > 0.5) createEnemy(enemyTex, x, y + PLATFORM_HEIGHT + ENEMY_RADIUS);
            }
            if (noise3 > 0.5) {
                float x = (float) ((width - PLATFORM_WIDTH) * noise4);
                createPlatform(x, y);
                if (noise6 > 0.5) createBouncyPlatform(x, y);
                if (noise8 > 0.5) createEnemy(enemyTex, x, y + PLATFORM_HEIGHT + ENEMY_RADIUS);
            }
            currentLevel++;
        }
    }

    private Entity createBouncyPlatform(float x, float y) {
        Entity entity = engine.createEntity();
        // create body component
        Box2DBodyComponent b2dbody = engine.createComponent(Box2DBodyComponent.class);
        b2dbody.body = bodyFactory.makeBoxBody(x, y, BOUNCE_PLATFORM_WIDTH, .5f, BodyFactory.STONE, BodyType.StaticBody, false);
        //make it a sensor so not to impede movement
        bodyFactory.makeAllFixturesSensors(b2dbody.body);

        // give it a texture..
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        texture.region = floorTex;

        TypeComponent type = engine.createComponent(TypeComponent.class);
        type.type = TypeComponent.SPRING;

        b2dbody.body.setUserData(entity);
        entity.add(b2dbody);
        entity.add(texture);
        entity.add(type);
        engine.addEntity(entity);

        return entity;
    }

    private void createPlatform(float x, float y) {
        // Create the entity
        Entity entity = engine.createEntity();

        // This entity is of type scenery, so it has the TypeComponent
        TypeComponent type = engine.createComponent(TypeComponent.class);
        type.type = TypeComponent.SCENERY;
        entity.add(type);

        // This entity has a Texture, so it has the TextureComponent
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        texture.region = floorTex;
        entity.add(texture);

        // This entity has a body, so it has a BodyComponent
        Box2DBodyComponent b2dbody = engine.createComponent(Box2DBodyComponent.class);
        b2dbody.body = bodyFactory.makeBoxBody(x, y, PLATFORM_WIDTH, PLATFORM_HEIGHT, BodyFactory.STONE, BodyType.StaticBody, false);
        b2dbody.body.setUserData(entity);
        entity.add(b2dbody);

        engine.addEntity(entity);
    }

    public void createFloor(TextureRegion tex) {
        Entity entity = engine.createEntity();
        Box2DBodyComponent b2dbody = engine.createComponent(Box2DBodyComponent.class);
        b2dbody.body = bodyFactory.makeBoxBody(0, 0, RenderingSystem.getScreenSizeInMeters().x, 1, BodyFactory.STONE, BodyType.StaticBody, false);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        texture.region = tex;
        TypeComponent type = engine.createComponent(TypeComponent.class);
        type.type = TypeComponent.SCENERY;

        b2dbody.body.setUserData(entity);

        entity.add(b2dbody);
        entity.add(texture);
        entity.add(type);

        engine.addEntity(entity);
    }

    /**
     * Creates the water entity that steadily moves upwards towards player
     *
     * @return
     */
    public Entity createWaterFloor(TextureRegion tex) {
        Entity entity = engine.createEntity();
        Box2DBodyComponent b2dbody = engine.createComponent(Box2DBodyComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);
        WaterFloorComponent waterFloor = engine.createComponent(WaterFloorComponent.class);

        type.type = TypeComponent.ENEMY;
        texture.region = tex;
        b2dbody.body = bodyFactory.makeBoxBody(0, -10, RenderingSystem.getScreenSizeInMeters().x, 10, BodyFactory.STONE, BodyType.KinematicBody, true);
        position.position.set(0, -10, 0);
        entity.add(b2dbody);
        entity.add(position);
        entity.add(texture);
        entity.add(type);
        entity.add(waterFloor);

        b2dbody.body.setUserData(entity);

        engine.addEntity(entity);

        return entity;
    }

    public Entity createPlayer(TextureRegion tex, OrthographicCamera cam) {

        Entity entity = engine.createEntity();
        Box2DBodyComponent b2dbody = engine.createComponent(Box2DBodyComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        PlayerComponent player = engine.createComponent(PlayerComponent.class);
        CollisionComponent colComp = engine.createComponent(CollisionComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);
        StateComponent stateCom = engine.createComponent(StateComponent.class);


        player.cam = cam;
        b2dbody.body = bodyFactory.makeCirclePolyBody(RenderingSystem.getScreenSizeInMeters().x / 2, 1, PLAYER_RADIUS, BodyFactory.STONE, BodyType.DynamicBody, true);
        // set object position (x,y,z) z used to define draw order 0 first drawn
        position.position.set(RenderingSystem.getScreenSizeInMeters().x / 2, 1, 0);
        texture.region = tex;
        type.type = TypeComponent.PLAYER;
        stateCom.set(StateComponent.STATE_NORMAL);
        b2dbody.body.setUserData(entity);

        entity.add(colComp);
        entity.add(b2dbody);
        entity.add(position);
        entity.add(texture);
        entity.add(player);
        entity.add(type);
        entity.add(stateCom);

        engine.addEntity(entity);
        return entity;
    }

    public void createWalls(TextureRegion wallRegion) {

        createWall(wallRegion, 0);
        createWall(wallRegion, RenderingSystem.getScreenSizeInMeters().x);
    }

    private void createWall(TextureRegion wallRegion, float xpos) {
        Entity entity = engine.createEntity();
        Box2DBodyComponent b2dbody = engine.createComponent(Box2DBodyComponent.class);
        b2dbody.body = bodyFactory.makeBoxBody(xpos, 0, 1, RenderingSystem.getScreenSizeInMeters().y, BodyFactory.STONE, BodyType.KinematicBody, false);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        texture.region = wallRegion;
        TypeComponent type = engine.createComponent(TypeComponent.class);
        type.type = TypeComponent.SCENERY;
        WallComponent wall = engine.createComponent(WallComponent.class);

        b2dbody.body.setUserData(entity);

        entity.add(b2dbody);
        entity.add(texture);
        entity.add(type);
        entity.add(wall);

        engine.addEntity(entity);
    }

    private Entity createEnemy(TextureRegion tex, float x, float y) {
        Entity entity = engine.createEntity();
        Box2DBodyComponent b2dbody = engine.createComponent(Box2DBodyComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        EnemyComponent enemy = engine.createComponent(EnemyComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);
        CollisionComponent colComp = engine.createComponent(CollisionComponent.class);

        b2dbody.body = bodyFactory.makeCirclePolyBody(x, y, ENEMY_RADIUS, BodyFactory.STONE, BodyType.KinematicBody, true);
        position.position.set(x, y, 0);
        texture.region = tex;
        enemy.xPosCenter = x;
        type.type = TypeComponent.ENEMY;
        b2dbody.body.setUserData(entity);

        entity.add(colComp);
        entity.add(b2dbody);
        entity.add(position);
        entity.add(texture);
        entity.add(enemy);
        entity.add(type);

        engine.addEntity(entity);

        return entity;
    }

    public Entity createBullet(float x, float y, float xVel, float yVel) {
        Entity entity = engine.createEntity();
        Box2DBodyComponent b2dbody = engine.createComponent(Box2DBodyComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);
        CollisionComponent colComp = engine.createComponent(CollisionComponent.class);
        BulletComponent bul = engine.createComponent(BulletComponent.class);

        b2dbody.body = bodyFactory.makeCirclePolyBody(x, y, BULLET_RADIUS, BodyFactory.STONE, BodyType.DynamicBody, true);
        b2dbody.body.setBullet(true); // increase physics computation to limit body travelling through other objects
        bodyFactory.makeAllFixturesSensors(b2dbody.body); // make bullets sensors so they don't move player
        position.position.set(x, y, 0);
        texture.region = bulletTex;
        type.type = TypeComponent.BULLET;
        b2dbody.body.setUserData(entity);
        bul.xVel = xVel;
        bul.yVel = yVel;

        entity.add(colComp);
        entity.add(bul);
        entity.add(b2dbody);
        entity.add(position);
        entity.add(texture);
        entity.add(type);

        engine.addEntity(entity);
        return entity;
    }
}