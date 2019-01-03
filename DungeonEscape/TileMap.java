//Aaron Liszt
//TileMap.java
//Reads tileMaps out of notepad files 

import java.awt.Graphics2D;
import java.io.FileReader;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;

public class TileMap{
	
	//Position
	private int x;//tileMaps current x position
	private int y;//tileMaps current y position
	private int xDest;//tileMaps next x position
	private int yDest;//tileMaps next y position
	private int speed;//speed at which the tileMap scrolls
	private boolean moving;//true when the tileMap is scrolling
	
	//Bounds
	private int xMin;//Minimum x position
	private int xMax;//Maximum x position
	private int yMin;//Minimum y position
	private int yMax;//Maximum y position
	
	//Map
	private int[][] map;//Matrix of ints that represents the tiles
	private int tileSize;//16 x 16 (In pixels)
	private int numRows;//Number of rows in the map
	private int numCols;//Number of columns in the map
	private int mapWidth;//Width of the map (in pixels)
	private int mapHeight;//Height of the map (in pixels)
	
	//Tileset
	private BufferedImage tileSet;//Full image of the tileSet
	private int numTilesAcross;//Number of tiles across the tileSet Image
	private Tiles[][] tiles;//Broken down tilSet int a matirx of Tile objects 
	
	//Drawing
	private int rowOffset;//Offset of the rows being drawn
	private int colOffset;//Offset of the columns being drawn
	private int numRowsDraw;//Number of rows being drawn
	private int numColsDraw;//Number of columns being drawn
	
	public TileMap(int tSize){
		tileSize = tSize;
		numRowsDraw = GamePanel.HEIGHT / tileSize + 2;
		numColsDraw = GamePanel.WIDTH / tileSize + 2;
		speed = 6;
	}	
	
	/*
	 * NOTE:
	 * loadTiles() breaks the tileSet image into 16 x 16 tiles. loadTiles() takes a fileName
	 * that represents the entire tileSet. It reads in that file and then begins breaking the
	 * tileSet into subImages. loadTiles() uses the getSubimage() method which takes an x postion
	 * y position, a width and a height. The x is the current column * tileSize, the y is the 
	 * current row (tileSize * row), the width and height are both tileSize. Each subImage is 
	 * stored in the Tiles[][] matrix which is used to draw the map.
	 */
	
	//Loads the images of the tiles
	public void loadTiles(String fileName){		
		try{
			tileSet = ImageIO.read(getClass().getResourceAsStream(fileName));
			numTilesAcross = tileSet.getWidth() / tileSize;
			tiles = new Tiles[6][numTilesAcross];
			
			BufferedImage subImage;
			for(int col = 0; col < numTilesAcross; col++){
				subImage = tileSet.getSubimage(col * tileSize, 0, tileSize, tileSize);
				tiles[0][col] = new Tiles(subImage, Tiles.WALKABLE);
				subImage = tileSet.getSubimage(col * tileSize, tileSize, tileSize, tileSize);
				tiles[1][col] = new Tiles(subImage, Tiles.BLOCKED);
				subImage = tileSet.getSubimage(col * tileSize, tileSize * 2, tileSize, tileSize);
				tiles[2][col] = new Tiles(subImage, Tiles.BLOCKED);
				subImage = tileSet.getSubimage(col * tileSize, tileSize * 3, tileSize, tileSize);
				tiles[3][col] = new Tiles(subImage, Tiles.BLOCKED);
				subImage = tileSet.getSubimage(col * tileSize, tileSize * 4, tileSize, tileSize);
				tiles[4][col] = new Tiles(subImage, Tiles.BLOCKED);
				subImage = tileSet.getSubimage(col * tileSize, tileSize * 5, tileSize, tileSize);
				tiles[5][col] = new Tiles(subImage, Tiles.EBLOCK);	
			}
			
		}
		catch(Exception e){ e.printStackTrace(); }
	}
	
