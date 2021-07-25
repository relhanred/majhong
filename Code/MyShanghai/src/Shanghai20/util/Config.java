package Shanghai20.util;

import java.awt.Color;
import java.net.URL;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Shanghai20.Shanghai;
import Shanghai20.controller.Board;
import Shanghai20.util.technical.saves.BoardSaveManager;
import Shanghai20.util.technical.saves.GameStateManager;
import Shanghai20.util.technical.saves.ShapeLoader;
import Shanghai20.util.technical.saves.TimerSaveManager;

public final class Config {

	// CONSTANTES

	/**
	 * Chaîne de caractère qui permet à l'utilisateur de parcourir des fichiers.
	 */
	public static final String BROWSER = "<...>";

	/**
	 * Les thèmes par défaut du projet.
	 */
	public static final String[] DEFAULTTHEMES = {
			"themes/Original.png",
			"themes/OriginalInv.png",
			"themes/Egypte.png",
			"themes/Plage.png",
			BROWSER
	};

	/**
	 * Le séparateur de chemins du système.
	 */
	public static final String FILE_SEPARATOR = System.getProperty("file.separator");

	/**
	 * Le dossier par défaut du jeu.
	 */
	public static final String GAMEFOLDER = System.getProperty("user.home")
			+ FILE_SEPARATOR + "Shanghai";

	/**
	 * Le dossier par défaut des formes de plateau.
	 */
	public static final String SHAPESFOLDER = "shapes/";

	/**
	 * Le dossier par défaut des sauvegardes.
	 */
	public static final String SAVESFOLDER = GAMEFOLDER +  FILE_SEPARATOR
			+ "saves";

	/**
	 * Les noms des fichiers de sauvegarde.
	 */
	public static final String[] SAVENAMES = {GameStateManager.FILENAME,
			TimerSaveManager.FILENAME, BoardSaveManager.FILENAME};

	/**
	 * L'extension des fichiers décrivant les formes de plateau.
	 */
	public static final String SHAPESSEXT = ".svf";

	/**
	 * L'entension des fichiers de sauvegarde.
	 */
	public static final String SAVESEXT = ".svf";

	/**
	 * Toutes les formes de plateau par défaut du projet.
	 */
	public static final String[] DEFAULTSHAPES = {
			"shapesImg/Pyramide.png",
			"shapesImg/V.png",
			"shapesImg/Towers.png",
			"shapesImg/Turtle.png",
			"shapesImg/Cyclone.png",
			"shapesImg/Bridge.png"
	};

	/**
	 * Tous les arrière-plan par défaut du projet.
	 */
	public static final String[] DEFAULTBACKGROUNDS = {
			"backgrounds/default.jpg",
			"backgrounds/future.png",
			"backgrounds/wood.jpg",
			"backgrounds/pyramids.jpg",
			"backgrounds/beach.jpg",
			BROWSER
	};

	/**
	 * Chemin d'accés du dossier de langues.
	 */
	public static final String PATHLANG = "Shanghai20/languages/langue";

	/**
	 * Couleur des boutons.
	 */
	public static final Color COLORBUTTON = new Color(6, 102, 96).brighter();

	/**
	 * Couleur des labels.
	 */
	public static final Color COLORLABEL = new Color(6, 102, 96);


	/**
	 * Tableau du nombres de symboles différents (ce qui représente la
	 * 	difficulté).
	 */
	public static final int[] DIFFICULTY = {10, 22, 34};

	/**
	 * chemin d'accés de la musique de fond.
	 */
	public static final URL MUSICTHEMEPATH =
			Shanghai.class.getResource("sounds/theme.wav");

	/**
	 * Permet de vérifier la validité d'un nom de fichier de sauvegarde.
	 */
	public static final Pattern SVFNAMECHECK =
			Pattern.compile("^([a-zA-Z0-9]+)");

	/**
	 * Permet d'extraire le nom d'un fichier à partir du son chemin absolu.
	 */
	private static final Pattern EXTRACTNAME =
			Pattern.compile(".*/([^/]*)\\.[a-z]{3,4}$");

	// REQUETES

	/**
	 * Renvoie le nom d'un fichier à partir de son chemin absolu.
	 * @pre
	 * 		file != null
	 */
	public static String getName(String file) {
		Contract.checkCondition(file != null);

		String name = null;
		Matcher m = EXTRACTNAME.matcher(file);
		if (m.reset(file).matches()) {
			name = m.group(1);
		} else {
			name = file;
		}
		return name;
	}

	/**
	 * Renvoie un tableau des noms des fichiers à partir d'un tableau de chemin
	 * 	absolu.
	 * @pre
	 * 		file != null
	 */
	public static String[] getNames(String[] files) {
		String[] names = new String[files.length];
		for (int i = 0; i < files.length; ++i) {
			names[i] = getName(files[i]);
		}
		return names;
	}

	/**
	 * Renvoie vrai si c'est un fichier du projet, faux sinon.
	 * @pre
	 * 		path != null
	 */
	public static boolean isURL(Object path) {
		Contract.checkCondition(path != null);

		return (path instanceof URL);
	}

	// COMMANDES

	/**
	 * Crée un plateau de jeu avec une <code>difficulty</code> qui représente
	 * 	l'indice du tableau DIFFICULTY.
	 * @pre
	 * 		0 <= difficulty < len(DIFFICULTY)
	 * @post
	 * 		board != null
	 */
	public static Board createBoard(URL pathShape, int difficulty) {
		Contract.checkCondition(pathShape != null && difficulty >= 0);
		Contract.checkCondition(difficulty < Config.DIFFICULTY.length);

		ShapeLoader loader = new ShapeLoader(pathShape);
		LinkedList<Triplet> triplets = new LinkedList<Triplet>();
		Board board = null;
		try {
			loader.load();
			board = loader.getTarget();
			triplets.addAll(loader.getAllTiles());
		} catch (Exception e) {
			e.printStackTrace();
		}

		board.createBoard(triplets, Config.DIFFICULTY[difficulty]);
		return board;
	}
}
