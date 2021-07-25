package Shanghai20.controller;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import Shanghai20.model.BoardManager;
import Shanghai20.model.History;
import Shanghai20.model.Move;
import Shanghai20.model.Stage;
import Shanghai20.model.StdBoardManager;
import Shanghai20.model.StdHistory;
import Shanghai20.model.StdMove;
import Shanghai20.model.StdStage;
import Shanghai20.model.StdTile;
import Shanghai20.model.Tile;
import Shanghai20.model.StdHistory.UnReDo;
import Shanghai20.model.StdMove.Play;
import Shanghai20.util.Contract;
import Shanghai20.util.Coord;
import Shanghai20.util.Triplet;
import Shanghai20.util.Couple;


public class StdBoard implements Board {

	// CONSTANTES

	public final static long serialVersionUID = 1L;

	// ATTRIBUTS

	private static LinkedList<Stage> stages;
	private int nbStage;
	private boolean isCreated;
	private int maxX;
	private int maxY;
	private History<Move> hist;

	// CONSTRUCTEUR

	public StdBoard(int nb, int maxX, int maxY) {
		Contract.checkCondition(nb >= 0 && maxX >= 0 && maxY >= 0);

		this.maxX = maxX;
		this.maxY = maxY;
		isCreated = false;
		nbStage = nb;
		stages = new LinkedList<Stage>();
		for (int i = 1; i <= nbStage; ++i) {
			stages.add(new StdStage(i, maxX, maxY));
		}	
		hist = new StdHistory<Move>();
	}

	// REQUETES

	public int getMaxX() {
		return maxX;
	}

	public int getMaxY() {
		return maxY;
	}

	public int getNbStage() {
		return nbStage;
	}

	public boolean isCreated() {
		return isCreated;
	}

	public final List<Stage> getStages() {
		final List<Stage> rst = stages;
		return rst;
	}

	public Stage getStageOfIndex(int index) {
		Contract.checkCondition(index >= 0 && index < stages.size());

		return stages.get(index);
	}

	public Stage getLevelOf(Tile id) {
		Contract.checkCondition(id != null);

		for (Stage s : stages) {
			if (s.isIn(id)) {
				return s;
			}
		}
		return null;
	}
	
	public boolean isPlayable(Tile id) {
		Contract.checkCondition(id != null);

		return (!hasNeighborsAbove(id) 
				&& (!hasNeighborsLeft(id) || !hasNeighborsRight(id)));
	}


