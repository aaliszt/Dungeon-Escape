//Aaron Liszt
//Content.java
//Loads all sprites when the game starts, allows for easy access to sprites

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

/*
 * NOTE:
 * Content has a general load() method that takes a fileName, a width, and a height.
 * It uses the width and height to divide the image file specified by fileName into
 * smaller subimages of size width and height. 
 */

public class Content{
	
	public static BufferedImage[][] PLAYER = load("/Resources/Sprites/Hero/heroSprites.gif", 16, 16);
	public static BufferedImage[][] SWORD_D = load("/Resources/Sprites/Hero/heroSwordD.gif", 16, 28);
	public static BufferedImage[][] SWORD_U = load("/Resources/Sprites/Hero/heroSwordU.gif", 16, 28);
	public static BufferedImage[][] SWORD_L = load("/Resources/Sprites/Hero/heroSwordL.gif", 28, 16);
	public static BufferedImage[][] SWORD_R = load("/Resources/Sprites/Hero/heroSwordR.gif", 28, 16);
	
	public static BufferedImage[][] BADDIE = load("/Resources/Sprites/Baddies/stalfos.gif", 16, 16);
	
	public static BufferedImage[][] SWORD = load("/Resources/Sprites/Bullets/sword.gif", 16, 16);
	
	public static BufferedImage[][] load(String fileName, int w, int h){
		BufferedImage[][] ret;
		try{
			BufferedImage spriteSheet = ImageIO.read(Content.class.getResourceAsStream(fileName));
			int width = spriteSheet.getWidth() / w;
			int height = spriteSheet.getHeight() / h;
			ret = new BufferedImage[height][width];
			for(int i = 0; i < height; i++){
				for(int j = 0; j < width; j++){
					ret[i][j] = spriteSheet.getSubimage(j * w, i * h, w, h);
				}
			}
			return ret;
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("Error loading graphics!");
			System.exit(0);
		}
		return null;
	}
}