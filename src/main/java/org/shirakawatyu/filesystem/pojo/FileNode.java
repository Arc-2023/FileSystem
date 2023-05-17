package org.shirakawatyu.filesystem.pojo;

import java.util.ArrayList;

public class FileNode {
    public String filename;
    public boolean isdir; //判断是目录还是文件
    public String content; //内容
    public FileNode parent; //父节点
    public ArrayList<FileNode> child; // 子节点
    public ArrayList<DiskBlock> blocks; // 盘块

    public FileNode(String filename, boolean isdir) {
        this.filename = filename;
        this.isdir = isdir;
        this.parent = null;
        this.content = "";
        this.child = new ArrayList<>();
        this.blocks = new ArrayList<>();
    }
}