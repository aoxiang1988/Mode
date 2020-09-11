package lte.td.tech;

class WorkUtils {
    private static String TAG = "WorkUtils";
    static void linkStack() {
        /* 单链表堆栈实现
         * */
        LinkStackUtils.StackCreater creater = new LinkStackUtils.StackCreater();
        LinkStackUtils.getInstance().initStack(creater);
        for (int i = 0; i<5; i++) {
            LinkStackUtils.getInstance().push(creater, i);
        }

        while (!LinkStackUtils.getInstance().isEmpty(creater)) {
            LinkStackUtils.StackNode popNode = LinkStackUtils.getInstance().pop(creater);
            Log.i(TAG, "value = " + popNode.value);
        }

        LinkStackUtils.getInstance().destroyStack(creater);
    }

    static void linkDLStack() {
        /* 双向链表堆栈实现
         * */
        LinkStackUtils.StackCreater creater = new LinkStackUtils.StackCreater();
        LinkStackUtils.getInstance().initDLStack(creater);
        for (int i = 0; i<5; i++) {
            LinkStackUtils.getInstance().pushDLStack(creater, i);
        }

        while (!LinkStackUtils.getInstance().isEmptyOfDLStack(creater)) {
            LinkStackUtils.StackNode_D popNode = LinkStackUtils.getInstance().popDLStack(creater);
            Log.i(TAG, "value = " + popNode.value);
        }

        LinkStackUtils.getInstance().destroyDLStack(creater);
    }

    static void fifoQueue() {
        QueueUtils.Queue currentQueue = QueueUtils.getInstance().initLinkQueue(0);

        for (int i=0; i < 5; i++) {
            QueueUtils.getInstance().pushToQueue(currentQueue, i+1);
        }
        for (int i=0; i <= 7; i++) {
            QueueUtils.QueueNode result = QueueUtils.getInstance().popQueue(currentQueue);
            if (result == null) {
                break;
            }
            Log.d(TAG, "value = " + result.value);
        }
    }
}
