package org.shirakawatyu.filesystem.util;

import org.shirakawatyu.filesystem.pojo.FileNode;
import org.shirakawatyu.filesystem.pojo.Item;

import java.util.ArrayList;
import java.util.List;

public class FileSystemUtil {
    public static void addNode(FileNode dest, FileNode node) {
        node.parent = dest;
        node.child = new ArrayList<>();
        dest.child.add(node);
    }

    public static Item<FileNode> matchByName(String filename, List<FileNode> list) {
        int i = 0;
        for (FileNode node : list) {
            if (node.filename.equals(filename)) {
                return new Item<>(i, node);
            }
            i++;
        }
        return null;
    }
}
