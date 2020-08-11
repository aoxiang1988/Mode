public class QueueUtils {

    private static QueueUtils mQueueUtils = null;

    static QueueUtils getInstance() {
        if (mQueueUtils == null) {
            mQueueUtils = new QueueUtils();
        }
        return mQueueUtils;
    }

    static class QueueNode {
        QueueNode next;
        Object value;
    }

    private QueueNode mHeadNode = null;
    private QueueNode mRearNode = null;
    private QueueNode mLinkQueue = null;

    void initLinkQueue(Object v) {
        mLinkQueue = new QueueNode();
        mLinkQueue.next = null;
        mLinkQueue.value = v;

        mHeadNode = mLinkQueue;
        mRearNode = mLinkQueue;
    }

    void pushToQueue(Object v) {
        mRearNode.next = new QueueNode();
        mRearNode.next.value = v;
        mRearNode.next.next = null;
        mRearNode = mRearNode.next;
    }

    QueueNode popQueue() {
        QueueNode result = new QueueNode();
        if (mHeadNode != null) {
            result.next = null;
            result.value = mHeadNode.value;
            mHeadNode = mHeadNode.next;

            QueueNode queue = mLinkQueue.next;
            mLinkQueue.next = null;
            mLinkQueue = queue;
        } else {
            return null;
        }
        return result;
    }

}
