package broadcast.intent;

import java.util.ArrayList;

public class IntentFilter {

    private static ArrayList<Intent> mFilter ;

    public IntentFilter() {
        mFilter = new ArrayList<>();
    }

    public void addAction(String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        mFilter.add(intent);
    }

    public Intent getCurrentIntent(String action) {
        for (Intent i : mFilter) {
            if (i.getAction().equalsIgnoreCase(action)) {
                return i;
            }
        }
        return null;
    }

}
