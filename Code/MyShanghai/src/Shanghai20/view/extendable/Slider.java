package Shanghai20.view.extendable;

import java.awt.Color;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JSlider;

import Shanghai20.util.Config;
import Shanghai20.util.Contract;

public class Slider {
	
	// ATTRIBUTS
	
	private JSlider model;
	private Color color;
	
	// CONSTRUCTEUR

	public Slider(int tick) {
		Contract.checkCondition(tick > 0);
		color = Config.COLORBUTTON;
		model = new JSlider();
		model.setMaximum(tick);
		model.setPaintTicks(true);
		model.setBackground(color.brighter());
		model.setForeground(color.brighter());
		model.setOpaque(false);
	}
	
	public Slider(int tick, int interval, int begin) {
		this(tick);
		model.setMajorTickSpacing(interval);
		model.setPaintLabels(true);
		Hashtable<Integer, JLabel> pos = new Hashtable<Integer, JLabel>();
		for (int k = begin; k <= tick; k += interval) {
			JLabel label = new JLabel(String.valueOf(k));
			label.setForeground(color);
			pos.put(k, label);
		}
		model.setLabelTable(pos);
	}
	
	// ATTRIBUTS
	
	public JSlider getSlider() {
		return model;
	}
	
	// COMMANDES
}
