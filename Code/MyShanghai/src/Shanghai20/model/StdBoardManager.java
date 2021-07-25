package Shanghai20.model;

import java.util.Iterator;
import java.util.LinkedList;

import Shanghai20.util.Contract;
import Shanghai20.util.Triplet;

public class StdBoardManager implements BoardManager {

	public StdBoardManager() {

	}

	/**
	 * Retourne sous forme d'une liste de Triplet tous les voisins à gauche et à droite du Triplet {triple} dans la liste {L}
	 * @pre 
	 * 		L != null
	 */
	public LinkedList<Triplet> getAllCoordAround(LinkedList<Triplet> L, int maxX, int maxY, Triplet triple) {
		Contract.checkCondition(L != null);

		LinkedList<Triplet> coordAround = new LinkedList<Triplet>();

		int x = triple.getFirst();
		int y = triple.getSecond();
		int s = triple.getThird();

		if (x >= 2) {
			coordAround.add(new Triplet(x - 2, y, s));
			if (y < maxY - 2) {
				coordAround.add(new Triplet(x - 2, y + 1, s));
			}
		}

		if (y > 0 && x < maxX - 2  ) {
			coordAround.add(new Triplet(x + 2, y - 1, s));
		}

		if (y > 0 && x >= 2) {
			coordAround.add(new Triplet(x - 2, y - 1, s));
		}

		if (x < maxX - 2) {
			coordAround.add(new Triplet(x + 2, y , s));
			if (y < maxY - 2) {
				coordAround.add(new Triplet(x + 2, y + 1, s));
			}
		}

		Iterator<Triplet> iteCoord = coordAround.iterator();	
		while (iteCoord.hasNext()) {
			Triplet t = iteCoord.next();
			if(!t.contains(L)) {
				iteCoord.remove();
			}
		}

		return coordAround;
	}

	/**
	 * Retourne sous forme d'une liste de Triplet tous les voisins en haut du Triplet {triple} contenu dans la liste {L}
	 *  si le boolean {b} est à true
	 * 
	 * @pre 
	 * 		L != null
	 */
	public LinkedList<Triplet> getAllNeighborsAbove(LinkedList<Triplet> L, Triplet triple, int maxX, int maxY, boolean b) {
		Contract.checkCondition(L != null);
		Contract.checkCondition(triple != null);

		int s = triple.getThird();
		LinkedList<Triplet> coordAbove = new LinkedList<Triplet>();

		if (b == true) {
			s = s - 1;
		}else {
			s = s + 1;
		}

		int x = triple.getFirst();
		int y = triple.getSecond();

		coordAbove.add(new Triplet(x, y, s));
		if (x  < maxX - 2) {
			coordAbove.add(new Triplet(x + 1, y, s));
			if (y > 0) {
				coordAbove.add(new Triplet(x, y + 1, s));
			}
			if (y < maxY - 1) {
				coordAbove.add(new Triplet(x + 1, y + 1, s));
			}
		}
		if (x > 0 && y > 0) {
			coordAbove.add(new Triplet(x - 1, y - 1, s));
		}
		if (x > 0) {
			coordAbove.add(new Triplet(x - 1, y, s));
			if (y < maxY) {
				coordAbove.add(new Triplet(x - 1, y + 1, s));
			}
		}
		if (y > 0) {
			coordAbove.add(new Triplet(x, y - 1, s));
			if (x < maxX - 2) {
				coordAbove.add(new Triplet(x + 1, y - 1, s));
			}
		}

		Iterator<Triplet> iteCoord = coordAbove.iterator();	
		while (iteCoord.hasNext()) {
			Triplet tri = iteCoord.next();
			if(!tri.contains(L)) {
				iteCoord.remove();
			}
		}

		return coordAbove;
	}

	/**
	 * Retourne sous forme d'une liste de Triplet tous les voisins en bas du Triplet {triple} dans la liste {L}
	 * @pre 
	 * 		L != null
	 * 		triple != null;
	 */
	public LinkedList<Triplet> getAllNeighborsBelow(LinkedList<Triplet> L, Triplet triple, int maxX, int maxY) {
		Contract.checkCondition(L != null);
		Contract.checkCondition(triple != null);

		return getAllNeighborsAbove(L, triple, maxX, maxY, false);    
	}

	/**
	 * Supprime toutes les tuiles de {L} situé sur la même ligne que c (sauf les voisins direct) et les ajoutes dans {L2}
	 * @pre
	 * 		L != null
	 * 		L2 != null
	 * 		c != null
	 */
	public void removeLigne(LinkedList<Triplet> L,LinkedList<Triplet> L2, Triplet c, int maxX, int maxY) {
		Contract.checkCondition(c != null);
		Contract.checkCondition(L != null);
		Contract.checkCondition(L2 != null);

		LinkedList<Triplet> coordAround = new LinkedList<Triplet>();

		int x = c.getFirst();
		int y = c.getSecond();
		int z = c.getThird();
		int n = 0;
		boolean b = true;
		x = x - 2;
		Triplet triple = new Triplet(x,y,z);

		while(b) {
			Triplet c0 = new Triplet(x - 2, y - 1, z);
			Triplet c1 = new Triplet(x - 2, y, z);
			Triplet c2 = new Triplet(x - 2, y + 1, z);

			if (x >= 2 && c1.contains(L) && triple.contains(L)) {
				coordAround.add(c1);
				++n;
			}
			if (y < maxY - 2 && c2.contains(L) && triple.contains(L)) {
				coordAround.add(c2);
				++n;
			}
			if (y > 0 && x >= 2 && c0.contains(L) && triple.contains(L)) {
				coordAround.add(c0);
				++n;
			}
			if (n == 0) {
				b = false;
			}
			n = 0;
			x = x - 2;
		}

		x = c.getFirst();
		y = c.getSecond();
		n = 0;
		b = true;
		x = x + 2;
		triple = new Triplet(x,y,z);

		while(b) {
			Triplet c3 = new Triplet(x + 2, y - 1, z);
			Triplet c4 = new Triplet(x + 2, y, z);
			Triplet c5 = new Triplet(x + 2, y + 1, z);
			if (y > 0 && x < maxX - 2  && c3.contains(L) && triple.contains(L)) {
				coordAround.add(c3);
				++n;
			}
			if (x < maxX - 2 && c4.contains(L) && triple.contains(L)) {
				coordAround.add(c4);
				++n;
			}
			if ((y < maxY - 2) && c5.contains(L) && triple.contains(L)) {
				coordAround.add(c5);
				++n;
			}
			if (n == 0) {
				b = false;
			}
			n = 0;
			x = x + 2;
		}

		Iterator<Triplet> iterator = coordAround.iterator();
		while(iterator.hasNext()) {
			Triplet crd = iterator.next();
			if (crd.contains(L)) {
				L2.add(crd);
				L.remove(crd);
			}
		}
	}

}
