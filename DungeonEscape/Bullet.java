//Aaron Liszt
//Bullet.java
//Creates the bullet object

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Bullet{
	
	private int x;//Current x position
	private int y;//Current y position
	private int xMap;//Current x position relative to the map
	private int yMap;//Current y position relative to the map
	
	private int width;//Width of the bullet
	private int height;//Height of the bullet
	
	private int speed;//How fast the bullet moves
	
	private int rowTile;//Current row in the tilemap
	private int colTile;//Current col in the tilemap
	
	private TileMap map;//The tilemap the bullet is currently in
	private int tileSize;//The size of the tiles being drawn
	
	//Direction 
	private boolean left;//True when the bullet is moving left
	private boolean right;//True when the bullet is moving right
	private boolean down;//True when the bullet is moving down
	private boolean up;//Ture when the bullet is moving up
	
	//Sprites
	BufferedImage[] swordDOWN;//Down facing sword
	BufferedImage[] swordLEFT;//Left facing sword
	BufferedImage[] swordUP;//Up facing sword
	BufferedImage[] swordRIGHT;//Right facing sword
	
	Bullet(TileMap m, boolean l, boolean r, boolean u, boolean d, int startX, int startY){
		map = m;
		tileSize = m.getTileSize();

		x = startX;
		y = startY; 
		
		width = 8;
		height = 8;
		
		speed = 3;
		rowTile = startX / tileSize;
		colTile = startY / tileSize;
		
		left = l;
		right = r;
		down = d;
		up = u;
		
		swordDOWN = Content.SWORD[0];
		swordLEFT = Content.SWORD[1];
		swordUP = Content.SWORD[2];
		swordRIGHT = Content.SWORD[3];
		
	}
	
	/*
	 * NOTE:
	 * The following get methods return various protected 
	 * variables. Some are only for testing purposes and are
	 * no longer being used.
	 */
	
	public boolean getRight(){ return right; }
	public boolean getLeft(){ return left; }
	public boolean getDown(){ return down; }
	public boolean getUp(){ return up; }
	public int getRow(){ return rowTile; }
	public int getCol(){ return colTile; }
	public int getX(){ return x; }
	public int getY(){ return y; }
	
	/*
	 * NOTE:
	 * path() updates the bullets current location using the speed
	 * and direction variables. path() checks the direction that the
	 * bullet is moving in and then adds or subtracts from x or y to
	 * move the bullet in that direction.
	 */
	
	//Current path of the bullet
	public void path(){
		if(left){
			x = x - speed;
		}
		else if(right){
			x = x + speed;
		}
		else if(down){
			y = y + speed;
		}
		else{
			y = y - speed;
		}
		
		xMap = map.getX();
		yMap = map.getY();
		
		rowTile = y / tileSize;
		colTile = x / tileSize;
	}
	
	//Updates the path of the bullet, returns true when the bullet should be removed
	public boolean update(){
		if(map.getType(rowTile, colTile) == Tiles.BLOCKED){ return true; }
		path();	
		return false;				
	}
	
	//Draws the image of the bullet based of of the direction variables
	public void draw(Graphics2D bulletG){
			BufferedImage bullet;
			
			if(left){ bullet = swordLEFT[0]; }
			else if(right){ bullet = swordRIGHT[0]; }
			else if(up){ bullet = swordUP[0]; }
			else{ bullet = swordDOWN[0]; }
			
			bulletG.drawImage(bullet, x + xMap - width / 2, y + yMap - height / 2, null);	
	}
}