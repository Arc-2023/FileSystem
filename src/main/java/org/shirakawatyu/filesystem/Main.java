package org.shirakawatyu.filesystem;

import org.shirakawatyu.filesystem.service.FileSystemService;

import java.util.Scanner;




public class Main {
    static Scanner jin = new Scanner(System.in);
    static FileSystemService fileSystemService = new FileSystemService(64 * 1024 * 1024, jin);

    public static void main(String[] args) {
        fileSystemService.createRoot();
        System.out.println();
        System.out.println("=======================================");
        System.out.println("touch [filename] - 创建         ");
        System.out.println("cat [filename] - 读取            ");
        System.out.println("nano [filename] - 写入           ");
        System.out.println("rm [filename] - 删除文件      ");
        System.out.println("rm -rf [dir] - 删除目录          ");
        System.out.println("mkdir [dirname] - 建立目录       ");
        System.out.println("cd [dir] - 切换目录          ");
        System.out.println("ls [dir] - 显示目录         ");
        System.out.println("exit - 退出登录      ");
        System.out.println("=======================================");
        System.out.println();
        run();
    }

    static void run() {
        String[] command;
        while (true) {
            System.out.print("user@pc:" + fileSystemService.getPath() + "# ");
            command = jin.nextLine().split(" ");
            switch (command[0]) {
                case "mkdir" -> fileSystemService.mkdir(command[1]);
                case "ls" -> fileSystemService.ls();
                case "cd" -> fileSystemService.cd(command[1]);
                case "touch" -> fileSystemService.touch(command[1]);
                case "cat" -> fileSystemService.cat(command[1]);
                case "nano" -> fileSystemService.nano(command[1]);
                case "rm" -> {
                    if (command.length == 2)
                        fileSystemService.rm(command[1]);
                    else if (command[1].equals("-rf"))
                        fileSystemService.rmrf(command[2]);
                }
                case "exit" -> {
                    return;
                }
            }
        }
    }
}