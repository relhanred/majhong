package Shanghai20.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Shanghai20.GameState;
import Shanghai20.UserSave;
import Shanghai20.Shanghai;
import Shanghai20.controller.gui.GraphicBoard;
import Shanghai20.util.Config;
import Shanghai20.util.saves.AbstractSaveManager;
import Shanghai20.util.technical.*;
import Shanghai20.util.technical.saves.*;
import Shanghai20.view.extendable.CadrePanel;
import Shanghai20.view.extendable.JBackground;
import Shanghai20.view.extendable.OvalButton;
import Shanghai20.view.gui.GraphicTimer;
import Shanghai20.view.gui.Leaderboard;


public class GameWindow {

	// CONSTANTE

	private static final URL UNDOPATH =
			Shanghai.class.getResource("img/undo.png");
	private static final URL REDOPATH =
			Shanghai.class.getResource("img/redo.png");
	private static final URL HINTPATH =
			Shanghai.class.getResource("img/hint.png");
	private static final URL HELPPATH =
			Shanghai.class.getResource("img/help.png");

	private static final URL UNDOSOUND =
			Shanghai.class.getResource("sounds/MoveAdd.wav");
	private static final URL REDOSOUND =
			Shanghai.class.getResource("sounds/MoveSup.wav");

	// ATTRIBUTS

	private JFrame mainFrame;
	private GraphicBoard graphics;
	private JLabel playerName;
	private GraphicTimer timer;
	private Map<String, OvalButton> leftButtons;
	private Map<String, OvalButton> rightButtons;
	private boolean hasFinished;
	private MusicPlayer undoSound;
	private MusicPlayer redoSound;

	// CONSTRUCTEURS

	public GameWindow() {
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
		final int width = 900;
		final int height = 600;
		mainFrame = new JFrame("Shangaï");
		mainFrame.setPreferredSize(new Dimension(width, height));
		graphics = new GraphicBoard(UserSave.board);
		playerName = new JLabel(UserSave.pseudo);
		timer = new GraphicTimer(UserSave.timer, 250, 50);
		leftButtons = new LinkedHashMap<String, OvalButton>();
		OvalButton undoButton = new OvalButton(new ImageIcon(UNDOPATH).getImage());
		undoButton.setEnabled(false);
		leftButtons.put("undo", undoButton);
		OvalButton redoButton = new OvalButton(new ImageIcon(REDOPATH).getImage());
		redoButton.setEnabled(false);
		leftButtons.put("redo", redoButton);
		leftButtons.put("hint", new OvalButton(new ImageIcon(HINTPATH).getImage()));
		leftButtons.put("help", new OvalButton(new ImageIcon(HELPPATH).getImage()));

		rightButtons = new LinkedHashMap<String, OvalButton>();
		rightButtons.put("save",
				new OvalButton(UserSave.lang.getString("save")));
		rightButtons.put("saveAs",
				new OvalButton(UserSave.lang.getString("saveAs")));
		rightButtons.put("menu",
				new OvalButton(UserSave.lang.getString("menu")));
		hasFinished = false;
		undoSound = new MusicPlayer(UNDOSOUND);
		redoSound = new MusicPlayer(REDOSOUND);
		UserSave.MUSICTHEMEPLAYER.play().loop();
		if (!UserSave.timer.hasStopped()) {
			if (!UserSave.timer.hasStarted()) {
				UserSave.timer.start();
			} else {
				if (UserSave.timer.isPaused()) UserSave.timer.resume();
			}
		}
	}

	private void placeComponents() {
		JBackground bg = new JBackground();
		bg.setPathImage(UserSave.pathBackground);
		mainFrame.setContentPane(bg);
		JPanel p = new JPanel(new GridLayout(1, 3, 50, 0)); {
			JPanel p1 = new CadrePanel(); {
				Font font = new Font("Arial", Font.BOLD, 30);
				playerName.setFont(font);
				p1.add(playerName);
				p1.setOpaque(false);
			}
			p.add(p1);
			p.add(timer);
			p.add(new JLabel());
			p.setOpaque(false);
		}
		mainFrame.add(p, BorderLayout.NORTH);

		JPanel q = new JPanel(new BorderLayout()); {
			q.add(graphics, BorderLayout.CENTER);
			q.setOpaque(false);
		}
		mainFrame.add(q, BorderLayout.CENTER);

		p = new JPanel(new GridLayout(0, 1)); {
			for (int i = 0; i < 2; ++i) {
				JPanel empty = new JPanel();
				empty.setOpaque(false);
				p.add(empty);
			}
			for (OvalButton b : leftButtons.values()) {
				b.setPreferredSize(new Dimension(100, 100));
				p.add(b);
			}
			for (int i = 0; i < 2; ++i) {
				JPanel empty = new JPanel();
				empty.setOpaque(false);
				p.add(empty);
			}
			p.setOpaque(false);
		}
		mainFrame.add(p, BorderLayout.WEST);

		q = new JPanel(new GridLayout(3, 1)); {
			JPanel empty = new JPanel();
			empty.setOpaque(false);
			q.add(empty);
			JPanel q1 = new JPanel(new GridLayout(0, 1)); {
				for (OvalButton b : rightButtons.values()) {
					q1.add(b);
				}
				q1.setOpaque(false);
			}
			q.add(q1);
			q.setOpaque(false);
		}
		mainFrame.add(q, BorderLayout.EAST);
	}

