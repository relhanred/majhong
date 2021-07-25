package Shanghai20.util.saves;

import java.io.File;

public interface SaveManager<E> {

    // REQUETES

    /**
     * Renvoie l'objet courant.
     */
    E getTarget();

    /**
     * Renvoie le fichier de sauvegarde.
     */
    File getSavefile();

    /**
     * Indique si un objet a été défini.
     */
    boolean hasTarget();

    /**
     * Inidique si le fichier de sauvegarde a été spécifié.
     */
    boolean hasSavefile();

    // COMMANDES

    /**
     * Change l'objet courant.
     * @pre
     *      newTarget != null
     * @post
     *      getTarget() == newTarget
     *      getSavefile() == old getSavefile()
     */
    void setTarget(E newTarget);

    /**
     * Change le fichier courant.
     * @pre
     *      newSvf != null
     * @post
     *      getTarget() == old getTarget()
     *      getSavefile() == newSvf
     */
    void setSavefile(File newSvf);

    /**
     * Enregistre l'objet courant dans le fichier.
     * @pre
     *      hasTarget()
     *      hasSavefile()
     */
    boolean save();

    /**
     * Charge l'objet sauvegardé dans le fichier.
     * @pre
     *      hasSavefile()
     */
    boolean load();
}
