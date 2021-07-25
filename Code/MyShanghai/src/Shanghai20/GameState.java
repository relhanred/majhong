package Shanghai20;

import Shanghai20.controller.Board;
import Shanghai20.controller.StdBoard;
import Shanghai20.model.Stage;
import Shanghai20.model.Tile;
import Shanghai20.util.Config;
import Shanghai20.util.Contract;
import Shanghai20.util.Coord;
import Shanghai20.util.Triplet;
import Shanghai20.util.technical.StopWatch;

import java.io.Serializable;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

/**
 * Une instance de cette classe représente l'état du jeu à un moment donné.
 */
public class GameState implements Serializable {

    public static final long serialVersionUID = 1L;

    // ATTRIBUTS

    /**
     * Le plateau courant.
     */
    private Board board;

    /**
     * Pseudonyme du joueur courant.
     */
    private String pseudo;

    /**
     * Le thème courant.
     */
    private Object pathTheme;

    /**
     * L'arrière-plan courant.
     */
    private Object pathBackground;

    // CONSTRUCTEURS

    public GameState() {
        this.refresh();
    }

    // REQUETES

    public Board getBoard() {
        return this.board;
    }

    // COMMANDES

    public void setBoard(Board b) {
        Contract.checkCondition(b != null);

        this.board = b;
    }

    /**
     * Met à jour cette instance avec les informations d'UserSave.
     */
    public void refresh() {
        board = UserSave.board;
        pseudo = UserSave.pseudo;
        pathTheme = UserSave.pathTheme;
        pathBackground = UserSave.pathBackground;
    }

    /**
     * Met à jour UserSave avec les informations de cette instance.
     */
    public void update() {
        UserSave.board = this.board;
        UserSave.pseudo = this.pseudo;
        UserSave.pathTheme = this.pathTheme;
        UserSave.pathBackground = this.pathBackground;
    }
}
