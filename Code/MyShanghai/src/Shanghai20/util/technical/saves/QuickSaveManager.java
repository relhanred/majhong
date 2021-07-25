package Shanghai20.util.technical.saves;

import Shanghai20.util.Config;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * Cette classe est une instance particulière de GameStateManager qui se charge
 * de manipuler le fichier de reprise de partie.
 */
public class QuickSaveManager extends GameStateManager {

    // CONSTANTES

    public static final String FOLDERPATH = Config.SAVESFOLDER
            + Config.FILE_SEPARATOR + "quicksave";

    // CONSTRUCTEURS

    public QuickSaveManager() {
        super(FOLDERPATH);
    }

    // REQUETES

    public boolean hasGameInProgress() throws IOException {
        InputStream readStream = new FileInputStream(getSavefile());
        InputStreamReader is = new InputStreamReader(readStream);
        BufferedReader br = new BufferedReader(is);
        return br.readLine() != null;
    }
}
