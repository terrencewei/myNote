package com.terrencewei.markdown.main;

import java.io.*;
import java.util.*;

/**
 * Created by terrencewei on 2017/04/13.
 */
public class GenerateTOC {

    // private static final String inputFolderPath =
    // "/home/terrencewei/Downloads/md_input";
    private static final String       inputFolderPath = "/home/terrencewei/Desktop/开发/";
    private static final List<String> inputFileNames  = new ArrayList<String>();
    static {
        inputFileNames.add("ATG.md");
        inputFileNames.add("Endeca.md");
        inputFileNames.add("Java.md");
        inputFileNames.add("Linux.md");
        inputFileNames.add("misc.md");
        inputFileNames.add("reactJS.md");
        inputFileNames.add("server.md");
        inputFileNames.add("SVN and Git.md");
        inputFileNames.add("Ubuntu.md");
        inputFileNames.add("Web前端.md");
        inputFileNames.add("云.md");
        inputFileNames.add("数据库.md");
        inputFileNames.add("阿里技术栈.md");
    }

    // private static final String outputFileFolder =
    // "/home/terrencewei/Downloads/md_output/";
    private static final String outputFileFolder         = "/home/terrencewei/Desktop/开发/";
    private static final String outputAllInOneFileName   = "ALL_IN_ONE.md";

    private static final String TOC_start_symbol         = "# 目录";
    private static final String TOC_end_symbol           = "***";
    private static boolean      TOC_start                = false;

    private static final String title_level_1            = "# ";
    private static final String title_level_2            = "## ";
    private static final String title_level_3            = "### ";
    private static final String title_level_4            = "#### ";
    private static final String title_level_5            = "##### ";

    private static final String title_level_1_replace    = "* ";
    private static final String title_level_2_replace    = "  * ";
    private static final String title_level_3_replace    = "    * ";
    private static final String title_level_4_replace    = "      * ";
    private static final String title_level_5_replace    = "        * ";

    private static final String code_block_symbol        = "```";

    private static List<String> singleFileTOCCache       = new ArrayList<String>();
    private static List<String> singleFileContentCache   = new ArrayList<String>();
    private static List<String> allInOneFileTOCCache     = new ArrayList<String>();
    private static List<String> allInOneFileContentCache = new ArrayList<String>();
    private static int          level1Count              = 0;
    private static int          level2Count              = 0;
    private static int          level3Count              = 0;
    private static int          level4Count              = 0;
    private static int          level5Count              = 0;
    private static boolean      isLevel1Start            = false;
    private static boolean      isLevel2Start            = false;
    private static boolean      isLevel3Start            = false;
    private static boolean      isLevel4Start            = false;
    private static boolean      isLevel5Start            = false;



