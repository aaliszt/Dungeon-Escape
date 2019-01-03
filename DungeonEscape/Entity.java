//Aaron Liszt
//Entity.java
//Abstract class that defines all player objects

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Color;

public abstract class Entity{
	
	//Dimensions
	protected int width;
	protected int height;
	
	//Position
	protected int x;//The entitys x position in the map
	protected int y;//The entitys y position in the map
	protected int xDest;//The entity's next x position in the map
	protected int yDest;//The entitiy's next y postition in the map
	protected int rowTile;//The entity's current row in the tileMap
	protected int colTile;//The entity's current col in the tileMap
	
	//Movement
	protected boolean attack;//True when the entity is attacking
	protected boolean moving;//True when the entity is moving
	protected boolean left;//True when the entity is moving left
	protected boolean right;//True when the entity is moving right
	protected boolean up;//True when the entity is moving up
	protected boolean down;//True when the entity is moving down
	
	//Direction
	protected boolean faceLeft;//True when the entity is facing left
	protected boolean faceRight;//True when the entity is facing right
	protected boolean faceUp;//True when the entity is facing up
	protected boolean faceDown;//True when the entity is facing down
	
	//Characteristics
	protected int moveSpeed;//The speed at which the player moves
	protected int HP;//The health of the player (Must be doubled du to key Inputs)
	protected boolean dead;
	
	//Tilemap
	protected TileMap playerMap;//The tilemap that entity is currently in
	protected int tileSize;//Size of the tiles in pixels
	protected int yMap;//The players current xposition relative to the map(Used for drawing)
	protected int xMap;//The players current yposition relative to the map(Used for drawing)
	
	//Animation
	protected Animation animation;//Image of the player
	protected int currentAnimation;//Current animation being show
	
	public Entity(TileMap tm){
		playerMap = tm;
		tileSize = playerMap.getTileSize();
		animation = new Animation();
		dead = false;
		attack = false;
	}
	
	/*
	 * NOTE:
	 * The following get methods return various protected 
	 * variables. Some are only for testing purposes and are
	 * no longer being used.
	 */
	
	public int getX(){ return x; }
	public int getY(){ return y; }
	public boolean getUp(){ return faceUp; }
	public boolean getRight(){ return faceRight; }
	public boolean getLeft(){ return faceLeft; }
	public boolean getDown(){ return faceDown; }	
	public int getRow(){ return rowTile; }
	public int getCol(){ return colTile; }
	public boolean getDead(){ return dead; }
	public int getHP() { return HP; }
	public boolean attacking(){ return attack; }
	public boolean isDead(){ return dead; }
	
	/*
	 * NOTE:
	 * setAnimation() sets the entities current animation
	 * when a corrisponding key is pressed. This method is 
	 * called by update(), and updates the current animations
	 * being shown when the user presses the proper keys.
	 */
	protected void setAnimation(int i, BufferedImage[] bi, int d){
		currentAnimation = i;
		animation.setFrames(bi);
		animation.setDelay(d);
	}
	
	//Gets the next row based off of the entity's facing
	public int getNextRow(){
		if(faceUp){ return rowTile - 1; }
		else if(faceDown){ return rowTile + 1; }
		else{ return rowTile; }
	}
	
	//Gets the next col based off of the entity's facing
	public int getNextCol(){
		if(faceRight){ return colTile + 1; }
		else if(faceLeft){ return colTile - 1; }
		else { return colTile; }
	}
	
	//Stops updating dead entities
	public void setDead(){
		dead = true;
		x = 0; y = 300;
		rowTile = 17; colTile = 1;
		xDest = x; yDest = y;
	}
	
	//Updates the xMap & yMap values with the entity's curent x & y position
	public void setMapPosition(){
		xMap = playerMap.getX();
		yMap = playerMap.getY();
	}
	
	//Puts the entity on a specific tile in the tileMap
	public void setTilePosition(int r, int c){
		y = r * tileSize + tileSize / 2;
		x = c * tileSize + tileSize / 2;
		yDest = y;
		xDest = x;
	}
	
	//Checks a specific tile in the TileMap
	public boolean checkTile(int r, int c){
		if(playerMap.getType(r, c) == Tiles.BLOCKED || playerMap.getType(r, c) == Tiles.EBLOCK)
			return false;
		else
			return true;		
	}
	
	/* NOTE:
	 * The following set methods update the entity's
	 * direction variables based off of the input key
	 * values from the user. If a key corresponding 
	 * to a specific direction is pressed the direction 
	 * variable will be changed to true if the entity
	 * is not moving. Moving is then updated based off of
	 * a check to the next tile in the direction the entity
	 * is moving. 
	 */
	
	public void setLeft(){ 
		if(moving || dead) { return; }	
		left = true;
		faceLeft = true;
		moving = checkNextTile(); 
	}
	public void setRight(){ 
		if(moving || dead) { return; }
		right = true;
		faceRight = true;
		moving = checkNextTile(); 
	}
	public void setUp(){ 
		if(moving || dead) { return; }
		up = true;
		faceUp = true;
		moving = checkNextTile(); 
	}
	public void setDown(){ 
		if(moving || dead){ return; }
		down = true;
		faceDown = true;
		moving = checkNextTile();
	}
	public void setAttack(){
		attack = true;
	}
	
	/* NOTE:
	 * checkNextTile() checks the tile in the direction that
	 * the entity is moving to see if it is walkable. This method
	 * uses the entitys current rowTile and colTile and then checks
	 * the TYPE of the next tile in the direction that the entity is
	 * moving. If the tile is BLOCKED then the methods returns false.
	 * Otherwise the entity's xDest or yDest are updated to move the
	 * entity in the proper direction.
	 */
	
