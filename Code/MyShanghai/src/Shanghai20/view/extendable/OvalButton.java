package Shanghai20.view.extendable;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
 
import javax.swing.ButtonModel;
import javax.swing.JButton;

import Shanghai20.util.Config;


public class OvalButton extends JButton {
 
	// CONSTANTE
	
	private static final long serialVersionUID = 1L;
	
	/*
	 * Arrondi extérieur du bouton.
	 */
	private final static int OUTROUND = 15;
	
	/*
	 * Arrondi intérieur du bouton.
	 */
	private final static int INROUND = 13;
	
	// ATTRIBUTS
	
	private Color bgColor;
	private GradientPaint gradPaint;
	private Image img;
	
	// CONSTRUCTEUR
	
	public OvalButton() {
		super();
		setContentAreaFilled(false);
		setBorderPainted(false);
		setFocusable(false);
		bgColor = Config.COLORBUTTON;
		img = null;
	}

	public OvalButton(String text) {
		this();
		setText(text);
	}
	
	public OvalButton(Image img) {
		this();
		this.img = img;
	}
	
	public OvalButton(String text, ActionListener l) {
		this(text);
		this.addActionListener(l);
	}
	
	public OvalButton(Image img, ActionListener l) {
		this(img);
		this.addActionListener(l);
	}
	
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		if (img != null) {
			this.setSize(Math.min(getWidth(), getHeight()), Math.min(getWidth(),
					getHeight()));
		}
		int h = getHeight();
		int w = getWidth();
		ButtonModel model = getModel();
 
		if (!model.isEnabled()) {
			setForeground(Color.LIGHT_GRAY);
			gradPaint = new GradientPaint(0, 0, Color.GRAY, 0, h, 
					Color.GRAY, true);
		} else {
			setForeground(Color.BLACK);
			if (model.isRollover()) {
				gradPaint = new GradientPaint(0, 0, bgColor.brighter(), 0, h, 
						bgColor.brighter(), true);
			} else {
				gradPaint = new GradientPaint(0, 0, bgColor, 0, h, bgColor, 
						true);
			}
		}
		g2d.setPaint(gradPaint);
		GradientPaint p1;
		GradientPaint p2;
 
		if (model.isPressed()) {
			gradPaint = new GradientPaint(0, 0, bgColor.darker(), 0, h, 
					bgColor.darker(), true);
			g2d.setPaint(gradPaint);
			p1 = new GradientPaint(0, 0, Color.BLACK, 0, h - 1, 
					Color.LIGHT_GRAY);
			p2 = new GradientPaint(0, 1, new Color(0, 0, 0, 50), 0, h - 3,
					Color.GRAY);
		} else {
			p1 = new GradientPaint(0, 0, Color.GRAY, 0, h - 1, Color.BLACK);
			p2 = new GradientPaint(0, 1, new Color(255, 255, 255, 100), 0,
					h - 3, new Color(0, 0, 0, 50));
			gradPaint = new GradientPaint(0, 0, bgColor, 0, h, bgColor, true);
		}
 
		RoundRectangle2D.Float r2d = new RoundRectangle2D.Float(0, 0, w - 1,
				h - 1, OUTROUND, OUTROUND);
		Shape clip = g2d.getClip();
		g2d.clip(r2d);
		g2d.fillRect(0, 0, w, h);
		g2d.setClip(clip);
		g2d.setPaint(p1);
		g2d.drawRoundRect(0, 0, w - 1, h - 1, OUTROUND, OUTROUND);
		g2d.setPaint(p2);
		g2d.drawRoundRect(1, 1, w - 3, h - 3, INROUND, INROUND);
		if (img != null) {
			g2d.drawImage(img, 0, 0, Math.min(h, w) - 1, Math.min(h, w) - 1, 
					null);
		}
		g2d.dispose();
		super.paintComponent(g);
	}

	public void setBackgroundColor(Color color) {
		bgColor = color;
		repaint();
	}

	public Color getBackgroundColor() {
		return bgColor;
	}
}
