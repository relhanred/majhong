package Shanghai20.util.saves;

import Shanghai20.util.Contract;
import java.io.FileWriter;
import java.io.BufferedWriter;


/**
 * Une instance d'une classe dérivant celle-ci est spécifique à un objet non
 * sérializable mais sauvegardable.
 * @see Savable
 */
public abstract class SavableSaveManager<E extends Savable>
        extends AbstractSaveManager<E> {

    // CONSTRUCTEURS

    public SavableSaveManager() {
        super();
    }

    public SavableSaveManager(String filepath) {
        super(filepath);
    }

    // COMMANDES

    public boolean save() {
        Contract.checkCondition(hasTarget());

        String data = getTarget().describe();
        try {
            FileWriter fw = new FileWriter(getSavefile());
            BufferedWriter br = new BufferedWriter(fw);
            br.write(data);
            br.flush();

            fw.close();
            br.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Cette méthode est propre à chaque classe dérivant celle-ci.
     */
    public abstract boolean load();
}
