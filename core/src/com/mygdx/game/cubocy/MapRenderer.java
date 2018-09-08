
package com.mygdx.game.cubocy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.math.Vector3;

public class MapRenderer {
    private final Map map;
    private final OrthographicCamera cam;
    private final SpriteCache cache;
    private final SpriteBatch batch = new SpriteBatch(5460);
    private final ImmediateModeRenderer20 renderer = new ImmediateModeRenderer20(false, true, 0);
    private final int[][] blocks;
    private TextureRegion tile;
    private Animation<TextureRegion> bobLeft;
    private Animation<TextureRegion> bobRight;
    private Animation<TextureRegion> bobJumpLeft;
    private Animation<TextureRegion> bobJumpRight;
    private Animation<TextureRegion> bobIdleLeft;
    private Animation<TextureRegion> bobIdleRight;
    private Animation<TextureRegion> bobDead;
    private TextureRegion cube;
    private Animation<TextureRegion> cubeFixed;
    private TextureRegion cubeControlled;
    private TextureRegion dispenser;
    private Animation<TextureRegion> spawn;
    private Animation<TextureRegion> dying;
    private TextureRegion spikes;
    private Animation<TextureRegion> rocket;
    private Animation<TextureRegion> rocketExplosion;
    private TextureRegion rocketPad;
    private TextureRegion endDoor;
    private TextureRegion movingSpikes;
    private TextureRegion laser;
    private final Vector3 lerpTarget = new Vector3();

    public MapRenderer(Map map) {
        this.map = map;
        this.cam = new OrthographicCamera(24, 16);
        this.cam.position.set(map.bob.pos.x, map.bob.pos.y, 0);
        this.cache = new SpriteCache(this.map.tiles.length * this.map.tiles[0].length, false);
        this.blocks = new int[(int) Math.ceil(this.map.tiles.length / 24.0f)][(int) Math.ceil(this.map.tiles[0].length / 16.0f)];

        createAnimations();
        createBlocks();
    }

    private void createBlocks() {
        int width = map.tiles.length;
        int height = map.tiles[0].length;
        for (int blockY = 0; blockY < blocks[0].length; blockY++) {
            for (int blockX = 0; blockX < blocks.length; blockX++) {
                cache.beginCache();
                for (int y = blockY * 16; y < blockY * 16 + 16; y++) {
                    for (int x = blockX * 24; x < blockX * 24 + 24; x++) {
                        if (x > width) continue;
                        if (y > height) continue;
                        //noinspection UnnecessaryLocalVariable
                        int posX = x;
                        int posY = height - y - 1;
                        if (map.match(map.tiles[x][y], Map.TILE)) cache.add(tile, posX, posY, 1, 1);
                        if (map.match(map.tiles[x][y], Map.SPIKES))
                            cache.add(spikes, posX, posY, 1, 1);
                    }
                }
                blocks[blockX][blockY] = cache.endCache();
            }
        }
        Gdx.app.debug("Cubocy", "blocks created");
    }

    private void createAnimations() {
        this.tile = new TextureRegion(new Texture(Gdx.files.internal("cubocy/tile.png")), 0, 0, 20, 20);
        Texture bobTexture = new Texture(Gdx.files.internal("cubocy/bob.png"));
        TextureRegion[] split = new TextureRegion(bobTexture).split(20, 20)[0];
        TextureRegion[] mirror = new TextureRegion(bobTexture).split(20, 20)[0];
        for (TextureRegion region : mirror)
            region.flip(true, false);
        spikes = split[5];
        bobRight = new Animation<TextureRegion>(0.1f, split[0], split[1]);
        bobLeft = new Animation<TextureRegion>(0.1f, mirror[0], mirror[1]);
        bobJumpRight = new Animation<TextureRegion>(0.1f, split[2], split[3]);
        bobJumpLeft = new Animation<TextureRegion>(0.1f, mirror[2], mirror[3]);
        bobIdleRight = new Animation<TextureRegion>(0.5f, split[0], split[4]);
        bobIdleLeft = new Animation<TextureRegion>(0.5f, mirror[0], mirror[4]);
        bobDead = new Animation<TextureRegion>(0.2f, split[0]);
        split = new TextureRegion(bobTexture).split(20, 20)[1];
        cube = split[0];
        cubeFixed = new Animation<TextureRegion>(1, split[1], split[2], split[3], split[4], split[5]);
        split = new TextureRegion(bobTexture).split(20, 20)[2];
        cubeControlled = split[0];
        spawn = new Animation<TextureRegion>(0.1f, split[4], split[3], split[2], split[1]);
        dying = new Animation<TextureRegion>(0.1f, split[1], split[2], split[3], split[4]);
        dispenser = split[5];
        split = new TextureRegion(bobTexture).split(20, 20)[3];
        rocket = new Animation<TextureRegion>(0.1f, split[0], split[1], split[2], split[3]);
        rocketPad = split[4];
        split = new TextureRegion(bobTexture).split(20, 20)[4];
        rocketExplosion = new Animation<TextureRegion>(0.1f, split[0], split[1], split[2], split[3], split[4], split[4]);
        split = new TextureRegion(bobTexture).split(20, 20)[5];
        endDoor = split[2];
        movingSpikes = split[0];
        laser = split[1];
    }

