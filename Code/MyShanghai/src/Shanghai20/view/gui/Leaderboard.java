package Shanghai20.view.gui;

import Shanghai20.UserSave;
import Shanghai20.util.Config;
import Shanghai20.util.saves.Savable;
import Shanghai20.util.technical.Scoreboard;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Leaderboard extends JComponent implements Savable {

    // CONSTANTE

	private static final int DEFAULT_HEIGHT = 400;
	private static final int DEFAULT_WIDTH = 400;
	public static final String SEPARATOR = "; ";
	
	// ATTRIBUT
	
	private Scoreboard board;

    // CONSTRUCTEURS
	
    public Leaderboard() {
        this.board = new Scoreboard();
        this.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        Color bgColor = Config.COLORBUTTON;
        setBackground(bgColor);
        Border b = BorderFactory.createLineBorder(bgColor.darker(), 3);
        setBorder(b);
    }

    public Leaderboard(Scoreboard sb, int w, int h) {
        this.board = sb;
        this.setPreferredSize(new Dimension(w, h));
    }

    // REQUETES

    @Override
    public String describe() {
        StringBuilder res = new StringBuilder();
        for (String entry : board.getEntries()) {
            res.append(entry);
            res.append(SEPARATOR);
        }

        return res.toString();
    }

    // COMMANDES

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawContainer(g);

        Font font = new Font("Arial", Font.PLAIN,
                (getWidth() + getHeight()) / 100);
        g.setFont(font);
        drawTitle(g);
        drawEntries(g);
    }

    public void updateWithEntry(String user, String time) {
        board.updateWithEntry(user, time);
        repaint();
    }

    // OUTILS

    private void drawContainer(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
    }

    private void drawTitle(Graphics g) {
        g.setColor(Color.BLACK);
        Font font = new Font("Arial", Font.BOLD,
                (getWidth() + getHeight()) / 75);
        g.setFont(font);

        FontMetrics fm = g.getFontMetrics();
        final String title = UserSave.lang.getString("leaderboard");
        final int x = (this.getWidth() - fm.stringWidth(title)) / 2;
        final int y = (this.getHeight() / 10) - fm.getAscent() * 2;
        g.drawString(title, x, y);
    }

    private void drawEntries(Graphics g) {
        g.setColor(Color.WHITE);
        Font font = new Font("Arial", Font.PLAIN,
                (getWidth() + getHeight()) / 75);
        g.setFont(font);

        final int tenthWidth = getWidth() / 10;
        final int tenthHeight = getHeight() / 10;
        final int x = tenthWidth + 5;
        int y = tenthHeight + 10;
        FontMetrics fm = g.getFontMetrics();

        for (String entry : board.getEntries()) {
            String[] current = entry.split(Scoreboard.SEPARATOR);
            g.drawString(current[0], x, y);
            g.drawString(current[1], this.getWidth() - (x + tenthWidth), y);
            y += this.getHeight() / 10 - fm.getDescent();
        }

        final int centerX = getWidth() / 2;
        final int topY = getHeight() / 10 - fm.getAscent();
        g.drawLine(centerX, topY, centerX, getHeight());
        g.drawLine(0, topY, getWidth(), topY);
    }
}
