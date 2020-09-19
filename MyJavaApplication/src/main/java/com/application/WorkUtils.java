package com.application;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

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
    
    static void getBaiduResult() {
        Log.i(TAG, "getBaiduResult");
        OnLineWorkerThread<String> thread = new OnLineWorkerThread<>();
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


            BlockingQueue<Runnable> mBlockingQueue = new LinkedBlockingQueue<>();//when the queue empty , it cann't get any runnable, after add new runnable, the queue will be wake up and can be got runnable to to work

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
}
