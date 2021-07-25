package Shanghai20.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Shanghai20.Shanghai;
import Shanghai20.UserSave;
import Shanghai20.controller.Board;
import Shanghai20.util.Config;
import Shanghai20.util.Contract;
import Shanghai20.view.extendable.CadreComboBox;
import Shanghai20.view.extendable.JBackground;
import Shanghai20.view.extendable.OvalButton;
import Shanghai20.view.extendable.Slider;
import Shanghai20.view.gui.GraphicStatistic;

public class StatisticWindow {
	
	// ATTRIBUT
	
	private JFrame mainFrame;
	private GraphicStatistic graphic;
	private JSlider sliderX;
	private JSlider sliderY;
	private OvalButton cancel;
	private OvalButton info;
	private static final String[] ALLDIFFICULTY = {
			UserSave.lang.getString("easy"), 
			UserSave.lang.getString("medium"),
			UserSave.lang.getString("hard")
	};
	private CadreComboBox difficulty;
	private CadreComboBox shape;
	private OvalButton compute;
	private boolean isRunning;
	private boolean stopped;
	private Thread thread;
	
	// CONSTRUCTEUR
	
    public StatisticWindow() {
        createView();
        placeComponents();
        createController();
    }
    
    // COMMANDE
	
    public void display() {
    	mainFrame.pack();
    	mainFrame.setLocationRelativeTo(null);
    	mainFrame.setVisible(true);
    }
    
    // OUTILS
    
    private void createView() {
    	final int width = 800;
    	final int height = 600;
    	mainFrame = new JFrame(UserSave.lang.getString("statistic"));
    	mainFrame.setPreferredSize(new Dimension(width, height));
    	graphic = new GraphicStatistic();
    	sliderX = new Slider(100, 10, 10).getSlider();
    	sliderX.setForeground(Color.GRAY);
    	sliderX.setValue(graphic.getMaxX());
    	sliderX.setMinimum(10);
    	
    	sliderY = new Slider(10, 1, 1).getSlider();
    	sliderY.setOrientation(JSlider.VERTICAL);
    	sliderY.setForeground(Color.GRAY);
    	sliderY.setValue(graphic.getMaxY());
    	sliderY.setMinimum(1);
    	
    	cancel = new OvalButton(UserSave.lang.getString("back"));
    	info = new OvalButton(UserSave.lang.getString("info"));
    	difficulty = new CadreComboBox(ALLDIFFICULTY);
    	difficulty.setSelectedIndex(1);
    	shape = new CadreComboBox(Config.getNames(Config.DEFAULTSHAPES));
    	compute = new OvalButton(UserSave.lang.getString("compute"));
    	isRunning = false;
    	thread = null;
    }
    
