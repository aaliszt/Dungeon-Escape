//Aaron Liszt
//GamePanel.java
//Main Game engine

import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;


public class GamePanel extends JPanel implements Runnable, KeyListener{
	
	//Panel Dimensions
	public static final int WIDTH = 256;//Playing area width
	public static final int HEIGHT = 256;//Playing are height
	public static final int SCALE = 3;//Doubles the size of the images being drawn
	
	//Camera position(4 X 4 Grid of Sectors)
	public static int xSector;//Hero's current xSector relative to the map 
	public static int ySector;//Hero's current ySector relative to the map
	private int sectorSize;//Size of the sector being shown
	
	//Image
	private BufferedImage image;
	private Graphics2D g;
	
	//Map
	private TileMap tMap;
	
	//Entities
	public static Hero hero;
	private ArrayList<Baddy> enemies;
	
	//Bullets
	public static ArrayList<Bullet> bullets; 
	
	//Game Loop
	private boolean running;
	
	public GamePanel(){
		setPreferredSize(new Dimension(WIDTH * SCALE - 8, HEIGHT * SCALE - 8));
		setFocusable(true);
		requestFocus();
	}
	
	/*
	 * NOTE:
	 * intitializeIt() initilizes all variables nessesary
	 * for the game loop to begin running. This includes
	 * all entities, the tileMap, the camera, and the keyListner.
	 * initializeIt() is called when run() is called. 
	 */
	
	//Initializes variables in GamePanel once the game loop begins
	public void initializeIt(){
		image = new BufferedImage(WIDTH, HEIGHT, 2);
		g = (Graphics2D) image.getGraphics();
		
		//Map
		tMap = new TileMap(16);
		tMap.loadTiles("Resources/Tiles/myTileSet.gif");
		tMap.loadMap("Resources/Maps/Dungeon.txt");
		
		//Hero
		hero = new Hero(tMap);
		hero.setTilePosition(8, 8);
		
		//Enemies
		enemies = new ArrayList<Baddy>();
		for(int i = 0; i < 23; i++){ enemies.add(new Baddy(tMap)); }

		//Enemies Spawn Points in the map
		enemies.get(0).setTilePosition(5, 39);
		enemies.get(1).setTilePosition(5, 40);
		enemies.get(2).setTilePosition(10, 39);
		enemies.get(3).setTilePosition(10, 40);
		enemies.get(4).setTilePosition(23, 18);
		enemies.get(5).setTilePosition(24, 18);
		enemies.get(6).setTilePosition(23, 30);
		enemies.get(7).setTilePosition(24, 30);
		enemies.get(8).setTilePosition(21, 18);
		enemies.get(9).setTilePosition(21, 28);
		enemies.get(10).setTilePosition(44, 44);
		enemies.get(11).setTilePosition(44, 34);
		enemies.get(12).setTilePosition(24, 62);
		enemies.get(13).setTilePosition(25, 62);
		enemies.get(14).setTilePosition(26, 62);
		enemies.get(15).setTilePosition(27, 62);
		enemies.get(16).setTilePosition(46, 39);
		enemies.get(17).setTilePosition(46, 40);
		enemies.get(18).setTilePosition(35, 19);
		enemies.get(19).setTilePosition(35, 29);
		enemies.get(20).setTilePosition(53, 37);
		enemies.get(21).setTilePosition(53, 44);
		enemies.get(22).setTilePosition(58, 40);			
		
		//Bullets
		bullets = new ArrayList<Bullet>();
		
		//Starting camera position 
		sectorSize = WIDTH;
		xSector = hero.getX() / sectorSize;
		ySector = hero.getY() / sectorSize;
		tMap.setPositionNOW(-xSector * sectorSize, -ySector * sectorSize);
		
		//Game Loop Stuff
		running = true;
		addKeyListener(this);
	}

	/*
	 * NOTE:
	 * The Game loop calls update(), draw(), and drawToScreen()
	 * repeatedly. These methods all call the update(), and draw()
	 * methods of the other variables in the gamePanel.
	 */

	//Starts the Game loop
	public void run(){
		initializeIt();
		
		//Game Loop
		while(running){	
			update();
			draw();
			drawToScreen();
		}
	}
	
