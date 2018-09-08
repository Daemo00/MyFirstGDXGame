
package com.mygdx.game.cubocy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Array;

public class Map {
    static final int EMPTY = 0;
    static final int TILE = 0xffffff;
    static final int SPIKES = 0x00ff00;
    public Bob bob;
    public EndDoor endDoor;
    int[][] tiles;
    Cube cube;
    final Array<Dispenser> dispensers = new Array<Dispenser>();
    final Array<Rocket> rockets = new Array<Rocket>();
    final Array<MovingSpikes> movingSpikes = new Array<MovingSpikes>();
    final Array<Laser> lasers = new Array<Laser>();
    private Dispenser activeDispenser = null;

    public Map() {
        loadBinary();
    }

    private void loadBinary() {
        Pixmap pixmap = new Pixmap(Gdx.files.internal("cubocy/levels.png"));
        tiles = new int[pixmap.getWidth()][pixmap.getHeight()];
        for (int y = 0; y < 35; y++) {
            for (int x = 0; x < 150; x++) {
                int pix = (pixmap.getPixel(x, y) >>> 8) & 0xffffff;
                int LASER = 0x00ffff;
                int MOVING_SPIKES = 0xffff00;
                int ROCKET = 0x0000ff;
                int DISPENSER = 0xff0100;
                int END = 0xff00ff;
                int START = 0xff0000;
                if (match(pix, START)) {
                    Dispenser dispenser = new Dispenser(x, pixmap.getHeight() - 1 - y);
                    dispensers.add(dispenser);
                    activeDispenser = dispenser;
                    bob = new Bob(this, activeDispenser.bounds.x, activeDispenser.bounds.y);
                    cube = new Cube(this, activeDispenser.bounds.x, activeDispenser.bounds.y);
                } else if (match(pix, DISPENSER)) {
                    Dispenser dispenser = new Dispenser(x, pixmap.getHeight() - 1 - y);
                    dispensers.add(dispenser);
                } else if (match(pix, ROCKET)) {
                    Rocket rocket = new Rocket(this, x, pixmap.getHeight() - 1 - y);
                    rockets.add(rocket);
                } else if (match(pix, MOVING_SPIKES)) {
                    movingSpikes.add(new MovingSpikes(this, x, pixmap.getHeight() - 1 - y));
                } else if (match(pix, LASER)) {
                    lasers.add(new Laser(this, x, pixmap.getHeight() - 1 - y));
                } else if (match(pix, END)) {
                    endDoor = new EndDoor(x, pixmap.getHeight() - 1 - y);
                } else {
                    tiles[x][y] = pix;
                }
            }
        }

        for (int i = 0; i < movingSpikes.size; i++) {
            movingSpikes.get(i).init();
        }
        for (int i = 0; i < lasers.size; i++) {
            lasers.get(i).init();
        }
    }

    boolean match(int src, int dst) {
        return src == dst;
    }

    public void update(float deltaTime) {
        bob.update(deltaTime);
        if (bob.state == Bob.BobState.DEAD)
            bob = new Bob(this, activeDispenser.bounds.x, activeDispenser.bounds.y);
        cube.update(deltaTime);
        if (cube.state == Cube.DEAD)
            cube = new Cube(this, bob.bounds.x, bob.bounds.y);
        for (int i = 0; i < dispensers.size; i++) {
            if (bob.bounds.overlaps(dispensers.get(i).bounds)) {
                activeDispenser = dispensers.get(i);
            }
        }
        for (int i = 0; i < rockets.size; i++) {
            Rocket rocket = rockets.get(i);
            rocket.update(deltaTime);
        }
        for (int i = 0; i < movingSpikes.size; i++) {
            MovingSpikes spikes = movingSpikes.get(i);
            spikes.update(deltaTime);
        }
        for (int i = 0; i < lasers.size; i++) {
            lasers.get(i).update();
        }
    }

    public boolean isDeadly(int tileId) {
        return tileId == SPIKES;
    }
}
