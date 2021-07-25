package Shanghai20.view.gui;

import Shanghai20.util.Config;
import Shanghai20.util.technical.StopWatch;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class GraphicTimer extends JComponent {

    // CONSTANTE

	private static final long serialVersionUID = 1L;
	
	// ATTRIBUT
	
	private StopWatch model;
	private Color fgColor;

    // CONSTRUCTEURS
	
    public GraphicTimer(StopWatch sw, int w, int h) {
        model = sw;
        addModelListener();
        this.setPreferredSize(new Dimension(w, h));
        fgColor = Color.DARK_GRAY;
        Color bgColor = Config.COLORBUTTON;
        setBackground(bgColor);
        Border b = BorderFactory.createLineBorder(bgColor.darker(), 3);
        setBorder(b);
    }
    
    // REQUETE
    
    public Color getForegroundColor() {
    	return fgColor;
    }

    // COMMANDES

    public void start() {
        model.start();
    }

    public void pause() {
        model.pause();
    }

    public void resume() {
        model.resume();
    }

    public void stop() {
        model.stop();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawContainer(g);
        drawTime(g);
    }
    
    public void setForegroundColor(Color c) {
    	fgColor = c;
    }

    // OUTILS

    private void drawContainer(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
    }

    private void drawTime(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        Font font = new Font("Arial", Font.BOLD,
                (getWidth() + getHeight()) / 10);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        final int x = (this.getWidth() - fm.stringWidth(model.getTime())) / 2;
        final int y = ((this.getHeight() - fm.getHeight()) / 2) + fm.getAscent();

        g2d.setColor(fgColor);
        g2d.drawString(model.getTime(), x, y);
        g2d.dispose();
    }

    private void addModelListener() {
        model.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                repaint();
            }
        });
    }
}
