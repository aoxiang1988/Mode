package org.example.difffilecheck;

public class TagInfo {
    String mRemoteFetch = null;
    String mProjectPath = null;
    String mOldRevision = null;
    String mNewRevision = null;

    String mAndroidProjectPath = null;
/* /data/24MM/24MM_AOSP/PCS2_PCS3 */
    public void setAndroidProjectPath(String mAndroidProjectPath) {
        this.mAndroidProjectPath = mAndroidProjectPath;
    }

    public String getAndroidProjectPath() {
        if (mAndroidProjectPath == null) {
            mAndroidProjectPath = mProjectPath;
        }
        return mAndroidProjectPath;
    }

    public void setNewRevision(String mNewRevision) {
        this.mNewRevision = mNewRevision;
    }

    public void setOldRevision(String mOldRevision) {
        this.mOldRevision = mOldRevision;
    }

    public void setProjectPath(String mProjectPath) {
        this.mProjectPath = mProjectPath;
    }

    public void setRemoteFetch(String mRemoteFetch) {
        this.mRemoteFetch = mRemoteFetch;
    }

    public String getNewRevision() {
        return mNewRevision;
    }

    public String getOldRevision() {
        return mOldRevision;
    }

    public String getProjectPath() {
        return mProjectPath;
    }

    public String getRemoteFetch() {
        return mRemoteFetch;
    }

    public String getResultFileName () {
        String resultName;
        resultName = "result_" + mProjectPath.replace("/", "_") + ".txt";
        return resultName;
    }

    public String getModelName () {
        String modelName = mProjectPath.replace("/", "_");
        return modelName;
    }

    public String getPatchFileName () {
        String resultName = mProjectPath.replace("/", "_") + "_patches_file.patch";
        return resultName;
    }
}