    private void placeComponents() {
    	Font font = new Font("Arial", Font.BOLD, 20);
    	mainFrame.setContentPane(new JBackground());
    	JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER)); {
			JLabel label = new JLabel(UserSave.lang.getString("shape") + ": ");
    		label.setFont(font);
    		label.setForeground(Config.COLORLABEL);
    		p.add(label);
    		p.add(shape);
    		label = new JLabel(" " + UserSave.lang.getString("difficulty") + ": ");
    		label.setFont(font);
    		label.setForeground(Config.COLORLABEL);
    		p.add(label);
    		p.add(difficulty);
    		p.add(new JLabel(" "));
    		p.add(compute);
    		p.setOpaque(false);
    	}
    	mainFrame.add(p, BorderLayout.NORTH);
    	JPanel center = new JPanel(new BorderLayout()); {
    		JPanel q = new JPanel(new BorderLayout()); {
        		q.add(new JLabel(" "), BorderLayout.NORTH);
        		q.add(sliderY, BorderLayout.CENTER);
        		q.add(new JLabel(" "), BorderLayout.SOUTH);
        		q.add(new JLabel(" "), BorderLayout.EAST);
        		q.setOpaque(false);
        	}
        	mainFrame.add(q, BorderLayout.WEST);
    		center.add(graphic, BorderLayout.CENTER);
    		JPanel q1 = new JPanel(new BorderLayout()); {
        		q1.add(new JLabel(" "), BorderLayout.NORTH);
        		q1.add(sliderX, BorderLayout.CENTER);
        		q1.add(new JLabel(" "), BorderLayout.SOUTH);
        		q1.add(new JLabel(" "), BorderLayout.EAST);
        		q1.setOpaque(false);
        	}
        	center.add(q1, BorderLayout.SOUTH);
    		center.setOpaque(false);
    	}
    	mainFrame.add(center, BorderLayout.CENTER);
    	p = new JPanel(new GridLayout(1, 2)); {
    		JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT)); {
    			p1.add(cancel);
            	p1.setOpaque(false);
    		}
        	p.add(p1);
        	JPanel p2 = new JPanel(new FlowLayout(FlowLayout.RIGHT)); {
        		p2.add(info);
            	p2.setOpaque(false);
    		}
        	p.add(p2);
    		p.setOpaque(false);
    	}
    	mainFrame.add(p, BorderLayout.SOUTH);
    }
    
    private void createController() {
    	mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	compute.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isRunning) {
					stopped = false;
					actionCompute();
				} else {
					stopped = true;
					refresh();
				}
			}
		});
    	cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isRunning) {
	    			stopped = true;
	    			refresh();
	    			if (thread != null) {
	    				try {
							thread.join();
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
	    			}
	    		}
				thread = null;
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
    	info.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel p = new JPanel(new BorderLayout()); {
					p.setBackground(new Color(Config.COLORLABEL.getRed(), 
							Config.COLORLABEL.getGreen(), 
							Config.COLORLABEL.getBlue(), 50));
					JLabel label = new JLabel(textInfo());
					label.setForeground(Config.COLORBUTTON);
					p.add(label, BorderLayout.CENTER);
				}
				CadreOptionPane.showInformationDialog(
					p, 
					UserSave.lang.getString("info")
				);
			}
		});
    	sliderX.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				graphic.setMaxX(sliderX.getValue());
			}
		});
    	sliderY.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				graphic.setMaxY(sliderY.getValue());
			}
		});
    }
    
    private void actionCompute() {
    	Contract.checkCondition(!isRunning);
    	isRunning = true;
    	refresh();
    	graphic.clear();
    	final int nbPartie = graphic.getMaxX();
		final URL path = Shanghai.class.getResource(Config.SHAPESFOLDER 
				+ shape.getSelectedItem() + Config.SHAPESSEXT);
		final int diff = difficulty.getSelectedIndex();
    	thread = new Thread() {
			public void run() {
				Board board = null;
				for (int i = 1; !stopped && i <= nbPartie; ++i) {
					for (Board.IA_Player ia : Board.IA_Player.values()) {
						if (stopped) {
							break;
						}
						board = Config.createBoard(path, diff);
						long begin = System.currentTimeMillis();
						boolean isWon = board.playIA(ia);
						long end = System.currentTimeMillis();
						float time = (end - begin) / 1000.0f;
						graphic.addCurve(ia.toString(), time, isWon);
					}
					
				}
				board = null;
				isRunning = false;
				stopped = false;
				refresh();
			}
		};
    	thread.start();
    }
    
    private void refresh() {
    	String isOff = UserSave.lang.getString("compute");
    	String isOn = UserSave.lang.getString("stop");
    	compute.setText(isRunning ? isOn : isOff);
    	compute.setEnabled(!stopped);
    	cancel.setEnabled(!stopped);
    }
    
    private String textInfo() {
    	StringBuilder buff = new StringBuilder();
    	buff.append("<html><head></head><body>");
    	buff.append(String.format("<h1><center>%s</center></h1>", 
    			UserSave.lang.getString("stat")));
    	buff.append(String.format("<h2>&nbsp;%s</h2><p>", 
    			UserSave.lang.getString("strat")));
    	for (Board.IA_Player p : Board.IA_Player.values()) {
    		buff.append(String.format("<strong>&nbsp;- %s</strong> : %s<br>", 
    			p.toString(), 
    			UserSave.lang.getString(p.toString())));
    	}
    	buff.append(String.format("</p><br><p>&nbsp;&#9679; %s<br>&nbsp;X %s</p>", 
    			UserSave.lang.getString("winsymb"), 
    			UserSave.lang.getString("lossymb")));
    	buff.append(String.format("<h2>&nbsp;%s</h2>", 
    			UserSave.lang.getString("setcalc")));
    	buff.append(String.format("<p>&nbsp;%s<br>&nbsp;%s</p>", 
    			UserSave.lang.getString("formdif"), 
    			UserSave.lang.getString("nbpart")));
    	buff.append("<br></body></html>");
    	return buff.toString();
    }
    
}
