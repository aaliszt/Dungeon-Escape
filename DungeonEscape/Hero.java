//Aaron Liszt
//Hero.java
//Controllable player class

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Hero extends Entity{
	
	//Sprites
	private BufferedImage[] leftSprites;//Left-facing sprites
	private BufferedImage[] rightSprites;//Right-facing sprites
	private BufferedImage[] upSprites;//Up-facing sprites
	private BufferedImage[] downSprites;//Down-facing sprites
	private BufferedImage[] downSword;//Downward-sword facing sprites
	private BufferedImage[] upSword;//Upward-sword facing sprites
	private BufferedImage[] leftSword;//Leftward-sword facing sprites
	private BufferedImage[] rightSword;//Rightward-sword facing sprites
	
	//Animation (Unique itegers that corrisond to the hero's currentAnimation)
	private final int DOWN = 0;
	private final int LEFT = 1;
	private final int RIGHT = 2;
	private final int UP = 3;
	private final int DOWNSWORD = 4;
	private final int UPSWORD = 5;
	private final int LEFTSWORD = 6;
	private final int RIGHTSWORD = 7;
		
	public Hero(TileMap m){
		super(m);
		width = 16;
		height = 16;
		
		HP = 32;
		moveSpeed = 3;
		
		downSprites = Content.PLAYER[0];
		leftSprites = Content.PLAYER[1];
		rightSprites = Content.PLAYER[2];
		upSprites = Content.PLAYER[3];
		downSword = Content.SWORD_D[0];
		upSword = Content.SWORD_U[0];
		leftSword = Content.SWORD_L[0];
		rightSword = Content.SWORD_R[0];
		
		animation.setFrames(downSprites);
		animation.setDelay(10);	
	}
	
	//Set methods are the same as entities
	public void setDown() {
		super.setDown();
	}
	public void setLeft() {
		super.setLeft();
	}
	public void setRight() {
		super.setRight();
	}
	public void setUp() {
		super.setUp();
	}
	
	//Updates the hero's animation based off of the hero's direction
	public void update(){
		if(down){ if(currentAnimation != DOWN) { setAnimation(DOWN, downSprites, 10); } }
		if(left){ if(currentAnimation != LEFT) { setAnimation(LEFT, leftSprites, 10); } }
		if(right){ if(currentAnimation != RIGHT){ setAnimation(RIGHT, rightSprites, 10); } }
		if(up){ if(currentAnimation != UP){ setAnimation(UP, upSprites, 10); } }
		if(attack && faceDown){ if(currentAnimation != DOWNSWORD){ setAnimation(DOWNSWORD, downSword, 10); } }
		if(attack && faceUp){ if(currentAnimation != UPSWORD){ setAnimation(UPSWORD, upSword, 10); } }
		if(attack && faceLeft){ if(currentAnimation != LEFTSWORD){ setAnimation(LEFTSWORD, leftSword, 10); } }
		if(attack && faceRight){ if(currentAnimation != RIGHTSWORD){ setAnimation(RIGHTSWORD, rightSword, 10); } }
		super.update();
	}
	
	//Draws the image of the hero
	public void draw(Graphics2D heroG){
		if(currentAnimation == UPSWORD){//Adjust where upward-sword sprites are drawn
			if(dead){ return; }
			setMapPosition();
			heroG.drawImage(animation.getImage(), x + xMap - width / 2 + 4, y + yMap - height / 2 - 8, null);
		}
		else if(currentAnimation == LEFTSWORD){//Adjusts leftward-sword sprites are drawn
			if(dead){ return; }
			setMapPosition();
			heroG.drawImage(animation.getImage(), x + xMap - width / 2 - 8, y + yMap - height / 2 + 4, null);
		}
		else{ super.draw(heroG); }
	}
}