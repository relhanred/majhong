package Shanghai20.model;

import Shanghai20.model.StdMove.Play;
import Shanghai20.util.Coord;

import java.io.Serializable;

public interface Move extends Serializable {

	// CONSTANTES

	long serialVersionUID = 1L;

	// REQUETES
	
	/**
	 * Retourne le type d'opération effectué : ADD ou SUP.
	 */
	Play getOp();
	
	/**
	 * Retourne la tuile 1.
	 */
	Tile getTile1();
	
	/**
	 * Retourne la tuile 2.
	 */
	Tile getTile2();
	
	/**
	 * Retourne l'étage de la tuile 1.
	 */
	int getStage1();
	
	/**
	 * Retourne l'étage de la tuile 2.
	 */
	int getStage2();
	
	/**
	 * Retourne les coordodés de la tuile 1.
	 */
	Coord getCoord1();
	
	/**
	 * Retourne les coordonnés de la tuile 2.
	 */
	Coord getCoord2();
	
	// COMMANDES
	
	/**
	 * Inverse l'action (ADD deviens SUP et SUP deviens ADD).
	 */
	void reverseOp();
	
}