	//Checks if hero can move to the next tile
	public boolean checkNextTile(){
		if(moving){ return true; }
		
		rowTile = y / tileSize;
		colTile = x / tileSize;
		
		/* NOTE:
		 * Y relates to row(up or down movement)
		 * X relates to col(left of right movement)
		 */
		
		if(left){
			if(playerMap.getType(rowTile, colTile - 1) == Tiles.BLOCKED)
				return false;
			else
				xDest = x - tileSize;	
		}
		else if(right){
			if(playerMap.getType(rowTile, colTile + 1) == Tiles.BLOCKED)
				return false;
			else
				xDest = x + tileSize;	
		}
		else if(up){
			if(playerMap.getType(rowTile - 1, colTile) == Tiles.BLOCKED)
				return false;
			else
				yDest = y - tileSize;
		}
		else{
			if(playerMap.getType(rowTile + 1, colTile) == Tiles.BLOCKED)
				return false;
			else
				yDest = y + tileSize;	
		}
		return true;			
	}
	
	/* NOTE:
	 * getNextPos() updates the entity's x & y values based
	 * off of the direction variables. This method checks 
	 * the direction of the entity. Then it checks if x or y
	 * need to be updated based off of xDest or yDest. moveSpeed
	 * is added to or subtracted from x & y in order to move the
	 * entity in the appropriate direction.
	 */
	
	//Detemines the next position of the player
	public void getNextPos(){
		if(left && x > xDest)
			x -= moveSpeed;//Subtracting from X moves left
		else{ left = false; }
		if(left && x < xDest){ x = xDest; }	
			
		if(right && x < xDest)
			x += moveSpeed;//Adding to X moves right	
		else{ right = false; }	
		if(right && x > xDest){ x = xDest; }
			
		if(up && y > yDest)
			y -= moveSpeed;//Subtracting from Y moves up
		else{ up = false; }
		if(up && y < yDest){ y = yDest; }
		
		if(down && y < yDest)
			y += moveSpeed;//Adding to Y moves down
		else{ down = false; }
		if(down && y > yDest){ y = yDest; }													
	}
	
	/*
	 * NOTE:
	 * fixFacing() is called by update(). It ensures that only
	 * one of the entities direction variables is true. It sets
	 * all of the direction variables to false, except for the
	 * that corresponds to the last key that was pressed by the user.
	 */
	
	//Updates the direction the player is facing
	public void fixFacing(){
		if(left){
			faceLeft = true;
			faceRight = false;
			faceUp = false;
			faceDown = false;
		}	
		if(right){
			faceRight = true;
			faceLeft = false;
			faceUp = false;
			faceDown = false;
		}	
		if(up){
			faceUp = true;
			faceDown = false;
			faceRight = false;
			faceLeft = false;
		}	
		if(down){
			faceDown = true;
			faceRight = false;
			faceLeft = false;
			faceUp = false;
		}			
	}
	
	/*
	 * NOTE:
	 * hit() contains logic for recoil when an entity is hit.
	 * hit() takes away form the entities Hit Points(HP). Recoil
	 * is based of off the direction the entity is being hit from.
	 * the entity is moved back 0 - 2 tiles opposite of the direction 
	 * that it was hit from. hit() takes 4 direction variables, only
	 * one wil be true. The true direction variable is then used for recoil. 
	 */
	
	//Updates the entity's HP and recoil 
	public void hit(boolean l, boolean r, boolean u, boolean d){
		if(r){
			setRight();
			if(checkTile(rowTile, colTile + 2))
				setTilePosition(rowTile, colTile + 2);	
			else if(checkTile(rowTile, colTile + 1))
				setTilePosition(rowTile, colTile + 1);	
			else{}			
		}
		else if(l){
			setLeft();
			if(checkTile(rowTile, colTile - 2))
				setTilePosition(rowTile, colTile - 2);	
			else if(checkTile(rowTile, colTile - 1))
				setTilePosition(rowTile, colTile - 1);	
			else{}			
		}
		else if(d){
			setDown();
			if(checkTile(rowTile + 2, colTile))
				setTilePosition(rowTile + 2, colTile);	
			else if(checkTile(rowTile + 1, colTile))
				setTilePosition(rowTile + 1, colTile);	
			else{}		
		}
		else{
			setUp();
			if(checkTile(rowTile - 2, colTile))
				setTilePosition(rowTile - 2, colTile);
			else if(checkTile(rowTile -1, colTile))
				setTilePosition(rowTile - 1, colTile);	
			else{}	
		}
		HP--;
		if(HP == 0){ setDead(); }		
	}
	
	/*
	 * NOTE:
	 * update() updates the entity's direction and movement variables,
	 * as well as the entitiy's current animation.
	 */
		
	//Updates the entity's direction variables and it's current row/colTile.
	public void update(){
		if(dead){ return; }
		if(moving){ getNextPos(); }
		//If x and y destinations have been reached stop moving	
		if(x == xDest && y == yDest){
			fixFacing();
			left = false;
			right = false;
			up = false;
			down = false;
			moving = false;
			
			rowTile = y / tileSize;
			colTile = x / tileSize;
		}
		if(attack){ GamePanel.bullets.add(new Bullet(playerMap, getLeft(), getRight(), getUp(), getDown(), x, y)); }
		attack = false;
		animation.update();		 			
	}
	
	//Draws the Image of the entity
	public void draw(Graphics2D entityG){
		if(dead){ return; }
		setMapPosition();
		entityG.drawImage(animation.getImage(), x + xMap - width / 2 + 4, y + yMap - height / 2 + 4, null);
	}
}