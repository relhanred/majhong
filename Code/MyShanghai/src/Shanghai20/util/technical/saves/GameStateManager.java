package Shanghai20.util.technical.saves;

import Shanghai20.GameState;

import Shanghai20.util.Config;
import Shanghai20.util.Contract;
import Shanghai20.util.saves.SerializableSaveManager;

public class GameStateManager extends SerializableSaveManager<GameState> {

    // CONSTANTES

    public static final String FILENAME = "gs" + Config.SAVESEXT;

    // CONSTRUCTEURS

    public GameStateManager() {
        super();
    }

    public GameStateManager(String folderpath) {
        super(folderpath + Config.FILE_SEPARATOR + "gs");
    }

    // COMMANDES

    @Override
    public boolean save() {
        Contract.checkCondition(hasTarget());
        Contract.checkCondition(hasSavefile());

        if (!super.save()) return false;
        return saveBoard();
    }

    @Override
    public boolean load() {
        Contract.checkCondition(hasSavefile());

        if (!super.load()) return false;
        return loadBoard();
    }

    // OUTILS

    private boolean saveBoard() {
        String path = getPath();
        BoardSaveManager bsm = new BoardSaveManager(path);
        bsm.setTarget(this.getTarget().getBoard());
        if(!bsm.save()) throw new AssertionError("Board saving failed");
        return true;
    }

    private boolean loadBoard() {
        String path = getPath();
        BoardSaveManager bsm = new BoardSaveManager(path);
        if(!bsm.load()) throw new AssertionError("Board loading failed");
        this.getTarget().setBoard(bsm.getTarget());
        return true;
    }

    protected String getPath() {
        String path = this.getSavefile().toURI().getPath();
        String[] folders = path.replace(Config.FILE_SEPARATOR, "/").split("/");

        StringBuilder res = new StringBuilder();
        if (folders[0].equals("")) res.append("/"); // Test pour la compabilité avec Linux
        for (int i = 1; i < folders.length - 1; i++) {
            res.append(folders[i]);
            if (i != folders.length - 2) {
                res.append(Config.FILE_SEPARATOR);
            }
        }
        return res.toString();
    }
}
