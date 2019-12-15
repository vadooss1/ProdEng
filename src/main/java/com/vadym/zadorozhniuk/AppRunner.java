package com.vadym.zadorozhniuk;

public class AppRunner {
    public static void main(String[] args) {
        String rootPath = "C:\\Intel";
        int depth = 0;
        String mask = "";

        DirTreeKeeper dirTreeKeeper = new DirTreeKeeper(rootPath);

        System.out.println(dirTreeKeeper.getDirElementsBy(depth, mask));
    }

}
