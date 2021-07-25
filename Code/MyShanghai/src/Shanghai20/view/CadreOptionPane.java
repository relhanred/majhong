package Shanghai20.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Shanghai20.UserSave;
import Shanghai20.util.Config;
import Shanghai20.util.Contract;
import Shanghai20.view.extendable.CadreTextField;
import Shanghai20.view.extendable.JBackground;
import Shanghai20.view.extendable.OvalButton;

public class CadreOptionPane {
	
	// CONSTANTES
	
	public final static int INFORMATION_MESSAGE = 1;
	public final static int YES_NO_CANCEL_OPTION = 2;
	public final static int YES_NO_OPTION = 3;
	public final static int YES_CANCEL_OPTION = 4;
	
	public final static int YES_OPTION = 1;
	public final static int NO_OPTION = 2;
	public final static int CANCEL_OPTION = 3;
	
	private final static int DEFAULT_OPTION = -1;
	
	// ATTRIBUT
	
	/**
	 * Valeur retourné par <code>showConfirmDialog</code>. Il vaut :
	 *  - YES_OPTION,
	 *  - NO_OPTION,
	 *  - CANCEL_OPTION,
	 *  - DEFAULT_OPTION.
	 */
	private static int typeReturn = DEFAULT_OPTION;
	
	/**
	 * Boîte de dialogue.
	 */
	private static JDialog dialog;
	
	// COMMANDE
	
	/**
	 * Affiche un message d'information.
	 * @param message: message à afficher dans la fenêtre.
	 * @param title: message à afficher dans la barre de titre de la fenêtre.
	 */
	public static void showInformationDialog(String message, String title) {
		Contract.checkCondition(message != null && title != null);
		
		JLabel label = new JLabel(message);
		label.setFont(new Font("Arial", Font.BOLD, 30));
		label.setForeground(Config.COLORLABEL);
		showOptionDialog(label, title, INFORMATION_MESSAGE, null);
	}
	
	/**
	 * Affiche un message d'information.
	 * @param panel: un component.
	 * @param title: message à afficher dans la barre de titre de la fenêtre.
	 */
	public static void showInformationDialog(Component panel, String title) {
		Contract.checkCondition(panel != null && title != null);
		
		showOptionDialog(panel, title, INFORMATION_MESSAGE, null);
	}
	
	/**
	 * Affiche une fenêtre de dialogue de confirmation.
	 * @param message: message à afficher dans la fenêtre.
	 * @param title: message à afficher dans la barre de titre de la fenêtre.
	 * @param messageType: type de la fenêtre de dialogue égale à :
	 *  	- INFORMATION_MESSAGE,
	 *  	- YES_NO_CANCEL_OPTION,
	 *   	- YES_NO_OPTION.
	 * @return le bouton appuyé par l'utiliateur : 
	 *  	- YES_OPTION,
	 *  	- NO_OPTION,
	 *  	- CANCEL_OPTION,
	 *  	- DEFAULT_OPTION.
	 */
	public static int showConfirmDialog(String message, String title, 
			int messageType) {
		Contract.checkCondition(message != null && title != null);
		
		JLabel label = new JLabel(message);
		label.setFont(new Font("Arial", Font.BOLD, 30));
		label.setForeground(Config.COLORLABEL);
		return showOptionDialog(label, title, messageType, null);
	}
	
