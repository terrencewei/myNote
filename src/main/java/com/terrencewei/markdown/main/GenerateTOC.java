package markdown;

import java.io.*;
import java.util.*;

/**
 * Created by terrencewei on 2017/04/13.
 */
public class GenerateTOC {

    // config for input
    private static final String       CONFIG_inputFolderPath        = "/home/terrencewei/Downloads/md_input";
    private static final boolean      CONFIG_validateInputFileNames = false;
    private static final List<String> CONFIG_inputFileNames         = new ArrayList<String>();
    static {
        CONFIG_inputFileNames.add("ATG.md");
        CONFIG_inputFileNames.add("Endeca.md");
        CONFIG_inputFileNames.add("Java.md");
        CONFIG_inputFileNames.add("Linux.md");
        CONFIG_inputFileNames.add("misc.md");
        CONFIG_inputFileNames.add("reactJS.md");
        CONFIG_inputFileNames.add("server.md");
        CONFIG_inputFileNames.add("SVN and Git.md");
        CONFIG_inputFileNames.add("Ubuntu.md");
        CONFIG_inputFileNames.add("Web前端.md");
        CONFIG_inputFileNames.add("云.md");
        CONFIG_inputFileNames.add("数据库.md");
        CONFIG_inputFileNames.add("阿里技术栈.md");
    }
    // config for output
    private static final boolean CONFIG_outputSingleFile       = false;
    private static final boolean CONFIG_outputAllInOneFile     = true;
    private static final String  CONFIG_outputFileFolder       = "/home/terrencewei/Downloads/md_output/";
    private static final String  CONFIG_outputAllInOneFileName = "ALL_IN_ONE.md";

    // auxiliary var
    private static final String  toc_start_symbol              = "# 目录";
    private static final String  toc_end_symbol                = "***";

    private static final String  title_level_1                 = "# ";
    private static final String  title_level_2                 = "## ";
    private static final String  title_level_3                 = "### ";
    private static final String  title_level_4                 = "#### ";
    private static final String  title_level_5                 = "##### ";

    private static final String  title_level_1_replace         = "* ";
    private static final String  title_level_2_replace         = "  * ";
    private static final String  title_level_3_replace         = "    * ";
    private static final String  title_level_4_replace         = "      * ";
    private static final String  title_level_5_replace         = "        * ";

    private static final String  code_block_symbol             = "```";

    private static List<String>  singleFileTOCCache            = new ArrayList<String>();
    private static List<String>  singleFileContentCache        = new ArrayList<String>();
    private static List<String>  allInOneFileTOCCache          = new ArrayList<String>();
    private static List<String>  allInOneFileContentCache      = new ArrayList<String>();
    private static int           level1Count                   = 0;
    private static int           level2Count                   = 0;
    private static int           level3Count                   = 0;
    private static int           level4Count                   = 0;
    private static int           level5Count                   = 0;
    private static boolean       isLevel1Start                 = false;
    private static boolean       isLevel2Start                 = false;
    private static boolean       isLevel3Start                 = false;
    private static boolean       isLevel4Start                 = false;
    private static boolean       isLevel5Start                 = false;



