package arena;

import java.awt.Graphics;

public class Robot extends SceneNode{
	
	public String name;
	
	public Robot(int xPos, int yPos, Direction dir, String name){
		this.xPos = xPos;
		this.yPos = yPos;
		this.name = name;
		
		this.direction = dir;
	}
	
	@Override
	public void Draw(Graphics g) {
		g.drawRect(Arena.cellWidth * xPos, Arena.cellHeight * yPos, 100, 100);
		g.drawString(name, 
				(Arena.cellWidth * xPos) + (Arena.cellWidth/2), 
				(Arena.cellHeight * yPos) + (Arena.cellHeight/2));
	}

}
