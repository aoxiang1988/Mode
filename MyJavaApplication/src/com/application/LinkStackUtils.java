package com.application;

public class LinkStackUtils {
    private static String TAG = "com.application.LinkStackUtils";
    private static LinkStackUtils mLinkStackUtils = null;

    public static LinkStackUtils getInstance() {
        if (mLinkStackUtils == null) {
            mLinkStackUtils = new LinkStackUtils();
        }
        return mLinkStackUtils;
    }

    //单向链表堆栈节点
    public static class StackNode {

        StackNode () {
            this.next = null;
            this.value = -1;
        }

        StackNode next;
        Object value;
    }



    static class StackCreater {
        private StackNode top;
        private StackNode taskStack;

        private StackNode_D top_D;
        private StackNode_D taskStack_D;

        public void setTop(StackNode top) {
            this.top = top;
        }

        public void setTop_D(StackNode_D top_D) {
            this.top_D = top_D;
        }
    }


    public void initStack(StackCreater creater) {
        creater.taskStack = new StackNode();
        creater.top = creater.taskStack;
    }

    public void push(StackCreater creater, Object v) {
        creater.top.next = new StackNode();
        creater.top.next.value = v;
        creater.setTop(creater.top.next);
    }

    private StackNode getLastSecondNode(StackCreater creater) {
        StackNode temp = creater.taskStack;
        while (temp.next.next != null) {
            temp = temp.next;
        }
        temp.next = null;
        return temp;
    }

    public boolean isEmpty(StackCreater creater) {
        if (creater.taskStack.next == null) {
            Log.d(TAG,"the stack is null");
            return true;
        }
        return false;
    }

    StackNode pop(StackCreater creater) {
        if(isEmpty(creater)) {
            return null;
        }
        StackNode s = new StackNode();
        s.next = null;
        s.value = creater.top.value;
        creater.setTop(getLastSecondNode(creater));
        return s;
    }

    //双向链表堆栈节点
    public static class StackNode_D {
        StackNode_D () {
            this.next = null;
            this.front = null;
            this.value = -1;
        }
        StackNode_D next;
        StackNode_D front;
        public Object value;
    }



    public void initDLStack(StackCreater creater) {
        creater.taskStack_D = new StackNode_D();
        creater.top_D = creater.taskStack_D;
    }

    public void pushDLStack(StackCreater creater, Object v) {
        creater.top_D.next = new StackNode_D();
        creater.top_D.next.value = v;
        creater.top_D.next.front = creater.top_D;
        creater.setTop_D(creater.top_D.next);
    }

    private StackNode_D getLastSecondNodeOfDLStack(StackCreater creater) {
        StackNode_D temp = creater.top_D.front;
        temp.next = null;
        return temp;
    }

    public StackNode_D getDLStack(StackCreater creater) {
        return creater.taskStack_D;
    }

    public boolean isEmptyOfDLStack(StackCreater creater) {
        if (creater.taskStack_D.next == null) {
            Log.d(TAG,"the DL stack is null");
            return true;
        }
        return false;
    }

    StackNode_D popDLStack(StackCreater creater) {
        if(isEmptyOfDLStack(creater)) {
            return null;
        }
        StackNode_D s = new StackNode_D();
        s.value = creater.top_D.value;
        creater.top_D = getLastSecondNodeOfDLStack(creater);
        return s;
    }


    /*销毁*/
    public void destroyStack(StackCreater creater) {
        if (creater.taskStack != null) {
            creater.taskStack = null;
            creater.top = null;
        }
    }

    public void destroyDLStack(StackCreater creater) {
        if (creater.taskStack_D != null) {
            creater.taskStack_D = null;
            creater.top_D = null;
        }
    }
}