	/*
	 * NOTE:
	 * update() calls the update methods of the entites, the tileMap, and the bullets.
	 * It contains the logic for hit detection between the enemies and the hero, which is based off
	 * of the enemies nextTile, and the hero's current tile. It also contains logic for hit detection
	 * between the hero's bullets and the enemies. This hit detection loops through the bullets and  
	 * enemies and checks if any of the bullets are on the same tile as an enemy. update() also 
	 * updates and checks for keyEvents.
	 */

	private void update(){ 
		//update camera
		xSector = hero.getX() / sectorSize;
		ySector = hero.getY() / sectorSize;
		tMap.setPosition(-xSector * sectorSize, - ySector * sectorSize);
		tMap.update();
		
		if(tMap.isMoving()){ return; }
		
		//Enemy hit detection the hero
		for(int i = 0; i < enemies.size(); i++){
			if(hero.getRow() == enemies.get(i).getNextRow() && hero.getCol() == enemies.get(i).getNextCol() && enemies.get(i).getNextRow() != 0){
			System.out.println("Hero Hit!");
			hero.hit(enemies.get(i).getLeft(), enemies.get(i).getRight(), enemies.get(i).getUp(), enemies.get(i).getDown());
			System.out.println("HP: " + hero.getHP());
			if(hero.getDead()){ System.out.println("GAME-OVER!"); System.out.println("You died!"); }	
			}	
		}
		
		//Updates all of the enemies					
		for(int e = 0; e < enemies.size(); e++){		
			if(enemies.get(e).getDead()){
				enemies.remove(e);
				e--;
			}
			else
				enemies.get(e).update();		
		}
		
		//Updates all of the bullets and detects hits with the enemies
		for(int i = 0; i < bullets.size(); i++){
			boolean remove = bullets.get(i).update();
			for(int e = 0; e < enemies.size(); e++){
				if(bullets.get(i).getRow() == enemies.get(e).getRow() && bullets.get(i).getCol() == enemies.get(e).getCol()){
					enemies.get(e).hit(bullets.get(i).getLeft(), bullets.get(i).getRight(), bullets.get(i).getUp(), bullets.get(i).getDown());	
					remove = true;
				}
			}
			if(tMap.getType(bullets.get(i).getRow(), bullets.get(i).getCol()) == Tiles.EBLOCK)
				remove = true;
			if(remove){
				bullets.remove(i);
				i--;		
			}
		}
		
		//Updates Keys and hero
		hero.update();
		checkKeys();
		Keys.update();
		if(enemies.size() == 0){ hero.setDead(); System.out.println("ALL ENEMIES KILLED!"); System.out.println("You Win!"); running = false; } //Ends Game				
	}
	
	/*
	 * NOTE:
	 * draw() calls all of the entities draw() methods, the bullet draw() 
	 * method, as well as the tileMap's draw() method. All of the methods 
	 * draw their graphics to one Graphics2D variable aka (g).
	 */
	
	private void draw(){
		tMap.draw(g);
		hero.draw(g); 
		for(int i = 0; i < bullets.size(); i++)
			bullets.get(i).draw(g);
		for(int e = 0; e < enemies.size(); e++)
			enemies.get(e).draw(g);	
					
	}
	
	/*
	 * NOTE:
	 * drawToScreen() draws the final Image of graphics
	 * after all of the draw() methods from the individual
	 * variable have been called.
	 */
	
	private void drawToScreen(){
		Graphics g6 = getGraphics();
		g6.drawImage(image, -8, -8, WIDTH * SCALE, HEIGHT * SCALE, null);//-8 Adjusts the positioning of the image in the JPanel
		g6.dispose();
	}
	
	/*
	 * NOTE:
	 * checkKey() checks for keyEvents and 
	 * updates hero when a key is pressed that
	 * corrisponds to the hero's movement. 
	 */
	
	//Checks for keyEvents
	private void checkKeys(){
		if(Keys.isDown(Keys.LEFT)){ hero.setLeft(); }
		if(Keys.isDown(Keys.RIGHT)){ hero.setRight(); }
		if(Keys.isDown(Keys.UP)){ hero.setUp(); }
		if(Keys.isDown(Keys.DOWN)){ hero.setDown(); }
		if(Keys.isPressed(Keys.SPACE)){hero.setAttack(); }
	}
		
	public void keyTyped(KeyEvent key){	}
	public void keyPressed(KeyEvent key){ Keys.keySet(key.getKeyCode(), true); }
	public void keyReleased(KeyEvent key){ Keys.keySet(key.getKeyCode(), false); }		
}