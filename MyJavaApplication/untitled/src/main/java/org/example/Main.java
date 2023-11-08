package org.example;

import org.example.difffilecheck.TagInfo;
import org.example.difffilecheck.TagInfoListUtil;

import java.io.*;
import java.util.*;

public class Main {
    private static Map<String, String> mDiffCmdNameMap;

    private static TagInfoListUtil mUtil;

    private static String mPatchesParentPath;
    private static String mModelInitResult;
    private static String mPatchesResult;

    static final String MAIN_FILE_PATH_INPUT_MESSAGE = "请输入要对比的ManiFest文本及最终sh文件的路径(例如G:/) :> ";

    public static void main(String[] args) {

        mUtil = new TagInfoListUtil();

        InputInfoUtils mInputUtils = new InputInfoUtils();
        System.out.print(MAIN_FILE_PATH_INPUT_MESSAGE);
        String diffFilePath = mInputUtils.putFilePath();

        buildDiffFile(diffFilePath);
        diffInitFun (diffFilePath, mInputUtils);
    }

    /**
     *生成diff文件
     * */
    private static void buildDiffFile(String diffFilePath) {
        Map<String, String> mManiFestNameMap = new HashMap<>();
        mDiffCmdNameMap = new HashMap<>();

        File file = new File(diffFilePath);
        List<String> diffFileList = new ArrayList<>();
        if(file.isDirectory()) { // 判断File对象对应的目录是否存在
            String[] names = file.list(); // 获得目录下的所有文件的文件名
            assert names != null;
            for (String name : names) {
                if (name.endsWith(".xml")) {
                    //System.out.println(diffFilePath + "/" + name);
                    diffFileList.add(diffFilePath + "\\" + name);
                    String diffCmdName = name.split("-")[0] + "-" + name.split("-")[1];
                    mManiFestNameMap.put(diffFilePath + "\\" + name, diffCmdName);
                }
            }

            for (int n = 1; n<diffFileList.size(); n++) {
                String command = "git diff " + diffFileList.get(n-1) + " " + diffFileList.get(n) + " > " + diffFilePath + "\\" + "temp"+ n + "_patch.diff";
                String diffCmdName = mManiFestNameMap.get(diffFileList.get(n-1)) + "_" + mManiFestNameMap.get(diffFileList.get(n));
                mDiffCmdNameMap.put("temp" + n + "_patch.diff", diffCmdName);
                execute(command);
            }

        } else {
            System.out.println("error!!!");
        }
    }

