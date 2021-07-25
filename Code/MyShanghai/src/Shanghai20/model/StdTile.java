package Shanghai20.model;

public class StdTile implements Tile {

	// CONSTANTES

	public static final long serialVersionUID = 1L;
	public static final String DESC_SEP = ", ";

	// ATTRIBUTS

	private int id;
	private int symbol;

	// CONSTRUCTEURS

	public StdTile(int id, int symbol) {
		this.id = id;
		this.symbol = symbol;
	}

	// REQUETES

	public int getId() {
		return id;
	}

	public int getSymbol() {
		return symbol;
	}

	public String displayAttribut() {
		return "Id : "+getId()+" Symbol : "+getSymbol();
	}

	public boolean equals(Object o) {
		if (!(o instanceof StdTile))
			return false;

		StdTile other = (StdTile) o;
		return other.canEquals(this)
				&& other.getId() == id
				&& other.getSymbol() == symbol;
	}

	public boolean canEquals(Object o) {
		return o instanceof StdTile;
	}

	public String describe() {
		return id + DESC_SEP + symbol;
	}

}
