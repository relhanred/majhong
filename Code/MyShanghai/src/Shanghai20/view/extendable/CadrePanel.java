package Shanghai20.view.extendable;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import Shanghai20.util.Config;

public class CadrePanel extends JPanel {
	 
	// CONSTANTE
	
	private static final long serialVersionUID = 1L;
	
	// ATTRIBUTS
	
	private Color bgColor;
	
	// CONSTRUCTEUR
	
	public CadrePanel() {
		super();
		bgColor = Config.COLORBUTTON;
		setBackground(bgColor);
		Border b = BorderFactory.createLineBorder(bgColor.darker(), 3);
		setBorder(b);
	}
	
	public CadrePanel(LayoutManager layout) {
		this();
		setLayout(layout);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
	}

	public void setBackgroundColor(Color color) {
		bgColor = color;
		setBackground(bgColor);
		Border b = BorderFactory.createLineBorder(bgColor.darker(), 3);
		setBorder(b);
		repaint();
	}

	public Color getBackgroundColor() {
		return bgColor;
	}
}
