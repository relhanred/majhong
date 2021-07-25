package Shanghai20.util.technical.saves;

import Shanghai20.UserSave;
import Shanghai20.util.Config;
import Shanghai20.util.Contract;
import Shanghai20.util.saves.SavableSaveManager;
import Shanghai20.util.saves.SaveFileAnalyzer;
import Shanghai20.util.technical.StopWatch;

import java.io.IOException;
import java.net.URL;


public class TimerSaveManager extends SavableSaveManager<StopWatch> {

    // CONSTANTES

    public static final String FILENAME = "tm" + Config.SAVESEXT;

    // CONSTRUCTEURS

    public TimerSaveManager() {
        super();
    }

    public TimerSaveManager(String folderpath) {
        super(folderpath + Config.FILE_SEPARATOR + "tm");
    }

    // COMMANDES

    public void setTarget() {
        super.setTarget(UserSave.timer);
    }

    @Override
    public boolean load() {
        Contract.checkCondition(hasSavefile());

        try {
            URL u = getSavefile().toURI().toURL();
            long val =
                    SaveFileAnalyzer.getLongNumericValueOf(u, "time");
            this.setTarget(new StopWatch(val));
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
}
