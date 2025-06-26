package org.example;

public class Main {

    static final String MAIN_FILE_PATH_INPUT_MESSAGE = "please input the patch of ManiFest and sh which will be built(example G:/) :> ";

    public static void main(String[] args) {

        DiffFileCheckerUtils mInputUtils = new DiffFileCheckerUtils();
        System.out.print(MAIN_FILE_PATH_INPUT_MESSAGE);
        String diffFilePath = mInputUtils.putFilePath();

        mInputUtils.buildDiffFile(diffFilePath);
        mInputUtils.diffInitFun (diffFilePath, mInputUtils);
    }
}