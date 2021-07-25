package Shanghai20.view.extendable;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.Border;

import Shanghai20.UserSave;
import Shanghai20.util.Config;

public class CadreTextField extends JTextField {

	// CONSTANTE
	
	private static final long serialVersionUID = 1L;
	
	// ATTRIBUTS
	
	private Color bgColor;
	private Color placeHolder;
	
	// CONSTRUCTEUR
	
	public CadreTextField(int w, int h) {
		super();
		bgColor = Config.COLORBUTTON;
		placeHolder = Color.DARK_GRAY;
		setPreferredSize(new Dimension(w, h));
		Font font = new Font("Arial", Font.BOLD, 20);
		setFont(font);
		setHorizontalAlignment(JTextField.CENTER);
		setForeground(Color.BLACK);
		setBackground(bgColor);
		Border b = BorderFactory.createLineBorder(bgColor.darker(), 3);
		setBorder(b);
	}
	
	// REQUETE
	
	@Override
	public String getText() {
		if (getForeground() == placeHolder || super.getText().equals("")) {
			return null;
		}
		return super.getText();
	}
	
	// COMMANDES
	
	protected void paintComponent(Graphics g) {
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
		super.paintComponent(g);
	}
	
	public void placeHolderUsername() {
		setForeground(placeHolder);
		setText(UserSave.lang.getString("username"));
		setCaretPosition(0);
		this.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				
				if (getForeground() == placeHolder) {
					setForeground(Color.BLACK);
					setText("");
				} else if (getText() == null || getText().equals("")) {
					e.consume();
					setForeground(placeHolder);
					setText(UserSave.lang.getString("username"));
					setCaretPosition(0);
				}
				if (e.getKeyChar() == ' ') {
					e.consume();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
			
		});
	}
	
	public void errorValue() {
		Border b = BorderFactory.createLineBorder(Color.RED, 3);
		setBorder(b);
	}
}
