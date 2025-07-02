package com.application.broadcast.intent;


import com.application.WorkUtils;

@SuppressWarnings("ALL")
public class IntentFilter {

    private WorkUtils.QueueUtils mQueueUtils;
    private WorkUtils.QueueUtils.Queue mCurrentQueue;

    public IntentFilter() {
        mQueueUtils = WorkUtils.QueueUtils.getInstance();
        mCurrentQueue = mQueueUtils.initLinkQueue(new Intent());
    }

    public void addAction(String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        mQueueUtils.pushToQueue(mCurrentQueue, intent);
    }

    public Intent getCurrentIntent(String action) {
        WorkUtils.QueueUtils.QueueNode result = mCurrentQueue.getLinkQueue();
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
