package Shanghai20.controller.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Arrays;

import javax.swing.JComponent;

import Shanghai20.model.Tile;
import Shanghai20.util.Config;
import Shanghai20.util.Contract;
import Shanghai20.util.Coord;

public class GraphicTile extends JComponent {

	// CONSTANTE
	
	private static final long serialVersionUID = 1L;
	
	// Largeur de la marge entre une tuile et son image.
	private static final int MARGIN_WIDTH = 5;
	
	// Hauteur de la marge entre une tuile et son image.
	private static final int MARGIN_HEIGHT = 10;
	
	// Largeur minimale d'une tuile.
	public static final int MIN_WIDTH_TILE = 2;
		
	// Longueur minimale d'une tuile.
	public static final int MIN_HEIGHT_TILE = 3;
	
	// Profondeur d'une tuile.
	public static final int DEPTH_TILE = 8;
	
	// ATTRIBUTS
	
	private Tile model;
	private Color colorTile;
	private boolean isSelected;
	private Coord abstCoord;
	private Coord realCoord;
	private int stage;
	private Image image;
	
	// La largeur d'une tuile.
	private int WIDTH_TILE;
	
	// La longueur d'une tuile.
	private int HEIGHT_TILE;
	
	private int X_MIN;
	private int Y_MIN;
	
	private boolean isDarkened;
	private boolean isHelp;
	
	// CONSTRUCTEUR
	
	public GraphicTile(int x, int y) {
		Contract.checkCondition(x >= 0 && y >= 0);
		model = null;
		colorTile = new Color(247, 239, 223);
		isSelected = false;
		abstCoord = new Coord(x, y);
		realCoord = new Coord();
		this.stage = 0;
		this.image = null;
		
		WIDTH_TILE = MIN_WIDTH_TILE;
		HEIGHT_TILE = MIN_HEIGHT_TILE;
		X_MIN = 0;
		Y_MIN = 0;
		isDarkened = false;
		isHelp = false;
	}
	
	public GraphicTile(Tile tile, int x, int y, int stage, Image image) {
		this(x, y);
		Contract.checkCondition(tile != null && stage >= 0 && image != null);
		
		model = tile;
		this.stage = stage;
		this.image = image;
	}
	
	// REQUETE
	
	public boolean isSelected() {
		return isSelected;
	}
	
	public Tile getModel() {
		return model;
	}

	public int getRealX() {
		return realCoord.getX();
	}

	public int getRealY() {
		return realCoord.getY();
	}

	// COMMANDE
	
	public void toogleSelected() {
		isSelected = !isSelected;
	}
	
	public void setDarkened() {
		isDarkened = true;
	}
	
	public void setHelp() {
		isHelp = true;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Color color = colorTile;
		if (isDarkened) {
			color = color.darker();
		}
		
		g.setColor(color.darker());
		setRealCoord();
    	int[][] xyTab = tabDrawPolygon(0, false);
    	g.fillPolygon(xyTab[0], xyTab[1], xyTab[0].length);
    	g.setColor(Color.GRAY);
    	g.drawPolygon(xyTab[0], xyTab[1], xyTab[0].length);
    	g.setColor(color);
    	g.fill3DRect(realCoord.getX(), realCoord.getY(), WIDTH_TILE, 
    			HEIGHT_TILE, true);
    	g.setColor(Color.GRAY);
    	g.drawRect(realCoord.getX(), realCoord.getY(), WIDTH_TILE, HEIGHT_TILE);
    	if (image != null) {
        	g.drawImage(image, realCoord.getX() + MARGIN_WIDTH, 
        			realCoord.getY() + MARGIN_WIDTH, WIDTH_TILE - MARGIN_HEIGHT, 
        			HEIGHT_TILE - MARGIN_HEIGHT, null);
    	}
    	
    	if (isHelp || isSelected) {
    		Color c = Config.COLORLABEL;
    		g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 90));
    		g.fillRect(realCoord.getX(), realCoord.getY(), WIDTH_TILE, 
    			HEIGHT_TILE);
    		g.fillPolygon(xyTab[0], xyTab[1], xyTab[0].length);
    	}
    	
		isDarkened = false;
		isHelp = false;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	
	public void setTileColor(Color color) {
		colorTile = color;
	}
	
	/*
	 * Défini la taille d'une tuile.
	 */
	public void setSizeTile(int h, int w) {
		WIDTH_TILE = w;
		HEIGHT_TILE = h;
	}
	
	/*
	 * Défini la marge entre le plateau de jeu et les tuiles.
	 */
	public void setCoordTile(int x, int y) {
		X_MIN = x;
		Y_MIN = y;
	}
	
	// OUTILS
	
	/**
	 * Calcul les coordonnées graphique de la tuile, par rapport aux 
	 * 	coordonnées abstraite de la tuile.
	 */
	private void setRealCoord() {
		realCoord.setX(X_MIN + abstCoord.getX() * WIDTH_TILE / 2 
				+ stage * DEPTH_TILE);
		realCoord.setY(Y_MIN + abstCoord.getY() * HEIGHT_TILE / 2 
				+ stage * DEPTH_TILE);
	}
	
	/**
	 * Donne un tableau contenant un tableau de coordonnées x et un tableau de
	 * 	coordonnées y dont chaque x et y est multiplié par <code>coeff</code>, 
	 * 	ce qui permet de dessiner le côté (et l'ombre) d'une tuile.
	 */
	private int[][] tabDrawPolygon(int coeff, boolean r) {
    	int[] xTab = {
        	realCoord.getX() + WIDTH_TILE + coeff * DEPTH_TILE, 
        	realCoord.getX() + WIDTH_TILE + (coeff + 1) * DEPTH_TILE, 
        	realCoord.getX() + WIDTH_TILE + (coeff + 1) * DEPTH_TILE, 
        	realCoord.getX() + (coeff + 1) * DEPTH_TILE, 
        	realCoord.getX() + coeff * DEPTH_TILE, 
        	realCoord.getX() + WIDTH_TILE + coeff * DEPTH_TILE
        };
        int[] yTab = {
        	realCoord.getY() + coeff * DEPTH_TILE, 
        	realCoord.getY() + (coeff + 1) * DEPTH_TILE, 
        	realCoord.getY() + HEIGHT_TILE + (coeff + 1) * DEPTH_TILE,
        	realCoord.getY() + HEIGHT_TILE + (coeff + 1) * DEPTH_TILE, 
        	realCoord.getY() + HEIGHT_TILE + coeff * DEPTH_TILE, 
        	realCoord.getY() + HEIGHT_TILE + coeff * DEPTH_TILE
        };
        int begin = r? 2: 0;
        int [][] xyTab = {
        	Arrays.copyOfRange(xTab, begin, xTab.length), 
        	Arrays.copyOfRange(yTab, begin, yTab.length)
        };
        return xyTab;	
	}
	
}
