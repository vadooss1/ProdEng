package com.vadym.zadorozhniuk;

import java.io.PrintWriter;

public class ThreadTestRunner {
    public static void main(String[] args) {

        String rootPath = "C:\\Intel";
        int depth = 1;
        String mask = "";

        DirTreeKeeperTest dirTreeKeeper = new DirTreeKeeperTest(rootPath);

        Thread scan = new Thread(() -> dirTreeKeeper.scanningDirTreeAllElements());
        Thread receive = new Thread(() -> dirTreeKeeper.gettingDirElementsBy(depth, mask, new PrintWriter(System.out)));
        scan.start();
        receive.start();
        try {
            scan.join();
            receive.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
