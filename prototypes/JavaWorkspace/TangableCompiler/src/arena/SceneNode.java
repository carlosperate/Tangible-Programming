package arena;

import java.awt.Graphics;

public abstract class SceneNode {

	public enum Direction{
		NORTH,
		SOUTH,
		EAST,
		WEST
	}
	
	public int xPos;
	public int yPos;
	
	public Direction direction;
	
	public abstract void Draw(Graphics g); 
}
