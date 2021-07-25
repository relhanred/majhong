package Shanghai20.model;

import java.util.LinkedList;

import Shanghai20.util.Contract;

public class StdHistory<E> implements History<E> {
	
	public enum UnReDo {
		UNDO,
		REDO;
	}
	
	// ATTRIBUTS
	
	private LinkedList<E> hist;
	private LinkedList<E> redohist;
	
	private int nbMove;
	private int nbUnReDo;
	
	// CONSTRUCTEUR
	
	public StdHistory() {
		hist = new LinkedList<E>();
		redohist = new LinkedList<E>();
		nbMove = 0;
		nbUnReDo = 0;
	}
	
	// REQUETES
	
	public LinkedList<E> getHistory() {
		return hist;
	}
	
	public E getLastMove() {
		return hist.getFirst();
	}
	
	public E getRedoMove() {
		return redohist.getFirst();
	}
	
	public int getNbMove() {
		return nbMove;
	}
	
	public int getNbUnReDo() {
		return nbUnReDo;
	}
	
	public boolean undoIsPossible() {
		return !hist.isEmpty();
	}
	
	public boolean redoIsPossible() {
		return !redohist.isEmpty();
	}
	
	// COMMANDES
	
	public void newMove(E move) {
		if (!redohist.isEmpty()) {
			redohist.clear();
		}
		hist.addFirst(move);
		nbMove += 1;
	}
	
	public void undoMove(E newmove) {
		Contract.checkCondition(!hist.isEmpty());
		
		hist.pop();
		redohist.addFirst(newmove);
		
		nbMove += 1;
		nbUnReDo += 1;
	}
	
	public void redoMove(E newmove) {
		Contract.checkCondition(!redohist.isEmpty());
		
		redohist.pop();
		hist.addFirst(newmove);

		nbMove += 1;
		nbUnReDo += 1;
	}
}
