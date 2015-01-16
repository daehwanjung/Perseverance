package us.hwan.game.entities;

import us.hwan.game.gfx.Screen;
import us.hwan.game.level.Level;

public abstract class Projectile extends Entity {

	protected int dir;
	protected int dur;
	protected int type = 0;
	
	public Projectile(Level level, int x, int y, int dir, int dur) {
		super(level);
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.dur = dur;
	}

	public void tick() {
		if (level.hasPlayer() && hit()) level.player.death();
		if (dur > 0) {
			x += dir * 2 - 1;
			dur--;
		} else {
			level.removeEntity(this);
		}
	}

	public abstract void render(Screen screen);
	
	private boolean hit() {
		for (int xx = level.player.x + level.player.hitbox[2]; xx <= level.player.x + level.player.hitbox[3]; xx++) {
			for (int yy = level.player.y + level.player.hitbox[0]; yy <= level.player.y + level.player.hitbox[1]; yy++) {
				if (x == xx && y == yy) {
					level.removeEntity(this);
					return level.player.hit(dir, type);
				}
			}
		}
		return false;
	}

}
