package Shanghai20.model;

import Shanghai20.util.Coord;

public class StdMove implements Move {
	
	public enum Play {
		ADD, // ajout tuile
		SUP; // suppression tuile
	}

	// ATTRIBUS
	
	private Play op;
	
	private Tile t1;
	private int s1;
	private Coord c1;
	
	private Tile t2;
	private int s2;
	private Coord c2;
	
	// CONSTRUCTEUR
	
	public StdMove(Play op, Tile t1, Tile t2, int s1, int s2, Coord c1,
			Coord c2) {
		this.op = op;
		this.t1 = t1;
		this.t2 = t2;
		this.s1 = s1;
		this.s2 = s2;
		this.c1 = c1;
		this.c2 = c2; 

	}
	
	public StdMove(Move m, Play op) {
		this.op = op;
		this.t1 = m.getTile1();
		this.t2 = m.getTile2();
		this.s1 = m.getStage1();
		this.s2 = m.getStage2();
		this.c1 = m.getCoord1();
		this.c2 = m.getCoord2();
	}
	
	// REQUETES
	
	public Play getOp() {
		return op;
	}
	
	public Tile getTile1() {
		return t1;
	}
	
	public Tile getTile2() {
		return t2;
	}
	
	public int getStage1() {
		return s1;
	}
	
	public int getStage2() {
		return s2;
	}
	
	public Coord getCoord1() {
		return c1;
	}
	
	public Coord getCoord2() {
		return c2;
	}
	
	// COMMANDES
	
	public void reverseOp() {
		if (op == Play.ADD) {
			op = Play.SUP;
		} else {
			op = Play.ADD;
		}
	}
}