	public boolean hasNeighborsRight(Tile t) {
		Contract.checkCondition(t != null);

		Stage s = getLevelOf(t);
		Coord c = s.getCoord(t);
		Coord[] coordTab = {
				new Coord(c.getX()+ 2, c.getY()),
				new Coord(c.getX() + 2, c.getY() + 1)
		};
		LinkedList<Tile> allTile = s.getAllTiles();
		for(Tile tl: allTile) {
			Coord tlCoord = s.getCoord(tl);
			if (tlCoord.getX() > c.getX()) {
				List<Coord> CoordOfTl = Arrays.asList(s.getAllCoord(tl));
				for(Coord cd: CoordOfTl) {
					if (cd.isEqual(coordTab[0]) || cd.isEqual(coordTab[1])) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean hasNeighborsLeft(Tile t) {
		Contract.checkCondition(t != null);

		Stage s = getLevelOf(t);
		Coord c = s.getCoord(t);
		if (c.getX() < 2) {
			return false;
		}
		Coord[] coordTab = {
				new Coord(c.getX() - 1, c.getY()), 
				new Coord(c.getX() - 1, c.getY() + 1)
		};
		LinkedList<Tile> allTile = s.getAllTiles();
		for(Tile tl: allTile) {
			Coord tlCoord = s.getCoord(tl);
			if (tlCoord.getX() < c.getX()) {
				List<Coord> CoordOfTl = Arrays.asList(s.getAllCoord(tl));
				for(Coord cd: CoordOfTl) {
					if (cd.isEqual(coordTab[0]) || cd.isEqual(coordTab[1])) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean hasNeighborsAbove(Tile t) {
		Contract.checkCondition(t != null);

		int stage = getLevelOf(t).getStage();
		if (stage == 1) {
			return false;
		}
		Stage st = getStageOfIndex(stage - 2);
		List<Coord> coordTab = Arrays.asList(getLevelOf(t).getAllCoord(t));
		LinkedList<Tile> allTile = st.getAllTiles();
		for (Tile til: allTile) {
			List<Coord> CoordOfTl = Arrays.asList(st.getAllCoord(til));
			for (Coord a: CoordOfTl) {
				for (Coord b: coordTab) {
					if(a.isEqual(b)) {
						return true;
					}
				}

			}
		}
		return false;
	}

	public boolean canUndo() {
		return hist.undoIsPossible();
	}

	public boolean canRedo() {
		return hist.redoIsPossible();
	}

	public boolean isGamePlayable() {
		return !allTilesPlayabled().isEmpty() && nbTiles() != 0;
	}

	public boolean hasWon() {
		return nbTiles() == 0;
	}
	
	public boolean hasLost() {
		return (allCouplePlayable().size() == 0 && nbTiles() != 0);
	}

	public int nbTiles() {
		int nbTiles = 0;
		for(Stage s: stages) {
			nbTiles += s.getNbTiles();
		}
		return nbTiles;
	}

	public Tile getBrother(Tile t) {
		Contract.checkCondition(t != null);

		LinkedList<Tile> list = allTilesPlayabled();
		for (Tile tile : list) {
			if (tile.getSymbol() == t.getSymbol() 
					&& tile.getId() != t.getId()) {
				return tile;
			}
		}
		return null;
	}
	
	public String displayBoard() {
		String res = "";
		for (Stage s: this.getStages()) {
			for (Tile tile: s.getAllTiles()) {
				Coord c = s.getCoord(tile);
				res += tile.displayAttribut() + " coord" + c.displayAttribut() 
						+ " Stage: " + s.getStage() + "\n";
			}
		}
		return res;
	}

	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();

		res.append("nbStages" + " = " + nbStage + "\n");
		res.append("maxX" + " = " + maxX + "\n");
		res.append("maxY" + " = " + maxY + "\n");
		for (Stage s : getStages()) {
			final String desc = s.describe();
			final int stageNb = s.getStage();
			res.append("s" + stageNb + " = " + desc + "\n");
		}

		return res.toString();
	}

	@Override
	public String describe() {
		return toString();
	}

	// COMMANDES

	public void playUndo() {
		playUnReDo(UnReDo.UNDO);
	}

	public void playRedo() {
		playUnReDo(UnReDo.REDO);
	}
	
	public void playTiles(Tile id1, Tile id2) {
		Contract.checkCondition(id1 != null && isPlayable(id1));
		Contract.checkCondition(id2 != null && isPlayable(id2));

		Stage s1 = getLevelOf(id1);
		Stage s2 = getLevelOf(id2);
		hist.newMove(new StdMove(
				Play.SUP,
				id1,
				id2,
				s1.getStage(),
				s2.getStage(),
				s1.getCoord(id1),
				s2.getCoord(id2)));

		removeTile(id1);
		removeTile(id2);
	}

	public void addTile(Tile id, int s, int x, int y) {
		Contract.checkCondition(id != null);
		Contract.checkCondition(s >= 1);
		Contract.checkCondition(x >= 0 && y >= 0);

		Stage liveStage = stages.get(s - 1);
		liveStage.addTile(id, new Coord(x, y));
	}

	public void removeTile(Tile id) {
		Contract.checkCondition(isPlayable(id));

		Stage liveStage = getLevelOf(id);
		liveStage.removeTile(id);
	}

	@SuppressWarnings("unchecked")
	public void createBoard(LinkedList<Triplet> allPosition, int NbSymbol) {
		Contract.checkCondition(allPosition != null);
		Contract.checkCondition(NbSymbol > 0);

		LinkedList<Triplet> unmodifiedList = 
				(LinkedList<Triplet>) allPosition.clone();
		while (!isCreated()) {
			LinkedList<Triplet> position = 
					(LinkedList<Triplet>) unmodifiedList.clone();
			boardTry(position, NbSymbol);
		}
	}

	public void boardTry(LinkedList<Triplet> allPosition, int NbSymbol) {
		Contract.checkCondition(allPosition != null);
		Contract.checkCondition(NbSymbol > 0);

		LinkedList<Triplet> L3 = new LinkedList<Triplet>();
		BoardManager board = new StdBoardManager();
		Iterator<Triplet> iterator = allPosition.iterator();
		int symbol[] = new int[NbSymbol];
		int id = 1;

		while (iterator.hasNext()) {
			Triplet t = iterator.next();
			if(t.getThird() == getNbStage()) {
				L3.add(t);
				iterator.remove();       
			}
		}

		while(L3.size() != 0 || allPosition.size() != 0) {

			if (L3.size() <= 1) {
				L3.clear();
				return;
			}

			Random rand = new Random();
			Triplet tab[] = new Triplet[2];
			int cpt = 0;
			int symb = (int) (Math.random() * symbol.length) + 1;
			while(cpt < 2 ) {
				Triplet P1 = L3.get(rand.nextInt(L3.size()));
				Tile T1 = new StdTile(id,symb);
				id += 1;
				Stage lvl = stages.get(P1.getThird() - 1);
				lvl.addTile(T1, new Coord(P1.getFirst(), P1.getSecond()));
				L3.remove(P1);
				tab[cpt] = P1;
				cpt++;
			}

			for (int i = 0; i < tab.length; i++) {

				LinkedList<Triplet> neighbours = board.getAllCoordAround(
						allPosition, maxX, maxY, tab[i]);
				iterator = neighbours.iterator();
				board.removeLigne(L3, allPosition, tab[i],maxX, maxY);

				if (neighbours.size() != 0) {
					while(iterator.hasNext()) {
						Triplet nei = iterator.next();
						LinkedList<Triplet> sonL1 = board.getAllNeighborsBelow(
								allPosition, nei, maxX, maxY);
						LinkedList<Triplet> sonL3 = board.getAllNeighborsBelow(
								L3, nei, maxX, maxY);

						if (sonL1.size() == 0 && sonL3.size() == 0) {
							L3.add(nei);
							allPosition.remove(nei);
						}
					}
				}

				if (tab[i].getThird() > 1) {
					LinkedList<Triplet> parents = board.getAllNeighborsAbove(
							allPosition, tab[i], maxX, maxY, true);
					iterator = parents.iterator();
					while(iterator.hasNext()) {
						Triplet par = iterator.next();
						LinkedList<Triplet> sonL1 = board.getAllNeighborsBelow(
								allPosition, par, maxX, maxY);
						LinkedList<Triplet> sonL3 = board.getAllNeighborsBelow(
								L3, par, maxX, maxY);
						if (sonL1.size() == 0 && sonL3.size() == 0) {
							L3.add(par);
							allPosition.remove(par);
						}
					}
				}
			} 
		}                  
		if  (allPosition.size() != 0 && L3.size() == 0) {
			return;
		}
		isCreated = true;
	}
	
	public LinkedList<Couple> allCouplePlayable() {
		LinkedList<Tile> tileList = allTilesPlayabled();
		LinkedList<Couple> listCouple = new LinkedList<Couple>();
		for (Tile t : tileList) {
			Tile tile1 = t;
			for (Tile ti: tileList) {
				if (tile1.getSymbol() == ti.getSymbol() 
						&& tile1.getId() != ti.getId()) {
					Tile tile2 = ti;
							Couple cpl = new Couple(tile1, tile2);
					listCouple.add(cpl);
				}
			}

		}
		Iterator<Couple> iterator = listCouple.iterator();    
		while (iterator.hasNext()) {
			Couple c = iterator.next();
			if (c.isDuplicate(listCouple)) {
				iterator.remove();
			}
		}
		return listCouple;
	}
		

	
	public int playGameIA(LinkedList<Triplet> L1, int symb, int nbParties, 
			IA_Player ia) {
		int cpt = 0;
		int s = this.getNbStage();
		int maxX = this.getMaxX();
		int maxY = this.getMaxY();
		Board b;
		while (nbParties != 0) {
			b = new StdBoard(s,maxX, maxY);
			b.createBoard(L1, symb);
			if(b.playIA(ia)) {
				cpt++;
			}
			nbParties--;
		}
		return cpt;
	}

	public boolean playIA(IA_Player ia) {
		boolean b = false;
		
		switch(ia) {
			case RANDOM:
				b = randomIA();
			case TOP:
				b = topIA();
			case MOST_POSITION: 
				b = mostPositionsIA();	
			case MAX_ID:
				b = maxId();
		}
		return b;
	}
	
	public Tile getTilePlayable() {
		LinkedList<Couple> listCouple = allCouplePlayable();
		Random rand = new Random();
		Couple couple = listCouple.get(rand.nextInt(listCouple.size()));
		return couple.getFirst();
	}
	
	// OUTILS
	
	/*
	 * Une IA qui joue une partie en choisissant un couple aléatoirement à 
	 * 	chaque tour et qui renvoie true si la partie est gagné ou false si elle 
	 * 	est perdue.
	 */
	private boolean randomIA() {
		while (allCouplePlayable().size() != 0) {
			LinkedList<Couple> listCouple = allCouplePlayable();
			if (listCouple.size() == 0) {
				return false;
			}
			Random rand = new Random();
			Couple c = listCouple.get(rand.nextInt(listCouple.size()));
			playTurn(c);
		}
		if (this.hasWon()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Une IA qui joue une partie en choisissant le couple le plus haut à chaque
	 * 	tour et qui renvoie true si la partie est gagné ou false si elle est 
	 * 	perdue.
	 */
	private boolean topIA() {
		while (allCouplePlayable().size() != 0) {
			Couple c = getCoupleTop();
			playTurn(c);
		}
		if (this.hasWon()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Une IA qui joue une partie en choisissant le couple qui libère le plus de
	 * 	positions à chaque tour et qui renvoie true si la partie est gagné ou 
	 * 	false si elle est perdue.
	 */
	private boolean mostPositionsIA() {
		while (allCouplePlayable().size() != 0) {
			Couple c = getMostPositions();
			playTurn(c);
		}
		if (this.hasWon()) {
			return true;
		}
		return false;
	}
	
	private boolean maxId() {
		while (allCouplePlayable().size() != 0) {
			Couple c = getCoupleWithMaxId();
			playTurn(c);
		}
		if (this.hasWon()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Joue un undo ou redo en fonction du Parametre (UNDO ou REDO).
	 * @pre:
	 * 		unRe != null
	 * 		(unRe == UnReDo.UNDO && canUndo()) 
	 * 			|| (unRe == UnReDo.REDO && canRedo())
	 */
	private void playUnReDo(UnReDo unRe) {
		Contract.checkCondition(unRe != null);
		Contract.checkCondition((unRe == UnReDo.UNDO && canUndo()) 
				|| (unRe == UnReDo.REDO && canRedo()));

		Move m;
		Play play;
		if (unRe == UnReDo.UNDO) {
			m = hist.getLastMove();
			play = m.getOp();
			m.reverseOp();
			hist.undoMove(m);
		} else {
			m = hist.getRedoMove();
			play = m.getOp();
			m.reverseOp();
			hist.redoMove(m);
		}

		if (play == Play.SUP) {
			addTile(m.getTile1(), m.getStage1(), m.getCoord1().getX(),
					m.getCoord1().getY());
			addTile(m.getTile2(), m.getStage2(), m.getCoord2().getX(),
					m.getCoord2().getY());
		} else if (play == Play.ADD) {
			removeTile(m.getTile1());
			removeTile(m.getTile2());
		}

	}
	
	private void playTurn(Couple c) {
		if (c != null) {
			removeTile(c.getFirst());
			removeTile(c.getSecond());
		}
	}
	
	private Coord getCoordFromTile(Tile t) {
		Contract.checkCondition(t != null);
		Coord c = null;
		for (Stage s: stages) {
			for (Tile tile: s.getAllTiles()) {
		    	if (t.getId() == tile.getId()) {
		    		c = s.getCoord(tile);
		    	}
			}
		}
		return c;
	}

	private boolean canBeChosen(Tile t, LinkedList<Tile> list) {
		Contract.checkCondition(list != null);
		Contract.checkCondition(t != null);

		for (Tile tile: list) {
			if (tile.getSymbol() == t.getSymbol() 
					&& tile.getId() != t.getId()) {
				return true;
			}
		}
		return false;
	}
	
	private Couple getMostPositions() {
		LinkedList<Couple> listCouple = allCouplePlayable();
		Couple couple = null;
		if (listCouple.size() == 0) {
			return couple;
		}
		int size = 0;
		for (Couple c: listCouple) {
			Tile t1 = c.getFirst();
			int s1 = getStageFromTile(t1);
			int x1 = getCoordFromTile(t1).getX();
			int y1 = getCoordFromTile(t1).getY();
			Tile t2 = c.getSecond();
			int s2 = getStageFromTile(t2);
			int x2 = getCoordFromTile(t2).getX();
			int y2 = getCoordFromTile(t2).getY();
			this.removeTile(c.getFirst());
			this.removeTile(c.getSecond());
			if (allCouplePlayable().size() >= size) {
				size = allCouplePlayable().size();
				couple = c;
			}
			
			addTile(t1, s1, x1, y1);
			addTile(t2, s2, x2, y2);
		}
		return couple;
	}
	
	private int getStageFromTile(Tile t) {
		Contract.checkCondition(t != null);
		for(Stage s : stages) {
			for (Tile tile: s.getAllTiles()) {
				if (tile.equals(t)) {
					return s.getStage();
				}
			}
		}
		return -1;
	}
	
	private Couple getCoupleWithMaxId() {
		LinkedList<Couple> listCouple = allCouplePlayable();
		int id = 0;
		Couple couple = null;
		for (Couple c: listCouple) {
			int i = 0;
			i += c.getFirst().getId();
			i += c.getSecond().getId();
			if (i > id) {
				id = i;
				couple = c;
			}
		}
		return couple;
	}
		
	private LinkedList<Tile> allTilesPlayabled() {
		LinkedList<Tile> tilePlayable = new LinkedList<Tile>();
		for (Stage s: stages) {
			for (Tile t: s.getAllTiles()) {
				if (isPlayable(t)) {
					tilePlayable.add(t);
				}
			}
		}
		
		Iterator<Tile> iterator = tilePlayable.iterator();    
		while (iterator.hasNext()) {
			Tile t = iterator.next();
			if (!canBeChosen(t,tilePlayable)) {
				iterator.remove();
			}
		}
		return tilePlayable;
	}
	
	private Couple getCoupleTop() {
		LinkedList<Couple> listCouple = allCouplePlayable();
		int max = 1000;
		Couple couple = null;
		if (listCouple.size() == 0) {
			return couple;
		}
		for (Couple c: listCouple) {
			int i = getStageFromTile(c.getFirst());
			int y = getStageFromTile(c.getSecond());
			if (i != -1 && y != -1 && (i + y) < max) {
				max = (i + y);
				couple = c;
			}
		}
		return couple;
	}

}