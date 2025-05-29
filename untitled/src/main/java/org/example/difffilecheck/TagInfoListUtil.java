package org.example.difffilecheck;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TagInfoListUtil {

    Map<String, TagInfo> mDiffList = new HashMap<>();
    FileInputStream mDiffFileInputStream;
    String mRemoteFetch = "https://git.codelinaro.org/clo/la";
    String mPatchFileName;

    public String getPatchFileName(Map<String, String> mDiffCmdNameMap, String diffFileName) {
        mPatchFileName = mDiffCmdNameMap.get(diffFileName.replace("\\", ""));
        return mPatchFileName;
    }

    public Map<String, TagInfo> getDiffList() {
        return mDiffList;
    }

    public void ReadFile(String diffFilePath) throws IOException {
        File mDiffFile = new File(diffFilePath);
        mDiffFileInputStream = new FileInputStream(mDiffFile);
        BufferedReader br = new BufferedReader(new InputStreamReader(mDiffFileInputStream));
        /*String firstLine = br.readLine();
        String[] firstLines = firstLine.split(" ");

        String firstName = null;
        String lastName = null;
        for (String key:firstLines) {
            if (key.contains("a/")) {
                firstName = key.split("/")[1].split("-")[0]+"-"+key.split("/")[1].split("-")[1];
            }
            if (key.contains("b/")) {
                lastName = key.split("/")[1].split("-")[0]+"-"+key.split("/")[1].split("-")[1];
            }
        }*/
        //mPatchFileName = firstName + "_" + lastName;
        //System.out.println(mPatchFileName);

        String line = null;
        while ((line = br.readLine()) != null) {
            if (line.contains("remote fetch")) {
                String[] lineInfo = new String[line.split("").length];
                for (int k = 0; k < line.split(" ").length; k++) {
                    lineInfo[k] = line.split(" ")[k];
                    if (lineInfo[k].contains("fetch=")) {
                        Pattern p1=Pattern.compile("\"(.*?)\"");
                        Matcher m = p1.matcher(lineInfo[k]);
                        while (m.find()) {
                            mRemoteFetch = m.group().trim().replace("\"", "");
                            //System.out.println(mRemoteFetch);
                        }
                    }
                }
            }
            if (line.contains("-  <project ") || line.contains("+  <project ")) {
                TagInfo info = new TagInfo();
                info.setRemoteFetch(mRemoteFetch);
                String[] lineInfo = new String[line.split("").length];
                for (int k = 0; k < line.split(" ").length; k++) {
                    lineInfo[k] = line.split(" ")[k];
                    if (lineInfo[k].contains("name=") /*&& !line.contains("path=") */) {
                        Pattern p1=Pattern.compile("\"(.*?)\"");
                        Matcher m = p1.matcher(lineInfo[k]);
                        while (m.find()) {
                            info.setProjectPath(m.group().trim().replace("\"", ""));
                            //System.out.println(mRemoteFetch);
                        }
                    }
                    if (lineInfo[k].contains("path=")) {
                        Pattern p1=Pattern.compile("\"(.*?)\"");
                        Matcher m = p1.matcher(lineInfo[k]);
                        while (m.find()) {
                            info.setAndroidProjectPath(m.group().trim().replace("\"", ""));
                            //System.out.println(mRemoteFetch);
                        }
                    }
                    if (lineInfo[k].contains("revision=") && line.startsWith("-  <") ) {
                        Pattern p1=Pattern.compile("\"(.*?)\"");
                        Matcher m = p1.matcher(lineInfo[k]);
                        while (m.find()) {
                            info.setOldRevision(m.group().trim().replace("\"", ""));
                            //System.out.println(mRemoteFetch);
                        }
                    }
                    if (lineInfo[k].contains("revision=") && line.startsWith("+  <") ) {
                        Pattern p1=Pattern.compile("\"(.*?)\"");
                        Matcher m = p1.matcher(lineInfo[k]);
                        while (m.find()) {
                            info.setNewRevision(m.group().trim().replace("\"", ""));
                            //System.out.println(mRemoteFetch);
                        }
                    }

                    if (mDiffList.containsKey(info.getProjectPath())) {
                        TagInfo tempInfo = mDiffList.get(info.getProjectPath());
                        if (tempInfo.getNewRevision() == null && info.getNewRevision() != null) {
                            tempInfo.setNewRevision(info.getNewRevision());
                        }
                        if (tempInfo.getOldRevision() == null && info.getOldRevision() != null) {
                            tempInfo.setOldRevision(info.getOldRevision());
                        }
                    } else {
                        mDiffList.put(info.getProjectPath(), info);
                    }
                }
            }
        }
        br.close();
    }
}
