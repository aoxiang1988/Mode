package com.module;

import com.module.difffilecheck.TagInfo;
import com.module.difffilecheck.TagInfoListUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    /**
     * A native method that is implemented by the 'nativelib' native library,
     * which is packaged with this application.
     */
    private static Map<String, TagInfo> mMainMap;

    private static TagInfoListUtil mUtil;

    private static String mPatchesParentPath;
    private static String mPatchOfModelPath;
    private static String mModelInitResult;
    private static String mPatchesResult;

    public static void main(String[] args) {

        mUtil = new TagInfoListUtil();

        InputInfoUtils mInputUtils = new InputInfoUtils();
        System.out.print("please input Diff file path :> ");
        String diffFilePath = mInputUtils.putFilePath();
        File file = new File(diffFilePath);
        List<String> diffFileList = new ArrayList<>();
        if(file.isDirectory()) { // 判断File对象对应的目录是否存在
            String[] names = file.list(); // 获得目录下的所有文件的文件名
            for (String name : names) {
                if (name.endsWith(".diff")) {
                    //System.out.println(diffFilePath + "/" + name);
                    diffFileList.add(diffFilePath + "\\" + name);
                }
            }
        }

        System.out.print("please input patches file download path :> ");
        String patchFilePath = mInputUtils.putFilePath();
        mPatchesParentPath = patchFilePath + "/patches/";
        mPatchOfModelPath = patchFilePath + "/preCS1_preCS2_patches/";
        mModelInitResult = patchFilePath + "/initial_result/";
        mPatchesResult = patchFilePath + "/patches_result/";

        //System.out.print("please input path of cmd file where you want to store (like G:\\):> ");
        //String patchCmdFile = mInputUtils.putFilePath();

        for (int n = 0; n<diffFileList.size(); n++) {
            buildCmdFile (diffFileList.get(n), diffFilePath);
        }
    }

    private static void buildCmdFile(String diffFilePath, String patchCmdFile) {
        try {
            System.out.println("diff file path: "+diffFilePath);
            mUtil.ReadFile(diffFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMainMap = mUtil.getDiffList();

        //写入中文字符时解决中文乱码问题
        FileOutputStream patchCmdFos= null;
        try {
            patchCmdFile = patchCmdFile + "\\" + mUtil.getPatchFileName() + "_cmd.sh";
            System.out.println("diff cmd file path: "+patchCmdFile);
            patchCmdFos = new FileOutputStream(new File(patchCmdFile));
            OutputStreamWriter patchCmdOsw = new OutputStreamWriter(patchCmdFos, "UTF-8");
            BufferedWriter patchCmdBw=new BufferedWriter(patchCmdOsw);

            /*String[] resultFileCmd = {
                    "mkdir -p " + patches_result,  //  patch下载完成后结果保存位置   /home/soft2/24MM/LINUX/temp/patches_result",
                    "cd " + patches_result,    // patch下载完成后结果保存位置    /home/soft2/24MM/LINUX/temp/patches_result",
            };*/

            patchCmdBw.write("#!/bin/bash" + "\t\n");
            patchCmdBw.write("\t\n");

            patchCmdBw.write("echo \"cmd start\"" + "\t\n");

            patchCmdBw.write("PATCHESDIRECTORY=" + mPatchesParentPath /*具体patch最终保存的路径 /home/soft2/24MM/LINUX/temp/patches/"*/ + mUtil.getPatchFileName() + "\t\n");
            patchCmdBw.write("mkdir -p $PATCHESDIRECTORY" + "\t\n");
            patchCmdBw.write("\t\n");
            for(String path:mMainMap.keySet()) {
                TagInfo readInfo = mMainMap.get(path);

                String homePath = mPatchOfModelPath;  //存在patch的模块git下载处 "/home/soft2/24MM/LINUX/temp/preCS1_preCS2_patches/";

                String patchCmd = null;

                if (readInfo.getOldRevision() == null) {
                    patchCmd = "git format-patch --src-prefix=\"a/"
                            + readInfo.getProjectPath()
                            + "\" --dst-prefix=\"b/"
                            + readInfo.getProjectPath()
                            + "\" -o $PATCHESDIRECTORY "
                            + readInfo.getNewRevision()
                            + " --stdout > " + readInfo.getPatchFileName();
                }

                if (readInfo.getNewRevision() == null) {
                    patchCmd = "git format-patch --src-prefix=\"a/"
                            + readInfo.getProjectPath()
                            + "\" --dst-prefix=\"b/"
                            + readInfo.getProjectPath()
                            + "\" -o $PATCHESDIRECTORY "
                            + readInfo.getOldRevision()
                            + " --stdout > " + readInfo.getPatchFileName();
                }

                if (readInfo.getOldRevision() != null && readInfo.getNewRevision() != null) {
                    patchCmd = "git format-patch --src-prefix=\"a/"
                            + readInfo.getProjectPath()
                            + "\" --dst-prefix=\"b/"
                            + readInfo.getProjectPath()
                            + "\" -o $PATCHESDIRECTORY "
                            + readInfo.getOldRevision()
                            + " "
                            + readInfo.getNewRevision()
                            + " --stdout > " + readInfo.getPatchFileName();
                }


                String initFile = mModelInitResult/*"/home/soft2/24MM/LINUX/temp/initial_result/"*/ + readInfo.getResultFileName();
                String resultFile = mPatchesResult /*"/home/soft2/24MM/LINUX/temp/patches_result/"*/ + readInfo.getResultFileName();
                String[] patchCmdArrs={
                        "#init Start!",
                        "echo \"" + readInfo.getModelName() + " start!\"",
                        "DIRECTORY=" + homePath + readInfo.getProjectPath(),
                        "if [ ! -d $DIRECTORY ]; then",
                        "    mkdir -p $DIRECTORY",
                        "    cd $DIRECTORY",
                        "    git init",
                        "fi",
                        "INITFILE=" + initFile,
                        "if [ ! -f \"$INITFILE\" ]; then",
                        "    git remote add origin " + readInfo.getRemoteFetch() + "/" + readInfo.getProjectPath() + ".git",
                        "    git fetch",
                        "    if [ $? = '0' ]; then",
                        "        mkdir -p " + mModelInitResult, ///home/soft2/24MM/LINUX/temp/initial_result",
                        "        echo \"finished!\" > $INITFILE",
                        "    else echo \"init has failed!\"",
                        "    fi",
                        "fi",
                        "echo \"init has finished!\"",
                        "#init Finished!",
                        "#patch down Start!",
                        "FILE=" + resultFile,
                        "if [ ! -f \"$FILE\" ]; then",
                        "    " + patchCmd,
                        "    if [ $? = '0' ]; then",
                        "        mkdir -p " + mPatchesResult, ///home/soft2/24MM/LINUX/temp/patches_result",
                        "        echo \"finished!\" > $FILE",
                        "    else echo \"down has failed!\"",
                        "    fi",
                        "fi",
                        "echo \"down has finished!\"",
                        "echo \"" + readInfo.getModelName() + " Successed!\"",
                        "echo","echo","echo",
                        "#patch down Finished!",
                };
                for (String arr:patchCmdArrs) {
                    patchCmdBw.write(arr + "\t\n");
                }
                patchCmdBw.write("\t\n");
            }

            //注意关闭的先后顺序，先打开的后关闭，后打开的先关闭
            patchCmdBw.close();
            patchCmdOsw.close();
            patchCmdFos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}