package Shanghai20.util.saves;

import Shanghai20.util.Config;
import Shanghai20.util.Contract;
import Shanghai20.util.technical.saves.QuickSaveManager;

import java.io.File;
import java.io.IOException;

public abstract class AbstractSaveManager<E> implements SaveManager<E> {

    // ATTRIBUTS

    private File savefile;
    private E target;

    // CONSTRUCTEURS

    public AbstractSaveManager() {
        // rien
    }

    public AbstractSaveManager(String filepath) {
        createPath();

        String pathname =  filepath + Config.SAVESEXT;
        File test =  new File(pathname);
        try {
            if (!test.exists()) test.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        savefile = new File(pathname);
    }

    // REQUETES

    public E getTarget() {
        return target;
    }

    public File getSavefile() {
        return savefile;
    }

    public boolean hasTarget() {
        return target != null;
    }

    public boolean hasSavefile() {
        return savefile != null && savefile.exists();
    }

    // COMMANDES

    public void setTarget(E newTarget) {
        Contract.checkCondition(newTarget != null);

        this.target = newTarget;
    }

    public void setSavefile(File newSaveFile) {
        Contract.checkCondition(newSaveFile != null);

        this.savefile = newSaveFile;
    }

    /**
     * Ces méthodes sont propres à chaque classe dérivant celle-ci.
     */
    public abstract boolean save();
    public abstract boolean load();

    public static void createFolderAndFiles(String folderpath)
            throws IOException {
        // On vérifie l'existence du dossier
        File f = new File(folderpath);
        if (!f.exists()) f.mkdir();

        // On vérifie l'existence des fichiers
        for (String name : Config.SAVENAMES) {
            String filepath = folderpath + Config.FILE_SEPARATOR + name;
            f = new File(filepath);
            if (!f.exists()) f.createNewFile();
        }
    }

    // OUTILS

    private void createPath() {
        createGameFolders();

        try {
            createFolderAndFiles(QuickSaveManager.FOLDERPATH);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void createGameFolders() {
        File f = new File(Config.GAMEFOLDER);
        if (!f.exists()) f.mkdir();

        f = new File(Config.SAVESFOLDER);
        if (!f.exists()) f.mkdir();
    }
}
