package us.hwan.game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;

import javax.swing.JFrame;

import us.hwan.game.entities.Enemy;
import us.hwan.game.entities.Player;
import us.hwan.game.gfx.Colors;
import us.hwan.game.gfx.Font;
import us.hwan.game.gfx.Screen;
import us.hwan.game.gfx.SpriteSheet;
import us.hwan.game.level.Level;

import sun.audio.*;
import java.io.IOException;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 160;
	public static final int HEIGHT = WIDTH / 12 * 9;
	public static final int SCALE = 3;
	public static final String NAME = "Perseverance";

	private JFrame frame;

	public boolean running = false;
	public int tickCount = 0;

	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	private int[] colors = new int[6 * 6 * 6];
	
	private Screen screen;
	public InputHandler input;
	public Level level;
	public Player player;
	public ArrayList<Enemy> enemies;
	
	private AudioPlayer AP = AudioPlayer.player;
	private AudioStream AS;
	private AudioData AD;

	public Game() {
		setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

		frame = new JFrame(NAME);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		frame.add(this, BorderLayout.CENTER);
		frame.pack();

		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public void init() {
		int index = 0, rr, gg, bb;
		for(int r = 0; r < 6; r++) {
			for(int g = 0; g < 6; g++) {
				for(int b = 0; b < 6; b++) {
					rr = (r * 255 / 5);
					gg = (g * 255 / 5);
					bb = (b * 255 / 5);
					
					colors[index++] = rr << 16 | gg << 8 | bb;
				}
			}
		}
		
		screen = new Screen(WIDTH, HEIGHT, new SpriteSheet("/sprite_sheet.png"));
		input = new InputHandler(this);
		level = new Level("/levels/main_level.png");
		player = new Player(level, 16, 216, input);
		level.addPlayer(player);
		level.spawn();
		
		ContinuousAudioDataStream loop = null;

		try {
			AS = new AudioStream(Game.class.getResourceAsStream("/audio/background.wav"));
			AD = AS.getData();
			loop = new ContinuousAudioDataStream(AD);
		} catch(IOException e) {
			System.out.println(e);
		}

		if (loop != null) {
			AP.start(loop);
		}
	}

	public synchronized void start() {
		running = true;
		new Thread(this).start();
	}

	public synchronized void stop() {
		running = false;
	}

	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D / 60D;

		int ticks = 0;
		int frames = 0;

		long lastTimer = System.currentTimeMillis();
		double delta = 0;

		init();

		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = true;

			while (delta >= 1) {
				ticks++;
				tick();
				delta -= 1;
				shouldRender = true;
			}

			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (shouldRender) {
				frames++;
				render();
			}

			if (System.currentTimeMillis() - lastTimer > 1000) {
				lastTimer += 1000;
				System.out.println(ticks + " ticks, " + frames + " frames");
				frames = 0;
				ticks = 0;
			}
		}
	}

	public void tick() {
		tickCount++;
		if(!level.hasPlayer()) {
			player = new Player(level, 16, 216, input);
			level.addPlayer(player);
		}
		level.tick();
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		int xOffset = player.x - (screen.width / 2);
		int yOffset = player.y - (screen.height / 2);
		
		level.renderTiles(screen, xOffset, yOffset);
		
		// Show column numbers
		/*
		for(int x = 0; x < level.width; x++) {
			int color = Colors.get(-1, -1, -1, 000);
			if(x % 10 == 0 && x != 0) {
				color = Colors.get(-1, -1, -1, 500);
			}
			Font.render((x % 10) + "", screen, 0 + (x * 8), 0, color, 1);
		}
		*/
		
		level.renderEntities(screen);
		
		Font.render(NAME, screen, 16, 160, Colors.get(-1, -1, -1, 441), 1);
		Font.render("A game by E. Jung", screen, 0, 168, Colors.get(-1, -1, -1, 331), 1);
		Font.render("Arrow Keys to Move", screen, 0, 184, Colors.get(-1, -1, -1, 111), 1);
		Font.render("Z to Block Black", screen, 168, 192, Colors.get(-1, -1, -1, 111), 1);
		Font.render("X to Eat Red", screen, 176, 200, Colors.get(-1, -1, -1, 111), 1);
		
		for(int y = 0; y < screen.height; y++) {
			for(int x = 0; x < screen.width; x++) {
				int colorCode = screen.pixels[x + y * screen.width];
				if(colorCode < 255) pixels[x + y * WIDTH] = colors[colorCode];
			}
		}

		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
	}

}
