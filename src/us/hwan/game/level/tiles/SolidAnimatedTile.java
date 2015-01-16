package us.hwan.game.level.tiles;

public class SolidAnimatedTile extends AnimatedTile {

	public SolidAnimatedTile(int id, int[][] animation, int tileColor, int levelColor, int animationDelay) {
		super(id, animation, tileColor, levelColor, animationDelay);
		this.solid = true;
	}

}