    /**
     * 调用ProcessBuilder执行，相比Runtime方式，返回值不易丢失
     * @param command 命令
     * @return 执行结果
     */
    private static String execute(String command) {
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = new StringBuilder();
        String result = null;
        try {
            File file = new File("C:\\daemonTmp");
            // 新建一个存储结果的缓存文件
            File tmpFile = new File("C:\\daemonTmp\\temp.tmp");
            if (!file.exists()) {
                file.mkdirs();
            }
            if (!tmpFile.exists()) {
                tmpFile.createNewFile();
            }
            ProcessBuilder processBuilder = new ProcessBuilder()
                    .command("cmd.exe", "/c", command).inheritIO();
            // 把控制台中的红字变成了黑字，用通常的方法其实获取不到，控制台的结果是pb.start()方法内部输出的。
            processBuilder.redirectErrorStream(true);
            // 输出执行结果。
            processBuilder.redirectOutput(tmpFile);
            // 等待语句执行完成，否则可能会读不到结果。
            processBuilder.start().waitFor();
            InputStream inputStream = new FileInputStream(tmpFile);
            //设置编码
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            bufferedReader.close();
            bufferedReader = null;

            result = stringBuilder.toString();

        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     *处理diff生成的文件
     * */
    private static void diffInitFun(String diffFilePath, InputInfoUtils mInputUtils) {
        /*InputInfoUtils mInputUtils = new InputInfoUtils();
        System.out.print("please input Diff file path :> ");
        String diffFilePath = mInputUtils.putFilePath();*/
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

        System.out.print("请输入patch文件下载路径（如/home/soft2/24MM/LINUX/temp） :> ");
        String patchFilePath = mInputUtils.putFilePath();
        System.out.print("请输入差分代码存储文件夹名（如 CS1_CS2） :> ");
        String codeFilePath = mInputUtils.putFilePath();
        mPatchesParentPath = patchFilePath + "/patches";
        String mPatchOfModelPath = patchFilePath + "/" + codeFilePath + "_patches";
        mModelInitResult = patchFilePath + "/initial_result";
        mPatchesResult = patchFilePath + "/patches_result";

        //System.out.print("please input path of cmd file where you want to store (like G:\\):> ");
        //String patchCmdFile = mInputUtils.putFilePath();

        for (int n = 0; n<diffFileList.size(); n++) {
            buildCmdFile (diffFileList.get(n), diffFilePath);
        }
    }


    /**
     * 生成patch 下载命令的批处理sh文件
     * */
    private static void buildCmdFile(String diffFilePath, String patchCmdFile) {
        String diffFileName = diffFilePath.replace(patchCmdFile, "");
        try {
            System.out.println("diff file path: "+diffFilePath);
            mUtil.ReadFile(diffFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**
         * A native method that is implemented by the 'nativelib' native library,
         * which is packaged with this application.
         */
        Map<String, TagInfo> mMainMap = mUtil.getDiffList();

        //写入中文字符时解决中文乱码问题
        FileOutputStream patchCmdFos= null;
        try {
            patchCmdFile = patchCmdFile + "\\" + mUtil.getPatchFileName(mDiffCmdNameMap, diffFileName) + "_cmd.sh";
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

            patchCmdBw.write("PATCHESDIRECTORY=" + mPatchesParentPath + "/" /*具体patch最终保存的路径 /home/soft2/24MM/LINUX/temp/patches/"*/ + mUtil.getPatchFileName(mDiffCmdNameMap, diffFileName) + "\t\n");
            patchCmdBw.write("mkdir -p $PATCHESDIRECTORY" + "\t\n");
            patchCmdBw.write("\t\n");
            for(String path: mMainMap.keySet()) {
                TagInfo readInfo = mMainMap.get(path);

                String sourceCodePath = mPatchesParentPath + "/AndroidOpenSource";//mPatchOfModelPath;  //存在patch的模块git下载处 "/home/soft2/24MM/LINUX/temp/preCS1_preCS2_patches/";

                String patchCmd = null;

                if (readInfo.getOldRevision() == null) {
                    patchCmd = "git format-patch --src-prefix=\"a/"
                            + readInfo.getAndroidProjectPath()
                            + "\" --dst-prefix=\"b/"
                            + readInfo.getAndroidProjectPath()
                            //+ "\" -o $PATCHESDIRECTORY "
                            + "\" "
                            + readInfo.getNewRevision()
                            + " --stdout > $PATCHESDIRECTORY/" + readInfo.getPatchFileName();
                }

                if (readInfo.getNewRevision() == null) {
                    patchCmd = "git format-patch --src-prefix=\"a/"
                            + readInfo.getAndroidProjectPath()
                            + "\" --dst-prefix=\"b/"
                            + readInfo.getAndroidProjectPath()
                            //+ "\" -o $PATCHESDIRECTORY "
                            + "\" "
                            + readInfo.getOldRevision()
                            + " --stdout > $PATCHESDIRECTORY/" + readInfo.getPatchFileName();
                }

                if (readInfo.getOldRevision() != null && readInfo.getNewRevision() != null) {
                    patchCmd = "git format-patch --src-prefix=\"a/"
                            + readInfo.getAndroidProjectPath()
                            + "\" --dst-prefix=\"b/"
                            + readInfo.getAndroidProjectPath()
                            //+ "\" -o $PATCHESDIRECTORY "
                            + "\" "
                            + readInfo.getOldRevision()
                            + " "
                            + readInfo.getNewRevision()
                            + " --stdout > $PATCHESDIRECTORY/" + readInfo.getPatchFileName();
                }


                String initFile = mModelInitResult + "/" /*"/home/soft2/24MM/LINUX/temp/initial_result/"*/ + readInfo.getResultFileName();
                String resultFile = mPatchesResult  + "/" /*"/home/soft2/24MM/LINUX/temp/patches_result/"*/ + readInfo.getResultFileName();
                String[] patchCmdArrs={
                        "#init Start!",
                        "echo \"" + readInfo.getModelName() + " start!\"",
                        "DIRECTORY=" + sourceCodePath  + "/" + readInfo.getAndroidProjectPath(),
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
                        "echo",
                        "echo",
                        "echo",
                        //"rm -rf .git",
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