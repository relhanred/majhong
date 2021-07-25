package Shanghai20.model;

import java.io.Serializable;
import java.util.LinkedList;

import Shanghai20.util.Triplet;

public interface BoardManager extends Serializable {

	// CONSTANTES

	long serialVersionUID = 1L;

	// REQUETES

	/**
	 * Retourne sous forme d'une liste de Triplet tous les voisins à gauche et à
	 * 	droite du Triplet {triple} dans la liste {L}.
	 * @pre 
	 * 		L != null
	 */
	LinkedList<Triplet> getAllCoordAround(LinkedList<Triplet> L, int maxX, 
			int maxY, Triplet triple);


	/**
	 * Retourne sous forme d'une liste de Triplet tous les voisins en haut du 
	 * 	Triplet {triple} contenu dans la liste {L} si le boolean {b} est à true.
	 * @pre 
	 * 		L != null
	 */
	LinkedList<Triplet> getAllNeighborsAbove(LinkedList<Triplet> L, 
			Triplet triple, int maxX, int maxY, boolean b);

	/**
	 * Retourne sous forme d'une liste de Triplet tous les voisins en bas du 
	 * 	Triplet {triple} dans la liste {L}.
	 * @pre 
	 * 		L != null
	 * 		triple != null;
	 */
	LinkedList<Triplet> getAllNeighborsBelow(LinkedList<Triplet> L, 
			Triplet triple, int maxX, int maxY);

	/**
	 * Supprime toutes les tuiles de {L} situé sur la même ligne que c (sauf les
	 * 	voisins direct) et les ajoutes dans {L2}.
	 * @pre
	 * 		L != null
	 * 		L2 != null
	 * 		c != null
	 */
	void removeLigne(LinkedList<Triplet> L,LinkedList<Triplet> L2, Triplet c, 
			int maxX, int maxY);

}
