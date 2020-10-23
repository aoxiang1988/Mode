package com.application.broadcast.intent;


import com.application.QueueUtils;

public class IntentFilter {

    private QueueUtils mQueueUtils;
    private QueueUtils.Queue mCurrentQueue;

    public IntentFilter() {
        mQueueUtils = QueueUtils.getInstance();
        mCurrentQueue = mQueueUtils.initLinkQueue(new Intent());
    }

    public void addAction(String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        mQueueUtils.pushToQueue(mCurrentQueue, intent);
    }

    public Intent getCurrentIntent(String action) {
        QueueUtils.QueueNode result = mCurrentQueue.getLinkQueue();
        while (result != null) {
            if (((Intent) result.value).getAction() != null &&
                    ((Intent) result.value).getAction().equalsIgnoreCase(action)) {
                return (Intent) result.value;
            } else if (mQueueUtils.nextNodeState(result)) {
                result = mQueueUtils.getNextNode(result);
            } else {
                result = null;
            }
        }
        return null;
    }
}
