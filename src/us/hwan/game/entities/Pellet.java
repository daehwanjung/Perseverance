package us.hwan.game.entities;

import us.hwan.game.gfx.Colors;
import us.hwan.game.gfx.Screen;
import us.hwan.game.level.Level;

public class Pellet extends Projectile {
	
	private int color = Colors.get(-1, 211, 411, -1);

	public Pellet(Level level, int x, int y, int dir, int str) {
		super(level, x, y, dir, str);
		type = 2;
	}
	
	public void render(Screen screen) {
		int xTile = 1;
		int yTile = 26;
		int flip = (dir + 1) % 2;
		screen.render(x, y - 6, xTile + yTile * 32, color, flip, 1);
	}

}
