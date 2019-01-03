//Aaron Liszt
//DungeonEscape.java
//This is the game window

import javax.swing.JFrame;

public class DungeonEscape{
	
	public static void main(String[] args)
	{	
		GamePanel panel = new GamePanel();
		
		JFrame window = new JFrame("Dungeon Escape");
		window.add(panel);
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		window.setResizable(false);
		window.pack();
		
		window.setVisible(true);
		window.setAlwaysOnTop(true);
		panel.run();
	}
}
