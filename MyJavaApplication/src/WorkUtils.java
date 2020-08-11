class WorkUtils {
    static void linkStack() {
        /* 单链表堆栈实现
         * */
        LinkStackUtils.getInstance().initStack();
        for (int i = 0; i<100; i++) {
            LinkStackUtils.getInstance().push(i);
        }

        while (!LinkStackUtils.getInstance().isEmpty()) {
            LinkStackUtils.StackNode popNode = LinkStackUtils.getInstance().pop();
        }

        LinkStackUtils.getInstance().destroyStack();
    }

    static void linkDLStack() {
        /* 双向链表堆栈实现
         * */
        LinkStackUtils.getInstance().initDLStack();
        for (int i = 0; i<100; i++) {
            LinkStackUtils.getInstance().pushDLStack(i);
        }

        while (!LinkStackUtils.getInstance().isEmptyOfDLStack()) {
            LinkStackUtils.StackNode_D popNode = LinkStackUtils.getInstance().popDLStack();
        }

        LinkStackUtils.getInstance().destroyDLStack();
    }

    static void fifoQueue() {
        QueueUtils.getInstance().initLinkQueue(0);

        for (int i=0; i < 5; i++) {
            QueueUtils.getInstance().pushToQueue(i+1);
        }
        for (int i=0; i <= 7; i++) {
            QueueUtils.QueueNode result = QueueUtils.getInstance().popQueue();
            if (result == null) {
                break;
            }
            System.out.println(result.value);
        }
    }
}
