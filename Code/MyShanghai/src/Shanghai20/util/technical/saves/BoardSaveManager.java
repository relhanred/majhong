package Shanghai20.util.technical.saves;

import Shanghai20.controller.Board;
import Shanghai20.controller.StdBoard;
import Shanghai20.model.StdStage;
import Shanghai20.model.StdTile;
import Shanghai20.model.Tile;
import Shanghai20.util.Config;
import Shanghai20.util.Contract;
import Shanghai20.util.Coord;
import Shanghai20.util.saves.SavableSaveManager;
import Shanghai20.util.saves.SaveFileAnalyzer;

import java.net.URL;


public class BoardSaveManager extends SavableSaveManager<Board> {

    // CONSTANTES

    public static final String FILENAME = "bo" + Config.SAVESEXT;

    // CONSTRUCTEURS

    public BoardSaveManager() {
        super();
    }

    public BoardSaveManager(String folderpath) {
        super(folderpath + Config.FILE_SEPARATOR + "bo");
    }

    // COMMANDES

    @Override
    public boolean load() {
        Contract.checkCondition(hasSavefile());

        if (!initializeBoard()) return false;
        try {
            URL u = this.getSavefile().toURI().toURL();
            Board target = this.getTarget();
            for (int i = 0; i < target.getNbStage(); i++) {
                String data = SaveFileAnalyzer.getValueOfProperty(u, "s" + (i + 1));
                if (data != null) {
                    String[] identifiers = data.split(StdStage.DESC_SEP);
                    for (String id : identifiers) {
                        Object[] res = new Splitter(id).split();
                        Tile t = (Tile) res[Splitter.TILE];
                        Coord c = (Coord) res[Splitter.COORD];
                        target.addTile(t, (i + 1), c.getX(), c.getY());
                    }
                }
            }
            this.setTarget(target);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    // OUTILS

    private boolean initializeBoard() {
        try {
            URL u = getSavefile().toURI().toURL();
            int nbStages = SaveFileAnalyzer.getNumericValueOf(u, "nbStages");
            int maxX = SaveFileAnalyzer.getNumericValueOf(u, "maxX");
            int maxY = SaveFileAnalyzer.getNumericValueOf(u, "maxY");
            this.setTarget(new StdBoard(nbStages, maxX, maxY));
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    private class Splitter {

        // CONSTANTES

        public static final int TILE = 0;
        public static final int COORD = 1;

        // ATTRIBUTS

        private String toSplit;

        // CONSTRUCTEURS

        public Splitter(String s) {
            this.toSplit = s;
        }

        // COMMANDES

        public Object[] split() {
            String[] data = toSplit.split(StdStage.DESC_INNER_SEP);
            return computeResult(data);
        }

        // OUTILS

        private Object[] computeResult(String[] s) {
            assert(s.length == 4);

            String[] first = s[0].split("\\(");
            String[] last = s[3].split("\\)");
            if (first.length != 2 || last.length != 1) {
                throw new AssertionError();
            }

            int id = 0, symbol = 0, x = 0, y = 0;
            try {
                id = Integer.parseInt(first[1]);
                symbol = Integer.parseInt(s[1]);
                x = Integer.parseInt(s[2]);
                y  = Integer.parseInt(last[0]);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
            Tile t = new StdTile(id, symbol);
            Coord c = new Coord(x, y);
            return new Object[] {t, c};
        }
    }
}
