package com.module;

import java.util.ArrayList;
import java.util.Scanner;

public class InputInfoUtils {

    public String putFilePath() {

        ArrayList<String> a = new ArrayList<String>();
        Scanner scan = new Scanner(System.in);
        String l = scan.nextLine();
        Scanner scan_l = new Scanner(l);
        while(scan_l.hasNextLine()) {
            a.add(scan_l.next());
        }
        System.out.println(a);
        return a.get(0);
    }

}
