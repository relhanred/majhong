package Shanghai20.view.extendable;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Shanghai20.Shanghai;
import Shanghai20.util.Config;
import Shanghai20.util.Contract;

public class JBackground extends JPanel {
	
	// CONSTRUCTEUR
	
	private static final long serialVersionUID = 1L;
	private URL DEFAULTBG = Shanghai.class.getResource("backgrounds/default.jpg");
	
	// ATTRIBUT
	
	private String path;
	private BufferedImage image;
	
	// CONSTRUCTEUR

	public JBackground() {
		super();
		image = null;
		path = null;
		try {
			image = ImageIO.read(DEFAULTBG);
			path = DEFAULTBG.getFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setLayout(new BorderLayout());
		setOpaque(false);
	}
	
	public JBackground(LayoutManager layout) {
		this();
		Contract.checkCondition(layout != null);
		
		setLayout(layout);
	}
	
	// REQUETES
	
	public String getPath() {
		return path;
	}
	
	// COMMANDES
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
	}
	
	public void setPathImage(Object path) {
		Contract.checkCondition(path != null);
		if (Config.isURL(path)) {
			try {
				this.image = ImageIO.read((URL) path);
				this.path = ((URL) path).getFile();
			} catch (IOException e) {
				try {
					image = ImageIO.read(DEFAULTBG);
					this.path = DEFAULTBG.getFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} else {
			try {
				this.image = ImageIO.read((File) path);
				this.path = ((File) path).getAbsolutePath();
			} catch (IOException e) {
				try {
					image = ImageIO.read(DEFAULTBG);
					this.path = DEFAULTBG.getFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		repaint();
	}
}
