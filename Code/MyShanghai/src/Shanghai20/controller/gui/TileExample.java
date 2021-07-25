package Shanghai20.controller.gui;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JComponent;

import Shanghai20.util.Contract;

public class TileExample extends JComponent {
	
	// CONSTANTE
	
	private static final long serialVersionUID = 1L;
	
	// ATTRIBUT
	
	private GraphicTile example1;
	private GraphicTile example2;
	private int width;
	private int height;
	private int xmin;
	private int ymin;
	
	// CONSTRUCTEUR
	
	public TileExample() {
		example1 = new GraphicTile(0, 0);
		example2 = new GraphicTile(3, 0);
	}
	
	// COMMANDE
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		setBoardSize();
		example1.setSizeTile(height, width);
		example1.setCoordTile(xmin, ymin);
		example1.paintComponent(g);
		
		example2.setSizeTile(height, width);
		example2.setCoordTile(xmin, ymin);
		example2.paintComponent(g);
	}
	
	public void setImage(Image img1, Image img2) {
		example1.setImage(img1);
		example2.setImage(img2);
	}
	
	// OUTIL
	
	/**
	 * Calcul la moitié de la différence entre <code>a</code> et <code>b</code>.
	 * @pre
	 * 		b >= a
	 * @post
	 * 		(b - a) / 2
	 */
	private int halfDiff(int a, int b) {
		Contract.checkCondition(b >= a);
		
		return (b - a) / 2;
	}
	
	/**
	 * Centre et redimensionne les tuiles du plateau.
	 */
	private void setBoardSize() {
		int n = 3;
		int m = 1;
		int w = getWidth() / (n * 2);
		int h = getHeight() / (m * 3);
		int x = Math.min(w, h);
		
		width = GraphicTile.MIN_WIDTH_TILE * x - GraphicTile.DEPTH_TILE;
		height = GraphicTile.MIN_HEIGHT_TILE * x - GraphicTile.DEPTH_TILE;
		xmin = halfDiff(width * n  + GraphicTile.DEPTH_TILE - (width / 2), 
				getWidth());
		ymin = halfDiff(height * m + GraphicTile.DEPTH_TILE, getHeight());
	}
}
