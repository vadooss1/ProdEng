package com.vadym.zadorozhniuk;

import java.io.File;
import java.util.*;

public class DirTreeKeeper {
    private File rootPath;
    private Queue<File> dirElements;
    private List<String> treeAllElements;
    private String NO_SUCH_ELEMENT = "No such element !";
    private String NO_SUCH_PATH = "No such path !";

    public DirTreeKeeper(String path) {
        this.rootPath = new File(path);
        this.dirElements = new PriorityQueue();
        treeAllElements = new LinkedList();
    }

    public List<String> getDirTreeAllElements() {
        if (rootPath.exists()) {
            treeAllElements.add(rootPath.getAbsolutePath());
            dirElements.addAll(Arrays.asList(rootPath.listFiles()));
            while (!dirElements.isEmpty()) {
                File currentElement = dirElements.remove();
                if (currentElement.isDirectory() && currentElement.listFiles() != null && currentElement.listFiles().length > 0) {
                    dirElements.addAll(Arrays.asList(currentElement.listFiles()));
                }
                treeAllElements.add(currentElement.getAbsolutePath());

            }
            return treeAllElements;
        } else {
            return null;
        }
    }

    public String getDirElementsBy(int depth, String mask) {
        StringBuilder result = new StringBuilder();
        List<String> allElements = getDirTreeAllElements();

        if (allElements == null) {
            result.append(NO_SUCH_PATH);
        } else if (allElements.isEmpty() || depth < 0) {
            result.append(NO_SUCH_ELEMENT);
        } else {
            int depthWithRoot = depth + rootPath.getAbsolutePath().split("[\\\\/]").length;
            for (String entry : allElements) {
                if (entry.contains(mask) && entry.split("[\\\\/]").length == depthWithRoot) {
                    result.append(entry).append("\n");
                }
            }
        }
        return result.length() > 0 ? result.toString() : result.append(NO_SUCH_ELEMENT).toString();
    }
}
