public class QueueUtils {

    private static QueueUtils mQueueUtils = null;

    static QueueUtils getInstance() {
        if (mQueueUtils == null) {
            mQueueUtils = new QueueUtils();
        }
        return mQueueUtils;
    }

    static class Queue {
        QueueNode headNode;
        QueueNode rearNode;
        QueueNode linkQueue;

        Queue(QueueNode linkQueue) {
            this.headNode = linkQueue;
            this.rearNode = linkQueue;
            this.linkQueue = linkQueue;
        }

        QueueNode getHeadNode() {
            return headNode;
        }

        QueueNode getLinkQueue() {
            return linkQueue;
        }

        QueueNode getRearNode() {
            return rearNode;
        }

        void setHeadNode(QueueNode headNode) {
            this.headNode = headNode;
        }

        void setRearNode(QueueNode rearNode) {
            this.rearNode = rearNode;
        }

        void setLinkQueue(QueueNode linkQueue) {
            this.linkQueue = linkQueue;
        }
    }

    static class QueueNode {
        QueueNode next;
        Object value;
    }

    //private QueueNode mHeadNode = null;
    //private QueueNode mRearNode = null;
    //private QueueNode mLinkQueue = null;

    Queue initLinkQueue(Object v) {
        QueueNode linkQueue = new QueueNode();
        linkQueue.next = null;
        linkQueue.value = v;
        return new Queue(linkQueue);
    }

    void pushToQueue(Queue queue, Object v) {
        QueueNode rearNode = queue.getRearNode();
        rearNode.next = new QueueNode();
        rearNode.next.value = v;
        rearNode.next.next = null;
        queue.setRearNode(rearNode.next);
    }

    QueueNode popQueue(Queue queue) {
        QueueNode result = new QueueNode();
        QueueNode mHeadNode = queue.getHeadNode();
        if (mHeadNode != null) {
            result.next = null;
            result.value = mHeadNode.value;
            queue.setHeadNode(mHeadNode.next);

            QueueNode mLinkQueue = queue.getLinkQueue();
            QueueNode queueNode = mLinkQueue.next;
            mLinkQueue.next = null;
            queue.setLinkQueue(queueNode);
        } else {
            return null;
        }
        return result;
    }

}
