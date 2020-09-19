package com.module;

import java.io.File;

public class GitPatchUtil {
    void makeDirs(String path) {
        File file1 = new File("D:/patch/"+path);
        if (file1.mkdirs()) {
            System.out.println("多级层文件夹创建成功！创建后的文件目录为：" + file1.getPath() + ",上级文件为:" + file1.getParent());
        }
    }

    void gitInit() {

    }
}
