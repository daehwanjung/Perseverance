package us.hwan.game.entities;

import us.hwan.game.gfx.Colors;
import us.hwan.game.gfx.Screen;
import us.hwan.game.level.Level;

public class Finish extends Entity {

	private int color = Colors.get(-1, 222, 111, 411);
	
	public Finish(Level level, String name, int x, int y, int speed) {
		super(level);
		this.name = name;
		this.x = x;
		this.y = y;
	}

	public void tick() {
		if (finished()) {
			level.player.complete();
		}
	}

	public void render(Screen screen) {
		int xTile = 2;
		int yTile = 27;
		screen.render(x - 4, y - 6, xTile + yTile * 32, color, 0, 1);
	}
	
	private boolean finished() {
		for (int xx = level.player.x + level.player.hitbox[2]; xx <= level.player.x + level.player.hitbox[3]; xx++) {
			for (int yy = level.player.y + level.player.hitbox[0]; yy <= level.player.y + level.player.hitbox[1]; yy++) {
				if (x == xx && y == yy) {
					return true;
				}
			}
		}
		return false;
	}

}
