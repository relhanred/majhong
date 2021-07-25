package Shanghai20.model;

import java.io.Serializable;
import java.util.LinkedList;

import Shanghai20.util.Coord;
import Shanghai20.util.saves.Savable;

/**
 * Modélise un étage.
 * @cons
 *     $ARGS$ 
 *     		int stage
 *     $PRE$
 *     		stage != 0
 *     $POST$
 *         getStage() == stage
 */  
public interface Stage extends Savable, Serializable {

	// CONSTANTES

	long serialVersionUID = 1L;

	// REQUETES
	
	/**
	 * Indique si la tuile <code>t</code> est dans l'étage.
	 * @pre
	 * 		t != null
	 */
	boolean isIn(Tile t);
	
	/**
	 * Retourne le niveau de l'étage.
	 */
	int getStage();

	/**
	 * Retourne sous forme de liste toutes les tuiles de l'etage
	 */
	LinkedList<Tile> getAllTiles();
	
	/**
	 * Retourne les coordonnées de la tuile <code>t</code>.
	 * Retourne null si la tuile n'est pas trouvée.
	 * @pre
	 * 		t != null
	 */
	Coord getCoord(Tile t);
	
	/**
	 * Retourne la tuile de coordonnées <code>c</code>.
	 * @pre
	 * 		c != null
	 */
	Tile getTile(Coord c);
	
	/**
	 * Indique si une tuile se trouve à la coordonée c.
	 * @pre
	 * 		c != null
	 */
	boolean haveTileAt(Coord c);
	
	/**
	 * Renvoie un tableau contenant les 4 coordonnées associé à la tuile 
	 * 	<code>tile<code>.
	 */
	Coord[] getAllCoord(Tile tile);
	
	/**
	 * Renvoie le nombre de tuiles présentent dans l'étage.
	 */
	int getNbTiles();
	
	// COMMANDES
	
	/**
	 * Ajoute la tuile <code>t</code> a l'étage, aux coordonnées c.
	 * 
	 * @pre
	 * 		t != null
	 *      c != null
	 */
	void addTile(Tile t, Coord c);
	
	/**
	 * Supprime la tuile <code>t</code> de l'étage.
	 * @pre
	 * 		t != null
	 */
	void removeTile(Tile t);
}
