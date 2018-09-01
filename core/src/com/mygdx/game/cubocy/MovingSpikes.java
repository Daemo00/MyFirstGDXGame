
package com.mygdx.game.cubocy;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MovingSpikes {
    private static final int FORWARD = 1;
    private static final int BACKWARD = -1;
    private static final float FORWARD_VEL = 10;
    private static final float BACKWARD_VEL = 4;
    Vector2 pos = new Vector2();
    float angle = 0;
    private int state = FORWARD;
    private Map map;
    private Rectangle bounds = new Rectangle();
    private Vector2 vel = new Vector2();
    private int fx = 0;
    private int fy = 0;
    private int bx = 0;
    private int by = 0;

    MovingSpikes(Map map, float x, float y) {
        this.map = map;
        pos.x = x;
        pos.y = y;
        bounds.x = x;
        bounds.y = y;
        bounds.width = bounds.height = 1;
    }

    public void init() {
        int ix = (int) pos.x;
        int iy = (int) pos.y;

        int left = map.tiles[ix - 1][map.tiles[0].length - 1 - iy];
        int right = map.tiles[ix + 1][map.tiles[0].length - 1 - iy];
        int top = map.tiles[ix][map.tiles[0].length - 1 - iy - 1];
        int bottom = map.tiles[ix][map.tiles[0].length - 1 - iy + 1];

        if (left == Map.TILE) {
            vel.x = FORWARD_VEL;
            angle = -90;
            fx = 1;
        }
        if (right == Map.TILE) {
            vel.x = -FORWARD_VEL;
            angle = 90;
            bx = 1;
        }
        if (top == Map.TILE) {
            vel.y = -FORWARD_VEL;
            angle = 180;
            by = -1;
        }
        if (bottom == Map.TILE) {
            vel.y = FORWARD_VEL;
            angle = 0;
            fy = -1;
        }
    }

    public void update(float deltaTime) {
        pos.add(vel.x * deltaTime, vel.y * deltaTime);
        boolean change = false;
        if (state == FORWARD) {
            change = map.tiles[(int) pos.x + fx][map.tiles[0].length - 1 - (int) pos.y + fy] == Map.TILE;
        } else {
            change = map.tiles[(int) pos.x + bx][map.tiles[0].length - 1 - (int) pos.y + by] == Map.TILE;
        }
        if (change) {
            pos.x -= vel.x * deltaTime;
            pos.y -= vel.y * deltaTime;
            state = -state;
            vel.scl(-1);
            if (state == FORWARD) vel.nor().scl(FORWARD_VEL);
            if (state == BACKWARD) vel.nor().scl(BACKWARD_VEL);
        }

        bounds.x = pos.x;
        bounds.y = pos.y;

        if (map.bob.bounds.overlaps(bounds)) {
            if (map.bob.state != Bob.DYING) {
                map.bob.state = Bob.DYING;
                map.bob.stateTime = 0;
            }
        }

        if (map.cube.bounds.overlaps(bounds)) {
            map.cube.state = Cube.DEAD;
            map.cube.stateTime = 0;
        }
    }
}
