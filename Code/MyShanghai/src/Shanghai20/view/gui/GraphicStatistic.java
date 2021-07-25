package Shanghai20.view.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;

import Shanghai20.UserSave;
import Shanghai20.util.Contract;
import Shanghai20.util.Coord;

public class GraphicStatistic extends JComponent {
	
	// CONSTANTES
	
	private static final long serialVersionUID = 1L;

	// marge horizontale interne de part et d'autre du composant
	private static final int MARGIN = 20;
	
	private static final int CHAR_LENGTH = 10;
	
	private static final Color COLOR = Color.GRAY;
	
	private static final Color[] ARRAYCOLOR = {Color.BLUE, Color.RED, 
			Color.ORANGE, Color.GREEN, Color.PINK, Color.MAGENTA};
	
	// ATTRIBUT
	
	private int maxX;
	private int maxY;
	private Map<String, float[][]> statMap;
	
	// CONSTRUCTEUR
	
	public GraphicStatistic() {
		maxX = 30;
		maxY = 5;
		statMap = new HashMap<String, float[][]>();
		setOpaque(false);
	}
	
	// REQUETE
	
	public int getMaxX() {
		return maxX;
	}
	
	public int getMaxY() {
		return maxY;
	}
	
	public String[] getLabels() {
		Set<String> keys = statMap.keySet();
		return keys.toArray(new String[keys.size()]);
	}
	
	public boolean contain(String name) {
		return statMap.containsKey(name);
	}
	
	// COMMANDES
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		graduation(g);
		int i = 0;
		int lastStringLength = 0;
		for (String s : statMap.keySet()) {
			Color c = ARRAYCOLOR[i];
			g.setColor(c);
			g.drawString(s, lastStringLength + 2 * MARGIN, getHeight() - 5);
			for (int k = 0; k < statMap.get(s).length - 1; ++k) {
				drawLine(g, k, statMap.get(s)[k][0], k + 1, 
						statMap.get(s)[k + 1][0], 
						(statMap.get(s)[k + 1][1] == 1), c);
			}
			lastStringLength += (s.length() + 2) * CHAR_LENGTH;
			i += 1;
		}
	}
	
	public void setMaxX(int max) {
		Contract.checkCondition(max > 0);
		
		this.maxX = max;
		repaint();
	}
	
	public void setMaxY(int max) {
		Contract.checkCondition(max > 0);
		
		this.maxY = max;
		repaint();
	}
	
	public void addCurve(String name, float y, boolean isWon) {
		Contract.checkCondition(name != null && y > 0);
		float[][] yTab = null;
		if (statMap.containsKey(name)) {
			float[][] value = statMap.get(name);
			yTab = new float[value.length + 1][2];
			for (int i = 0; i < value.length; ++i) {
				yTab[i] = value[i];
			}
			yTab[value.length][0] = y;
			yTab[value.length][1] = isWon ? 1 : 0;
		} else {
			yTab = new float[1][2];
			yTab[0][0] = 0;
			yTab[0][1] = 1;
		}
		statMap.put(name, yTab);
		repaint();
	}
	
	public void removeCurve(String name) {
		statMap.remove(name);
		repaint();
	}
	
	public void clear() {
		statMap.clear();
		repaint();
	}
	
	// OUTILS
	
	private void graduation(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		
		g.setColor(COLOR);
		int x = 0;
		int y = 0;
		g.drawString(UserSave.lang.getString("time"), x + MARGIN, y + MARGIN);
		g.drawString(UserSave.lang.getString("nbGame"), width - 6 * MARGIN, 
				height - MARGIN / 4);
		while (x <= maxX || y <= maxY) {
			x = (x > maxX) ? maxX : x;
			y = (y > maxY) ? maxY : y;
			Coord c = getCoord(x, y);
			g.drawLine(c.getX(), 2 * MARGIN, c.getX(), height - 2 * MARGIN);
			g.drawString(String.valueOf(x), c.getX() - 5, height - MARGIN);
			g.drawLine(2 * MARGIN, c.getY(), width - 2 * MARGIN, c.getY());
			g.drawString(String.valueOf(y), MARGIN / 2, c.getY() + 5);
			x = (maxX <= 20)? ++x : x + 5;
			y += 1;
		}
	}
	
	private void drawLine(Graphics g, int x1, float y1, int x2, float y2, 
			boolean isWon, Color color) {
		g.setColor(color);
		Coord c1 = getCoord(x1, y1);
		Coord c2 = getCoord(x2, y2);
		if (isWon) {
			g.fillOval(c2.getX() - 5, c2.getY() - 5, 10, 10);
		} else {
			g.drawLine(c2.getX() - 5, c2.getY() - 5, c2.getX() + 5, 
					c2.getY() + 5);
			g.drawLine(c2.getX() + 5, c2.getY() - 5, c2.getX() - 5, 
					c2.getY() + 5);
		}
		g.drawLine(c1.getX(), c1.getY(), c2.getX(), c2.getY());
	}
	
	private Coord getCoord(int x, float y) {
		int x1 = (x > maxX) ? maxX : x;
		float y1 = (y > maxY) ? maxY : y;
		int realX = 2 * MARGIN + (getWidth() - 4 * MARGIN) * x1 / maxX;
		int realY = (int) (2 * MARGIN + (getHeight() - 4 * MARGIN) * 
				(maxY - y1) / maxY);
		return new Coord(realX, realY);
	}
}
