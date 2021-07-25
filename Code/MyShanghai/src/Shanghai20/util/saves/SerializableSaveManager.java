package Shanghai20.util.saves;

import Shanghai20.util.Contract;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Une instance de cette classe permet de sauvegarder et charger des objets
 * dont les états sont décrits dans un fichier dont l'extension est ".svf".
 */
public class SerializableSaveManager<E extends Serializable> extends AbstractSaveManager<E> {

    // CONSTRUCTEURS

    public SerializableSaveManager() {
        super();
    }

    public SerializableSaveManager(String filepath) {
        super(filepath);
    }

    // COMMANDES

    public boolean save() {
        Contract.checkCondition(this.hasTarget());
        Contract.checkCondition(this.hasSavefile());

        try {
            OutputStream printStream = new FileOutputStream(getSavefile());
            ObjectOutputStream oos = new ObjectOutputStream(printStream);
            oos.writeObject(getTarget());
            oos.flush();

            printStream.close();
            oos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean load() {
        Contract.checkCondition(this.hasSavefile());

        try {
            InputStream readStream = new FileInputStream(getSavefile());
            InputStream buffer = new BufferedInputStream(readStream);
            ObjectInputStream ois = new ObjectInputStream(buffer);
            this.setTarget((E) ois.readObject());

            readStream.close();
            ois.close();
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}
