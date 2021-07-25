package Shanghai20;

import java.io.Serializable;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import Shanghai20.controller.Board;
import Shanghai20.controller.StdBoard;
import Shanghai20.util.Config;
import Shanghai20.util.technical.MusicPlayer;
import Shanghai20.util.technical.StopWatch;

public class UserSave implements Serializable {
	
	// CONSTANTES

	private static final long serialVersionUID = 1L;
	
	public static final MusicPlayer MUSICTHEMEPLAYER = 
			new MusicPlayer(Config.MUSICTHEMEPATH);
	
	// ATTRIBUTS
		
	/*
	 * Charge la langue par défault.
	 */
	public static ResourceBundle lang = 
			ResourceBundle.getBundle(Config.PATHLANG, Locale.getDefault());
	
	/**
	 * Chemin d'accés de l'arrière-plan du jeu.
	 */
	public static Object pathBackground = 
			Shanghai.class.getResource(Config.DEFAULTBACKGROUNDS[0]);
	
	/**
	 * Chemin d'accés du thème des tuiles.
	 */
	public static Object pathTheme = 
			Shanghai.class.getResource(Config.DEFAULTTHEMES[0]);
	
	/**
	 * Chemin d'accés de la forme du plateau.
	 */
	public static URL pathShape = null;
		
	/**
	 * Indique si les bruits sont coupés ou non.
	 */
	public static boolean isMuted = false;

	/**
	 * Le plateau courant.
	 */
	public static Board board = null;
	
	/**
	 * Temps de partie.
	 */
	public static StopWatch timer = null;
	
	/**
	 * Pseudonyme du joueur.
	 */
	public static String pseudo = null;
}
