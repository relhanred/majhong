package Shanghai20.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Shanghai20.UserSave;
import Shanghai20.util.Config;
import Shanghai20.view.extendable.CadreCheckBox;
import Shanghai20.view.extendable.CadreComboBox;
import Shanghai20.view.extendable.JBackground;
import Shanghai20.view.extendable.OvalButton;
import Shanghai20.view.extendable.Slider;
import Shanghai20.view.gui.ThemeShapeSelection;
import Shanghai20.view.gui.ThemeShapeSelection.Window;

public class OptionsMenuWindow {
	
	// ATTRIBUTS
	
	private JFrame mainFrame;
	private OvalButton backButton;
	private OvalButton validate;
	private CadreCheckBox soundCheck;
	private JSlider volume;
	private CadreComboBox language;
	private ThemeShapeSelection tss;
	private Map<String, Locale> langMap;

    // CONSTRUCTEURS
    
    public OptionsMenuWindow() {
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
    	final int width = 650;
    	final int height = 500;
    	mainFrame = new JFrame(UserSave.lang.getString("options"));
    	mainFrame.setPreferredSize(new Dimension(width, height));
    	soundCheck = new CadreCheckBox();
    	soundCheck.setSelected(!UserSave.isMuted);
    	backButton = new OvalButton(UserSave.lang.getString("back"));
    	volume = new Slider(100, 10, 0).getSlider();
    	volume.setValue(UserSave.MUSICTHEMEPLAYER.getVolume());
    	tss = new ThemeShapeSelection(Window.OPTION);
    	langMap = new LinkedHashMap<String, Locale>();
    	String[] allLanguage = getAllLanguage();
    	language = new CadreComboBox(allLanguage);
    	validate = new OvalButton(UserSave.lang.getString("validate"));
    	validate.setEnabled(false);
    }
    
    private void placeComponents() {
    	mainFrame.setContentPane(new JBackground());
    	JPanel n = new JPanel(); {
    		Font font = new Font("Arial", Font.BOLD, 40);
    		JLabel label = new JLabel(UserSave.lang.getString("options"));
    		label.setFont(font);
    		label.setForeground(Config.COLORLABEL.brighter());
    		n.add(label, FlowLayout.LEFT);
    		n.setOpaque(false);
    	}
    	mainFrame.add(n, BorderLayout.NORTH);
    	JPanel p = new JPanel(new GridLayout(2, 1)); {
	    	JPanel p1 = new JPanel(new GridLayout(3, 1)); {
	    		Font font = new Font("Arial", Font.BOLD, 35);
	    		JPanel p11 = new JPanel(new FlowLayout(FlowLayout.LEFT)); {
	    			JLabel label = new JLabel(UserSave.lang.getString("sound") + " ");
	    	    	label.setFont(font);
	    	    	label.setForeground(Config.COLORLABEL.brighter());
	    	    	p11.add(label);
	    	    	p11.add(soundCheck);
	        		p11.setOpaque(false);
	    		}
	    		p1.add(p11);
	    		JPanel p12 = new JPanel(new BorderLayout()); {
	    			JLabel label = new JLabel(UserSave.lang.getString("volume") + " ");
	    	    	label.setFont(font);
	    	    	label.setForeground(Config.COLORLABEL.brighter());
	    	    	p12.add(label, BorderLayout.WEST);
	        		p12.add(volume);
	    			p12.setOpaque(false);
	    		}
	    		p1.add(p12);
	    		JPanel p13 = new JPanel(new FlowLayout(FlowLayout.LEFT)); {
	    			JLabel label = new JLabel(UserSave.lang.getString("language") 
	    					+ " ");
	    	    	label.setFont(font);
	    	    	label.setForeground(Config.COLORLABEL.brighter());
	    	    	p13.add(label);
	    	    	p13.add(language);
	    			p13.setOpaque(false);
	    		}
	    		p1.add(p13);
	    		p1.setOpaque(false);
	    	}
	    	p.add(p1);
	    	p.add(tss.getComponent());
	    	p.setOpaque(false);
    	}
    	mainFrame.add(p, BorderLayout.CENTER);
    	JPanel q = new JPanel(new GridLayout(1, 2)); {
    		JPanel q1 = new JPanel(new FlowLayout(FlowLayout.LEFT)); {
    			q1.add(backButton);
            	q1.setOpaque(false);
    		}
        	q.add(q1);
        	JPanel q2 = new JPanel(new FlowLayout(FlowLayout.RIGHT)); {
        		q2.add(validate);
            	q2.setOpaque(false);
    		}
        	q.add(q2);
    		q.setOpaque(false);
    	}
    	mainFrame.add(q, BorderLayout.SOUTH);
    }
    
    private void createController() {
    	mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	soundCheck.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				UserSave.isMuted = !soundCheck.isSelected();
			}
    	});
    	volume.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				UserSave.MUSICTHEMEPLAYER.pause();
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				UserSave.MUSICTHEMEPLAYER.play().loop();
			}
		});
    	volume.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				UserSave.MUSICTHEMEPLAYER.setVolume(volume.getValue());
			}
		});
    	backButton.addActionListener(new ActionListener() {
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
    	tss.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				validate.setEnabled(true);
			}
		});
    	language.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				validate.setEnabled(true);
			}
		});
    	validate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String s = (String) language.getSelectedItem();
				UserSave.lang = ResourceBundle.getBundle(Config.PATHLANG, 
						langMap.get(s));
				tss.applyChange();
				
				// REFRESH
				mainFrame.dispose();
				mainFrame = null;
				SwingUtilities.invokeLater(new Runnable() {
		            @Override
		            public void run() {
		                new OptionsMenuWindow().display();
		            }
		        });
			}
		});
    }
    
    // OUTILS
    
    /*
     * Liste toutes les langues présentes dans util/languages.
     */
    private String[] getAllLanguage() {
    	langMap.put(UserSave.lang.getString("lang"), UserSave.lang.getLocale());
    	String[] existLangs = Locale.getISOLanguages();
    	for (String s : existLangs) {
    		URL rb = ClassLoader.getSystemResource(Config.PATHLANG + "_" + s + 
    				".properties");
    		if (rb != null) {
    			String language = ResourceBundle.getBundle(Config.PATHLANG, 
    					new Locale(s)).getString("lang");
    			langMap.put(language, new Locale(s));
    		}
    	}
    	return langMap.keySet().toArray(new String[0]);
    }
}
