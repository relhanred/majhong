package Shanghai20.util.technical;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import Shanghai20.Shanghai;
import Shanghai20.util.Config;
import Shanghai20.util.Contract;

public class SpriteSheetBuilder {
	
	// CONSTANTE
	
	private URL DEFAULTPATH = Shanghai.class.getResource("themes/Original.png");
    private static int ROWS = 5;
    private static int COLS = 10;
	
	// ATTRIBUTS
	
	private BufferedImage[] tabSprite;
	private BufferedImage sheet;
	private int count;
	
	
	// CONSTRUCTEUR
	
	public SpriteSheetBuilder() {
		count = 34;
		tabSprite = new BufferedImage[count];
		sheet = null;
		try {
			sheet = ImageIO.read(DEFAULTPATH);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public SpriteSheetBuilder(Object path) {
		this();
		Contract.checkCondition(path != null);
		
		setPathImage(path);
	}
	
	// REQUETES
	
	public int getCount() {
		return count;
	}
	
	public BufferedImage getSprite(int index) {
		Contract.checkCondition(index > 0 && index <= count);
		
		return tabSprite[index - 1];
	}
	
	// COMMANDES
	
	public void setPathImage(Object path) {
		Contract.checkCondition(path != null);
		
		if (Config.isURL(path)) {
			try {
				sheet = ImageIO.read((URL) path);
			} catch (IOException e) {
				try {
					sheet = ImageIO.read(DEFAULTPATH);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} else {
			try {
				sheet = ImageIO.read((File) path);
			} catch (IOException e) {
				try {
					sheet = ImageIO.read(DEFAULTPATH);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		build();
	}
	
	private void build() {
		int w = sheet.getWidth() / COLS;
		int h = sheet.getHeight() / ROWS;
		for (int i = 0; i < count; ++i) {
			tabSprite[i] = sheet.getSubimage((i % COLS) * w, (i / COLS) * h, w, 
					h);
		}
	}
}
