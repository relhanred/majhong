package Shanghai20.util.saves;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Une instance de cette classe permet d'analyser le contenu d'un fichier de
 * sauvegarde, pourvu que ce-dernier suive le format suivant :
 *
 * prop1 = valProp1
 * prop2 = valProp2
 * ...
 * propN = valPropN
 */
public abstract class SaveFileAnalyzer {

    // REQUETES

    public static String getValueOfProperty(URL file, String propertyName)
            throws IOException {
        assert (propertyName != null && propertyName.length() > 0);

        BufferedReader br =
                new BufferedReader(new InputStreamReader(file.openStream()));
        try {
            String line = br.readLine();
            while(line != null) {
                String[] content = line.split(" = ");

                if (content[0].equals(propertyName) && content.length == 2) {
                    return content[1];
                }
                line = br.readLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            br.close();
        }
        return null;
    }

    public static int getNumericValueOf(URL file, String propertyName)
            throws IOException, NumberFormatException {
        String val = getValueOfProperty(file, propertyName);
        if (val == null) throw new IOException();

        return Integer.parseInt(val);
    }

    public static long getLongNumericValueOf(URL file, String propertyName)
            throws IOException, NumberFormatException {
        String val = getValueOfProperty(file, propertyName);
        if (val == null) throw new IOException();

        return Long.parseLong(val);
    }
}
