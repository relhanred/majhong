package Shanghai20.util.technical;

import java.util.LinkedList;
import java.util.List;

import Shanghai20.util.Contract;

public class Scoreboard {

    // CONSTANTES

    public static final String SEPARATOR = " - ";
    private static final int MAX_ENTRIES_NB = 10;

    // ATTRIBUTS

    private List<Entry> entries;

    // CONSTRUCTEURS

    public Scoreboard() {
        this.entries = new LinkedList<Entry>();
    }

    // REQUETES

    public String[] getEntries() {
        String[] res = new String[entries.size()];
        for (int i = 0; i < entries.size(); i++) {
            Entry e = entries.get(i);
            res[i] = e.getUsername() + SEPARATOR + e.getTime();
        }

        return res;
    }

    // COMMANDES

    /**
     * Met a jour ce tableau si et seulement si le temps newTime est meilleur,
     *  c'est-à-dire inférieur, à un temps dans ce tableau.
     * @pre
     *      newUser != null
     *      newTime != null
     *          && newTime est un temps valide
     */
    public void updateWithEntry(String newUser, String newTime) {
        Contract.checkCondition(newUser != null);
        Contract.checkCondition(newTime != null);
        Contract.checkCondition(TimeComparator.isValidTime(newTime));

        Entry newEntry = new Entry(newUser, newTime);
        int index = getIndexOfFirstWorseEntry(newEntry);
        entries.add(index, newEntry);
        this.setSizeTo(MAX_ENTRIES_NB);
    }

    // OUTILS

    /**
     * Cherche la première entrée de ce tableau qui est pire au sens de
     *  compareTo() que celle donnée en paramètre et renvoie son index.
     * @pre
     *      e != null
     * @post
     *      result == entries.size() ==> il n'existe aucune entrée pire que e
     *      result != entries.size() ==> result est l'index de la première
     *              entrée qui est pire que celle donnée en paramètre
     */
    private int getIndexOfFirstWorseEntry(Entry e) {
        assert (e != null);

        for (int i = 0; i < entries.size(); i++) {
            Entry e2 = entries.get(i);
            if (e2.compareTo(e) > 0) {
                return i;
            }
        }
        return entries.size();
    }

    /**
     * Enlève des entrées à ce tableau jusqu'à ce que sa taille soit newSize.
     * @post
     *      entries.size() == newSize
     */
    private void setSizeTo(int newSize) {
        assert (newSize > 0);

        if (entries.size() > newSize) {
            for (int i = newSize; i < entries.size(); i++) {
                entries.remove(entries.get(i));
            }
        }
    }

    /**
     * Modélise une entrée non mutable dans ce tableau.
     */
    private static class Entry implements Comparable<Entry> {

        // ATTRIBUTS

        private String username;
        private String time;

        // CONSTRUCTEURS

        public Entry(String username, String time) {
            this.username = username;
            this.time = time;
        }

        // REQUETES

        public String getUsername() {
            return username;
        }

        public String getTime() {
            return time;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Entry)) {
                return false;
            }

            Entry other = (Entry) o;
            return other.canEquals(this)
                    && other.getTime().equals(time)
                    && other.getUsername().equals(username);
        }

        public boolean canEquals(Object other) {
            return other instanceof Entry;
        }

        @Override
        public int compareTo(Entry o) {
            String otherTime = o.getTime();

            return new TimeComparator().compare(time, otherTime);
        }
    }
}
