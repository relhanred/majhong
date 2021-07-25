package Shanghai20.view.extendable;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;

import javax.swing.JToggleButton;

import Shanghai20.util.Config;

public class CadreCheckBox extends JToggleButton {
	
	// CONSTANTE
	
	private static final long serialVersionUID = 1L;
	
	/*
	 * Arrondi extérieur du bouton.
	 */
	private final static int OUTROUND = 15;
	
	/*
	 * Marge entre le bord et la croix.
	 */
	private final static int MARGEX = 5;
	
	/*
	 * Taille préférer du composant.
	 */
	private final static int PSIZE = 30;
	
	
	// ATTRIBUTS
	
	Color color;
	
	// CONSTRUCTEUR
	
	public CadreCheckBox() {
		super();
		color = Config.COLORBUTTON;
		setPreferredSize(new Dimension(PSIZE, PSIZE));
		setContentAreaFilled(false);
		setBorderPainted(false);
		setFocusable(false);
	}
	
	public CadreCheckBox(int side) {
		this();
		setPreferredSize(new Dimension(side, side));
	}
	
	// COMMANDES
	
	@Override
	protected void paintComponent(Graphics g) {
		final int w = getWidth() - 1;
		final int h = getHeight() - 1;
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		Stroke oldStroke = g2d.getStroke();
		g2d.setColor(color.darker());
		g2d.setStroke(new BasicStroke(2));
		if (model.isSelected()) {
			crossShape(g2d, MARGEX, MARGEX, w - MARGEX, h - MARGEX);
		}
		g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, OUTROUND, 
				OUTROUND);
		g2d.setStroke(oldStroke);
		g2d.dispose();
		
		super.paintComponent(g);
	}
	
	private void crossShape(Graphics2D g2d, int x, int y, int w, int h) {
		g2d.drawLine(x, y, w, h);
		g2d.drawLine(w, y, x, h);
	}
}
