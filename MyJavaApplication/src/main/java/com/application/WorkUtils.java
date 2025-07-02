package com.application;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("ALL")
public class WorkUtils {
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
    
    static void getBaiduResult() {
        Log.i(TAG, "getBaiduResult");
        OnLineWorkerThread<String> thread = new OnLineWorkerThread<String>();
        thread.SendNewRunnable(OnLineWorkerThread.OPERATION_HTTP_CONNECTION,
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(String val) {
                        Log.i(TAG, val);
                    }

                    @Override
                    public void onFail(String errorMessage) {
                    }
                });
    }

    private interface RequestCallBack<T> {
        void onSuccess(T val);
        void onFail(String errorMessage);
    }

    /**
     * Created by SRC-TJ-MM-BinYang on 2018/3/8.
     * this is the thread pool, we can request an new thread runnable to do the work which need more time,
     * I do the connect net work and Json resolution here
     * also we can do more work thread here...
     */

    private static class OnLineWorkerThread<T> {

        private String _TAG = "OnLineWorkerThread";
        public static final int OPERATION_HTTP_CONNECTION = 1;
        public static final int OPERATION_XXX_2 = 2;

        private ExecutorService mExecutorService;

        public OnLineWorkerThread(){


            BlockingQueue<Runnable> mBlockingQueue = new LinkedBlockingQueue<Runnable>();//when the queue empty , it cann't get any runnable, after add new runnable, the queue will be wake up and can be got runnable to to work

            ThreadFactory mFactory = new ThreadFactory() {

                private final AtomicInteger mCount = new AtomicInteger(1);

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "new Thread #" + mCount.getAndIncrement());
                }
            };
            RejectedExecutionHandler mRejectedExecutionHandler = new RejectedExecutionHandler() {
                @Override
                public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                    try {
                        Log.d(_TAG,"your work has been rejected, please do the work again.");
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                }
            };

            int mCorePoolSize = Runtime.getRuntime().availableProcessors();
            int mMaxiMumPoolSize = 10;
            long mKeepAliveTime = 500;
            TimeUnit mUnit = TimeUnit.SECONDS;

            mExecutorService = new ThreadPoolExecutor(mCorePoolSize,
                    mMaxiMumPoolSize, mKeepAliveTime,
                    mUnit, mBlockingQueue,
                    mFactory, mRejectedExecutionHandler);

            Log.d(_TAG,"OnLineWorkerThread init finish");
        }

        public void SendNewRunnable(int type) {
            mExecutorService.execute(new OnLineWorkerRunnable(type));
        }

        public void SendNewRunnable(int type, final int localId, int current_page) {
            mExecutorService.execute(new OnLineWorkerRunnable(type, localId, current_page, null));
        }
        public void SendNewRunnable(int type, String keyword, String string_type, int current_page) {
            mExecutorService.execute(new OnLineWorkerRunnable(type, keyword, string_type, current_page, null));
        }

        public synchronized void SendNewRunnable(int type, RequestCallBack requestCallBack, int channelID, int current_page) {
            if(current_page != -1)
                mExecutorService.execute(new OnLineWorkerRunnable(type, channelID, current_page, requestCallBack));
            else
                mExecutorService.execute(new OnLineWorkerRunnable(type, channelID, requestCallBack));
        }

        public void SendNewRunnable(int type, RequestCallBack<T> callBack) {
            mExecutorService.execute(new OnLineWorkerRunnable(type, (RequestCallBack<T>) callBack));
        }

        public void SendNewRunnable(int type, RequestCallBack<T> callBack, int category_id) {
            mExecutorService.execute(new OnLineWorkerRunnable(type, (RequestCallBack<T>) callBack, category_id));
        }

        public void SendNewRunnableWaPi(int type, RequestCallBack<T> callBack, int category_id) {
            mExecutorService.execute(new OnLineWorkerRunnable(type, (RequestCallBack<T>) callBack, category_id));
        }

        public void SendNewRunnable(int type, RequestCallBack<T> callBack, String url) {
            mExecutorService.execute(new OnLineWorkerRunnable(type, (RequestCallBack<T>) callBack, url));
        }

        public void SendNewRunnableRecommends(int type, RequestCallBack<T> callBack, int category_id) {
            mExecutorService.execute(new OnLineWorkerRunnable(type, (RequestCallBack<T>) callBack, category_id));
        }

        private class OnLineWorkerRunnable implements Runnable {

            private int mType;
            private String mKeyword;
            private String mString_Type;
            private int mLocalId;
            private int mCurrentPage = 1;
            //   private RequestCallBack mCallBack;

            private String mUrl;
            private int mCategory_id;
            private RequestCallBack<T> mCallBack;

            private OnLineWorkerRunnable(int type) {
                mType = type;
            }

            private OnLineWorkerRunnable(int type , RequestCallBack<T> callBack) {
                mType = type;
                mCallBack = callBack;
            }

            private OnLineWorkerRunnable(int type, RequestCallBack<T> mCallBack, String url) {
                mType = type;
                mUrl = url;
                this.mCallBack = mCallBack;
            }

            private OnLineWorkerRunnable(int type, RequestCallBack<T> mCallBack, int category_id) {
                mType = type;
                mCategory_id = category_id;
                this.mCallBack = mCallBack;
            }

            private OnLineWorkerRunnable(int type, final int localId, RequestCallBack requestCallBack) {
                mType = type;
                mLocalId = localId;
                if(requestCallBack != null) mCallBack = requestCallBack;
            }

            private OnLineWorkerRunnable(int type, final int localId, int current_page, RequestCallBack requestCallBack) {
                mType = type;
                mLocalId = localId;
                mCurrentPage = current_page;
                if(requestCallBack != null) mCallBack = requestCallBack;
            }

            private OnLineWorkerRunnable(int type, String keyword, String string_type, int current_page, RequestCallBack<T> requestCallBack) {
                mType = type;
                mString_Type = string_type;
                mKeyword = keyword;
                mCurrentPage = current_page;
                if(requestCallBack != null) mCallBack = requestCallBack;
            }

            @Override
            public void run() {
                switch (mType) {
                    case OPERATION_HTTP_CONNECTION:
                        Log.d(_TAG, "####### OPERATION xxx 1 ####"+ mCategory_id);
                        setBusyState(true);
                        RequestCallBack<String> callBack = (RequestCallBack<String>) mCallBack;
                        HTTPConnectionUtils httpConnectionUtils = HTTPConnectionUtils.getUtils();
                        httpConnectionUtils.setUrl("https://www.baidu.com");
                        String val = httpConnectionUtils.httpConnect();
                        callBack.onSuccess(val);
                        //callBack.onFail("empty");
                        break;
                    case OPERATION_XXX_2:
                        Log.d(_TAG, "####### OPERATION xxx 2 ####"+ mCategory_id);
                        setBusyState(true);
                        RequestCallBack<T> callBack1 = (RequestCallBack<T>) mCallBack;
                        callBack1.onFail("empty");
                        break;
                    default:
                        break;
                }
            }
        }

        private boolean mIsBusy = false;

        public boolean isBusy() {
            return mIsBusy;
        }

        public void setBusyState(boolean isBusy) {
            mIsBusy = isBusy;
        }

        public void shutdown(){
            mExecutorService.shutdown();//not send new task to queue, not stop runnable..
            try {
                if (!mExecutorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    mExecutorService.shutdownNow();
                    if (!mExecutorService.awaitTermination(5, TimeUnit.SECONDS))
                        System.err.println("Pool did not terminate");
                }
            } catch (InterruptedException e) {
                Log.d(_TAG, "shut down task now!!!");
                mExecutorService.shutdownNow();
                // Preserve interrupt status
                Thread.currentThread().interrupt();
            }
        }
    }

    public static class LinkStackUtils {
        private static String TAG = "com.application.WorkUtils.LinkStackUtils";
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

    public static class QueueUtils {

        private static QueueUtils mQueueUtils = null;

        public static QueueUtils getInstance() {
            if (mQueueUtils == null) {
                mQueueUtils = new QueueUtils();
            }
            return mQueueUtils;
        }

        public static class Queue {
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

            public QueueNode getLinkQueue() {
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

        public static class QueueNode {
            QueueNode next;
            public Object value;
        }

        //private QueueNode mHeadNode = null;
        //private QueueNode mRearNode = null;
        //private QueueNode mLinkQueue = null;

        public Queue initLinkQueue(Object v) {
            QueueNode linkQueue = new QueueNode();
            linkQueue.next = null;
            linkQueue.value = v;
            return new Queue(linkQueue);
        }

        public void pushToQueue(Queue queue, Object v) {
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

        public boolean nextNodeState(QueueNode result) {
            return result.next != null;
        }

        public QueueNode getNextNode(QueueNode result) {
            return result.next;
        }
    }
}
