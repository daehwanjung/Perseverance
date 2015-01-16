package us.hwan.game.level.tiles;

public class SolidTile extends BasicTile {

	public SolidTile(int id, int x, int y, int tileColor, int levelColor) {
		super(id, x, y, tileColor, levelColor);
		this.solid = true;
	}

}
