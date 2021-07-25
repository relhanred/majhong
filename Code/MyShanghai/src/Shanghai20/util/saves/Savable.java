package Shanghai20.util.saves;

/**
 * Cette interface définit les objets qui ne sont pas sérialisables mais
 * peuvent être sauvegardés.
 */
public interface Savable {

    // REQUETES

    /**
     * Décrit l'état actuel de l'objet.
     */
    String describe();
}
