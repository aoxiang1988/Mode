package com.module.difffilecheck;

public class TagInfo {
    String mRemoteFetch = null;
    String mProjectPath = null;
    String mOldRevision = null;
    String mNewRevision = null;

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
        String resultName = null;
        resultName = "result_" + mProjectPath.replace("/", "_") + ".txt";
        return resultName;
    }

    public String getModelName () {
        String modelName = null;
        modelName = mProjectPath.replace("/", "_");
        return modelName;
    }

    public String getPatchFileName () {
        String resultName = null;
        resultName = mProjectPath.replace("/", "_") + "_patches_file.patch";
        return resultName;
    }
}
