package us.hwan.game.entities;

import us.hwan.game.gfx.Screen;
import us.hwan.game.level.Level;

public abstract class Entity {

	public int x, y;
	protected Level level;
	public String name;
	
	public Entity(Level level) {
		init(level);
	}
	
	public final void init(Level level) {
		this.level = level;
	}
	
	public abstract void tick();
	
	public abstract void render(Screen screen);
}
