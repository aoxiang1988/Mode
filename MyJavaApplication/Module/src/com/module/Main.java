package com.module;

import com.module.difffilecheck.TagInfo;
import com.module.difffilecheck.TagInfoListUtil;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Map;

public class Main {
    /**
     * A native method that is implemented by the 'nativelib' native library,
     * which is packaged with this application.
     */
    static Map<String, TagInfo> mMainMap;

    public static void main(String[] args) {
        System.out.print("module main");
        TagInfoListUtil mUtil = new TagInfoListUtil();
        try {
            mUtil.ReadFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMainMap = mUtil.getDiffList();

        //写入中文字符时解决中文乱码问题
        FileOutputStream patchCmdFos= null;
        try {
            patchCmdFos = new FileOutputStream(new File("G:\\patch_cmd.sh"));
            OutputStreamWriter patchCmdOsw = new OutputStreamWriter(patchCmdFos, "UTF-8");
            BufferedWriter patchCmdBw=new BufferedWriter(patchCmdOsw);

            String[] resultFileCmd = {
                    "mkdir -p /home/soft2/24MM/LINUX/temp/patches_result",
                    "cd /home/soft2/24MM/LINUX/temp/patches_result",
            };

            patchCmdBw.write("#!/bin/bash" + "\t\n");
            patchCmdBw.write("\t\n");
            for(String path:mMainMap.keySet()) {
                TagInfo readInfo = mMainMap.get(path);
                String homePath = "/home/soft2/24MM/LINUX/temp/preCS1_preCS2_patches/";

                String patchCmd = null;

                if (readInfo.getOldRevision() == null) {
                    patchCmd = "git format-patch --src-prefix=\"a/"
                            + readInfo.getProjectPath()
                            + "\" --dst-prefix=\"b/"
                            + readInfo.getProjectPath()
                            + "\" -o . "
                            + readInfo.getNewRevision();
                }

                if (readInfo.getNewRevision() == null) {
                    patchCmd = "git format-patch --src-prefix=\"a/"
                            + readInfo.getProjectPath()
                            + "\" --dst-prefix=\"b/"
                            + readInfo.getProjectPath()
                            + "\" -o . "
                            + readInfo.getOldRevision();
                }

                if (readInfo.getOldRevision() != null && readInfo.getNewRevision() != null) {
                    patchCmd = "git format-patch --src-prefix=\"a/"
                            + readInfo.getProjectPath()
                            + "\" --dst-prefix=\"b/"
                            + readInfo.getProjectPath()
                            + "\" -o . "
                            + readInfo.getOldRevision()
                            + " "
                            + readInfo.getNewRevision();
                }

                String initFile = "/home/soft2/24MM/LINUX/temp/initial_result/" + readInfo.getResultFileName();
                String resultFile = "/home/soft2/24MM/LINUX/temp/patches_result/" + readInfo.getResultFileName();
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
                        "        mkdir -p /home/soft2/24MM/LINUX/temp/initial_result",
                        "        echo \"finished!\" > $INITFILE",
                        "    fi",
                        "fi",
                        "echo \"init has finished!\"",
                        "#init Finished!",
                        "#patch down Start!",
                        "FILE=" + resultFile,
                        "if [ ! -f \"$FILE\" ]; then",
                        "    " + patchCmd,
                        "    if [ $? = '0' ]; then",
                        "        mkdir -p /home/soft2/24MM/LINUX/temp/patches_result",
                        "        echo \"finished!\" > $FILE",
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
                System.out.println(readInfo.getResultFileName());
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