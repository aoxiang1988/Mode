class LinkStackUtils {

    private static LinkStackUtils mLinkStackUtils = null;

    static LinkStackUtils getInstance() {
        if (mLinkStackUtils == null) {
            mLinkStackUtils = new LinkStackUtils();
        }
        return mLinkStackUtils;
    }

    //单向链表堆栈节点
    static class StackNode {

        StackNode () {
            this.next = null;
            this.value = -1;
        }

        StackNode next;
        Object value;
    }

    private static StackNode mTop;
    private static StackNode mTaskStack;

    void initStack() {
        mTaskStack = new StackNode();
        mTop = mTaskStack;
    }

    void push(Object v) {
        mTop.next = new StackNode();
        mTop.next.value = v;
        mTop = mTop.next;
    }

    private StackNode getLastSecondNode() {
        StackNode temp = mTaskStack;
        while (temp.next.next != null) {
            temp = temp.next;
        }
        temp.next = null;
        return temp;
    }

    boolean isEmpty() {
        if (mTaskStack.next == null) {
            System.out.println("the stack is null");
            return true;
        }
        return false;
    }

    StackNode pop() {
        if(isEmpty()) {
            return null;
        }
        StackNode s = new StackNode();
        s.next = null;
        s.value = mTop.value;
        mTop = getLastSecondNode();
        return s;
    }

    //双向链表堆栈节点
    static class StackNode_D {
        StackNode_D () {
            this.next = null;
            this.front = null;
            this.value = -1;
        }
        StackNode_D next;
        StackNode_D front;
        Object value;
    }

    private static StackNode_D mTop_D;
    private static StackNode_D mTaskStack_D;

    void initDLStack() {
        mTaskStack_D = new StackNode_D();
        mTop_D = mTaskStack_D;
    }

    void pushDLStack(Object v) {
        mTop_D.next = new StackNode_D();
        mTop_D.next.value = v;
        mTop_D.next.front = mTop_D;
        mTop_D = mTop_D.next;
    }

    private StackNode_D getLastSecondNodeOfDLStack() {
        StackNode_D temp = mTop_D.front;
        temp.next = null;
        return temp;
    }

    boolean isEmptyOfDLStack() {
        if (mTaskStack_D.next == null) {
            System.out.println("the DL stack is null");
            return true;
        }
        return false;
    }

    StackNode_D popDLStack() {
        if(isEmptyOfDLStack()) {
            return null;
        }
        StackNode_D s = new StackNode_D();
        s.value = mTop_D.value;
        mTop_D = getLastSecondNodeOfDLStack();
        return s;
    }


    /*销毁*/
    void destroyStack() {
        if (mTaskStack != null) {
            mTaskStack = null;
            mTop = null;
        }
    }

    void destroyDLStack() {
        if (mTaskStack_D != null) {
            mTaskStack_D = null;
            mTop_D = null;
        }
    }
}
