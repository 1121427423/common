package com.upking.project.common.utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author king
 * @version 1.0
 * @className FileUtils
 * @description TODO
 * @date 2022/6/18
 */
public class FileUtils {

    public static byte[] getBytes(InputStream is) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        baos.flush();
        baos.close();
        return baos.toByteArray();
    }

    /**
     * 以字符为单位读取文件，常用于读文本，数字等类型的文件
     */
    public static void readFileByChars(String fileName) {
        File file = new File(fileName);
        Reader reader = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            reader = new InputStreamReader(fileInputStream);
            int tempchar;
            while ((tempchar = reader.read()) != -1) {
                // 对于windows下，\r\n这两个字符在一起时，表示一个换行。
                // 但如果这两个字符分开显示时，会换两次行。
                // 因此，屏蔽掉\r，或者屏蔽\n。否则，将会多出很多空行。
                if (((char) tempchar) != '\r') {
                    System.out.print((char) tempchar);
                }
            }
        } catch (Exception e) {
            System.out.println("FileUtils readFileByChars=" + e.toString());
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println("FileUtils readFileByChars reader close()异常" + e.toString());
                }
            }
            if (null != fileInputStream) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    System.out.println("FileUtils readFileByChars fileInputStream close()异常" + e.toString());
                }
            }
        }

        try {
            char[] tempchars = new char[30];
            int charread = 0;
            reader = new InputStreamReader(new FileInputStream(fileName));
            // 读入多个字符到字符数组中，charread为一次读取字符数
            while ((charread = reader.read(tempchars)) != -1) {
                if ((charread == tempchars.length) && (tempchars[tempchars.length - 1] != '\r')) {
                    System.out.print(tempchars);
                } else {
                    for (int i = 0; i < charread; i++) {
                        if (tempchars[i] == '\r') {
                            continue;
                        } else {
                            System.out.print(tempchars[i]);
                        }
                    }
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /**
     * 读取文件内容
     *
     * @param fileName
     *            文件名
     * @param sEnterCode
     *            换行符，默认\n
     * @return
     */
    public static String readFileString(String fileName, String sEnterCode) {

        StringBuffer sFileContent = new StringBuffer();

        if (StringUtils.isBlank(sEnterCode)) {
            sEnterCode = "\n";
        }
        List<String> lines = readFileByLines(fileName);
        if (lines != null) {
            for (String sLine : lines) {
                sFileContent.append(sLine + sEnterCode);
            }
        }

        return sFileContent.toString();

    }

    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static ArrayList<String> readFileByLines(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return null;
        }

        File file = new File(fileName);
        return readFileByLines(file);
    }

    public static ArrayList<String> readFileByLines(File file) {
        BufferedReader reader = null;
        ArrayList<String> results = new ArrayList<String>();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tmp = null;
            while ((tmp = reader.readLine()) != null) {
                results.add(new String(tmp.getBytes(), "UTF-8"));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    System.out.println("FileUtils readFileByLines=" + e1.toString());
                }
            }
        }
        return results;
    }

    public static void readFileByNIO(String sFileName) {

        File file = new File(sFileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println(String.format("readFileByNIO IOException【%s】", e.toString()));
            }
        }
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(file);
            FileChannel fc = fis.getChannel();

            ByteBuffer readBuffer = ByteBuffer.allocate((int) fc.size());
            ByteBuffer[] readByteBuf = new ByteBuffer[] { readBuffer };

            fc.read(readByteBuf);
        } catch (FileNotFoundException e) {
            System.out.println(String.format("readFileByNIO FileNotFoundException【%s】", e.toString()));
        } catch (IOException e) {
            System.out.println(String.format("readFileByNIO IOException【%s】", e.toString()));
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e1) {
                    System.out.println("readFileByNIO IOException=" + e1.toString());
                }
            }
        }
    }

    public static void writeHtmlFile(String sFileName, String sTitle, String sKeywords, String sDescription, String sFileCont) {
        writeHtmlFile(sFileName, sTitle, sKeywords, sDescription, sFileCont, "UTF-8");
    }

    public static void writeHtmlFile(String sFileName, String sTitle, String sKeywords, String sDescription, String sFileCont,
                                     String sEncoding) {
        StringBuffer htmlBuffer = new StringBuffer();

        htmlBuffer.append("<!DOCTYPE html>\n");
        htmlBuffer.append("<!--[if IE 7 ]>		 <html class=\"no-js ie ie7 lte7 lte8 lte9\" lang=\"zh-cmn-Hans-CN\"> <![endif]-->\n");
        htmlBuffer.append("<!--[if IE 8 ]>		 <html class=\"no-js ie ie8 lte8 lte9\" lang=\"zh-cmn-Hans-CN\"> <![endif]-->\n");
        htmlBuffer.append("<!--[if IE 9 ]>		 <html class=\"no-js ie ie9 lte9>\" lang=\"zh-cmn-Hans-CN\"> <![endif]-->\n");
        htmlBuffer.append("<!--[if (gt IE 9)|!(IE)]><!--><html class=\"no-js\" lang=\"zh-cmn-Hans-CN\"> <!--<![endif]-->\n");
        htmlBuffer.append("<html lang=\"zh-cmn-Hans-CN\">\n");
        htmlBuffer.append("<head>\n");
        htmlBuffer.append(" <meta charset=\"" + sEncoding + "\"/>\n");
        htmlBuffer.append("	<meta name=\"renderer\" content=\"webkit\"/>\n");
        htmlBuffer.append("	<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\" />\n");
        htmlBuffer.append("	\n");
        htmlBuffer.append("	<meta name=\"robots\" content=\"noodp, noydir\">\n");
        htmlBuffer.append("	<meta http-equiv=\"Cache-Control\" content=\"no-cache, no-store, must-revalidate\">\n");
        htmlBuffer.append("	<meta http-equiv=\"Pragma\" content=\"no-cache\">\n");
        htmlBuffer.append(" <meta http-equiv=\"Expires\" content=\"-1\">\n");
        htmlBuffer.append("	<meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + sEncoding + "\">\n");
        htmlBuffer.append("	<meta content=\"" + sKeywords + "\" name=\"keywords\">\n");
        htmlBuffer.append("	<meta content=\"" + sDescription + "\" name=\"description\">\n");
        htmlBuffer.append("\n");
        htmlBuffer.append("    <title>" + sTitle + "</title>\n");
        htmlBuffer.append("</head>\n");
        htmlBuffer.append("<body>\n");
        htmlBuffer.append(sFileCont + "\n");
        htmlBuffer.append("</body>\n");
        writeFile(sFileName, htmlBuffer.toString(), sEncoding);
    }

    public static void writeFile(String sFileName, String sFileCont) {
        writeFile(sFileName, sFileCont, "UTF-8");
    }

    public static void writeFile(String sFileName, String sFileCont, String sEncoding) {
        BufferedWriter out = null;
        OutputStreamWriter write = null;
        FileOutputStream fileOutputStream = null;
        try {
            File writename = new File(sFileName);
            if (!writename.getParentFile().exists()) {
                if (!writename.getParentFile().mkdirs()) {
                }
            }
            writename.createNewFile(); // 创建新文件
            fileOutputStream = new FileOutputStream(writename);
            write = new OutputStreamWriter(fileOutputStream, sEncoding);
            out = new BufferedWriter(write);
            // out= new BufferedWriter(new FileWriter(writename));
            out.write(sFileCont);
            out.flush();
            out.close();
        } catch (IOException e) {
            System.out.println(String.format("writeFile IOException【%s】", e.toString()));
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e1) {
                    System.out.println(String.format("writeFile IOException【%s】", e1.toString()));
                }
            }
            if (write != null) {
                try {
                    write.close();
                } catch (IOException e1) {
                    System.out.println(String.format("writeFile IOException【%s】", e1.toString()));
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e1) {
                    System.out.println(String.format("writeFile IOException【%s】", e1.toString()));
                }
            }
        }
    }

    public static void writeFileByNIO(String sFileName, String sFileCont) {
        writeFileByNIO(sFileName, sFileCont, "UTF-8");
    }

    public static void writeFileByNIO(String sFileName, String sFileCont, String sEncoding) {

        try {
            ByteBuffer contentBuf = ByteBuffer.wrap(sFileCont.getBytes(sEncoding));
            ByteBuffer[] byteBufs = new ByteBuffer[] { contentBuf };

            File file = new File(sFileName);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                FileChannel fc = fos.getChannel();
                fc.write(byteBufs);
            } catch (FileNotFoundException e) {
                System.out.println(String.format("writeFileByNIO FileNotFoundException【%s】", e.toString()));
            } catch (IOException e) {
                System.out.println(String.format("writeFileByNIO IOException【%s】", e.toString()));
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e1) {
                        System.out.println(String.format("writeFileByNIO IOException【%s】", e1.toString()));
                    }
                }
            }

        } catch (UnsupportedEncodingException e) {
            System.out.println(String.format("writeFileByNIO UnsupportedEncodingException【%s】", e.toString()));
        }

    }

    public static void delFile(String sFileName) {
        File file = new File(sFileName);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 删除目录下面的所有文件
     * @param filePath
     */
    public static void delAllFile(String filePath){
        File file = new File(filePath);
        if (file.exists()){
            //是文件直接删除
            file.delete();
        }

        //是文件夹
        if (file.isDirectory()){
            //遍历文件夹内部文件 全部删除
            deleteDirectoryFile(file, filePath);
            //删除文件夹
            file.delete();
        }

        System.out.printf("文件删除完成...");
    }

    /**
     * 删除文件夹下的所有文件
     * @param file 文件夹
     * @param filePath 文件夹路径
     */
    private static void deleteDirectoryFile(File file, String filePath){
        String[] temList = file.list();
        File tmpFile = null;
        for (int i = 0; i < temList.length; i++){
            if (filePath.endsWith(File.separator)){
                tmpFile = new File(filePath + temList[i]);
            } else {
                tmpFile = new File(filePath + File.separator + temList[i]);
            }
            if (tmpFile.isFile()){
                tmpFile.delete();
            } else if (tmpFile.isDirectory()){
                delAllFile(filePath + File.separator + temList[i]);
                tmpFile.delete();
            }
        }
    }

    public static File createDir(String fileName) throws IOException {
        File dir = new File(fileName);
        dir.mkdir();
        return dir;
    }

    /**
     * 去掉字符串右边的空格
     *
     * @param str
     *            要处理的字符串
     * @return 处理后的字符串
     */
    public static String rightTrim(String str) {
        if (str == null) {
            return "";
        }
        int length = str.length();
        for (int i = length - 1; i >= 0; i--) {
            if (str.charAt(i) != 0x20) {
                break;
            }
            length--;
        }
        return str.substring(0, length);
    }

    /**
     * 获取文件类型
     *
     * @param f
     * @return
     */
    public static String getMIMEType(File f) {
        String type = "";
        String sFileName = f.getName();
        String end = sFileName.substring(sFileName.lastIndexOf(".") + 1, sFileName.length()).toLowerCase();
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            type = "audio";
        } else if (end.equals("3gp") || end.equals("mp4")) {
            type = "video";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")) {
            type = "image";
        } else if (end.equals("apk")) {
            /* android.permission.INSTALL_PACKAGES */
            type = "application/vnd.android.package-archive";
        } else {
            type = "*";
        }
        if (end.equals("apk")) {
        } else {
            type += "/*";
        }
        return type;
    }

    /**
     * 获取文件，如果不存在，直接创建
     *
     * @param fileName
     * @return
     */
    public static File getOrCreateFile(String fileName) {
        File directFile = new File(fileName);
        if (!directFile.exists()) {
            directFile.mkdirs();
        }
        return directFile;
    }

    /**
     * 检查文件是否存在
     *
     * @param fileName
     * @return
     */
    public static boolean checkFileExist(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return false;
        }
        return true;
    }
}
