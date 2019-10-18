package com.okguess.admin.api.utils;

import java.io.*;

public final class FileUtil {

    private FileUtil() {
    }

    /**
     * 保存文件
     *
     * @param path
     * @param stream
     */
    public static void save(String path, String fileName, InputStream stream) {

        FileUtil.createNewFolder(path, false);

        OutputStream bos = null;
        try {
            bos = new FileOutputStream(path + fileName);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new RuntimeException(ioe);
        } finally {
            try {
                stream.close();
                bos.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                throw new RuntimeException(ioe);
            }

        }
    }

    public static boolean save(String path, String fileName, String saveContent) {
        FileUtil.createNewFolder(path, false);
        File file = new File(path + fileName);
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file, true), "UTF-8"));
            writer.write(saveContent);
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除文件
     *
     * @param file
     * @param completely
     * @return
     */
    public static boolean delete(final File file, final boolean completely) {

        if (file.isDirectory()) {
            for (File i : file.listFiles()) {
                if (!delete(i, true)) {
                    return false;
                }
            }

        }

        if (completely && file.exists()) {
            return file.delete();
        }

        return true;
    }

    /**
     * 指定文件名删除
     *
     * @param file
     * @param completely
     * @return
     */
    public static boolean delete(final String file, final boolean completely) {

        return delete(new File(file), completely);
    }

    /**
     * 复制文件
     *
     * @param inputfile
     * @param outputfile
     */
    public static void copyFile(String inputfile, String outputfile) {
    }

    /**
     * 文件是否存在
     *
     * @param targetFileName
     * @return
     */
    public static boolean fileExistsChk(String targetFileName) {

        File targetFile = new File(targetFileName);
        if (targetFile.isFile()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean move(File file, String destPath) {
        createNewFolder(destPath, false);
        File dir = new File(destPath);
        boolean success = file.renameTo(new File(dir, file.getName()));
        delete(file, true);
        return success;
    }

    /**
     * 创建文件夹
     *
     * @param folderPath
     * @param isForced   true先删后建
     */
    public static void createNewFolder(String folderPath, boolean isForced) {

        File resultFolder = new File(folderPath);
        if (resultFolder.exists()) {

            if (isForced) {
                delete(resultFolder, true);
                resultFolder.mkdir();
            }
        } else {
            resultFolder.mkdirs();
        }

    }


    /**
     * 获取文件的内容
     */
    public static String getFileContent(String file, String encode) {
        if (!FileUtil.fileExistsChk(file)) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        BufferedReader bf = null;
        String line = null;
        try {
            bf = new BufferedReader(new InputStreamReader(new FileInputStream(
                    file), encode));
            line = bf.readLine();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        while (line != null) {
            result.append(line).append("\n");
            try {
                line = bf.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String re = result.toString();
        if (re.length() != 0) {
            re = re.toString().substring(0, result.length() - 1);
        }
        return re;
    }

    public static String getFileContent(InputStream in, String encode) {

        StringBuilder result = new StringBuilder();
        BufferedReader bf = null;
        String line = null;
        try {
            bf = new BufferedReader(new InputStreamReader(in, encode));
            line = bf.readLine();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        while (line != null) {
            result.append(line).append("\n");
            try {
                line = bf.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String re = result.toString();
        if (re.length() != 0) {
            re = re.toString().substring(0, result.length() - 1);
        }
        return re;
    }

}
