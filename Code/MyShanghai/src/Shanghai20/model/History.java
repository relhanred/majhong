package Shanghai20.model;

import java.io.Serializable;
import java.util.LinkedList;

public interface History<E> extends Serializable {

	// CONSTANTES

	long serialVersionUID = 1L;
	
	// REQUETES
	
	/*
	 * Retourne l'historique.
	 */
	LinkedList<E> getHistory();
	
	/*
	 * Retourne le dernier move joué.
	 */
	E getLastMove();
	
	/*
	 * Retourne le dernier move pouvant etre redo.
	 */
	E getRedoMove();
	
	/*
	 * Retourne le nombre de move effectué : play / undo / redo.
	 */
	int getNbMove();
	
	/*
	 * Retourne le nombre de undo / redo effectué.
	 */
	int getNbUnReDo();
	
	/*
	 * Indique si nous pouvons effectuer un undo.
	 */
	boolean undoIsPossible();
	
	/*
	 * Indique si nous pouvons effectuer un redo.
	 */
	boolean redoIsPossible();
	
	// COMMANDES
	
	/*
	 * Ajoute un nouveau move a l'historique.
	 */
	void newMove(E move);
	
	/*
	 * Effectue un undo sur l'historique.
	 */
	void undoMove(E newmove);
	
	/*
	 * Effectue un redo sur l'historique.
	 */
	void redoMove(E newmove);
}
