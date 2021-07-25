package Shanghai20.util.technical;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import Shanghai20.util.Contract;
import Shanghai20.util.saves.Savable;

/**
 * Modélise un chronomètre.
 * On ne peut demander à cet objet de commencer à compter
 * (<code>start()</code>) que si le chronomètre a été arrété
 * (<code>hasStopped()</code>) ou il n'as jamais été utilisé auparavent
 * (<code>!hasStarted()</code> && <code>!hasStopped()</code>).
 * De même, on ne peut demander à cet objet de s'arrêter (<code>stop()</code>)
 * que si il a été démarré (<code>hasStarted()</code>).
 * Un appel à la méthode <code>start()</code> après avoir arrêté le
 * chronométrage remet le temps à 0.
 * @inv
 *      hasStarted() ==> le comptage a été démarré
 *      isPaused() ==> le comptage a été mis en pause
 *      hasStopped() ==> le comptage a été arrêté
 *      isRunning() == hasStarted && !hasStopped() && !isPaused()
 *      getTime() est la représentation du temps qui s'est écoulé entre l'appel
 *          à la méthode start() et l'appel à la méthode stop()
 */
public class StopWatch implements Savable {

    // CONSTANTES

    public static final String SEPARATOR = ":";

    // ATTRIBUTS

    private long startTime;
    private long totalTime;
    private boolean started;
    private boolean paused;
    private boolean stopped;
    private EventListenerList listenerList;
    private ChangeEvent changeEvent;
    private Thread timer;

    // CONSTRUCTEURS

    /**
     * Un chronomètre non lancé.
     * @post
     *      !isRunning()
     *      !isStopped()
     *      getTime() est la chaîne de caractères "00:00:00"
     */
    public StopWatch() {
        this.initialize();
        this.startTime = System.currentTimeMillis();
    }

    public StopWatch(long time) {
        this.initialize();
        startTime = System.currentTimeMillis() - time;
    }

    // REQUETES

    /**
     * Indique si le chronométrage a commencé.
     */
    public synchronized boolean hasStarted() {
        return started;
    }

    /**
     * Indique si le chronométrage a été arrêté.
     */
    public synchronized boolean hasStopped() {
        return stopped;
    }

    /**
     * Indique si le chronométrage est en cours.
     */
    public synchronized boolean isRunning() {
        return started && !paused && !stopped;
    }

    /**
     * Indique si le chronométrage est en pause.
     */
    public synchronized boolean isPaused() {
        return started && paused && !stopped;
    }

    /**
     * La représentation du temps qui s'est écoulé depuis le dernier appel à la
     * méthode start().
     * @see #start()
     */
    public synchronized String getTime() {
        final long[] time = this.computeTime();
        return timeToString(time[0], time[1], time[2]);
    }

    @Override
    public String describe() {
        return "time = " + this.getRawTime();
    }

    // COMMANDES

    /**
     * Démarre ce chronomètre.
     * @post
     *      hasStarted()
     *      !hasStopped()
     *      !isPaused()
     *      isRunning()
     */
    public synchronized void start() {
        this.initialize();
        this.started = true;
        this.timer.start();
    }

    /**
     * Met en pause le chronométrage.
     * @pre
     *      !isPaused()
     * @post
     *      hasStarted()
     *      !hasStopped()
     *      isPaused()
     *      !isRunning()
     */
    public synchronized void pause() {
        Contract.checkCondition(!isPaused());

        this.paused = true;
    }

    /**
     * Résume le chronométrage.
     * @pre
     *      isPaused()
     * @post
     *      hasStarted()
     *      !hasStopped()
     *      !isPaused()
     *      isRunning()
     */
    public synchronized void resume() {
        Contract.checkCondition(isPaused());

        this.paused = false;
    }

    /**
     * Met fin au chronométrage.
     * @pre
     *      hasStarted()
     * @post
     *      hasStarted()
     *      hasStopped()
     *      !isPaused()
     *      !isRunning()
     */
    public synchronized void stop() {
        Contract.checkCondition(hasStarted());

        this.stopped = true;
    }


    public void addChangeListener(ChangeListener listener) {
        Contract.checkCondition(listener != null);

        if (listenerList == null) {
            listenerList = new EventListenerList();
        }
        listenerList.add(ChangeListener.class, listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        Contract.checkCondition(listener != null);

        listenerList.remove(ChangeListener.class, listener);
    }

    private void fireStateChanged() {
        Object[] listeners = listenerList.getListenerList();

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }

    // OUTILS

    /**
     * Remet le compteur à 0.
     */
    private synchronized void initialize() {
        this.started = false;
        this.paused = false;
        this.stopped = false;
        this.timer = new Thread(new Counter());
    }

    /**
     * Calcule les secondes, minutes et heures appropriées à afficher.
     */
    private long[] computeTime() {
        final long s = totalTime / 1000;
        final long m = s / 60;
        final long h = m / 60;

        long[] res = new long[3];
        res[0] = h >= 24 ? h % 24 : h;
        res[1] = m >= 60 ? m % 60 : m;
        res[2] = s >= 60 ? s % 60 : s;
        return res;
    }

    /**
     * Décrit le temps donné en paramètre au format "hh:mm:ss"
     */
    private String timeToString(long h, long m, long s) {
        assert(h > 0 && m > 0 && s > 0);

        final String hours = h >= 10 ? h + "" : "0" + h;
        final String minutes = m >= 10 ? m + "" : "0" + m;
        final String seconds = s >= 10 ? s + "" : "0" + s;
        return hours + SEPARATOR + minutes + SEPARATOR + seconds;
    }

    /**
     * Le temps réel, en milisecondes, qui s'est écoulé depuis le dernier appel
     * à la méthode start().
     * @see #start()
     */
    private synchronized String getRawTime() {
        return totalTime + "";
    }

    /**
     * Une instance de la classe Counter se charge de mettre à jour, toutes les
     * secondes, le temps écoulé depuis le dernier appel à la commande start().
     */
    private class Counter implements Runnable {

        // ATTRIBUTS

        private long pausedtime;

        // CONSTRUCTEURS

        public Counter() {
            pausedtime = 0;
        }

        // COMMANDES

        @Override
        public void run() {
            while (!hasStopped()) {
                if (isRunning()) {
                    totalTime = System.currentTimeMillis() - startTime - pausedtime;
                    fireStateChanged();
                } else {
                    pausedtime += 1000;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    stop();
                }
            }
        }
    }
}