	/**
	 * Affiche une fenêtre de dialogue avec un champ de texte.
	 * @param message: message à afficher dans la fenêtre.
	 * @param title: message à afficher dans la barre de titre de la fenêtre.
	 * @return la chaîne de caractère entré si l'utilisateur à appuyé sur le 
	 * 		bouton de confirmation, null sinon.
	 */
	public static String showInputDialog(String message, String title) {
		Contract.checkCondition(message != null && title != null);
		
		final CadreTextField text = new CadreTextField(250, 40);
		JPanel p = new JPanel(new BorderLayout()); {
			JLabel label = new JLabel(message);
			label.setFont(new Font("Arial", Font.BOLD, 30));
			label.setForeground(Config.COLORLABEL);
			p.add(label, BorderLayout.NORTH);
			p.add(text, BorderLayout.SOUTH);
			p.setOpaque(false);
		}
		ActionListener yesListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (text.getText() == null) {
					text.errorValue();
				} else {
					typeReturn = YES_OPTION;
					dialog.dispose();
					dialog = null;
				}
			}
		};
		if (showOptionDialog(p, title, YES_CANCEL_OPTION, 
				yesListener) == YES_OPTION) {
			return text.getText();
		}
		return null;
	}
    
    // OUTILS
    
	private static int showOptionDialog(Component message, String title, 
			int messageType, ActionListener customYes) {
		Object[] options = frameType(messageType, customYes);
		dialog = new JDialog((JFrame) null, title, true);
		createView();
		placeComponents(message, options);
		createController();
		display();
		return typeReturn;
	}
	
    private static void display() {
    	dialog.pack();
    	dialog.setLocationRelativeTo(null);
    	dialog.setVisible(true);
    }
    
    private static void createView() {
    	dialog.setResizable(false);
		dialog.setAlwaysOnTop(true);
    }
    
	private static void placeComponents(Component component, Object[] options) {
		JBackground pane = new JBackground(new BorderLayout()); {
			JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER)); {
				p.add(component);
				p.setOpaque(false);
				p.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
			}
			pane.add(p, BorderLayout.CENTER);
			JPanel q = new JPanel(new GridLayout(1, 0, 5, 5)); {
				q.add(new JLabel(" "));
				for (Object o : options) {
					q.add((Component) o);
				}
				q.add(new JLabel(" "));
				q.setOpaque(false);
				q.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
			}
			pane.add(q, BorderLayout.SOUTH);
			pane.setOpaque(false);
		}
		dialog.add(pane);
	}
	
	private static void createController() {
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	/**
	 * Crée une liste d'object qui compose le bas de la fenêtre de dialogue.
	 * @param messageType: correspond au type de la fenêtre de dialogue.
	 * @return null si <code>messageType</code> n'est pas un type reconnu ou 
	 * 	correspond à <code>DEFAULT_OPTION</code>
	 */
	private static Object[] frameType(int messageType, ActionListener customYes) {
		Object[] objects = null;
		ActionListener yesListener = actionListener(YES_OPTION);
		if (customYes != null) {
			yesListener = customYes;
		}
		switch(messageType) {
		case INFORMATION_MESSAGE:
			objects = new Object[] {
				new OvalButton(UserSave.lang.getString("OK"), 
						actionListener(DEFAULT_OPTION))
			};
			break;
		case YES_NO_CANCEL_OPTION:
			objects = new Object[] {
				new OvalButton(UserSave.lang.getString("CANCEL"), 
						actionListener(CANCEL_OPTION)),
				new OvalButton(UserSave.lang.getString("NO"), 
						actionListener(NO_OPTION)),
				new OvalButton(UserSave.lang.getString("YES"), yesListener)
			};
			break;
		case YES_NO_OPTION:
			objects = new Object[] {
				new OvalButton(UserSave.lang.getString("NO"), 
						actionListener(NO_OPTION)),
				new OvalButton(UserSave.lang.getString("YES"), yesListener)
			};
			break;
		case YES_CANCEL_OPTION:
			objects = new Object[] {
					new OvalButton(UserSave.lang.getString("CANCEL"), 
							actionListener(CANCEL_OPTION)),
					new OvalButton(UserSave.lang.getString("YES"), yesListener)
			};
			break;
		default:
			break;
		}
		
		return objects;
	}
	
	/**
	 * Cette partie correspond au retour des <code>showConfirmDialog</code>.
	 * @param messageButton: correspond au type d'un certain bouton : 
	 *  	- YES_OPTION,
	 *   	- NO_OPTION,
	 *   	- CANCEL_OPTION,
	 *   	- DEFAULT_OPTION (correspond aux fenêtres d'informations qui ne 
	 *   		retournent rien).
	 * @return un ActionListner qui ne se contente qu'à assigner à 
	 * 	<code>typeReturn</code> la valeur que doit retourner le bouton (si il 
	 * 	est cliqué), puis de fermer la fenêtre de dialogue.
	 */
	private static ActionListener actionListener(int messageButton) {
		ActionListener l = null;
		switch(messageButton) {
		case YES_OPTION:
			l = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					typeReturn = YES_OPTION;
					dialog.dispose();
					dialog = null;
				}
			};
			break;
		case NO_OPTION:
			l = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					typeReturn = NO_OPTION;
					dialog.dispose();
					dialog = null;
				}
			};
			break;
		case CANCEL_OPTION:
			l = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					typeReturn = CANCEL_OPTION;
					dialog.dispose();
					dialog = null;
				}
			};
			break;
		default:
			l = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					typeReturn = DEFAULT_OPTION;
					dialog.dispose();
					dialog = null;
				}
			};
		}
		return l;
	}
}
