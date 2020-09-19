package com.module.difffilecheck;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TagInfoListUtil {
    File mDiffFile = new File("G:\\manifest.diff");
    Map<String, TagInfo> mDiffList = new HashMap<>();
    FileInputStream mDiffFileInputStream;
    String mRemoteFetch;

    public Map<String, TagInfo> getDiffList() {
        return mDiffList;
    }

    public void ReadFile() throws IOException {
        mDiffFileInputStream = new FileInputStream(mDiffFile);
        BufferedReader br = new BufferedReader(new InputStreamReader(mDiffFileInputStream));
        String line = null;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
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
            if (line.contains("<project")) {
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
                    /*if (lineInfo[k].contains("path=")) {
                        Pattern p1=Pattern.compile("\"(.*?)\"");
                        Matcher m = p1.matcher(lineInfo[k]);
                        while (m.find()) {
                            info.setProjectPath(m.group().trim().replace("\"", ""));
                            //System.out.println(mRemoteFetch);
                        }
                    }*/
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
            System.out.println("finish!");
        }
        br.close();
    }
}
