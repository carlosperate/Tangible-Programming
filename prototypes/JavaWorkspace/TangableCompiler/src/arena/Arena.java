package arena;

import java.awt.Color;
import java.awt.Graphics;
import java.util.*;

import javax.swing.JFrame;

import arena.SceneNode.Direction;

public class Arena extends JFrame{
	
	public static int numOfRows;

	public static int numOfColumns;
	
	public static int cellWidth;
	
	public static int cellHeight;
	
	public static boolean drawGridLines;
	
	public static int xBorderOffset;
	
	public static int yBorderOffset;
	
	public List<SceneNode> nodes = new ArrayList<SceneNode>();
	
	public Arena(int rows, int columns, int width, int height, boolean drawGrid){
		super("Tangble Programming Arena");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(width,height);
		
		numOfRows = rows;
		numOfColumns = columns;
		
		xBorderOffset = getSize().width - getContentPane().getSize().width;
		yBorderOffset = getSize().height - getContentPane().getSize().height;
		
		cellWidth = width / numOfColumns;
		cellHeight = height / numOfRows;
		
		drawGridLines = drawGrid;

		
		setVisible(true);
	}

	public void addNode(SceneNode sc){
		nodes.add(sc);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		if(drawGridLines){
			Color color = g.getColor();
			g.setColor(Color.LIGHT_GRAY);
			for(int c = 0; c < numOfColumns; c++){
				g.drawLine( xBorderOffset + (cellWidth * c),
						0,
						yBorderOffset + (cellWidth * c),
						getSize().height);
			}
			
			for(int r = 0; r < numOfRows; r++){
				g.drawLine(0, cellHeight * r, getSize().width, cellHeight * r);
			}
			g.setColor(color);
		}
		
		for(SceneNode sn : nodes){
			sn.Draw(g);
		}
	}
	
	
}
