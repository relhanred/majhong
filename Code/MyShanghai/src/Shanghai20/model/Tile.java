package Shanghai20.model;

import Shanghai20.util.saves.Savable;

import java.io.Serializable;

/**
 * Modélise une tuile.
 * @cons
 *     $ARGS$ 
 *     		int id, int symbol
 *     $PRE$ -
 *     $POST$
 *         getId == id
 *         getSymbol() == symbol
 */  
public interface Tile extends Savable, Serializable {

	// CONSTANTES

	long serialVersionUID = 1L;

	// REQUETES

	/**
	 * Renvoie l'Id unique de la tuile.
	 */
	int getId();

	/**
	 * Renvoie Le symbole de la tuile.
	 */
	int getSymbol();

	/**
	 * Renvoie une chaine de caractère qui affiche les informations relative à 
	 * 	la Tuile.
	 */	
	String displayAttribut();


	/**
	 * @param o
	 * Redefinition de la méthode equals pour une tuile
	 */
	boolean equals(Object o);

	boolean canEquals(Object o);

}