	/*
	 * NOTE:
	 * loadMap() loads the map by reading a notepad file
	 * that has integer values that represent tiles. Each
	 * The ints are read into a matrix used to draw the map
	 */
	
	//Reads notepad files and loads the map
	public void loadMap(String fileName){
		try{
			InputStream in = getClass().getResourceAsStream(fileName);
			BufferedReader bR = new BufferedReader(new InputStreamReader(in));
			
			numCols = Integer.parseInt(bR.readLine());
			numRows = Integer.parseInt(bR.readLine());
			map = new int[numCols][numRows];
			mapWidth = numCols * tileSize;
			mapHeight = numRows * tileSize;
		
			xMin = GamePanel.WIDTH - mapWidth;
			xMin = -mapWidth;
			xMax = 0;
			yMin = GamePanel.HEIGHT - mapHeight;
			yMin = -mapHeight;
			yMax = 0;
			
			String delimiters = "\\s+";
			
			for(int row = 0; row < numRows; row++){
				String line = bR.readLine();
				String[] nums = line.split(delimiters);
				for(int col = 0; col < numCols; col++){
					map[row][col] = Integer.parseInt(nums[col]);
				}
			}
		}
		catch(Exception e){ e.printStackTrace(); }
	} 

	//Update the current position of the tileMap
	public void update(){
		if(x < xDest){
			x += speed;
			if(x > xDest){ x = xDest; }		
		}
		if(x > xDest){
			x -= speed;
			if(x < xDest){ x = xDest; }
		}
		if(y < yDest){
			y += speed;
			if(y > yDest){ y = yDest; }		
		}
		if(y > yDest){
			y -= speed;
			if(y < yDest){ y = yDest; } 
		}	
			
		fixBounds();
		
		colOffset = -x / tileSize;
		rowOffset = -y / tileSize;
		
		if(x != xDest || y != yDest){ moving = true; }
		else{ moving = false; }
	}
	
	
	//Draws the image of the map using the map[][] int mattrix
	//and the tiles[][] tile matrix.
	public void draw(Graphics2D tileG){
		for(int row = rowOffset; row < rowOffset + numRowsDraw; row++){
			if(row >= numRows) break;
			for(int col = 0; col < colOffset + numColsDraw; col++){
				if(col >= numCols) break;
				if(map[row][col] == 0) continue;
				
				int rc = map[row][col];				
				int r = rc / numTilesAcross;
				int c = rc % numTilesAcross;
				
				tileG.drawImage(tiles[r][c].getImage(), x + col * tileSize + 4,
								y + row * tileSize + 4, null);//4 adjusts where the walls are drawn 				
			}												 //so that the player cannot "walk" the on walls 
		}
	}
	
	//Fixes the positioning of the tileMap after update() changes the position of the map
	public void fixBounds() {
		if(x < xMin) x = xMin;
		if(y < yMin) y = yMin;
		if(x > xMax) x = xMax;
		if(y > yMax) y = yMax;
	}
	
	/*
	 * NOTE:
	 * The following get methods return various protected 
	 * variables. Some are only for testing purposes and are
	 * no longer being used.
	 */
	
	public int getTileSize(){ return tileSize; }
	public int getX(){ return x; }
	public int getY(){ return y; }
	public int getWidth(){ return mapWidth; }
	public int getHeight(){ return mapHeight; }
	public int getNumCols(){ return numCols; }
	public int getNumRows(){ return numRows; }
	public int getTile(int r, int c){ return map[r][c]; }
	public boolean isMoving(){ return moving; }
	
	//Returns the type of a specified tile
	public int getType(int row, int col){
		int rc = map[row][col];
		int r = rc / numTilesAcross;
		int c = rc % numTilesAcross;
		return tiles[r][c].getType();
	}
	
	//Sets the position of the tileMap
	public void setPosition(int x, int y){
		xDest = x;
		yDest = y;
	}
	public void setPositionNOW(int Nx, int Ny){
		x = Nx;
		y = Ny;
	}
	public void setX(int v){ x = v; }
	public void setY(int v){ y = v; }
}