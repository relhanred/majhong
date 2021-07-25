package Shanghai20.util.technical.saves;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import Shanghai20.controller.StdBoard;
import Shanghai20.util.Triplet;
import Shanghai20.util.saves.SaveFileAnalyzer;
import Shanghai20.util.saves.SerializableSaveManager;

public class ShapeLoader extends SerializableSaveManager<StdBoard> {

    // CONSTANTES

    protected URL savefile;

    // CONSTRUCTEURS

    public ShapeLoader(URL file) {
        this.savefile = file;
    }

    // REQUETES

    public String getPatternName() throws IOException {
        return SaveFileAnalyzer.getValueOfProperty(savefile, "name");
    }

    public int getNumberOfTiles() throws IOException {
        return SaveFileAnalyzer.getNumericValueOf(savefile, "nbTiles");
    }

    public int getNumberOfStages() throws IOException {
        return SaveFileAnalyzer.getNumericValueOf(savefile,"nbStage");
    }

    public int getMaxX() throws IOException {
        return SaveFileAnalyzer.getNumericValueOf(savefile,"maxX");
    }

    public int getMaxY() throws IOException {
        return SaveFileAnalyzer.getNumericValueOf(savefile,"maxY");
    }

    public List<Triplet> getAllTiles() throws IOException {
        final int stagesNb = getNumberOfStages();
        String[] properties = new String[stagesNb];
        for (int i = 0; i < stagesNb; i++) {
            properties[i] = "s" + (i + 1);
        }

        List<Triplet> res = new LinkedList<Triplet>();
        for (String prop : properties) {
            String val =
                    SaveFileAnalyzer.getValueOfProperty(savefile, prop);
            if (val == null)
                return null;

            String[] allVal = val.split(";");
            for (String content : allVal)
                res.add(Triplet.parseTriplet(content));
        }
        return res;
    }

    // COMMANDES

    public boolean save() {
        throw new UnsupportedOperationException();
    }

    public boolean load()  {
        try {
            this.setTarget(new StdBoard(this.getNumberOfStages(),
                    this.getMaxX(), this.getMaxY()));
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
}
