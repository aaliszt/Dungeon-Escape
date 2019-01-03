//Aaron Liszt
//Baddy.java
//AI for enemies

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Baddy extends Entity{
	
	private boolean ready;//Controls when the enemies are drawn
	
	//Sprites
	private BufferedImage[] leftSprites;//Left facing sprites (Used for animation)
	private BufferedImage[] rightSprites;//Right facing sprites (Used for animation)
	private BufferedImage[] upSprites;//Up facing sprites (Used for animation)
	private BufferedImage[] downSprites;//Down facing sprites (Used for animation)
	
	//Animation (Unique integers that correspond to currentAnimation)
	private final int DOWN = 0;
	private final int LEFT = 1;
	private final int UP = 2;
	private final int RIGHT = 3;
	
	Baddy(TileMap m){
		super(m);
		width = 8;
		height = 8;
		HP = 2;
		moveSpeed = 1;
		ready = false;
		
		leftSprites = Content.BADDIE[1];
		rightSprites = Content.BADDIE[3];
		upSprites = Content.BADDIE[2];
		downSprites = Content.BADDIE[0];
		
		animation.setFrames(downSprites);
		animation.setDelay(10);
	}

	/*
	 * NOTE:
	 * move() contains the AI for Baddies. A baddie starts by facing a random direction
	 * which is determined by changeDir(). They it walks a random number of steps between
	 * 1 - 10 in it's current diretion. If it hits a wall then it turns in a random 
	 * direction and a new number of steps is generated. It continues this process until
	 * it is killed or possibly kills the player.
	 */
		
	//Controls the movement of the baddy
	public void move(){
		int steps = 0;
		if(ready){
			if(steps == 0){changeDir(-1); steps = (int)(Math.random() * (20)) + 10; steps *= 8; } //Resets steps between 1  10
			//System.out.println(steps / 8);
			if(left){
				if(playerMap.getType(rowTile, colTile - 1) != Tiles.BLOCKED && playerMap.getType(rowTile, colTile - 1) != Tiles.EBLOCK){
					setLeft();
					steps--;
				}
				else{ changeDir(0); }		
			}	
			if(right){
				if(playerMap.getType(rowTile, colTile + 1) != Tiles.BLOCKED && playerMap.getType(rowTile, colTile - 1) != Tiles.EBLOCK){
					setRight();
					steps--;
				}
				else{ changeDir(1); }		
			}
			if(up){
				if(playerMap.getType(rowTile - 1, colTile) != Tiles.BLOCKED && playerMap.getType(rowTile, colTile - 1) != Tiles.EBLOCK){
					setUp();
					steps--;
				}
				else{ changeDir(3); }
			}
			if(down){
				if(playerMap.getType(rowTile + 1, colTile) != Tiles.BLOCKED && playerMap.getType(rowTile, colTile - 1) != Tiles.EBLOCK){
					setDown();
					steps--;
				}
				else{ changeDir(2); }
			}
			if(steps == 0){
				if(left){ changeDir(0); }
				else if(right){ changeDir(1); }
				else if(down){ changeDir(2); }
				else{ changeDir(3); }
			}											
		}
	}
	
	/*
	 * NOTE:
	 * changeDir() changes the baddies current direction
	 * using Math.random(). changeDir() generates an int
	 * from 0 - 4, each int representing a different direction.
	 * changeDir() also takes an int that represents the baddy's
	 * previous direction to ensure a direction change.
	 */
	
	//Changes the enemy direction when a wall is hit
	private void changeDir(int currentDir){
		int dir = currentDir;
		
		while(dir == currentDir){//Generates int's until an new direction is produced
			dir = (int)(Math.random() * 4);
		}	
	
		if(dir == 0){ setLeft(); }
		else if(dir == 1){ setRight(); }
		else if(dir == 2){ setDown(); }
		else{ setUp(); }
	}
	
	//Checks if the baddy is in the same sector as the hero. If true the baddy is ready to be drawn.
	public boolean checkSector(){ return x / GamePanel.WIDTH == GamePanel.xSector && y / GamePanel.WIDTH == GamePanel.ySector; }
	
	//Updates the baddies animations and current position	
	public void update(){
		if(dead){ return; }
		ready = checkSector();
		move();
		if(down){ if(currentAnimation != DOWN) { setAnimation(DOWN, downSprites, 10); } }
		if(left){ if(currentAnimation != LEFT) { setAnimation(LEFT, leftSprites, 10); } }
		if(right){ if(currentAnimation != RIGHT){ setAnimation(RIGHT, rightSprites, 10); } }
		if(up){ if(currentAnimation != UP){ setAnimation(UP, upSprites, 10); } }	
		super.update();
	}
	
	//Draws the image of the baddy
	public void draw(Graphics2D baddyG){
		if(!dead){
			setMapPosition();
			baddyG.drawImage(animation.getImage(), x + xMap - width / 2, y + yMap - height / 2, null);
		}	
	}
	
	/*
	 * NOTE:
	 * This is the same method from the parent entity class
	 * EXCEPT this version checks for EBLOCK tiles which
	 * Baddies cannot walk on.
	 */  
	
	//Checks for EBLOCK tiles
	public boolean checkNextTile(){
		if(moving){ return true; }
		
		rowTile = y / tileSize;
		colTile = x / tileSize;
		
		/* NOTE:
		 * Y relates to row(up or down movement)
		 * X relates to col(left of right movement)
		 */
		
		if(left){
			if(playerMap.getType(rowTile, colTile - 1) == Tiles.BLOCKED || playerMap.getType(rowTile, colTile - 1) == Tiles.EBLOCK)
				return false;
			else
				xDest = x - tileSize;	
		}
		else if(right){
			if(playerMap.getType(rowTile, colTile + 1) == Tiles.BLOCKED || playerMap.getType(rowTile, colTile + 1) == Tiles.EBLOCK)
				return false;
			else
				xDest = x + tileSize;	
		}
		else if(up){
			if(playerMap.getType(rowTile - 1, colTile) == Tiles.BLOCKED || playerMap.getType(rowTile - 1, colTile) == Tiles.EBLOCK)
				return false;
			else
				yDest = y - tileSize;
		}
		else{
			if(playerMap.getType(rowTile + 1, colTile) == Tiles.BLOCKED || playerMap.getType(rowTile + 1, colTile) == Tiles.EBLOCK)
				return false;
			else
				yDest = y + tileSize;	
		}
		return true;			
	}
}