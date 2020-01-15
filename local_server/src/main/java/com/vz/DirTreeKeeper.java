package com.vz;

import java.io.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class DirTreeKeeper {
    private File rootPath;
    private Queue<File> dirElements;
    private BlockingQueue<String> treeAllElements;
    private String NO_SUCH_ELEMENT = "No such element !";
    private String NO_SUCH_PATH = "No such path !";
    private volatile boolean scanningFinished = false;

    public DirTreeKeeper(String path) {
        this.rootPath = new File(path);
        this.dirElements = new PriorityQueue<>();
        treeAllElements = new LinkedBlockingDeque<>();
    }

    public void scanningDirTreeAllElements() {
        if (rootPath.exists()) {
            try {
                treeAllElements.put(rootPath.getAbsolutePath());
                dirElements.addAll(Arrays.asList(rootPath.listFiles()));
            while (!dirElements.isEmpty()) {
                File currentElement = dirElements.remove();
                if (currentElement.isDirectory() && currentElement.listFiles() != null && currentElement.listFiles().length > 0) {
                    dirElements.addAll(Arrays.asList(currentElement.listFiles()));
                }
                treeAllElements.put(currentElement.getAbsolutePath());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        } else {
            try {
                treeAllElements.put(NO_SUCH_PATH);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        scanningFinished = true;
    }

    public void gettingDirElementsBy(int depth, String mask, PrintWriter writer) {
        if (treeAllElements == null) {
            writer.println(NO_SUCH_PATH);
            writer.flush();
        } else if (depth < 0) {
            writer.println(NO_SUCH_ELEMENT);
            writer.flush();
        } else {
            int depthWithRoot = depth + rootPath.getAbsolutePath().split("[\\\\/]").length;
            String entry;
            try {
            while (!scanningFinished || !treeAllElements.isEmpty()) {
                entry = treeAllElements.take();
                if (entry.contains(mask) && entry.split("[\\\\/]").length == depthWithRoot) {
                    writer.println(entry);
                    writer.flush();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        }
        writer.println("(END)");
        writer.flush();
    }
}
