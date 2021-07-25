package Shanghai20.util;

import Shanghai20.util.saves.Savable;

import java.io.Serializable;

public class Coord implements Savable, Serializable {

	// CONSTANTES

	public static final long serialVersionUID = 1L;
	public static final String DESC_SEP = ", ";

	// ATTRIBUTS

	private int x;
	private int y;

	// CONSTRUCTEURS
	
	public Coord() {
		x = 0;
		y = 0;
	}
	
	public Coord(int x, int y) {
		Contract.checkCondition(x >= 0 && y >= 0);
		
		this.x = x;
		this.y = y;
	}

	// REQUETES
	
	/**
	 * Retourne la valeur de <code>x</code>.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Retourne la valeur de <code>y</code>.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Indique si la coordonée <code>c</code> est égale à la coordonnée 
	 * 	courante.
	 */
	public boolean isEqual(Coord c) {
		return c.getX() == this.x && c.getY() == this.y;
	}

	/**
	 * Affiche les coordonnées sous forme d'une chaine de caractères.
	 */
	public String displayAttribut() {
		return "(" + x + "," + y + ")";
	}

	public String describe() {
		return getX() + DESC_SEP + getY();
	}
	
	// COMMANDES
	
	public void setX(int x) {
		Contract.checkCondition(x >= 0);
		
		this.x = x;
	}
	
	public void setY(int y) {
		Contract.checkCondition(y >= 0);
		
		this.y = y;
	}
}
