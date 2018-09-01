
package com.mygdx.game.cubocy;

import com.badlogic.gdx.Game;
import com.mygdx.game.cubocy.screens.CubocyMenu;

public class Cubocy extends Game {
    @Override
    public void create() {
        setScreen(new CubocyMenu(this));
    }
}
