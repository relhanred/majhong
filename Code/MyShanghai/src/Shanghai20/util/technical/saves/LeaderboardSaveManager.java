package Shanghai20.util.technical.saves;

import Shanghai20.util.Config;
import Shanghai20.util.Contract;
import Shanghai20.util.saves.SavableSaveManager;
import Shanghai20.util.technical.Scoreboard;
import Shanghai20.view.gui.Leaderboard;

import java.io.*;

public class LeaderboardSaveManager extends SavableSaveManager<Leaderboard> {

    // CONSTANTES

    public static final String FILEPATH = Config.SAVESFOLDER
            + Config.FILE_SEPARATOR + "leaderboard";

    // CONSTRUCTEURS

    public LeaderboardSaveManager() {
        super(FILEPATH);
    }

    // COMMANDES

    public boolean load() {
        Contract.checkCondition(hasSavefile());

        this.setTarget(new Leaderboard());
        if (hasEntries()) {
            try {
                FileReader fr = new FileReader(getSavefile());
                BufferedReader br = new BufferedReader(fr);
                String line = br.readLine();
                fr.close();
                br.close();

                Leaderboard res = new Leaderboard();
                String[] allEntries = line.split(Leaderboard.SEPARATOR);
                for (String entry : allEntries) {
                    String[] data = entry.split(Scoreboard.SEPARATOR);
                    res.updateWithEntry(data[0], data[1]);
                }
                this.setTarget(res);
            } catch (Exception ex) {
                return false;
            }
        }

        return true;
    }

    // OUTILS

    private boolean hasEntries() {
        try {
            InputStream readStream = new FileInputStream(getSavefile());
            InputStreamReader is = new InputStreamReader(readStream);
            BufferedReader br = new BufferedReader(is);
            return br.readLine() != null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
