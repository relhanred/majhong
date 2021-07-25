package Shanghai20.util;

import Shanghai20.util.exceptions.TripletFormatException;

import java.util.LinkedList;

public class Triplet {

    // ATTRIBUTS

    private final int first;
    private final int second;
    private final int third;

    // CONSTRUCTEURS

    public Triplet(int first, int second, int third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    // REQUETES

    /**
     * Retourne le premier indice du Triplet
     */
    public int getFirst() {
        return first;
    }

    /**
     * Retourne le deuxieme indice du Triplet
     */
    public int getSecond() {
        return second;
    }

    /**
     * Retourne le troisième indice du Triplet
     */
    public int getThird() {
        return third;
    }

    /**
     * Retourne une chaine de caractère affichant les valeurs du Triplet
     */
    public String getAll() {
        return "("+getFirst()+", "+getSecond()+", "+getThird()+")";
    }

    /**
     * Retourne true si le triplet est contenu dans la liste <code>L<code> false sinon
     */
    public boolean contains(LinkedList<Triplet> L) {
        for(Triplet t : L) {
            if (t.getFirst() == this.getFirst()
                    && t.getSecond() == this.getSecond()
                    && t.getThird() == this.getThird()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Redefinition de la méthode equals pour un Triplet
     */
    public boolean equals(Object o) {
        if (!(o instanceof Triplet))
            return false;

        Triplet other = (Triplet) o;
        return other.canEquals(this)
                && other.getFirst() == first
                && other.getSecond() == second
                && other.getThird() == third;
    }

    public boolean canEquals(Object o) {
        return o instanceof Triplet;
    }

    // COMMANDES

    public static Triplet parseTriplet(String t) throws TripletFormatException {
        // Séparation en 3 éléments
        String[] values = t.split(", ");
        if (values.length != 3) {
            throw new TripletFormatException();
        }

        // Isolation des contenus de chaque composante
        String[] first = values[0].split("\\(");
        String[] third = values[2].split("\\)");
        if (first.length > 2 || third.length > 2) {
            throw new TripletFormatException();
        }

        // Conversion en nombres
        int a, b, c;
        try {
            a = Integer.parseInt(first[1]);
            b = Integer.parseInt(values[1]);
            c = Integer.parseInt(third[0]);
        } catch (NumberFormatException ex) {
            throw new TripletFormatException(ex.getMessage());
        }

        return new Triplet(a, b, c);
    }
}
