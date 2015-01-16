package us.hwan.game.entities;

import us.hwan.game.level.Level;
import us.hwan.game.level.tiles.Tile;

public abstract class Mob extends Entity {

	protected String name;
	protected int speed;
	protected int numSteps = 0;
	protected boolean isMoving;
	protected int dir = 1;
	// hitbox = {xMin, xMax, yMin, yMax}
	public int[] hitbox = {0, 0, 0, 0};

	public Mob(Level level, String name, int x, int y, int speed) {
		super(level);
		this.name = name;
		this.x = x;
		this.y = y;
		this.speed = speed;
	}

	public void move(int xa, int ya) {
		if(xa != 0 && ya != 0) {
			move(xa, 0);
			move(0, ya);
			numSteps--;
			return;
		}
		if(!xCollided(xa, ya)) {
			numSteps++;
			if(xa < 0) dir = 0;
			if(xa > 0) dir = 1;
			x += xa * speed;
		}
		if(!yCollided(xa, ya)) {
			numSteps--;
			y += ya * speed;
		}
	}
	
	public abstract boolean xCollided(int xa, int ya);
	public abstract boolean yCollided(int xa, int ya);
	
	protected boolean isSolidTile(int xa, int ya, int x, int y) {
		if(level == null) return false;
		Tile prev = level.getTile((this.x + x) >> 3, (this.y + y) >> 3);
		Tile next = level.getTile((this.x + x + xa) >> 3, (this.y + y + ya) >> 3);
		if(prev.equals(next) && next.isSolid()) {
			return true;
		}
		return false;
	}
	
	public String getName() {
		return name;
	}

}
