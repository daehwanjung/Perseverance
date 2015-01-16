package us.hwan.game.entities;

import us.hwan.game.gfx.Colors;
import us.hwan.game.gfx.Screen;
import us.hwan.game.level.Level;

public class Enemy extends Mob {

	private int power;
	private int speed;
	private int counter = 0;
	private int color;
	private int offset = 0;
	private int type;
	
	public Enemy(Level level, String name, int x, int y, int dir, int power, int speed, int type) {
		super(level, name, x, y + 6, 0);
		this.dir = dir;
		this.type = type;
		if (type == 1) {
			color = Colors.get(-1, 222, 111, 411);
		} else {
			color = Colors.get(-1, 222, 441, 414);
		}
		if (dir == 0) {
			offset = -4;
		} else {
			offset = dir + 3;
		}
		this.power = power;
		this.speed = speed;
	}

	public boolean xCollided(int xa, int ya) {
		return false;
	}

	public boolean yCollided(int xa, int ya) {
		return false;
	}

	public void tick() {
		if (counter == speed) {
			if (type == 1) {
				level.addEntity(new Bullet(level, x + offset, y, dir, power));
			} else {
				level.addEntity(new Pellet(level, x + offset, y, dir, power));
			}
			counter = 0;
		}
		counter++;
	}

	public void render(Screen screen) {
		int xTile = 0;
		int yTile = 27;
		int flip = (dir + 1) % 2;
		if (counter >= 0 && counter < 32) xTile++;
		
		screen.render(x, y - 6, xTile + yTile * 32, color, flip, 1);
	}

}
