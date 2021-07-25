package Shanghai20.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import Shanghai20.UserSave;
import Shanghai20.util.Config;
import Shanghai20.util.technical.StopWatch;
import Shanghai20.Shanghai;
import Shanghai20.view.extendable.CadreComboBox;
import Shanghai20.view.extendable.CadreTextField;
import Shanghai20.view.extendable.JBackground;
import Shanghai20.view.extendable.OvalButton;
import Shanghai20.view.gui.ThemeShapeSelection;
import Shanghai20.view.gui.ThemeShapeSelection.Window;

public class NewGameWindow {
	
	public enum NewWindow {
		NEWGAME(3, 1010, 400), 
		QUICKGAME(2, 650, 200);
		
		private int nbPanel;
		private int width;
		private int height;
		
		NewWindow(int nbPanel, int width, int height) {
			this.nbPanel = nbPanel;
			this.width = width;
			this.height = height;
		}
		
		public int nbPanel() {
			return nbPanel;
		}
		
		public int getWidth() {
			return width;
		}
		
		public int getHeight() {
			return height;
		}
	}
	
	// ATTRIBUTS
	
	private JFrame mainFrame;
	private CadreTextField pseudo;
	private static final String[] ALLDIFFICULTY = {
			UserSave.lang.getString("easy"), 
			UserSave.lang.getString("medium"),
			UserSave.lang.getString("hard")
	};
	private CadreComboBox difficulty;
	private OvalButton cancel;
	private OvalButton validate;
	private ThemeShapeSelection tss;
	private NewWindow window;
	

    // CONSTRUCTEURS
    
    public NewGameWindow(NewWindow window) {
    	this.window = window;
        createView();
        placeComponents();
        createController();
    }
    
    // COMMANDES
    
    public void display() {
    	mainFrame.pack();
    	mainFrame.setLocationRelativeTo(null);
    	mainFrame.setVisible(true);
    }
    
    // OUTILS
    
    private void createView() {
    	final int width = window.getWidth();
    	final int height = window.getHeight();
    	mainFrame = new JFrame(UserSave.lang.getString("options"));
    	mainFrame.setPreferredSize(new Dimension(width, height));
    	pseudo = new CadreTextField(250, 40);
    	pseudo.placeHolderUsername();
    	difficulty = new CadreComboBox(ALLDIFFICULTY);
    	difficulty.setSelectedIndex(1);
    	cancel = new OvalButton(UserSave.lang.getString("back"));
    	validate = new OvalButton(UserSave.lang.getString("validate"));
    	tss = null;
    	if (window == NewWindow.NEWGAME) {
    		tss = new ThemeShapeSelection(Window.NEWGAME);
    	}
    }
    
    private void placeComponents() {
    	mainFrame.setContentPane(new JBackground());
    	Font font = new Font("Arial", Font.BOLD, 20);
    	JPanel p = new JPanel(new GridLayout(1, window.nbPanel)); {
    		JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT)); {
    			p1.add(pseudo);
            	p1.setOpaque(false);
    		}
        	p.add(p1);
        	JPanel p2 = new JPanel(new BorderLayout()); {
        		JLabel label = new JLabel(UserSave.lang.getString("difficulty") 
        				+ " ");
        		label.setFont(font);
        		label.setForeground(Config.COLORLABEL);
        		p2.add(label, BorderLayout.WEST);
        		p2.add(difficulty, BorderLayout.CENTER);
            	p2.setOpaque(false);
            	p2.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    		}
        	p.add(p2);
        	if (window == NewWindow.NEWGAME) {
        		JPanel p3 = new JPanel(); {
            		p3.setOpaque(false);
            	}
        		p.add(p3);
        	}
    		p.setOpaque(false);
    	}
    	mainFrame.add(p, BorderLayout.NORTH);
    	if (window == NewWindow.NEWGAME) {
    		mainFrame.add(tss.getComponent(), BorderLayout.CENTER);
    	}
    	p = new JPanel(new GridLayout(1, 2)); {
    		JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT)); {
    			p1.add(cancel);
            	p1.setOpaque(false);
    		}
        	p.add(p1);
        	JPanel p2 = new JPanel(new FlowLayout(FlowLayout.RIGHT)); {
        		p2.add(validate);
            	p2.setOpaque(false);
    		}
        	p.add(p2);
    		p.setOpaque(false);
    	}
    	mainFrame.add(p, BorderLayout.SOUTH);
    }
    
    private void createController() {
    	mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mainFrame.dispose();
				mainFrame = null;
				SwingUtilities.invokeLater(new Runnable() {
		            @Override
		            public void run() {
		                new MainMenuWindow().display();
		            }
		        });
			}
    	});
    	validate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (pseudo.getText() == null) {
					pseudo.errorValue();
					return;
				}
				if (window == NewWindow.NEWGAME) {
					tss.applyChange();
				} else {
					int max = Config.DEFAULTSHAPES.length - 1;
					String file = Config.DEFAULTSHAPES[random(0, max - 1)];
					String shape = Config.getName(file);
					UserSave.pathShape = Shanghai.class.getResource(
							Config.SHAPESFOLDER + shape + Config.SHAPESSEXT);
				}
				UserSave.board = Config.createBoard(UserSave.pathShape, 
						difficulty.getSelectedIndex());
				UserSave.timer = new StopWatch();
				UserSave.pseudo = pseudo.getText();
				mainFrame.dispose();
				mainFrame = null;
				SwingUtilities.invokeLater(new Runnable() {
		            @Override
		            public void run() {
		                new GameWindow().display();
		            }
		        });
			}
    	});
    }
    
    // OUTIL
    
    private int random(int min, int max) {
    	return (int)(Math.random() * (max - min) + 1) + min;
    }
}