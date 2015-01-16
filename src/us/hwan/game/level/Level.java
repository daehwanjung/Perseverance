package us.hwan.game.level;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import us.hwan.game.entities.Enemy;
import us.hwan.game.entities.Entity;
import us.hwan.game.entities.Finish;
import us.hwan.game.entities.Player;
import us.hwan.game.gfx.Screen;
import us.hwan.game.level.tiles.Tile;

public class Level {

	private byte[] tiles;
	public int width;
	public int height;
	public List<Entity> entities = new ArrayList<Entity>();
	private String imagePath;
	private BufferedImage image;
	public Player player;
	
	public Level(String imagePath) {
		if(imagePath != null) {
			this.imagePath = imagePath;
			this.loadLevel();
		} else {
			this.width = 64;
			this.height = 64;
			tiles = new byte[width * height];
			this.generateLevel();
		}
	}
	
	private void loadLevel() {
		try {
			this.image = ImageIO.read(Level.class.getResource(this.imagePath));
			this.width = image.getWidth();
			this.height = image.getHeight();
			tiles = new byte[width * height];
			this.loadTiles();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadTiles() {
		int[] tileColors = this.image.getRGB(0, 0, width, height, null, 0, width);
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				tileCheck: for(Tile t : Tile.tiles) {
					if(t != null && t.getColor() == tileColors[x + y * width]) {
						this.tiles[x + y * width] = t.getId();
						break tileCheck;
					}
				}
			}
		}
	}
	
	// Sandbox Capability
	/*
	private void saveLevel() {
		try {
			ImageIO.write(image, "png", new File(Level.class.getResource(this.imagePath).getFile()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void changeTile(int x, int y, Tile newTile) {
		this.tiles[x + y * width] = newTile.getId();
		image.setRGB(x,  y,  newTile.getColor());
	}
	*/
	
	// For testing purposes
	public void generateLevel() {
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				if(x * y % 10 < 7) {
					tiles[x + y * width] = Tile.BLANK.getId();
				} else {
					tiles[x + y * width] = Tile.DIRT.getId();
				}
			}
		}
	}
	
	public void tick() {
		for(int i = 0; i < entities.size(); i++) {
			entities.get(i).tick();
		}
		
		for(Tile t : Tile.tiles) {
			if(t != null) {
				t.tick();
			}
		}
	}
	
	public void renderTiles(Screen screen, int xOffset, int yOffset) {
		if(xOffset < 0) xOffset = 0;
		if(xOffset > ((width << 3) - screen.width)) xOffset = ((width << 3) - screen.width);
		if(yOffset < 0) yOffset = 0;
		if(yOffset > ((height << 3) - screen.height)) yOffset = ((height << 3) - screen.height);
		
		screen.setOffset(xOffset, yOffset);
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				getTile(x, y).render(screen, this, x << 3, y << 3);
			}
		}
	}
	
	public void renderEntities(Screen screen) {
		for(int i = 0; i < entities.size(); i++) {
			entities.get(i).render(screen);
		}
	}

	public Tile getTile(int x, int y) {
		if(0 > x || x >= width || 0 > y || y >= height) return Tile.VOID;
		return Tile.tiles[tiles[x + y * width]];
	}
	
	public void spawn() {
		addEntity(new Enemy(this, "grunt 1", 384, 240, 0, 128, 128, 1));
		addEntity(new Enemy(this, "grunt 2", 448, 224, 0, 128, 128, 2));
		addEntity(new Enemy(this, "climb 1", 480, 200, 0, 64, 128, 1));
		addEntity(new Enemy(this, "climb 2", 352, 192, 1, 128, 64, 1));
		addEntity(new Enemy(this, "climb 3", 480, 168, 0, 128, 64, 2));
		addEntity(new Enemy(this, "climb 4", 480, 136, 0, 128, 128, 1));
		addEntity(new Enemy(this, "climb 5", 480, 128, 0, 64, 16, 2));
		addEntity(new Enemy(this, "climb 6", 280, 112, 1, 128, 64, 2));
		addEntity(new Enemy(this, "climb 7", 480, 96, 0, 128, 64, 1));
		addEntity(new Enemy(this, "climb 8", 504, 88, 0, 128, 128, 2));
		addEntity(new Enemy(this, "jump 1", 360, 56, 1, 384, 128, 2));
		addEntity(new Enemy(this, "jump 2", 296, 40, 1, 384, 64, 1));
		addEntity(new Enemy(this, "jump 3", 792, 56, 0, 384, 256, 2));
		addEntity(new Enemy(this, "jump 4", 760, 48, 0, 384, 256, 1));
		addEntity(new Enemy(this, "cave 1", 488, 120, 1, 256, 128, 1));
		addEntity(new Enemy(this, "cave 2", 736, 112, 0, 256, 128, 2));
		addEntity(new Enemy(this, "step 1", 728, 80, 1, 192, 64, 2));
		addEntity(new Enemy(this, "step 2", 928, 88, 0, 192, 64, 1));
		addEntity(new Enemy(this, "step 3", 808, 120, 1, 80, 64, 1));
		addEntity(new Enemy(this, "step 4", 880, 128, 0, 64, 64, 2));
		addEntity(new Enemy(this, "trap", 880, 200, 0, 16, 16, 1));
		addEntity(new Enemy(this, "fall 1", 968, 72, 1, 32, 64, 2));
		addEntity(new Enemy(this, "fall 2", 1008, 96, 0, 32, 32, 1));
		addEntity(new Enemy(this, "fall 3", 968, 120, 1, 32, 32, 1));
		addEntity(new Enemy(this, "fall 4", 1008, 144, 0, 32, 64, 2));
		addEntity(new Enemy(this, "fall 5", 968, 168, 1, 32, 64, 2));
		addEntity(new Enemy(this, "fall 6", 1008, 192, 0, 32, 32, 1));
		addEntity(new Finish(this, "Child", 960, 222, 0));
	}
	
	public void addEntity(Entity entity) {
		entities.add(entity);
	}
	
	public void addPlayer(Player player) {
		entities.add(player);
		this.player = player;
	}
	
	public boolean hasPlayer() {
		if (player != null) return true;
		return false;
	}
	
	public void removeEntity(Entity entity) {
		entities.remove(entity);
	}
}
