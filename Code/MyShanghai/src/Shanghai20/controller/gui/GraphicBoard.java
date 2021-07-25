package Shanghai20.controller.gui;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import Shanghai20.UserSave;
import Shanghai20.Shanghai;
import Shanghai20.controller.Board;
import Shanghai20.model.Stage;
import Shanghai20.model.Tile;
import Shanghai20.util.Contract;
import Shanghai20.util.Coord;
import Shanghai20.util.technical.MusicPlayer;
import Shanghai20.util.technical.SpriteSheetBuilder;

public class GraphicBoard extends JComponent {

	// CONSTANTES
	
	private static final long serialVersionUID = 1L;
	private static final URL SELECTEDSOUND = 
			Shanghai.class.getResource("sounds/SelectTile.wav");
	private static final URL VALIDATESOUND = 
			Shanghai.class.getResource("sounds/Validated.wav");
	
	// ATTRIBUTS
	
	private SpriteSheetBuilder ssb;
	
	// La largeur d'une tuile.
	private int WIDTH_TILE;
	
	// La longueur d'une tuile.
	private int HEIGHT_TILE;
	
	private int X_MIN;
	private int Y_MIN;
	
	private Board board;
	private List<GraphicTile> tiles;
	private GraphicTile fstSelected;
    private EventListenerList listenerList;
    private ChangeEvent changeEvent;
    private boolean hint;
    private boolean help;
    private MusicPlayer selectedSound;
    private MusicPlayer validateSound;
	
	// CONSTRUCTEUR
	
	public GraphicBoard() {
		ssb = new SpriteSheetBuilder();
		tiles = new ArrayList<GraphicTile>();
		listenerList = new EventListenerList();
		hint = false;
		help = false;
		selectedSound = new MusicPlayer(SELECTEDSOUND);
		validateSound = new MusicPlayer(VALIDATESOUND);
	}
	
	public GraphicBoard(Board board) {
		this();
		this.board = board;
		ssb.setPathImage(UserSave.pathTheme);
		for (int i = board.getNbStage() - 1; i >= 0; --i) {
			Stage s = board.getStageOfIndex(i);
			for (Tile t: s.getAllTiles()) {
				Coord c = s.getCoord(t);
				int x = c.getX() - 1;
				int y = c.getY() - 1;
				GraphicTile gt = new GraphicTile(t, x, y, i, 
						ssb.getSprite(t.getSymbol()));
				tiles.add(gt);
			}
		}
		createController();
	}

	// REQUETES
	
	public Board getBoard() {
		return board;
	}
	
	public boolean canPlayable() {
		return board.isGamePlayable();
	}
	
	public boolean hasWon() {
		return board.hasWon();
	}
	
