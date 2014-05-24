package edition.one.epoint;

import org.json.JSONObject;

/**
 * This is a useful callback mechanism so we can abstract our AsyncTasks out into separate, re-usable
 * and testable classes yet still retain a hook back into the calling activity. Basically, it'll make classes
 * cleaner and easier to unit test.
 *
 * @param <T>
 */
public interface AsyncTaskCompleteListener {
    /**
     * Invoked when the AsyncTask has completed its execution.
     * @param <T>
     * @param result The resulting object from the AsyncTask.
     */
    public void onNeChTaskComplete(JSONObject json);
    public void onPrLoTaskComplete(JSONObject json);
    public void onPrSiTaskComplete(JSONObject json);
}