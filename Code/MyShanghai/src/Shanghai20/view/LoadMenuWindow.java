package Shanghai20.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import Shanghai20.GameState;
import Shanghai20.UserSave;
import Shanghai20.util.Config;
import Shanghai20.util.technical.saves.GameStateManager;
import Shanghai20.util.technical.saves.TimerSaveManager;
import Shanghai20.view.extendable.JBackground;
import Shanghai20.view.extendable.CadreComboBox;
import Shanghai20.view.extendable.OvalButton;

public class LoadMenuWindow {

    // ATTRIBUTS

    private JFrame mainFrame;
    private OvalButton backButton;
    private OvalButton validate;
    private CadreComboBox localSaves;
    private JButton browse;

    // CONSTRUCTEURS

    public LoadMenuWindow() {
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
        final int width = 450;
        final int height = 250;
        mainFrame = new JFrame(UserSave.lang.getString("browser"));
        mainFrame.setPreferredSize(new Dimension(width, height));

        localSaves = new CadreComboBox(getAllSaveFiles());
        browse = new OvalButton(UserSave.lang.getString("browser"));

        backButton = new OvalButton(UserSave.lang.getString("back"));
        validate = new OvalButton(UserSave.lang.getString("validate"));
        validate.setEnabled(!localSaves.getSelectedItem().equals(""));
    }

    private void placeComponents() {
        mainFrame.setContentPane(new JBackground());
        JPanel n = new JPanel(); {
            Font font = new Font("Arial", Font.BOLD, 40);
            JLabel label = new JLabel(UserSave.lang.getString("load"));
            label.setFont(font);
            label.setForeground(Config.COLORLABEL.brighter());
            n.add(label, FlowLayout.LEFT);
            n.setOpaque(false);
        }
        mainFrame.add(n, BorderLayout.NORTH);

        JPanel p = new JPanel(); {
            JPanel center = new JPanel(new GridLayout(3, 2)); {
                center.add(localSaves);
                center.add(new JLabel());
                center.add(browse);
                center.setOpaque(false);
            }
            p.add(center);
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

        localSaves.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validate.setEnabled(localSaves.getSelectedItem() != null);
            }
        });

        browse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jf = new JFileChooser(Config.SAVESFOLDER);
                jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                jf.setAcceptAllFileFilterUsed(false);

                int retval = jf.showOpenDialog(mainFrame);
                if (retval == JFileChooser.APPROVE_OPTION) {
                    File f = jf.getSelectedFile();
                    if (!f.exists() || !f.isDirectory()) {
                        throw new AssertionError();
                    }

                    loadFromChoice(f.getAbsolutePath());
                }
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

        validate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String choice = (String) localSaves.getSelectedItem();
                loadFromChoice(Config.SAVESFOLDER + Config.FILE_SEPARATOR + choice);

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

    private void loadFromChoice(String choice) {
        if (isSaveFolder(choice)) {
            // On charge le choix de l'utilisateur
            GameStateManager gsm = new GameStateManager(choice);
            TimerSaveManager tsm = new TimerSaveManager(choice);

            if (!gsm.load()) throw new AssertionError();
            if (!tsm.load()) throw new AssertionError();
            GameState gs = gsm.getTarget();
            gs.update();
            if (UserSave.timer != null
                    && !UserSave.timer.hasStopped()) {
                UserSave.timer.stop();
            }
            UserSave.timer = tsm.getTarget();

            // On lance le jeu
            mainFrame.dispose();
            mainFrame = null;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new GameWindow().display();
                }
            });
        } else {
            CadreOptionPane.showInformationDialog(
                    UserSave.lang.getString("invalidFormat"),
                    UserSave.lang.getString("invalidTitle"));
        }
    }

    private String[] getAllSaveFiles()  {
        File dir = new File(Config.SAVESFOLDER);
        File[] contents = dir.listFiles();

        StringBuilder acc = new StringBuilder();
        for (File f : contents) {
            String name = f.getName();
            if (f.isDirectory() && !name.equals("quicksave")) {
                acc.append(f.getName() + ";");
            }
        }
        return acc.toString().split(";");
    }

    private boolean isSaveFolder(String folderpath) {
        File folder = new File(folderpath);
        File[] files = folder.listFiles();
        if (files == null) return false;

        for (File f : files) {
            String name = f.getName();
            if (!name.equals("bo" + Config.SAVESEXT)
                    && !name.equals("tm" + Config.SAVESEXT)
                    && !name.equals("gs" + Config.SAVESEXT)) {
                return false;
            }
        }
        return true;
    }
}