	private void createController() {
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		leftButtons.get("undo").addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				UserSave.board.playUndo();
				graphics.resetSelected();
				graphics.repaint();
				updateButtons();
				undoSound.play();
			}
		});
		leftButtons.get("redo").addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				UserSave.board.playRedo();
				graphics.resetSelected();
				graphics.repaint();
				updateButtons();
				redoSound.play();
			}
		});
		leftButtons.get("hint").addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				graphics.setHint();
			}
		});
		leftButtons.get("help").addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				graphics.setHelp();
			}
		});
		rightButtons.get("save").addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UserSave.timer.pause();
				try {
					saveGame(QuickSaveManager.FOLDERPATH);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				UserSave.timer.resume();
			}
		});
		rightButtons.get("saveAs").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	UserSave.timer.pause();

            	String saveName = CadreOptionPane.showInputDialog(
									UserSave.lang.getString("saveName"), 
									UserSave.lang.getString("saveTitle")
								);
            	if (saveName == null) {
            		UserSave.timer.resume();
            		return;
            	}
            	if (!isValidSaveName(saveName)) {
            		CadreOptionPane.showInformationDialog(
            				UserSave.lang.getString("invalidSaveName"),
							UserSave.lang.getString("saveTitle")
					);
				} else {
					JFileChooser jf = new JFileChooser(Config.SAVESFOLDER);
					jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					jf.setAcceptAllFileFilterUsed(false);

					int retval = jf.showOpenDialog(mainFrame);
					if (retval == JFileChooser.APPROVE_OPTION) {
						File f = jf.getSelectedFile();
						if (!f.exists() || f.isDirectory()) {
							String path = f.getAbsolutePath() + Config.FILE_SEPARATOR + saveName;
							try {
								//path = extractFolderFrom(path);
								saveGame(path);
							} catch (IOException ex) {
								ex.printStackTrace();
							}
						}
					}
				}
                UserSave.timer.resume();
            }
        });
		rightButtons.get("menu").addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				UserSave.MUSICTHEMEPLAYER.pause();
				if (UserSave.timer.isRunning()) {
					UserSave.timer.pause();
				}
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

		graphics.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				updateButtons();
				if (!hasFinished && !graphics.canPlayable()) {
					String message;
					hasFinished = true;
					UserSave.timer.stop();
					if (graphics.hasWon()) {
						message = UserSave.lang.getString("winMessage");
						// Sauvegarde du score
						LeaderboardSaveManager saver =
								new LeaderboardSaveManager();
						try {
							saver.load();
							Leaderboard lb = saver.getTarget();
							lb.updateWithEntry(UserSave.pseudo,
									UserSave.timer.getTime());
							saver.setTarget(lb);
							if (!saver.save()) {
								CadreOptionPane.showInformationDialog(
										UserSave.lang.getString("updateFail"),
										UserSave.lang.getString("saveTitle")
								);
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					} else {
						message = UserSave.lang.getString("loseMessage");
					}
					CadreOptionPane.showInformationDialog(
							message,
							UserSave.lang.getString("finishGame")
					);
				}
			}
		});
	}

	private void updateButtons() {
		leftButtons.get("undo").setEnabled(UserSave.board.canUndo());
		leftButtons.get("redo").setEnabled(UserSave.board.canRedo());
	}

	private void saveGame(String path) throws IOException {
		AbstractSaveManager.createFolderAndFiles(path);

		GameStateManager gsm = new GameStateManager(path);
		TimerSaveManager tsm = new TimerSaveManager(path);

		gsm.setTarget(new GameState());
		tsm.setTarget();

		String t = UserSave.lang.getString("saveTitle");
		String msg = UserSave.lang.getString("saveFail");
		if (!gsm.save()) CadreOptionPane.showInformationDialog(msg, t);
		if (!tsm.save()) CadreOptionPane.showInformationDialog(msg, t);

		msg = UserSave.lang.getString("saveSucc");
		CadreOptionPane.showInformationDialog(msg, t);
	}

	private boolean isValidSaveName(String saveName) {
		Matcher m = Config.SVFNAMECHECK.matcher(saveName);

		return m.matches();
	}

}
