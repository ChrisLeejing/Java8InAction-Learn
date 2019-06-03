package com.chris.chap1;

import java.io.File;
import java.io.FileFilter;

/**
 * This is description.
 *
 * @author Chris Lee
 * @date 2019/6/4 7:02
 */
public class HiddenFile {
    public static void main(String[] args) {
        File[] hiddenFiles = new File("C:\\Users\\75756\\Desktop\\Java8 实战\\HiddenFile").listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isHidden();
            }
        });

        File[] hiddenFiles2 = new File("C:\\Users\\75756\\Desktop\\Java8 实战\\HiddenFile").listFiles(File::isHidden);
        for (File hiddenFile : hiddenFiles) {
            System.out.println(hiddenFile.getPath());
        }

        for (File hiddenFile : hiddenFiles2) {
            System.out.println(hiddenFile.getPath());
        }
    }
}