    public static void main(String[] a) {

        init();

        try {
            int filesCount = 0;
            List<String> filesName = new ArrayList<String>();

            List<File> files = Arrays.asList(new File(CONFIG_inputFolderPath).listFiles());
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
                if (file.isFile() && file.getName().endsWith(".md")
                        && (!CONFIG_validateInputFileNames || CONFIG_inputFileNames.contains(file.getName()))) {
                    readFile2Cache(file);

                    filesCount++;
                    filesName.add(file.getPath());

                    outputSingleFileWithTOC(file);
                }
            }

            outputAllInOneFile(filesCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private static void outputAllInOneFile(int pFilesCount) throws IOException {
        if (CONFIG_outputAllInOneFile) {
            String allInOneFilePath = CONFIG_outputFileFolder + CONFIG_outputAllInOneFileName;
            if (writeCache2File(allInOneFileTOCCache, allInOneFileContentCache, allInOneFilePath)) {
                System.out.println("------------------------------");
                System.out.println("All in one file output SUCCESS!!! --> total: " + pFilesCount + " files");
                System.out.println("file output to --> " + allInOneFilePath);
            } else {
                System.out.println("all in one file output FAILED!!! --> " + allInOneFilePath);
            }
        }
    }



    private static void outputSingleFileWithTOC(File file) throws IOException {
        if (CONFIG_outputSingleFile) {
            String singleFilePath = CONFIG_outputFileFolder + file.getName();
            boolean outputSuccess = writeCache2File(singleFileTOCCache, singleFileContentCache, singleFilePath);
            if (outputSuccess) {
                System.out.println("Sinle file output SUCCESS!!!");
                System.out.println("file output to --> " + singleFilePath);
            } else {
                System.out.println("Sinle file output FAILED!!! --> " + singleFilePath);
            }
        }
    }



    private static void init() {
        // clear the cache
        allInOneFileTOCCache.clear();
        allInOneFileContentCache.clear();
        // make empty dirs
        File dir = new File(CONFIG_inputFolderPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        dir = new File(CONFIG_outputFileFolder);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }



    private static boolean writeCache2File(List<String> fileTOCCache, List<String> fileContentCache, String filePath)
            throws IOException {

        long startTime = System.currentTimeMillis();
        System.out.println("start process file: " + filePath);
        if (fileTOCCache.isEmpty() || fileContentCache.isEmpty() || filePath == null || filePath.length() <= 0) {
            System.out.println("end process file: " + filePath + ", total " + getHumanTime(startTime));
            return false;
        }
        OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(new File(filePath)));
        BufferedWriter bufferedWriter = new BufferedWriter(write);

        // output TOC
        boolean shouldOutoutTOC = false;
        bufferedWriter.write(toc_start_symbol);
        bufferedWriter.newLine();
        for (String TOC : fileTOCCache) {
            if (TOC.startsWith(toc_start_symbol)) {
                shouldOutoutTOC = true;
                continue;
            }
            if (TOC.startsWith(toc_end_symbol)) {
                shouldOutoutTOC = false;
                continue;
            }
            if (shouldOutoutTOC) {
                bufferedWriter.write(TOC);
                bufferedWriter.newLine();
            }
        }
        bufferedWriter.write(toc_end_symbol);
        bufferedWriter.newLine();

        // output content
        bufferedWriter.flush();
        for (String content : fileContentCache) {
            bufferedWriter.write(content);
            bufferedWriter.newLine();
        }

        // the BufferedWriter use cache area to store data and when the cache area is
        // full, it will output the data to the target file automatically, so finally
        // need call 'bufferedWriter.flush()' method again so the remaining data in
        // cache area will be output to the file
        bufferedWriter.flush();

        write.close();

        System.out.println("end process file: " + filePath + ", total time: " + getHumanTime(startTime));
        return true;
    }



    private static String getHumanTime(long pStartTime) {
        long endTime = System.currentTimeMillis() - pStartTime;
        long days = endTime / (1000 * 60 * 60 * 24);
        long hours = (endTime % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (endTime % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (endTime % (1000 * 60)) / 1000;
        long milliseconds = endTime;
        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days).append(" days ");
        }
        if (hours > 0) {
            sb.append(hours).append(" hours ");
        }
        if (minutes > 0) {
            sb.append(minutes).append(" minutes ");
        }
        sb.append((double) seconds + ((double) milliseconds / 1000)).append(" seconds ");
        return sb.toString();
    }



    private static void readFile2Cache(File pFile) throws IOException {
        updateCache("clear", "single", "toc");
        updateCache("clear", "single", "content");

        InputStreamReader read = new InputStreamReader(new FileInputStream(new File(pFile.getPath())));

        BufferedReader bufferedReader = new BufferedReader(read);
        String lineTxt = null;
        updateCache("add", "single", "toc", toc_start_symbol);
        updateCache("add", "all", "toc", toc_start_symbol);

        boolean codeBlockStart = false;
        boolean TOCStart = false;

        // use file name as level 1 title
        dealWithTOC(1, "# " + pFile.getName().substring(0, pFile.getName().indexOf(".md")));

        while ((lineTxt = bufferedReader.readLine()) != null) {

            if (lineTxt.startsWith(toc_start_symbol)) {
                TOCStart = true;
            }
            if (lineTxt.startsWith(toc_end_symbol)) {
                TOCStart = false;
                continue;
            }
            if (TOCStart) {
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
                    updateCache("add", "single", "content", lineTxt);
                    updateCache("add", "all", "content", lineTxt);
                }
            } else {
                updateCache("add", "single", "content", lineTxt);
                updateCache("add", "all", "content", lineTxt);
            }

        }
        read.close();
        updateCache("add", "single", "toc", toc_end_symbol);
        updateCache("add", "all", "toc", toc_end_symbol);
        updateCache("add", "single", "toc", "\n");
        updateCache("add", "all", "toc", "\n");
    }



    private static void updateCache(String pAction, String pCacheType, String pFileType) {
        updateCache(pAction, pCacheType, pFileType, null);
    }



    private static void updateCache(String pAction, String pFileType, String pCacheType, String pString) {
        if (pAction == "add") {
            if (pFileType == "single" && CONFIG_outputSingleFile) {
                if (pCacheType == "toc") {
                    singleFileTOCCache.add(pString);
                } else if (pCacheType == "content") {
                    singleFileContentCache.add(pString);
                }
            } else if (pFileType == "all" && CONFIG_outputAllInOneFile) {
                if (pCacheType == "toc") {
                    allInOneFileTOCCache.add(pString);
                } else if (pCacheType == "content") {
                    allInOneFileContentCache.add(pString);
                }
            }
        } else if (pAction == "clear") {
            if (pFileType == "single" && CONFIG_outputSingleFile) {
                if (pCacheType == "toc") {
                    singleFileTOCCache.clear();
                } else if (pCacheType == "content") {
                    singleFileContentCache.clear();
                }
            } else if (pFileType == "all" && CONFIG_outputAllInOneFile) {
                if (pCacheType == "toc") {
                    allInOneFileTOCCache.clear();
                } else if (pCacheType == "content") {
                    allInOneFileContentCache.clear();
                }
            }
        }
    }



    private static void dealWithTOC(int level, String lineTxt) {
        changeLevelCount(level);

        String linkText = lineTxt.replace(getTitleLevel(level), "");

        String linkTitle = linkText;
        String linkContent = formattedText(linkText);

        String titleSymbol = "";
        String contentSymbol = "";

        updateCache("add", "single", "toc", getTitleLevelReplace(level) + "[" + titleSymbol + linkTitle + "](#"
                + contentSymbol + linkContent + ")");
        updateCache("add", "single", "content",
                lineTxt.replace(getTitleLevel(level), getTitleLevel(level) + titleSymbol));

        titleSymbol = getTitleSymbol(level);
        contentSymbol = getContentSymbol(level);

        updateCache("add", "all", "toc", getTitleLevelReplace(level) + "[" + titleSymbol + linkTitle + "](#"
                + contentSymbol + linkContent + ")");
        updateCache("add", "all", "content", lineTxt.replace(getTitleLevel(level), getTitleLevel(level) + titleSymbol));

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
