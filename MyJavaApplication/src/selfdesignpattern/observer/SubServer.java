package selfdesignpattern.observer;

public class SubServer {

    private static ObserverManager mObserverManager = null;
    private static SubServer mSubServer = null;

    public static SubServer getSubServer() {
        if(mSubServer == null) {
            mSubServer = new SubServer();
            mObserverManager = ObserverManager.getObserverManager();
        }
        return mSubServer;
    }

    public void serverNotifyObserver() {
        System.out.print("SubServer serverNotifyObserver!!!\n");
        mObserverManager.notifyObserver();
    }
}
