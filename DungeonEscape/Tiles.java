//Aaron Liszt
//Tiles.java
//Stores inforation for tiles

/* NOTE:
 * Tile objects store a BufferedImage and an int
 * (1 or 0) wich tells if they are WALKABLE or BLOCKED.
 * This information is used to draw the map and handel
 * movment withtin the map. 
 */

import java.awt.image.BufferedImage;

public class Tiles{
	
	private BufferedImage image;
	private int type;
	
	//Tile types
	public static final int WALKABLE = 0;
	public static final int BLOCKED = 1;
	public static final int EBLOCK = 2;
	
	public Tiles(BufferedImage i, int t){
		image = i;
		type = t;
	}
	
	public BufferedImage getImage(){ return image; }
	public int getType(){ return type; }
}