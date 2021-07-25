package Shanghai20.controller;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import Shanghai20.model.Stage;
import Shanghai20.model.Tile;
import Shanghai20.util.Couple;
import Shanghai20.util.Triplet;
import Shanghai20.util.saves.Savable;

public interface Board extends Savable, Serializable {

	// CONSTANTE
	
	enum IA_Player {
		RANDOM,
		TOP,
		MOST_POSITION,
		MAX_ID
	}

	long serialVersionUID = 1L;

	// REQUETES

	/**
	 * Retourne le maxX
	 */
	int getMaxX();

	/**
	 * Retourne le maxY
	 */
	int getMaxY();

	/**
	 * Retourne le nombre d'etage
	 */
	int getNbStage();

	/**
	 * Indique si le plateau a été crée
	 */
	boolean isCreated();

	/**
	 * Renvoie la liste des étages.
	 */
	List<Stage> getStages();

	/**
	 * Renvoie l'étage de la tuile id. null si la tuile n'est pas trouvee.
	 * 	@pre:
	 * 		id != null
	 */
	Stage getLevelOf(Tile id);

	/**
	 * 
	 * Indique si la tuile est jouable (donc quand on peut la supprimer).
	 * @pre 
	 * 		id != null
	 */
	boolean isPlayable(Tile id);
	
	/**
	 * 
	 * Indique si la tuile a un voisin à droite.
	 * @pre 
	 * 		id != null
	 */
	boolean hasNeighborsRight(Tile tile);

	/**
	 * 
	 * Indique si la tuile a un voisin à gauche.
	 * @pre 
	 * 		id != null
	 */
	boolean hasNeighborsLeft(Tile t);
	
	/**
	 * 
	 * Indique si la tuile a un voisin au dessus.
	 * @pre 
	 * 		id != null
	 */
	boolean hasNeighborsAbove(Tile t);
	
	
	/**
	 * Retourne l'etage associé à l'index <code>index<code>.
	 * @pre:
	 * 		0 <= index < stages.size()
	 */
	Stage getStageOfIndex(int index);
	
	/*
	 * Indique si nous pouvons effectuer un undo.
	 */
	boolean canUndo();
	
	/*
	 * Indique si nous pouvons effectuer un redo.
	 */
	boolean canRedo();
	
	/**
	 * Indique si le joueur peut encore jouer.
	 */
	boolean isGamePlayable();

	/**
	 * Indique si le joueur à gagné.
	 */
	boolean hasWon();
	
	/**
	 * Renvoie le nombre de tuiles encore présent dans le jeu.
	 */
	int nbTiles();
	
	/**
	 * Renvoie une tuile qui a le même symbole que la tuile donnée en paramètre.
	 * @pre:
	 * 		t != null
	 */
	Tile getBrother(Tile t);

	// COMMANDES 
	
	/**
	 * Joue un undo.
	 */
	void playUndo();
	
	/**
	 * Joue un redo
	 */
	void playRedo();
	
	/**
	 * Joue les tuiles d'id id1 et id2.
	 * @pre:
	 * 		id1 != null && id2 != null
	 * 		isPlayable(id1) && isPlayable(id2)
	 * @post:
	 * 		les tuiles id1 et id2 sont retiré et du jeu 
	 * 		et ajouté dans l'historique.
	 */
	void playTiles(Tile id1, Tile id2);
	
	/**
	 * Ajoute la tuile id dans le jeu, à l'étage s, aux coordonées x, y.
	 * @pre:
	 * 		id != null && s >= 1 && x >= 0 && y >= 0
	 * @post:
	 * 		la tuile <code>id</code> est ajouté au jeu.
	 */
	void addTile(Tile id, int s, int x, int y);

	/**
	 * Retire la tuile id du jeu.
	 * @pre:
	 * 		id != null
	 * @post:
	 * 		retire la tuile du jeu.
	 */
	void removeTile(Tile id);

	/**
	 * Permets de crée un Board à partir de la liste de <code>L1<code>. 
	 * 	<code>nbSymbol</code> indique le nombre de symbole différents.
	 * @pre
	 * 		L1 != null
	 * 		NbSymbol != 0
	 * @post:
	 * 		le plateau, avec toutes les tuiles, est créé.
	 */
	void createBoard(LinkedList<Triplet> L1, int NbSymbol);

	/**
	 * Essaye de créer un Board à partir de la liste de 
	 * 	<code>allPosition</code>. <code>nbSymbol</code> indique le nombre de 
	 * 	symbole différents.
	 * 	Ne fait rien si le plateau ne peut pas etre crée.
	 * @pre
	 * 		L1 != null
	 * 		NbSymbol != 0
	 * @post:
	 * 		le plateau, avec toutes les tuiles, est créé.
	 */
	void boardTry(LinkedList<Triplet> allPosition, int NbSymbol);
	
	/**
	 * Chaine de caractère qui affiche un Board.
	 */
	String displayBoard();
	
	/**
	 * Renvoie la liste de tous les couples jouable à un instant t.
	 */
	LinkedList<Couple> allCouplePlayable();
	
	/**
	 * Renvoie un true si la partie est perdu c'est à dire qu'il n'y a plus de
	 * 	de couples jouables.
	 */
	boolean hasLost();
	
	/**
	 * Permet de jouer un nombre de partie avec une ia prise en paramètre et 
	 * 	renvoie le nombre de victoires.
	 */
	int playGameIA(LinkedList<Triplet> L1, int symb, int nbParties, 
			IA_Player ia);
	
	/**
	 * Joue <code>ia</code> sur ce plateau et renvoie true si cette IA a gagné,
	 * 	false sinon.
	 */
	boolean playIA(IA_Player ia);
	
	/**
	 * Renvoie une tuile parmis toutes les tuiles jouables
	 */	
	Tile getTilePlayable();
}
