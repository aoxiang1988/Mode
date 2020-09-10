package broadcast;

import lte.td.tech.broadcast.intent.Intent;
import lte.td.tech.broadcast.intent.IntentFilter;

public class Receiver {

    private static IntentFilter mCurrentFilter;
    private static BroadCastReceiver mCurrentReceiver;

    protected static void registerReceiver(BroadCastReceiver receiver, IntentFilter filter) {
        mCurrentFilter = filter;
        mCurrentReceiver = receiver;
    }
    public static void unregisterReceiver() {

    }

    public static void sendBroadCastReceiver(Intent intent) {
        String mCurrentAction = intent.getAction();
        mCurrentReceiver.onReceiver(mCurrentFilter.getCurrentIntent(mCurrentAction));
    }
}
