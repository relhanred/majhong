package Shanghai20.view.extendable;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;

import Shanghai20.util.Config;

public class CadreComboBox extends JComboBox {

	// CONSTANTES
	
	private static final long serialVersionUID = 1L;
	
	// ATTRIBUTS
	
	private Color bgColor;
	private Font font;
	
	// CONSTRUCTEUR
	
	public CadreComboBox() {
		super();
		bgColor = Config.COLORBUTTON;
		font = new Font("Arial", Font.BOLD, 20);
		setFont(font);
		setForeground(Color.BLACK);
		setRenderer(new ComboBoxRenderer());
		setMaximumRowCount(3);
		setPreferredSize(new Dimension(200, 35));
		setBackground(bgColor);
		Border b = BorderFactory.createLineBorder(bgColor.darker(), 3);
		setBorder(b);
	}
	
	public CadreComboBox(ComboBoxModel model) {
		this();
		setModel(model);
	}
	
	public CadreComboBox(final Object[] items) {
		this();
		setModel(new DefaultComboBoxModel(items));
	}

	// COMMANDES
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(getBackground());
	}
	
	// CLASSE IMBRIQUEE
	
	private class ComboBoxRenderer extends JLabel implements ListCellRenderer {
		
		// CONSTANTE
		
		private static final long serialVersionUID = 1L;
		
		// ATTRIBUT
		
		private DefaultListCellRenderer delegate;
		
		// CONSTRUCTEUR

		public ComboBoxRenderer() {
			delegate = new DefaultListCellRenderer();
		}

		// COMMANDE
		
		@Override
		public Component getListCellRendererComponent(JList list, Object value, 
				int index, boolean isSelected, boolean cellHasFocus) {
			JLabel result = (JLabel) delegate.getListCellRendererComponent(list, 
					value, index, isSelected, cellHasFocus);
	    	result.setFont(font);
	    	result.setForeground(Color.BLACK);
			if (isSelected) {
				result.setBackground(bgColor.brighter());
			}
			if (cellHasFocus) {
				result.setBackground(Color.RED);
			}
			return result;
		}
		
	}
}
