package org.shirakawatyu.filesystem.service;

import org.shirakawatyu.filesystem.pojo.FileNode;
import org.shirakawatyu.filesystem.pojo.Item;
import org.shirakawatyu.filesystem.util.FileSystemUtil;

import java.util.*;

public class FileSystemService {

    private FileNode root;
    private FileNode recent;
    private final DiskService diskService;
    private String path;
    private final Scanner jin;

    public FileSystemService(int maxSize, Scanner jin) {
        this.diskService = new DiskService(maxSize);
        this.jin = jin;
    }

    public void createRoot() {
        String file = "/";
        recent = root = new FileNode(file, true);
        root.parent = null;
        root.child = new ArrayList<>();
        path = "";
    }

    public void mkdir(String file) {
        FileNode temp = new FileNode(file, true);
        if (recent.child.size() == 0) {
            FileSystemUtil.addNode(recent, temp);
        } else {
            Item<FileNode> fileNode = FileSystemUtil.matchByName(file, recent.child);
            Optional.ofNullable(fileNode).ifPresentOrElse(
                    o -> System.out.println("mkdir: cannot create directory '" + file +"': File exists"),
                    () -> FileSystemUtil.addNode(recent, temp)
            );
        }
    }

    public void rmrf(String filename) {
        // 遍历查找判断是否存在
        Item<FileNode> folder = FileSystemUtil.matchByName(filename, recent.child);
        if (folder == null) {
            System.out.println("rm: cannot remove '" + filename + "': No such file or directory");
            return;
        }
        // bfs删除所有子文件（夹）
        LinkedList<FileNode> queue = new LinkedList<>();
        queue.push(folder.v);
        while (!queue.isEmpty()) {
            FileNode i = queue.pop();
            if (i.isdir) {
                for (FileNode node : i.child) {
                    queue.push(node);
                }
            } else {
                diskService.free(i.blocks);
            }
        }
        recent.child.remove(folder.index);
    }

    public void cd(String folder) {
        if (folder.equals("..")) {
            if (recent.parent != null) {
                path = path.substring(0, path.length() - recent.filename.length() - 1);
                recent = recent.parent;
            }
        }
        else {
            Item<FileNode> node = FileSystemUtil.matchByName(folder, recent.child);
            if (node != null) {
                if (!node.v.isdir) {
                    System.out.println("cd: " + node.v.filename + ": Not a directory");
                } else {
                    recent = node.v;
                    path = path + "/" + node.v.filename;
                }
            } else {
                System.out.println("cd: " + folder + ": No such file or directory");
            }
        }
    }

    public void touch(String file) {
        Item<FileNode> node = FileSystemUtil.matchByName(file, recent.child);
        Optional.ofNullable(node).ifPresentOrElse(
                o -> System.out.println("touch: cannot create file '" + file +"': File exists"),
                () -> FileSystemUtil.addNode(recent, new FileNode(file, false)));
    }

    public void cat(String filename) {
        Item<FileNode> node = FileSystemUtil.matchByName(filename, recent.child);
        Optional.ofNullable(node).ifPresentOrElse(
                o -> System.out.println(o.v.content),
                () -> System.out.println("cat: '" + filename + "': No such file or directory"));
    }

    public void nano(String filename) {
        Item<FileNode> node = FileSystemUtil.matchByName(filename, recent.child);
        if (node != null) {
            System.out.println("Content: ");
            node.v.content = jin.nextLine();
            if (diskService.isNotEnough(node.v.content.length())) {
                System.out.println("You don't have enough free space");
            } else {
                diskService.free(node.v.blocks);
                node.v.blocks = diskService.allocate(node.v.content.length());
            }
        } else {
            System.out.println("nano: '" + filename + "': No such file or directory");
        }
    }

    public void rm(String filename) {
        Item<FileNode> node = FileSystemUtil.matchByName(filename, recent.child);
        if (node != null) {
            if (node.v.isdir) {
                System.out.println("rm: " + node.v.filename + ": is a directory");
                return;
            }
            diskService.free(node.v.blocks);
            recent.child.remove(node.index);
        } else {
            System.out.println("rm: cannot remove '" + filename + "': No such file or directory");
        }
    }

    public void ls() {
        System.out.println("Avail: " + diskService.getFreeSize() / 1024.0 + " KB\n");
        for (FileNode node : recent.child) {
            if (node.isdir) {
                System.out.println("\033[34m" + node.filename + "  \033[0m");
            } else {
                System.out.println(node.filename + "  ");
            }
        }
    }

    public String getPath() {
        return path;
    }
}
