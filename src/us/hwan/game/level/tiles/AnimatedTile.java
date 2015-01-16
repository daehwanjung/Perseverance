package us.hwan.game.level.tiles;

public class AnimatedTile extends BasicTile {

	private int[][] tiles;
	private int currentIndex;
	private long iterationTime;
	private int delay;
	
	public AnimatedTile(int id, int[][] animation, int tileColor, int levelColor, int animationDelay) {
		super(id, animation[0][0], animation[0][1], tileColor, levelColor);
		this.tiles = animation;
		this.currentIndex = 0;
		this.iterationTime = System.currentTimeMillis();
		this.delay = animationDelay;
	}
	
	public void tick() {
		if((System.currentTimeMillis() - iterationTime) >= (delay)) {
			iterationTime = System.currentTimeMillis();
			currentIndex = (currentIndex + 1) % tiles.length;
			this.tileId = (tiles[currentIndex][0] + (tiles[currentIndex][1] * 32));
		}
	}

}
