package Shanghai20.view.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import Shanghai20.UserSave;
import Shanghai20.Shanghai;
import Shanghai20.controller.gui.TileExample;
import Shanghai20.util.Config;
import Shanghai20.util.Contract;
import Shanghai20.util.technical.SpriteSheetBuilder;
import Shanghai20.view.extendable.CadreComboBox;
import Shanghai20.view.extendable.JBackground;

public class ThemeShapeSelection {
	
	public enum Window {
		NEWGAME(3),
		OPTION(2);
		
		private int nb;
		
		Window(int nb) {
			this.nb = nb;
		}
		
		public int nbPanel() {
			return nb;
		}
	}
	
	// ATTRIBUT
	
	private JPanel component;
	private CadreComboBox theme;
	private TileExample tileExample;
	private Map<String, File> externTheme;
	private CadreComboBox shape;
	private JBackground choiceShape;
	private CadreComboBox wallpaper;
	private JBackground choiceWallpaper;
	private Map<String, File> externWallpaper;
	private Window w;
	private SpriteSheetBuilder ssb;
    private EventListenerList listenerList;
    private ChangeEvent changeEvent;
	
	// CONSTRUCTEUR
	
	public ThemeShapeSelection(Window w) {
		this.w = w;
		listenerList = new EventListenerList();
		createView();
    	placeComponent();
    	createController();
	}
	
	// REQUETE
	
	public JPanel getComponent() {
		return component;
	}
	
	// COMMANDE
	
	public void applyChange() {
		int indexTheme = theme.getSelectedIndex();
		if (indexTheme < Config.DEFAULTTHEMES.length) {
			if (Config.DEFAULTTHEMES[indexTheme] == Config.BROWSER) {
				//
				return;
			}
			UserSave.pathTheme = Shanghai.class.getResource(
					Config.DEFAULTTHEMES[indexTheme]);
		} else {
			UserSave.pathTheme = externTheme.get(theme.getSelectedItem());
		}
		if (w == Window.NEWGAME) {
			UserSave.pathShape = Shanghai.class.getResource(
					Config.SHAPESFOLDER + shape.getSelectedItem() 
					+ Config.SHAPESSEXT);
		}
		int indexBackground = wallpaper.getSelectedIndex();
		if (indexBackground < Config.DEFAULTBACKGROUNDS.length) {
			if (Config.DEFAULTBACKGROUNDS[indexBackground] == Config.BROWSER) {
				//
				return;
			}
			UserSave.pathBackground = Shanghai.class.getResource(
					Config.DEFAULTBACKGROUNDS[indexBackground]);
		} else {
			UserSave.pathBackground = 
					externWallpaper.get(wallpaper.getSelectedItem());
		}
	}
	
	public void addChangeListener(ChangeListener listener) {
		Contract.checkCondition(listener != null);
    	
    	listenerList.add(ChangeListener.class, listener);
	}
	
	public void removeChangeListener(ChangeListener listener) {
		Contract.checkCondition(listener != null, "argument invalide!");
    	
    	listenerList.remove(ChangeListener.class, listener);
	}
	
	private void createView() {
		ssb = new SpriteSheetBuilder();
		ssb.setPathImage(UserSave.pathTheme);
    	theme = new CadreComboBox(Config.getNames(Config.DEFAULTTHEMES));
    	tileExample = new TileExample();
    	tileExample.setImage(ssb.getSprite(1), ssb.getSprite(2));
    	externTheme = new HashMap<String, File>();
    	if (Config.isURL(UserSave.pathTheme)) {
    		String s = Config.getName(((URL)UserSave.pathTheme).getFile());
    		theme.setSelectedItem(s);
    	} else {
    		File f = (File)UserSave.pathTheme;
    		String s = Config.getName(f.getAbsolutePath());
    		externTheme.put(s, f);
    		theme.addItem(s);
    		theme.setSelectedItem(s);
    	}
    	if (w == Window.NEWGAME) {
    		shape = new CadreComboBox(Config.getNames(Config.DEFAULTSHAPES));
    		choiceShape = new JBackground();
    		choiceShape.setPathImage(
    				Shanghai.class.getResource(Config.DEFAULTSHAPES[0]));
    	} else {
    		shape = null;
    		choiceShape = null;
    	}
    	wallpaper = new CadreComboBox(
    			Config.getNames(Config.DEFAULTBACKGROUNDS));
    	choiceWallpaper = new JBackground();
    	externWallpaper = new HashMap<String, File>();
    	if (Config.isURL(UserSave.pathBackground)) {
    		String s = Config.getName(((URL)UserSave.pathBackground).getFile());
    		wallpaper.setSelectedItem(s);
    	} else {
    		File f = (File)UserSave.pathBackground;
    		String s = Config.getName(f.getAbsolutePath());
    		externWallpaper.put(s, f);
    		wallpaper.addItem(s);
    		wallpaper.setSelectedItem(s);
    	}
    	choiceWallpaper.setPathImage(UserSave.pathBackground);
	}
	
