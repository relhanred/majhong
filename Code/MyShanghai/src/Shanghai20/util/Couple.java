package Shanghai20.util;

import java.io.Serializable;
import java.util.LinkedList;

import Shanghai20.model.Tile;

public class Couple implements Serializable {

	// CONSTANTES

	public static final long serialVersionUID = 1L;

	// ATTRIBUTS

	private Tile first;
	private Tile second;

	// CONSTRUCTEUR
	
	public Couple(Tile t1, Tile t2) {
		this.first = t1;
		this.second = t2;
	}

	// REQUETES

	public Tile getFirst() {
		return first;
	}
	
	public Tile getSecond() {
		return second;
	}

	public String displayCouple() {
		return first.displayAttribut()+" ; "+second.displayAttribut();
	}
	
	public boolean isDuplicate(LinkedList<Couple> list) {
		int cpt = 0;
		for (Couple c: list) {
			if (this.equals(c)) {
				cpt += 1;
			}
			if (cpt > 1) {
				return true;
			}
		}

		return false;
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof Couple))
			return false;

		Couple other = (Couple) o;
		return (other.canEquals(this))
				&& (other.getFirst().getId() == first.getId()
				&& other.getSecond().getId() == second.getId())
				|| (other.getFirst().getId() == second.getId()
					&& other.getSecond().getId() == first.getId());
				
	}
	
	public boolean canEquals(Object o) {
		return o instanceof Couple;
	}
	
}
