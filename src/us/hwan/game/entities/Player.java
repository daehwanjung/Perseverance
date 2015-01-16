package us.hwan.game.entities;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import us.hwan.game.InputHandler;
import us.hwan.game.gfx.Colors;
import us.hwan.game.gfx.Font;
import us.hwan.game.gfx.Screen;
import us.hwan.game.level.Level;

public class Player extends Mob{
	
	private InputHandler input;
	private int color = Colors.get(-1, 222, 111, 441);
	private int scale = 1;
	private boolean grounded = false;
	private boolean blocking = false;
	private boolean eating = false;
	private boolean onCooldown = false;
	private boolean done = false;
	private int jump = 18;
	private int armor = 256;
	private int stomach = 0;
	private int digestion = 256;
	private int cooldown = 256;
	private long time;
	private int timer = 1024;
	private String completion;
	private Clip block;
	private Clip eat;
	private Clip hit;

	public Player(Level level, int x, int y, InputHandler input) {
		super(level, "Player", x, y, 1);
		this.input = input;
		this.hitbox[0] = -2;
		this.hitbox[1] = 9;
		this.hitbox[2] = -7;
		this.hitbox[3] = 8;
		time = System.nanoTime();
		
		try {
			block = AudioSystem.getClip();
			eat = AudioSystem.getClip();
			hit = AudioSystem.getClip();
			block.open(AudioSystem.getAudioInputStream(Player.class.getResource("/audio/block.wav")));
			eat.open(AudioSystem.getAudioInputStream(Player.class.getResource("/audio/eat.wav")));
			hit.open(AudioSystem.getAudioInputStream(Player.class.getResource("/audio/hit.wav")));
		} catch(Exception e) {
			System.out.println(e);
		}
	}

	public void tick() {
		int xa = 0;
		int ya = 0;
		if ((grounded && jump == 18 && input.up.isPressed()) || (jump < 18 && jump > 0)) {
			jump--;
			ya--;
		} else if (!grounded) {
			ya++;
		} else {
			jump = 18;
		}
		
		if (input.left.isPressed()) xa--;
		if (input.right.isPressed()) xa++;
		
		if (!onCooldown) {
			if (armor > 0 && input.action1.isPressed()) {
				blocking = true;
				armor -= 2;
			} else {
				blocking = false;
				if (armor <= 0) {
					onCooldown = true;
				}
				if (armor < 256) {
					armor++;
				}
			}
		} else {
			cooldown--;
			if (cooldown <= 0) {
				onCooldown = false;
				cooldown = 256;
			}
		}
		
		if (input.action2.isPressed() && !input.action1.isPressed()) {
			eating = true;
		} else {
			eating = false;
		}
		
		if (stomach > 0) {
			digestion--;
			if (digestion == 0) {
				stomach = 0;
				digestion = 256;
			}
		}
		
		if(xa != 0 || ya != 0) {
			move(xa, ya);
			isMoving = true;
		} else {
			isMoving = false;
		}
	}

	public void render(Screen screen) {
		int xTile = 0;
		int yTile = 28;
		int walkSpeed = 4;
		int flip = (numSteps >> walkSpeed & 1);
		
		String meter = "";
		for (int i = 0; i < armor / 32; i++) {
			meter += "$";
		}
		Font.render("armor: " + meter, screen, screen.xOffset, screen.yOffset, Colors.get(-1, -1, -1, 000), 1);
		Font.render("stomach: " + stomach + "/3", screen, screen.xOffset, 8 + screen.yOffset, Colors.get(-1, -1, -1, 000), 1);
		
		int now = (int) ((System.nanoTime() - time) / 1000000000L);
		int sec = now % 60;
		int min = now / 60;
		
		String clock = min + ":";
		if (sec < 10) {
			clock += "0" + sec;
		}  else {
			clock += sec;
		}
		
		if (!done) {
			Font.render(clock, screen, screen.xOffset, 108 + screen.yOffset, Colors.get(-1, -1, -1, 000), 1);
		} else {
			if (timer == 1024) completion = clock;
			timer--;
			if (timer <= 0) {
				death();
			}
			String message = "Your Time: " + completion;
			Font.render("Congratulations!", screen, 16 + screen.xOffset, 48 + screen.yOffset, Colors.get(-1, -1, -1, 000), 1);
			Font.render(message, screen, 16 + screen.xOffset, 56 + screen.yOffset, Colors.get(-1, -1, -1, 000), 1);
		}
		
		if(blocking) {
			xTile = 6;
		} else if (eating) {
			xTile = 8;
		} else if (!grounded) {
			xTile = 4;
		} else {
			xTile += ((numSteps >> walkSpeed & 1) * 2);
		}

		flip = (dir + 1) % 2;
		
		int mod = 8 * scale;
		int xOffset = x - mod / 2;
		int yOffset = y - mod / 2 - 4;
		
		screen.render(xOffset + (mod * flip), yOffset, xTile + yTile * 32, color, flip, scale);
		screen.render(xOffset + mod - (mod * flip), yOffset, (xTile + 1) + yTile * 32, color, flip, scale);
		
		screen.render(xOffset + (mod * flip), yOffset + mod, xTile + (yTile + 1) * 32, color, flip, scale);
		screen.render(xOffset + mod - (mod * flip), yOffset + mod, (xTile + 1) + (yTile + 1) * 32, color, flip, scale);
	}
	
	public boolean xCollided(int xa, int ya) {
		for(int y = this.hitbox[2] + 1; y < this.hitbox[3]; y++) {
			if(isSolidTile(xa, ya, this.hitbox[0], y) || isSolidTile(xa, ya, this.hitbox[1], y)) {
				return true;
			}
		}
		return false;
	}

	public boolean yCollided(int xa, int ya) {
		for(int x = this.hitbox[0] + 1; x < this.hitbox[1]; x++) {
			if(isSolidTile(xa, ya, x, this.hitbox[2])) {
				grounded = false;
				return true;
			} else if(isSolidTile(xa, ya, x, this.hitbox[3])) {
				grounded = true;
				return true;
			}
		}
		grounded = false;
		return false;
	}
	
	public boolean hit(int side, int type) {
		if (type == 1 && blocking && dir != side) {
			playSound(block);
			return false;
		} else if (type == 2 && eating && dir != side) {
			if (stomach >= 3) {
				playSound(hit);
				return true;
			} else {
				stomach++;
				digestion = 256;
				playSound(eat);
				return false;
			}
		} else {
			playSound(hit);
			return true;
		}
	}
	
	private void playSound(Clip clip) {
		try {
			clip.setFramePosition(0);
			clip.start();
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public void complete() {
		done = true;
	}

	public void death() {
		level.removeEntity(this);
		level.player = null;
	}
	
}
