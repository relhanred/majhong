package Shanghai20;

import javax.swing.SwingUtilities;

import Shanghai20.view.MainMenuWindow;

public class Shanghai {
	
	// POINT D'ENTREE
	
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainMenuWindow().display();
            }
        });
    }
}
