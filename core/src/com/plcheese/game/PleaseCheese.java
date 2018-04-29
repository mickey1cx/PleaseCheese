package com.plcheese.game;

import com.badlogic.gdx.Game;

public class PleaseCheese extends Game {



	@Override
	public void create () {

		setScreen(new CheeseMenu(this));

	}

}