	// COMMANDES
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Tile tilePlayable = null;
		Tile brotherPlayable = null;
		if (help) {
			tilePlayable = board.getTilePlayable();
			brotherPlayable = board.getBrother(tilePlayable);
		}
		setBoardSize();
		for (GraphicTile gt : tiles) {
			gt.setSizeTile(HEIGHT_TILE, WIDTH_TILE);
			gt.setCoordTile(X_MIN, Y_MIN);
			if (board.getLevelOf(gt.getModel()) != null) {
				if (hint) {
					if (!board.isPlayable(gt.getModel())) {
						gt.setDarkened();
					}
				} else if (help) {
					if (gt.getModel().equals(tilePlayable) 
							|| gt.getModel().equals(brotherPlayable)) {
						gt.setHelp();
					}
				}
				gt.paintComponent(g);
			}
		}
		help = false;
	}
	
	private void createController() {
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				hint = false;
				help = false;
				GraphicTile clickedTile = getTileAt(e.getPoint());
				if (clickedTile == null) {
					return;
				}
				Tile t = clickedTile.getModel();
				if (board.isPlayable(t)) {
					if (fstSelected != null) {
						Tile selected = fstSelected.getModel();
						if (t.equals(selected)) {
							clickedTile.toogleSelected();
							fstSelected = null;
							selectedSound.play();
						} else if (t.getSymbol() == selected.getSymbol()) {
							board.playTiles(t, selected);
							fstSelected.toogleSelected();
							fstSelected = null;
							validateSound.play();
						} else {
							fstSelected.toogleSelected();
							fstSelected = clickedTile;
							clickedTile.toogleSelected();
							selectedSound.play();
						}
					} else {
						fstSelected = clickedTile;
						clickedTile.toogleSelected();
						selectedSound.play();
					}
					repaint();
				}
				fireStateChanged();
			}
		});

		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				repaint();
			}
		});
	}
	
	public void addChangeListener(ChangeListener listener) {
		Contract.checkCondition(listener != null);
    	
    	listenerList.add(ChangeListener.class, listener);
	}
	
	public void removeChangeListener(ChangeListener listener) {
		Contract.checkCondition(listener != null, "argument invalide!");
    	
    	listenerList.remove(ChangeListener.class, listener);
	}
	
	public void resetSelected() {
		if (fstSelected != null) {
			fstSelected.toogleSelected();
			fstSelected = null;
		}
	}
	
	public void setHint() {
		hint = !hint;
		resetSelected();
		repaint();
	}
	
	public void setHelp() {
		help = true;
		hint = false;
		resetSelected();
		repaint();
	}
	
	// OUTIL
	
	/**
	 * Calcul la moitié de la différence entre <code>a</code> et <code>b</code>.
	 * @pre
	 * 		b >= a
	 * @post
	 * 		(b - a) / 2
	 */
	private int halfDiff(int a, int b) {
		Contract.checkCondition(b >= a);
		
		return (b - a) / 2;
	}
	
	/**
	 * Centre et redimensionne les tuiles du plateau.
	 */
	private void setBoardSize() {		
		int nbTuilesX = (board.getMaxX() + 1) / 2;
		int nbTuilesY = ((board.getMaxY() + 1) / 2);
		int minBoardX = nbTuilesX * GraphicTile.MIN_WIDTH_TILE;
		int minBoardY = nbTuilesY * GraphicTile.MIN_HEIGHT_TILE;
		int stageDepth = board.getNbStage() * GraphicTile.DEPTH_TILE;
		int w = (getWidth() - stageDepth) / minBoardX;
		int h = (getHeight() - stageDepth) / minBoardY;
		int coeff = Math.min(w, h);
		WIDTH_TILE = GraphicTile.MIN_WIDTH_TILE * coeff;
		HEIGHT_TILE = GraphicTile.MIN_HEIGHT_TILE * coeff;
		X_MIN = halfDiff(WIDTH_TILE * nbTuilesX + stageDepth, getWidth());
		Y_MIN = halfDiff(HEIGHT_TILE * nbTuilesY + stageDepth, getHeight());
	}

	/**
	 * Renvoie la tuile au point p.
	 */
	private GraphicTile getTileAt(Point p) {
		assert(p != null);

		for (int i = tiles.size() - 1; i >= 0; --i) {
			GraphicTile gt = tiles.get(i);
			if (board.getLevelOf(gt.getModel()) != null) {
				int x = gt.getRealX();
				int y = gt.getRealY();
				int xMax = x + WIDTH_TILE;
				int yMax = y + HEIGHT_TILE;
				int realX = (int) p.getX();
				int realY = (int) p.getY();
				if (x <= realX && realX <= xMax && y <= realY && realY <= yMax)
					return gt;
			}
		}
		return null;
	}
	
	// OUTIL
	
	protected void fireStateChanged() {
    	Object[] lst = listenerList.getListenerList();
    	for (int i = lst.length - 2; i >= 0; i -= 2) {
    		if (lst[i] == ChangeListener.class) {
    			if (changeEvent == null) {
    				changeEvent = new ChangeEvent(this);
    			}
    			((ChangeListener) lst[i + 1]).stateChanged(changeEvent);
    		}          
    	}
    }

}
