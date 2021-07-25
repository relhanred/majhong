package Shanghai20.util.technical;

import Shanghai20.util.exceptions.TimeFormatException;

import java.util.Comparator;

public class TimeComparator implements Comparator<String> {

    // CONSTANTES

    private static final int HOURS_ID = 0;
    private static final int MINUTES_ID = 1;
    private static final int SECONDS_ID = 2;

    // CONSTRUCTEURS

    public TimeComparator() {
        // rien
    }

    // REQUETES

    @Override
    public int compare(String o1, String o2) {
        long[] t1 = new long[3];
        long[] t2 = new long[3];
        try {
            t1 = timeToLongArray(o1);
            t2 = timeToLongArray(o2);
        } catch (TimeFormatException ex) {
            ex.printStackTrace();
        }

        long hoursDiff = t1[HOURS_ID] - t2[HOURS_ID];
        if (hoursDiff == 0) {
            long minutesDiff = t1[MINUTES_ID] - t2[MINUTES_ID];
            if (minutesDiff == 0) {
                return (int) (t1[SECONDS_ID] - t2[SECONDS_ID]);
            } else {
                return (int) minutesDiff;
            }
        }
        return (int) hoursDiff;
    }

    /**
     * Convertit le temps t en un tableau numérique.
     * @throws TimeFormatException si t ne respecte pas le format hh:mm:ss
     */
    public static long[] timeToLongArray(String t) throws TimeFormatException {
        assert (t != null);

        String[] time = t.split(StopWatch.SEPARATOR);
        if (time.length != 3) {
            throw new TimeFormatException();
        }

        long[] res = new long[3];
        try {
            for (int i = 0; i < time.length; i++)
                res[i] = Long.parseLong(time[i]);
        } catch (NumberFormatException ex) {
            throw new TimeFormatException();
        }

        return res;
    }

    public static boolean isValidTime(String t) {
        assert (t != null);

        long[] time = new long[3];
        try {
            time = timeToLongArray(t);
        } catch (TimeFormatException ex) {
            ex.printStackTrace();
        }
        return time[HOURS_ID] < 24 && time[MINUTES_ID] < 60 
        		&& time[SECONDS_ID] < 60;
    }
}
