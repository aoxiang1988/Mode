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

    static final String MAIN_FILE_PATH_INPUT_MESSAGE = "please input the patch of ManiFest and sh which will be built(example G:/) :> ";

    public static void main(String[] args) {

        mUtil = new TagInfoListUtil();

        InputInfoUtils mInputUtils = new InputInfoUtils();
        System.out.print(MAIN_FILE_PATH_INPUT_MESSAGE);
        String diffFilePath = mInputUtils.putFilePath();

        buildDiffFile(diffFilePath);
        diffInitFun (diffFilePath, mInputUtils);
    }

    /**
     *����diff�ļ�
     * */
    private static void buildDiffFile(String diffFilePath) {
        Map<String, String> mManiFestNameMap = new HashMap<>();
        mDiffCmdNameMap = new HashMap<>();

        File file = new File(diffFilePath);
        List<String> diffFileList = new ArrayList<>();
        if(file.isDirectory()) { // �ж�File�����Ӧ��Ŀ¼�Ƿ����
            String[] names = file.list(); // ���Ŀ¼�µ������ļ����ļ���
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
     * ����ProcessBuilderִ�У����Runtime��ʽ������ֵ���׶�ʧ
     * @param command ����
     * @return ִ�н��
     */
    private static String execute(String command) {
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = new StringBuilder();
        String result = null;
        try {
            File file = new File("C:\\daemonTmp");
            // �½�һ���洢����Ļ����ļ�
            File tmpFile = new File("C:\\daemonTmp\\temp.tmp");
            if (!file.exists()) {
                file.mkdirs();
            }
            if (!tmpFile.exists()) {
                tmpFile.createNewFile();
            }
            ProcessBuilder processBuilder = new ProcessBuilder()
                    .command("cmd.exe", "/c", command).inheritIO();
            // �ѿ���̨�еĺ��ֱ���˺��֣���ͨ���ķ�����ʵ��ȡ����������̨�Ľ����pb.start()�����ڲ�����ġ�
            processBuilder.redirectErrorStream(true);
            // ���ִ�н����
            processBuilder.redirectOutput(tmpFile);
            // �ȴ����ִ����ɣ�������ܻ�����������
            processBuilder.start().waitFor();
            InputStream inputStream = new FileInputStream(tmpFile);
            //���ñ���
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
     *����diff���ɵ��ļ�
     * */
    private static void diffInitFun(String diffFilePath, InputInfoUtils mInputUtils) {
        /*InputInfoUtils mInputUtils = new InputInfoUtils();
        System.out.print("please input Diff file path :> ");
        String diffFilePath = mInputUtils.putFilePath();*/
        File file = new File(diffFilePath);
        List<String> diffFileList = new ArrayList<>();
        if(file.isDirectory()) { // �ж�File�����Ӧ��Ŀ¼�Ƿ����
            String[] names = file.list(); // ���Ŀ¼�µ������ļ����ļ���
            for (String name : names) {
                if (name.endsWith(".diff")) {
                    //System.out.println(diffFilePath + "/" + name);
                    diffFileList.add(diffFilePath + "\\" + name);
                }
            }
        }

        System.out.print("please input patch file download path(example : /home/soft2/24MM/LINUX/temp) :> ");
        String patchFilePath = mInputUtils.putFilePath();
        System.out.print("please input diff code store path(example CS1_CS2)  :> ");
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
     * ����patch ���������������sh�ļ�
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

        //д�������ַ�ʱ���������������
        FileOutputStream patchCmdFos= null;
        try {
            patchCmdFile = patchCmdFile + "\\" + mUtil.getPatchFileName(mDiffCmdNameMap, diffFileName) + "_cmd.sh";
            System.out.println("diff cmd file path: "+patchCmdFile);
            patchCmdFos = new FileOutputStream(new File(patchCmdFile));
            OutputStreamWriter patchCmdOsw = new OutputStreamWriter(patchCmdFos, "UTF-8");
            BufferedWriter patchCmdBw=new BufferedWriter(patchCmdOsw);

            /*String[] resultFileCmd = {
                    "mkdir -p " + patches_result,  //  patch������ɺ�������λ��   /home/soft2/24MM/LINUX/temp/patches_result",
                    "cd " + patches_result,    // patch������ɺ�������λ��    /home/soft2/24MM/LINUX/temp/patches_result",
            };*/

            patchCmdBw.write("#!/bin/bash" + "\t\n");
            patchCmdBw.write("\t\n");

            patchCmdBw.write("echo \"cmd start\"" + "\t\n");

            patchCmdBw.write("PATCHESDIRECTORY=" + mPatchesParentPath + "/" /*����patch���ձ����·�� /home/soft2/24MM/LINUX/temp/patches/"*/ + mUtil.getPatchFileName(mDiffCmdNameMap, diffFileName) + "\t\n");
            patchCmdBw.write("mkdir -p $PATCHESDIRECTORY" + "\t\n");
            patchCmdBw.write("\t\n");
            int cmdCount = 0;
            for(String path: mMainMap.keySet()) {
                cmdCount = cmdCount + 1;
                TagInfo readInfo = mMainMap.get(path);

                String sourceCodePath = mPatchesParentPath + "/AndroidOpenSource";//mPatchOfModelPath;  //����patch��ģ��git���ش� "/home/soft2/24MM/LINUX/temp/preCS1_preCS2_patches/";

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

                    makeCmdString(patchCmdBw, readInfo, cmdCount, sourceCodePath, patchCmd);
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

                    makeCmdString(patchCmdBw, readInfo, cmdCount, sourceCodePath, patchCmd);
                }

                if (readInfo.getOldRevision() != null
                        && readInfo.getNewRevision() != null
                        && !readInfo.getOldRevision().equals(readInfo.getNewRevision())) {

                    patchCmd = "git format-patch --src-prefix=\"a/"
                            + readInfo.getAndroidProjectPath()
                            + "\" --dst-prefix=\"b/"
                            + readInfo.getAndroidProjectPath()
                            //+ "\" -o $PATCHESDIRECTORY "
                            + "\" "
                            + readInfo.getOldRevision()
                            + ".."
                            + readInfo.getNewRevision()
                            + " --stdout > $PATCHESDIRECTORY/" + readInfo.getPatchFileName();

                    makeCmdString(patchCmdBw, readInfo, cmdCount, sourceCodePath, patchCmd);
                }
            }
            System.out.printf("cmd����: " + cmdCount);

            //ע��رյ��Ⱥ�˳���ȴ򿪵ĺ�رգ���򿪵��ȹر�
            patchCmdBw.close();
            patchCmdOsw.close();
            patchCmdFos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void makeCmdString(BufferedWriter patchCmdBw,
                                      TagInfo readInfo,
                                      int cmdCount,
                                      String sourceCodePath,
                                      String patchCmd) throws IOException {
        String initFile = mModelInitResult + "/" /*"/home/soft2/24MM/LINUX/temp/initial_result/"*/ + readInfo.getResultFileName();
        String resultFile = mPatchesResult  + "/" /*"/home/soft2/24MM/LINUX/temp/patches_result/"*/ + readInfo.getResultFileName();
        String[] patchCmdArrs={
                "#cmd " + cmdCount + " init Start!",
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
                "        mkdir -p " + mModelInitResult, ///home/soft2/24MM/LINUX/temp/initial_result
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
                "        mkdir -p " + mPatchesResult, ///home/soft2/24MM/LINUX/temp/patches_result
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

}