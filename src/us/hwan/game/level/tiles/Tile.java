package us.hwan.game.level.tiles;

import us.hwan.game.gfx.Colors;
import us.hwan.game.gfx.Screen;
import us.hwan.game.level.Level;

public abstract class Tile {

	public static final Tile[] tiles = new Tile[256];
	public static final Tile VOID = new SolidTile(0, 0, 0, Colors.get(000, 000, 000, 000), 0xFF555555);
	public static final Tile BLANK = new BasicTile(1, 1, 0, Colors.get(-1, 555, -1, -1), 0xFFFFFFFF);
	public static final Tile DIRT = new SolidTile(2, 2, 0, Colors.get(-1, -1, 441, 331), 0xFF000000);
	public static final Tile SKY = new BasicTile(3, 3, 0, Colors.get(-1, -1, 225, -1), 0xFF0000FF);
	public static final Tile GRASS = new SolidAnimatedTile(4, new int[][] {{0, 5}, {1, 5}, {2, 5}, {1, 5}},
			Colors.get(-1, 331, 131, -1), 0xFF00FF00, 500);
	public static final Tile RAIN = new AnimatedTile(6, new int[][] {{0, 4}, {1, 4}, {2, 4}, {3, 4}}, Colors.get(-1, -1, 222, 244), 0xFFFF0000, 100);
	
	protected byte id;
	protected boolean solid;
	protected boolean emitter;
	private int color;
	
	public Tile(int id, boolean isSolid, boolean isEmitter, int color) {
		this.id = (byte) id;
		if(tiles[id] != null) throw new RuntimeException("Duplicate tile id on " + id);
		this.solid = isSolid;
		this.emitter = isEmitter;
		this.color = color;
		tiles[id] = this;
	}
	
	public byte getId() {
		return id;
	}
	
	public boolean isSolid() {
		return solid;
	}
	
	public boolean isEmitter() {
		return emitter;
	}
	
	public int getColor() {
		return color;
	}
	
	public abstract void tick();
	
	public abstract void render(Screen screen, Level level, int x, int y);
}
