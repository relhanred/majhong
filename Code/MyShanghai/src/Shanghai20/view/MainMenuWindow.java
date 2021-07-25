package Shanghai20.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.*;

import Shanghai20.GameState;
import Shanghai20.UserSave;
import Shanghai20.util.Config;
import Shanghai20.util.saves.SaveManager;
import Shanghai20.util.technical.saves.*;
import Shanghai20.view.NewGameWindow.NewWindow;
import Shanghai20.view.extendable.JBackground;
import Shanghai20.view.extendable.OvalButton;
import Shanghai20.view.gui.Leaderboard;


public class MainMenuWindow {

	// ATTRIBUTS

	private JFrame mainFrame;
	private Map<String, OvalButton> leftButtons;
	private Leaderboard leaderBoard;

	// CONSTRUCTEURS

	public MainMenuWindow() {
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
		final int width = 700;
		final int height = 500;
		mainFrame = new JFrame("Menu");
		mainFrame.setPreferredSize(new Dimension(width, height));
		leftButtons = new LinkedHashMap<String, OvalButton>();
		leftButtons.put("newGame",
				new OvalButton(UserSave.lang.getString("newGame")));
		leftButtons.put("quickGame", new OvalButton(
				UserSave.lang.getString("quickGame")));
		leftButtons.put("load", new OvalButton(
				UserSave.lang.getString("load")));
		leftButtons.put("resume", new OvalButton(
				UserSave.lang.getString("resume")));
		leftButtons.put("statistic", new OvalButton(
				UserSave.lang.getString("statistic")));
		leftButtons.put("options", new OvalButton(
				UserSave.lang.getString("options")));
		leftButtons.put("quit", new OvalButton(UserSave.lang.getString("quit")));

		QuickSaveManager qsm = new QuickSaveManager();
		LeaderboardSaveManager loader = new LeaderboardSaveManager();
		try {
			if (!loader.load()) throw new AssertionError();
			leftButtons.get("resume").setEnabled(qsm.hasGameInProgress() 
					|| UserSave.board != null);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		leaderBoard = loader.getTarget();
	}

	private void placeComponents() {
		mainFrame.setContentPane(new JBackground());
		JPanel p = new JPanel(); {
			Font font = new Font("Arial", Font.BOLD, 40);
			JLabel label = new JLabel("Shanghaï");
			label.setFont(font);
			label.setForeground(Config.COLORLABEL);
			p.add(label, FlowLayout.LEFT);
			p.setOpaque(false);
		}
		mainFrame.add(p, BorderLayout.NORTH);
		JPanel q = new JPanel(new GridLayout(5, 1, 0, 30)); {
			q.add(leftButtons.get("newGame"));
			JPanel q1 = new JPanel(new GridLayout(1, 2)); {
				q1.add(leftButtons.get("resume"));
				JPanel q11 = new JPanel(new GridLayout(2, 1)); {
					q11.add(leftButtons.get("quickGame"));
					q11.add(leftButtons.get("load"));
					q11.setOpaque(false);
				}
				q1.add(q11);
				q1.setOpaque(false);
			}
			q.add(q1);
			q.add(leftButtons.get("statistic"));
			q.add(leftButtons.get("options"));
			q.add(leftButtons.get("quit"));
			q.setOpaque(false);
		}
		mainFrame.add(q, BorderLayout.WEST);
		p = new JPanel(); {
			p.add(leaderBoard);
			p.setOpaque(false);
		}
		mainFrame.add(p, BorderLayout.EAST);
		JPanel s = new JPanel(); {
			Font font = new Font("Arial", Font.BOLD, 40);
			JLabel label = new JLabel(" ");
			label.setFont(font);
			s.add(label, FlowLayout.LEFT);
			s.setOpaque(false);
		}
		mainFrame.add(s, BorderLayout.SOUTH);
	}

	private void createController() {
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		leftButtons.get("newGame").addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mainFrame.dispose();
				mainFrame = null;
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						new NewGameWindow(NewWindow.NEWGAME).display();
					}
				});
			}
		});
		leftButtons.get("quickGame").addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mainFrame.dispose();
				mainFrame = null;
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						new NewGameWindow(NewWindow.QUICKGAME).display();
					}
				});
			}
		});
		leftButtons.get("resume").addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (UserSave.board == null) {
					QuickSaveManager qsm = new QuickSaveManager();
					TimerSaveManager tsm = new TimerSaveManager(QuickSaveManager.FOLDERPATH);
					try {
						qsm.load();
						tsm.load();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					GameState savedGame = qsm.getTarget();
					savedGame.update();
					UserSave.timer = tsm.getTarget();

					// On efface le contenu des fichiers du dossier "quicksave"
					BoardSaveManager bsm = new BoardSaveManager(QuickSaveManager.FOLDERPATH);
					try {
						eraseSaveData(new SaveManager[]{qsm, tsm, bsm});
					} catch (FileNotFoundException ex) {
						ex.printStackTrace();
					}
				}
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
		leftButtons.get("load").addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// On lance le jeu
				mainFrame.dispose();
				mainFrame = null;
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						new LoadMenuWindow().display();
					}
				});
			}
		});
		leftButtons.get("statistic").addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (UserSave.board != null) {
					int option = CadreOptionPane.showConfirmDialog(
							UserSave.lang.getString("EraseGame"),
							UserSave.lang.getString("confirme"),
							CadreOptionPane.YES_NO_CANCEL_OPTION
					);
					if (option != CadreOptionPane.YES_OPTION) {
						return;
					}
				}
				UserSave.board = null;
				mainFrame.dispose();
				mainFrame = null;
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						new StatisticWindow().display();
					}
				});
			}
		});
		leftButtons.get("options").addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
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
		leftButtons.get("quit").addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mainFrame.dispose();
				mainFrame = null;
				System.exit(0);
			}
		});
	}

	private void eraseSaveData(SaveManager[] managers)
			throws FileNotFoundException {
		for (SaveManager man : managers) {
			String filepath = man.getSavefile().getAbsolutePath();
			PrintWriter pw = new PrintWriter(filepath);
			pw.close();
		}
	}
}