    public void render(float deltaTime) {
        if (map.cube.state != Cube.CONTROLLED)
            cam.position.lerp(lerpTarget.set(map.bob.pos.x, map.bob.pos.y, 0), 2f * deltaTime);
        else
            cam.position.lerp(lerpTarget.set(map.cube.pos.x, map.cube.pos.y, 0), 2f * deltaTime);
        cam.update();

        renderLaserBeams();

        cache.setProjectionMatrix(cam.combined);
        Gdx.gl.glDisable(GL20.GL_BLEND);
        cache.begin();
        for (int blockY = 0; blockY < 4; blockY++) {
            for (int blockX = 0; blockX < 6; blockX++) {
                cache.draw(blocks[blockX][blockY]);
            }
        }
        cache.end();
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        renderDispensers();
        if (map.endDoor != null)
            batch.draw(endDoor, map.endDoor.bounds.x, map.endDoor.bounds.y, 1, 1);
        renderLasers();
        renderMovingSpikes();
        renderBob();
        renderCube();
        renderRockets();
        batch.end();
        renderLaserBeams();
    }

    private void renderBob() {
        Animation<TextureRegion> anim = null;
        boolean loop = true;
        switch (map.bob.state) {
            case SPAWN:
                anim = spawn;
                loop = false;
                break;
            case IDLE:
                if (map.bob.dir == Bob.LEFT)
                    anim = bobIdleLeft;
                else
                    anim = bobIdleRight;
                break;
            case RUN:
                if (map.bob.dir == Bob.LEFT)
                    anim = bobLeft;
                else
                    anim = bobRight;
                break;
            case JUMP:
                if (map.bob.dir == Bob.LEFT)
                    anim = bobJumpLeft;
                else
                    anim = bobJumpRight;
                break;
            case DYING:
                anim = dying;
                loop = false;
                break;
            case DEAD:
                anim = bobDead;
                break;
        }
        batch.draw(anim.getKeyFrame(map.bob.stateTime, loop), map.bob.pos.x, map.bob.pos.y, 1, 1);
    }

    private void renderCube() {
        if (map.cube.state == Cube.FOLLOW)
            batch.draw(cube, map.cube.pos.x, map.cube.pos.y, 1.5f, 1.5f);
        if (map.cube.state == Cube.FIXED)
            batch.draw(cubeFixed.getKeyFrame(map.cube.stateTime, false), map.cube.pos.x, map.cube.pos.y, 1.5f, 1.5f);
        if (map.cube.state == Cube.CONTROLLED)
            batch.draw(cubeControlled, map.cube.pos.x, map.cube.pos.y, 1.5f, 1.5f);
    }

    private void renderRockets() {
        for (int i = 0; i < map.rockets.size; i++) {
            Rocket rocket = map.rockets.get(i);
            if (rocket.state == Rocket.FLYING) {
                TextureRegion frame = this.rocket.getKeyFrame(rocket.stateTime, true);
                batch.draw(frame, rocket.pos.x, rocket.pos.y, 0.5f, 0.5f, 1, 1, 1, 1, rocket.vel.angle());
            } else {
                TextureRegion frame = this.rocketExplosion.getKeyFrame(rocket.stateTime, false);
                batch.draw(frame, rocket.pos.x, rocket.pos.y, 1, 1);
            }
            batch.draw(rocketPad, rocket.startPos.x, rocket.startPos.y, 1, 1);
        }
    }

    private void renderDispensers() {
        for (int i = 0; i < map.dispensers.size; i++) {
            Dispenser dispenser = map.dispensers.get(i);
            batch.draw(this.dispenser, dispenser.bounds.x, dispenser.bounds.y, 1, 1);
        }
    }

    private void renderMovingSpikes() {
        for (int i = 0; i < map.movingSpikes.size; i++) {
            MovingSpikes spikes = map.movingSpikes.get(i);
            batch.draw(movingSpikes, spikes.pos.x, spikes.pos.y, 0.5f, 0.5f, 1, 1, 1, 1, spikes.angle);
        }
    }

    private void renderLasers() {
        for (int i = 0; i < map.lasers.size; i++) {
            Laser laser = map.lasers.get(i);
            batch.draw(this.laser, laser.pos.x, laser.pos.y, 0.5f, 0.5f, 1, 1, 1, 1, laser.angle);
        }
    }

    private void renderLaserBeams() {
        cam.update(false);
        renderer.begin(cam.combined, GL20.GL_LINES);
        for (int i = 0; i < map.lasers.size; i++) {
            Laser laser = map.lasers.get(i);
            float sx = laser.startPoint.x, sy = laser.startPoint.y;
            float ex = laser.cappedEndPoint.x, ey = laser.cappedEndPoint.y;
            renderer.color(0, 1, 0, 1);
            renderer.vertex(sx, sy, 0);
            renderer.color(0, 1, 0, 1);
            renderer.vertex(ex, ey, 0);
        }
        renderer.end();
    }

    public void dispose() {
        cache.dispose();
        batch.dispose();
        tile.getTexture().dispose();
        cube.getTexture().dispose();
    }
}
