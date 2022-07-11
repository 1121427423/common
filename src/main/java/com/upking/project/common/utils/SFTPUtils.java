package com.upking.project.common.utils;

import com.jcraft.jsch.*;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * @author king
 * @version 1.0
 * @className SFTPUtilss
 * @description TODO
 * @date 2022/6/18
 */
public class SFTPUtils {

    private static Logger logger = LoggerFactory.getLogger(SFTPUtils.class);

    private ChannelSftp sftp;

    private Session session;
    /**
     * SFTP 登录用户名
     */
    private String username;
    /**
     * SFTP 登录密码
     */
    private String password;
    /**
     * 私钥
     */
    private String privateKey;
    /**
     * SFTP 服务器地址IP地址
     */
    private String host;
    /**
     * SFTP 端口
     */
    private int port;


    /**
     * 构造基于密码认证的sftp对象
     */
    public SFTPUtils(String username, String password, String host, int port) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
    }

    /**
     * 构造基于秘钥认证的sftp对象
     */
    public SFTPUtils(String username, String host, int port, String privateKey) {
        this.username = username;
        this.host = host;
        this.port = port;
        this.privateKey = privateKey;
    }

    public SFTPUtils() {
    }

    /**
     * 连接sftp服务器
     */
    public void login() {
        try {
            JSch jsch = new JSch();
            if (privateKey != null) {
                /** 使用私钥登录 如果私钥需要密码打开，需要传入密码 */
                jsch.addIdentity(privateKey,"123456");
            }else {
                /** 使用密码登录 */
                session.setPassword(password);
            }

            session = jsch.getSession(username, host, port);

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");

            session.setConfig(config);
            session.connect(3000);

            Channel channel = session.openChannel("sftp");
            channel.connect(3000);

            sftp = (ChannelSftp) channel;
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭连接 server
     */
    public void logout() {
        if (sftp != null) {
            if (sftp.isConnected()) {
                sftp.disconnect();
            }
        }
        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
            }
        }
    }


    /**
     * 将输入流的数据上传到sftp作为文件。文件完整路径=basePath+directory
     *
     * @param directory    上传到该目录
     * @param sftpFileName sftp端文件名
     */
    public boolean upload(String directory, String sftpFileName, String localFileAbsPath) throws SftpException {
        try {
            if (directory != null && !"".equals(directory)) {
                sftp.cd(directory);
            }
            File file = new File(localFileAbsPath);
            FileInputStream is = new FileInputStream(file);
            sftp.put(is, sftpFileName);
            logger.info("文件上传成功：{} -> {}/{}", file.getAbsolutePath(), directory, sftpFileName);
            return true;
        } catch (FileNotFoundException | SftpException e) {
            e.printStackTrace();
            logger.info("文件上传失败：{} -> {}/{}", localFileAbsPath, directory, sftpFileName);
            return false;
        }
    }

    public void cd(String directory) throws SftpException {
        if (directory != null && !"".equals(directory) && !"/".equals(directory)) {
            sftp.cd(directory);
        }
    }


    /**
     * 下载文件。
     *
     * @param directory    下载目录
     * @param downloadFile 下载的文件
     * @param saveFile     存在本地的路径
     */
    public void download(String directory, String downloadFile, String saveFile) {
        File file = null;
        try {
            if (directory != null && !"".equals(directory)) {
                sftp.cd(directory);
            }
            file = new File(saveFile);
            sftp.get(downloadFile, new FileOutputStream(file));
            logger.info("文件下载成功：{}/{} -> {}", directory, downloadFile, file.getAbsolutePath());
        } catch (SftpException | FileNotFoundException e) {
            e.printStackTrace();
            if (file != null) {
                file.delete();
            }
            logger.info("文件下载失败：{}/{}", directory, downloadFile);
        }

    }

    /**
     * 下载文件
     *
     * @param directory    下载目录
     * @param downloadFile 下载的文件名
     * @return 字节数组
     */
    public byte[] download(String directory, String downloadFile) throws SftpException, IOException {
        if (directory != null && !"".equals(directory)) {
            sftp.cd(directory);
        }
        InputStream is = sftp.get(downloadFile);
        byte[] fileData = IOUtils.toByteArray(is);
        return fileData;
    }


    /**
     * 删除文件
     *
     * @param directory  要删除文件所在目录
     * @param deleteFile 要删除的文件
     */
    public void delete(String directory, String deleteFile) throws SftpException {
        if (directory != null && !"".equals(directory)) {
            sftp.cd(directory);
        }
        sftp.rm(deleteFile);
    }


    /**
     * 列出目录下的文件
     *
     * @param directory 要列出的目录
     */
    public Vector<?> listFiles(String directory) throws SftpException {
        return sftp.ls(directory);
    }

    /**
     * 查找目录下是否存在某文件
     * @param directory 查找的目录
     * @param fileName 查找的文件名
     * @return
     */
    public boolean isExistsFile(String directory, String fileName) {
        List<String> findFileList = new ArrayList<>();
        ChannelSftp.LsEntrySelector selector = new ChannelSftp.LsEntrySelector() {
            @Override
            public int select(ChannelSftp.LsEntry lsEntry) {
                if (lsEntry.getFilename().equals(fileName)) {
                    findFileList.add(fileName);
                }
                return 0;
            }
        };

        try {
            sftp.ls(directory, selector);
        } catch (SftpException e) {
            e.printStackTrace();
        }

        if (findFileList.size() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
