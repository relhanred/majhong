package Shanghai20.model;

import java.util.LinkedList;

import Shanghai20.util.Contract;
import Shanghai20.util.Coord;

public class StdStage implements Stage {

	// CONSTANTES

	public static final long serialVersionUID = 1L;
	public static final String DESC_INNER_SEP = ", ";
	public static final String DESC_SEP = "; ";

	// ATTRIBUTS

	public Tile[][] tiles;
	private int stage;
	private int maxX;
	private int maxY;
	private Coord[] coordOfTile;

	// CONSTRUCTEURS

	public StdStage(int stage, int maxX, int maxY) {
		this.stage = stage;
		this.maxX = maxX;
		this.maxY = maxY;
		tiles = new Tile[maxX][maxY];

		for (int i = 0; i < maxX; ++i) {
			for (int j = 0; j < maxY; ++j) {
				tiles[i][j] = null;
			}
		}
	}

	// REQUETES


	public boolean isIn(Tile t) {
		Contract.checkCondition(t != null);

		for (int i = 0; i < maxX; ++i) {
			for (int j = 0; j < maxY; ++j) {
				if (tiles[i][j] == t) {
					return true;
				}

			}
		}

		return false;
	}

	public int getStage() {
		return stage;
	}

	public LinkedList<Tile> getAllTiles() {
		LinkedList<Tile> tileList = new LinkedList<Tile>();

		for (int i = 0; i < maxX; ++i) {
			for (int j = 0; j < maxY; ++j) {
				if (tiles[i][j] != null) {
					tileList.add(tiles[i][j]);
				}
			}
		}
		return tileList;
	}

	public Coord getCoord(Tile t) {
		Contract.checkCondition(t != null);

		for (int i = 0; i < maxX; ++i) {
			for (int j = 0; j < maxY; ++j) {
				if (tiles[i][j] != null) {
					if (tiles[i][j].equals(t)) {
						return new Coord(i, j);
					}
				}
			}
		}

		return null;
	}
	
	
	
	public Coord[] getAllCoord(Tile t) {
		Contract.checkCondition(t != null);
		
		coordOfTile = new Coord[4];
		if (getCoord(t) != null) {
			Coord c = getCoord(t);
			coordOfTile[0] = c;
			coordOfTile[1] = new Coord(c.getX(), c.getY() + 1);
			coordOfTile[2] = new Coord(c.getX() + 1, c.getY());
			coordOfTile[3] = new Coord(c.getX() + 1, c.getY() + 1);
		}	
		return coordOfTile;
	}

	public Tile getTile(Coord c) {
		Contract.checkCondition(c != null);

		return tiles[c.getX()][c.getY()];
	}

	public boolean haveTileAt(Coord c) {
		Contract.checkCondition(c != null);

		return tiles[c.getX()][c.getY()] != null;
	}
	
    public int getNbTiles() {
        int nbTiles = 0;
        for (int i = 0; i < maxX; ++i) {
            for (int j = 0; j < maxY; ++j) {
                if (tiles[i][j] != null) {
                    nbTiles += 1;
                }

            }
        }
        return nbTiles;
    }

    public String describe() {
		StringBuilder res = new StringBuilder();
		for (Tile t : getAllTiles()) {
			Coord c = getCoord(t);
			String add = "(" + t.describe() + DESC_INNER_SEP + c.describe() + ")";
			res.append(add);
			res.append(DESC_SEP);
		}

		return res.toString();
	}

	// COMMANDES

	public void addTile(Tile t, Coord c) {
		Contract.checkCondition(t != null);
		Contract.checkCondition(c != null);

		tiles[c.getX()][c.getY()] = t;
	}

	public void removeTile(Tile t) {
		Contract.checkCondition(t != null);

		for (int i = 0; i < maxX; ++i) {
			for (int j = 0; j < maxY; ++j) {
				if (tiles[i][j] != null && tiles[i][j].equals(t)) {
					tiles[i][j] = null;
				}
			}
		}
	}
	
}
