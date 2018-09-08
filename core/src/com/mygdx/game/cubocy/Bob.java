
package com.mygdx.game.cubocy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bob {
    public final Rectangle bounds = new Rectangle();
    static final int LEFT = -1;
    static final int RIGHT = 1;
    private static final float ACCELERATION = 20f;
    private static final float JUMP_VELOCITY = 10;
    private static final float GRAVITY = 20.0f;
    private static final float MAX_VEL = 6f;
    private static final float DAMP = 0.90f;
    final Vector2 pos = new Vector2();
    private final Vector2 acceleration = new Vector2();
    private final Vector2 vel = new Vector2();
    private final Map map;
    int dir = LEFT;
    private final Rectangle[] r = {new Rectangle(), new Rectangle(), new Rectangle(), new Rectangle(), new Rectangle()};
    BobState state;
    float stateTime;
    Bob(Map map, float x, float y) {
        this.map = map;
        pos.x = x;
        pos.y = y;
        bounds.width = 0.6f;
        bounds.height = 0.8f;
        bounds.x = pos.x + 0.2f;
        bounds.y = pos.y;
        state = BobState.SPAWN;
        stateTime = 0;
    }

    public void update(float deltaTime) {
        processKeys();

        acceleration.y = -GRAVITY;
        acceleration.scl(deltaTime);
        vel.add(acceleration.x, acceleration.y);
        if (acceleration.x == 0) vel.x *= DAMP;
        if (vel.x > MAX_VEL) vel.x = MAX_VEL;
        if (vel.x < -MAX_VEL) vel.x = -MAX_VEL;
        vel.scl(deltaTime);
        tryMove();
        vel.scl(1.0f / deltaTime);

        switch (state) {
            case SPAWN:
                if (stateTime > 0.4f) state = BobState.IDLE;
                break;
            case DYING:
                if (stateTime > 0.4f) state = BobState.DEAD;
                break;
        }
        stateTime += deltaTime;
    }

    private void processKeys() {
        if (map.cube.state == Cube.CONTROLLED || state == BobState.SPAWN || state == BobState.DYING)
            return;

        float x0 = (Gdx.input.getX(0) / (float) Gdx.graphics.getWidth()) * 480;
        float x1 = (Gdx.input.getX(1) / (float) Gdx.graphics.getWidth()) * 480;
        float y0 = 320 - (Gdx.input.getY(0) / (float) Gdx.graphics.getHeight()) * 320;

        boolean leftButton = (Gdx.input.isTouched(0) && x0 < 70) || (Gdx.input.isTouched(1) && x1 < 70);
        boolean rightButton = (Gdx.input.isTouched(0) && x0 > 70 && x0 < 134) || (Gdx.input.isTouched(1) && x1 > 70 && x1 < 134);
        boolean jumpButton = (Gdx.input.isTouched(0) && x0 > 416 && x0 < 480 && y0 < 64)
                || (Gdx.input.isTouched(1) && x1 > 416 && x1 < 480 && y0 < 64);

        if ((Gdx.input.isKeyPressed(Keys.W) || jumpButton) && state != BobState.JUMP) {
            state = BobState.JUMP;
            vel.y = JUMP_VELOCITY;
        }

        if (Gdx.input.isKeyPressed(Keys.A) || leftButton) {
            if (state != BobState.JUMP) state = BobState.RUN;
            dir = LEFT;
            acceleration.x = ACCELERATION * dir;
        } else if (Gdx.input.isKeyPressed(Keys.D) || rightButton) {
            if (state != BobState.JUMP) state = BobState.RUN;
            dir = RIGHT;
            acceleration.x = ACCELERATION * dir;
        } else {
            if (state != BobState.JUMP) state = BobState.IDLE;
            acceleration.x = 0;
        }
    }

    private void tryMove() {
        bounds.x += vel.x;
        fetchCollidableRects();
        for (Rectangle rect : r) {
            if (bounds.overlaps(rect)) {
                if (vel.x < 0)
                    bounds.x = rect.x + rect.width + 0.01f;
                else
                    bounds.x = rect.x - bounds.width - 0.01f;
                vel.x = 0;
            }
        }

        bounds.y += vel.y;
        fetchCollidableRects();
        for (Rectangle rect : r) {
            if (bounds.overlaps(rect)) {
                if (vel.y < 0) {
                    bounds.y = rect.y + rect.height + 0.01f;
                    if (state != BobState.DYING && state != BobState.SPAWN)
                        state = Math.abs(acceleration.x) > 0.1f ? BobState.RUN : BobState.IDLE;
                } else
                    bounds.y = rect.y - bounds.height - 0.01f;
                vel.y = 0;
            }
        }

        pos.x = bounds.x - 0.2f;
        pos.y = bounds.y;
    }

    private void fetchCollidableRects() {
        int p1x = (int) bounds.x;
        int p1y = (int) Math.floor(bounds.y);
        int p2x = (int) (bounds.x + bounds.width);
        int p2y = (int) Math.floor(bounds.y);
        int p3x = (int) (bounds.x + bounds.width);
        int p3y = (int) (bounds.y + bounds.height);
        int p4x = (int) bounds.x;
        int p4y = (int) (bounds.y + bounds.height);

        int[][] tiles = map.tiles;
        int tile1 = tiles[p1x][map.tiles[0].length - 1 - p1y];
        int tile2 = tiles[p2x][map.tiles[0].length - 1 - p2y];
        int tile3 = tiles[p3x][map.tiles[0].length - 1 - p3y];
        int tile4 = tiles[p4x][map.tiles[0].length - 1 - p4y];

        if (state != BobState.DYING && (map.isDeadly(tile1) || map.isDeadly(tile2) || map.isDeadly(tile3) || map.isDeadly(tile4))) {
            state = BobState.DYING;
            stateTime = 0;
        }

        if (tile1 == Map.TILE)
            r[0].set(p1x, p1y, 1, 1);
        else
            r[0].set(-1, -1, 0, 0);
        if (tile2 == Map.TILE)
            r[1].set(p2x, p2y, 1, 1);
        else
            r[1].set(-1, -1, 0, 0);
        if (tile3 == Map.TILE)
            r[2].set(p3x, p3y, 1, 1);
        else
            r[2].set(-1, -1, 0, 0);
        if (tile4 == Map.TILE)
            r[3].set(p4x, p4y, 1, 1);
        else
            r[3].set(-1, -1, 0, 0);

        if (map.cube.state == Cube.FIXED) {
            r[4].x = map.cube.bounds.x;
            r[4].y = map.cube.bounds.y;
            r[4].width = map.cube.bounds.width;
            r[4].height = map.cube.bounds.height;
        } else
            r[4].set(-1, -1, 0, 0);
    }

    enum BobState {
        SPAWN,
        IDLE,
        RUN,
        JUMP,
        DYING,
        DEAD
    }
}