    public static void main(String[] a) {

        allInOneFileTOCCache.clear();
        allInOneFileContentCache.clear();

        try {
            int filesCount = 0;
            List<String> filesName = new ArrayList<String>();

            List<File> files = Arrays.asList(new File(inputFolderPath).listFiles());
            Collections.sort(files, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if (o1.isDirectory() && o2.isFile())
                        return -1;
                    if (o1.isFile() && o2.isDirectory())
                        return 1;
                    return o1.getName().compareTo(o2.getName());
                }
            });
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".md") && inputFileNames.contains(file.getName())) {
                    readFile2Cache(file.getPath());

                    filesCount++;
                    filesName.add(file.getPath());

                    String singleFilePath = outputFileFolder + file.getName();
                    writeCache2File(singleFileTOCCache, singleFileContentCache, singleFilePath);
                }
            }

            String allInOneFilePath = outputFileFolder + outputAllInOneFileName;
            if (writeCache2File(allInOneFileTOCCache, allInOneFileContentCache, allInOneFilePath)) {

                System.out.println("------------------------------");
                for (String fileName : filesName) {
                    System.out.println("processed file: " + fileName);
                }

                System.out.println("ALL FINISHED!!! --> total: " + filesCount + " files");
                System.out.println("file output to --> " + allInOneFilePath);
            } else {
                System.out.println("Input file(s) is empty, no file be output!!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private static boolean writeCache2File(List<String> fileTOCCache, List<String> fileContentCache, String filePath)
            throws IOException {

        if (fileTOCCache.isEmpty() || fileContentCache.isEmpty() || filePath == null || filePath.length() <= 0) {
            return false;
        }
        OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(new File(filePath)));
        BufferedWriter bufferedWriter = new BufferedWriter(write);

        // output TOC
        boolean shouldOutoutTOC = false;
        bufferedWriter.write(TOC_start_symbol);
        bufferedWriter.newLine();
        for (String TOC : fileTOCCache) {
            if (TOC.startsWith(TOC_start_symbol)) {
                shouldOutoutTOC = true;
                continue;
            }
            if (TOC.startsWith(TOC_end_symbol)) {
                shouldOutoutTOC = false;
                continue;
            }
            if (shouldOutoutTOC) {
                bufferedWriter.write(TOC);
                bufferedWriter.newLine();
            }
        }
        bufferedWriter.write(TOC_end_symbol);
        bufferedWriter.newLine();

        // output content
        bufferedWriter.flush();
        for (String content : fileContentCache) {
            bufferedWriter.write(content);
            bufferedWriter.newLine();
        }

        // 1. the BufferedWriter use cache area to store data and when the
        // cache area is full, it will output the data to the target file
        // automatically.
        // 2. finally need call flush method again so the remaining data in
        // cache area will be output to the file
        bufferedWriter.flush();

        write.close();

        return true;
    }



    private static void readFile2Cache(String pFilePath) throws IOException {
        singleFileTOCCache.clear();
        singleFileContentCache.clear();

        InputStreamReader read = new InputStreamReader(new FileInputStream(new File(pFilePath)));

        BufferedReader bufferedReader = new BufferedReader(read);
        String lineTxt = null;
        addToFileTOCCache(TOC_start_symbol);

        boolean codeBlockStart = false;

        while ((lineTxt = bufferedReader.readLine()) != null) {

            if (lineTxt.startsWith(TOC_start_symbol)) {
                TOC_start = true;
            }
            if (lineTxt.startsWith(TOC_end_symbol)) {
                TOC_start = false;
                continue;
            }
            if (TOC_start) {
                continue;
            }

            if (lineTxt.trim().startsWith(code_block_symbol)) {
                codeBlockStart = !codeBlockStart;
            }

            if (!codeBlockStart) {
                if (lineTxt.startsWith(title_level_1)) {
                    dealWithTOC(1, lineTxt);
                } else if (lineTxt.startsWith(title_level_2)) {
                    dealWithTOC(2, lineTxt);
                } else if (lineTxt.startsWith(title_level_3)) {
                    dealWithTOC(3, lineTxt);
                } else if (lineTxt.startsWith(title_level_4)) {
                    dealWithTOC(4, lineTxt);
                } else if (lineTxt.startsWith(title_level_5)) {
                    dealWithTOC(5, lineTxt);
                } else {
                    addToFileContentCache(lineTxt);
                }
            } else {
                addToFileContentCache(lineTxt);
            }

        }
        read.close();
        addToFileTOCCache(TOC_end_symbol);
        addToFileTOCCache("\n");
    }



    private static void addToFileTOCCache(String pString) {
        allInOneFileTOCCache.add(pString);
        singleFileTOCCache.add(pString);
    }



    private static void addToFileContentCache(String pString) {
        allInOneFileContentCache.add(pString);
        singleFileContentCache.add(pString);
    }



    private static void dealWithTOC(int level, String lineTxt) {
        changeLevelCount(level);

        String linkText = lineTxt.replace(getTitleLevel(level), "");

        String linkTitle = linkText;
        String linkContent = formattedText(linkText);

        String titleSymbol = "";
        String contentSymbol = "";

        singleFileTOCCache.add(getTitleLevelReplace(level) + "[" + titleSymbol + linkTitle + "](#" + contentSymbol
                + linkContent + ")");
        singleFileContentCache.add(lineTxt.replace(getTitleLevel(level), getTitleLevel(level) + titleSymbol));

        titleSymbol = getTitleSymbol(level);
        contentSymbol = getContentSymbol(level);

        allInOneFileTOCCache.add(getTitleLevelReplace(level) + "[" + titleSymbol + linkTitle + "](#" + contentSymbol
                + linkContent + ")");
        allInOneFileContentCache.add(lineTxt.replace(getTitleLevel(level), getTitleLevel(level) + titleSymbol));

    }



    private static String getTitleSymbol(int level) {
        switch (level) {
        case 1:
            return level1Count + " ";
        case 2:
            return getLevelCount(1) + "." + getLevelCount(level) + " ";
        case 3:
            return getLevelCount(1) + "." + getLevelCount(2) + "." + getLevelCount(level) + " ";
        case 4:
            return getLevelCount(1) + "." + getLevelCount(2) + "." + getLevelCount(3) + "." + getLevelCount(level)
                    + " ";
        case 5:
            return getLevelCount(1) + "." + getLevelCount(2) + "." + getLevelCount(3) + "." + getLevelCount(4) + "."
                    + getLevelCount(level) + " ";
        default:
            return null;
        }
    }



    private static String getContentSymbol(int level) {
        switch (level) {
        case 1:
            return level1Count + "-";
        case 2:
            return getLevelCount(1) + "" + getLevelCount(level) + "-";
        case 3:
            return getLevelCount(1) + "" + getLevelCount(2) + "" + getLevelCount(level) + "-";
        case 4:
            return getLevelCount(1) + "" + getLevelCount(2) + "" + getLevelCount(3) + "" + getLevelCount(level) + "-";
        case 5:
            return getLevelCount(1) + "" + getLevelCount(2) + "" + getLevelCount(3) + "" + getLevelCount(4) + ""
                    + getLevelCount(level) + "-";
        default:
            return null;
        }
    }



    private static int getLevelCount(int level) {
        switch (level) {
        case 1:
            return level1Count;
        case 2:
            return level2Count;
        case 3:
            return level3Count;
        case 4:
            return level4Count;
        case 5:
            return level5Count;
        default:
            return 0;
        }
    }



    private static void changeLevelCount(int level) {
        switch (level) {
        case 1:
            level1Count++;
            level2Count = 0;
            break;
        case 2:
            level2Count++;
            level3Count = 0;
            break;
        case 3:
            level3Count++;
            level4Count = 0;
            break;
        case 4:
            level4Count++;
            level5Count = 0;
            break;
        case 5:
            level5Count++;
            break;
        }
    }



    private static String getTitleLevel(int level) {
        switch (level) {
        case 1:
            return title_level_1;
        case 2:
            return title_level_2;
        case 3:
            return title_level_3;
        case 4:
            return title_level_4;
        case 5:
            return title_level_5;
        default:
            return null;
        }
    }



    private static String getTitleLevelReplace(int level) {
        switch (level) {
        case 1:
            return title_level_1_replace;
        case 2:
            return title_level_2_replace;
        case 3:
            return title_level_3_replace;
        case 4:
            return title_level_4_replace;
        case 5:
            return title_level_5_replace;
        default:
            return null;
        }
    }



    private static String formattedText(String s) {
        return s.toLowerCase().replaceAll("\\s", "-").replaceAll("\\.", "").replaceAll("\\,", "").replaceAll("\\(", "")
                .replaceAll("\\)", "").replaceAll("\\/", "").replaceAll("\\\\", "").replaceAll("\\:", "")
                .replaceAll("\\`", "");
    }

}