//Aaron Liszt
//Animation.java
//Cycles through images to 'animate' the entitiy

import java.awt.image.BufferedImage;

public class Animation{
	
	private BufferedImage[] frames;
	private int currentFrame;
	private int numFrames;
	
	private int count;
	private int delay;
	
	private int timesPlayed;
	
	public Animation(){
		timesPlayed = 0;
	}
	
	//Starts the animation cycle (called when an entites curren animation changes)
	public void setFrames(BufferedImage[] f){
		frames = f;
		currentFrame = 0;
		count = 0;
		timesPlayed = 0;
		delay = 2;
		numFrames = frames.length;
	}
	
	//Cycles through the images in frames[]
	public void update(){
		if(delay == -1){ return; }
		
		count++;
		if(count == delay){
			currentFrame++;
			count = 0;
		}
		if(currentFrame == numFrames){
			currentFrame = 0;
			timesPlayed++;
		}
	}
	
	public void setDelay(int i){ delay = i; }
	public void setFrame(int i){ currentFrame = i; }
	public void setNumFrames(int i){ numFrames = i; }
	
	/*
	 * NOTE:
	 * The following get methods return various protected 
	 * variables. Some are only for testing purposes and are
	 * no longer being used.
	 */
	
	public int getFrame(){ return currentFrame; }
	public int getCount(){ return count; }
	public BufferedImage getImage(){ return frames[currentFrame]; }
	public boolean hasPlayedOnce(){ return timesPlayed > 0; }
	public boolean hasPlayed(int i){ return timesPlayed == i; }
}