	private void placeComponent() {
		Border b = BorderFactory.createMatteBorder(0, 0, 0, 5, Color.BLACK);
		component = new JPanel(new GridLayout(1, w.nbPanel())); {
			JPanel p1 = panelSelection(UserSave.lang.getString("theme"), theme, 
					tileExample); {
				p1.setBorder(b);
			}
			component.add(p1);
			if (w == Window.NEWGAME) {
				JPanel p2 = panelSelection(UserSave.lang.getString("shape"), 
						shape, choiceShape); {
					p2.setBorder(b);
				}
				component.add(p2);
			}
			JPanel p3 = panelSelection(UserSave.lang.getString("wallpaper"), 
					wallpaper, choiceWallpaper); {
				p3.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
			}
			component.add(p3);
			component.setOpaque(false);
		}
	}
	
	private void createController() {
		theme.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					return;
				}
				String item = (String) theme.getSelectedItem();
				int i = theme.getSelectedIndex();
				if (item.equals(Config.BROWSER)) {
					File f = browseFile();
					if (f != null) {
						String baseName = Config.getName(f.getAbsolutePath());
						ssb.setPathImage(f);
						externTheme.put(baseName, f);
						theme.addItem(baseName);
						theme.setSelectedItem(baseName);
					}
					
				} else if (i < Config.DEFAULTTHEMES.length) {
					ssb.setPathImage(Shanghai.class.getResource(
							Config.DEFAULTTHEMES[i]));
				} else {
					ssb.setPathImage(externTheme.get(item));
				}
				tileExample.setImage(ssb.getSprite(1), ssb.getSprite(2));
				tileExample.repaint();
				fireStateChanged();
			}
		});
		if (w == Window.NEWGAME) {
			shape.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					int i = shape.getSelectedIndex();
					choiceShape.setPathImage(Shanghai.class.getResource(
							Config.DEFAULTSHAPES[i]));
					fireStateChanged();
				}
			});
		}
		wallpaper.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					return;
				}
				String item = (String) wallpaper.getSelectedItem();
				int i = wallpaper.getSelectedIndex();
				if (item.equals(Config.BROWSER)) {
					File f = browseFile();
					if (f != null) {
						String baseName = Config.getName(f.getAbsolutePath());
						choiceWallpaper.setPathImage(f);
						externWallpaper.put(baseName, f);
						wallpaper.addItem(baseName);
						wallpaper.setSelectedItem(baseName);
					}
				} else if (i < Config.DEFAULTBACKGROUNDS.length) {
					choiceWallpaper.setPathImage(Shanghai.class.getResource(
							Config.DEFAULTBACKGROUNDS[i]));
				} else {
					choiceWallpaper.setPathImage(externWallpaper.get(item));
				}
				fireStateChanged();
			}
		});
	}
	
	// OUTILS
	
	private JPanel panelSelection(String name, CadreComboBox comboBox, 
			JComponent body) {
		Font font = new Font("Arial", Font.BOLD, 20);
		JPanel p = new JPanel(new BorderLayout()); {
			JPanel p1 = new JPanel(new BorderLayout()); {
				JLabel label = new JLabel(name + " ");
        		label.setFont(font);
        		label.setForeground(Config.COLORLABEL);
        		p1.add(label, BorderLayout.WEST);
        		p1.add(comboBox, BorderLayout.CENTER);
        		p1.setOpaque(false);
        		p1.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
			}
			p.add(p1, BorderLayout.NORTH);
			JPanel p2 = new JPanel(new BorderLayout()); {
				p2.add(body, BorderLayout.CENTER);
				p2.setOpaque(false);
			}
			p.add(p2, BorderLayout.CENTER);
			p.setOpaque(false);
		}
		return p;
	}
	
	private File browseFile() {
		JFileChooser choice = new JFileChooser();
		File f = null;
		if (choice.showOpenDialog(component) == JFileChooser.APPROVE_OPTION) {
			f = choice.getSelectedFile();
		}
		return f;
	}
	
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
