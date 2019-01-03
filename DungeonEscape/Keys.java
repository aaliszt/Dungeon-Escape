//Aaron Liszt
//Keys.java
//Manages key inputs from GamePanel

/*
 * NOTE:
 * Keys keeps track of key inputs from the user. It is called
 * by the keyListner in GamePanel and updates the key values 
 * corrispond to the keys that the user is currently pressing.
 */

import java.awt.event.KeyEvent;

public class Keys{
	
	public static final int NUM_KEYS = 5;
	
	public static boolean keyState[] = new boolean[NUM_KEYS];
	public static boolean lastKeyState[] = new boolean[NUM_KEYS];
	
	public static int UP = 0;
	public static int LEFT = 1;
	public static int DOWN = 2;
	public static int RIGHT = 3;
	public static int SPACE = 4;
	
	public static void keySet(int i, boolean k){
		if(i == KeyEvent.VK_W){ keyState[UP] = k; }
		else if(i == KeyEvent.VK_A){ keyState[LEFT] = k; }
		else if(i == KeyEvent.VK_S){ keyState[DOWN] = k; }
		else if(i == KeyEvent.VK_D){ keyState[RIGHT] = k; }
		else if(i == KeyEvent.VK_SPACE){ keyState[SPACE] = k; }
	}
	
	public static void update(){
		for(int i = 0; i < NUM_KEYS; i++)
			lastKeyState[i] = keyState[i];
	}
	
	public static boolean isPressed(int i){ return keyState[i] != lastKeyState[i] && keyState[i] != false; }
	public static boolean isDown(int i){ return keyState[i]; }
	
	public static boolean anyKeyDown(){
		for(int i = 0; i < NUM_KEYS; i++){
			if(keyState[i])
				return true;
		}
		return false;	
	}
	
	public static boolean anyKeyPress(){
		for(int i = 0; i < NUM_KEYS; i++){
			if(keyState[i] && !lastKeyState[i])
				return true;
		}
		return false;
	}
}