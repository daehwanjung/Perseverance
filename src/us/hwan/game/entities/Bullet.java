package us.hwan.game.entities;

import us.hwan.game.gfx.Colors;
import us.hwan.game.gfx.Screen;
import us.hwan.game.level.Level;

public class Bullet extends Projectile {
	
	private int color = Colors.get(-1, 222, 111, -1);

	public Bullet(Level level, int x, int y, int dir, int str) {
		super(level, x, y, dir, str);
		type = 1;
	}
	
	public void render(Screen screen) {
		int xTile = 0;
		int yTile = 26;
		int flip = (dir + 1) % 2;
		screen.render(x, y - 6, xTile + yTile * 32, color, flip, 1);
	}

}
