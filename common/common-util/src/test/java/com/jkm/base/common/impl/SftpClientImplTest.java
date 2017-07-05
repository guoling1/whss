package com.jkm.base.common.impl;

import com.jkm.base.common.sftp.SftpClient;
import com.jkm.base.common.sftp.impl.SftpClientImpl;
import com.jkm.base.common.sftp.session.SftpSessionFactoryImpl;
import com.jkm.base.common.sftp.session.helper.CreateSessionParams;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * Created by hutao on 15/12/3.
 * 下午4:20
 */
@Ignore
public class SftpClientImplTest {
    @Test
    public void testDownloadFile() throws Exception {
        try (final SftpClient sftpClient = getSftpClient()) {
            final List<String> strings = sftpClient.ls("/LT/files");
            sftpClient.downloadFile("/LT/files/ZQC000003_20151223.txt",
                    "/Users/hutao/work/companyWorkPlace/work/jinkaimen/ZQC000003_20151223.txt");
            /*sftpClient.downloadFile("/LT/files/ZQC000003_20151203.txt",
                    "/Users/hutao/work/companyWorkPlace/work/jinkaimen/ZQC000003_20151203.txt");
            sftpClient.downloadFile("/LT/files/ZQC000003_20151203_01.txt",
                    "/Users/hutao/work/companyWorkPlace/work/jinkaimen/ZQC000003_20151203_01.txt");
            sftpClient.downloadFile("/LT/files/ZQC000003_20151203_02.txt",
                    "/Users/hutao/work/companyWorkPlace/work/jinkaimen/ZQC000003_20151203_02.txt");
            sftpClient.downloadFile("/LT/files/ZQC000003_20151203_03.txt",
                    "/Users/hutao/work/companyWorkPlace/work/jinkaimen/ZQC000003_20151203_03.txt");

            sftpClient.downloadFile("/LT/files/ZQC000003_20151208_03_03.txt",
                    "/Users/hutao/work/companyWorkPlace/work/jinkaimen/ZQC000003_20151208_03_03.txt");
            sftpClient.downloadFile("/LT/files/ZQC000003_20151208_03_08.txt",
                    "/Users/hutao/work/companyWorkPlace/work/jinkaimen/ZQC000003_20151208_03_08.txt");*/
            /*sftpClient.downloadFile("/LT/files/ZQC000003_20151208_03_05.txt",
                    "/Users/hutao/work/companyWorkPlace/work/jinkaimen/ZQC000003_20151208_03_05.txt");
            sftpClient.downloadFile("/LT/files/ZQC000003_20151208_03_06.txt",
                    "/Users/hutao/work/companyWorkPlace/work/jinkaimen/ZQC000003_20151208_03_06.txt");*/

            System.out.println(strings);
        }
    }

    private SftpClient getSftpClient() {
        final SftpClient sftpClient = new SftpClientImpl(new SftpSessionFactoryImpl(CreateSessionParams.buildByPassword(
                "66money", "v7357pvf")));
        sftpClient.connect("114.141.132.226", 22);
        return sftpClient;
    }

    @Test
    public void testName() throws Exception {
        final String filename = "/Users/hutao/work/companyWorkPlace/work/jinkaimen/ZQC000003_20151203_03.txt";
        final String filename1 = "/Users/hutao/work/companyWorkPlace/work/jinkaimen/ZQC000003_20151208_03_03.txt";
        final String filename2 = "/Users/hutao/work/companyWorkPlace/work/jinkaimen/ZQC000003_20151208_03_08.txt";
        final LineIterator lineIterator = FileUtils.lineIterator(new File(filename), "utf-8");
        while (lineIterator.hasNext()) {
            final String line = lineIterator.nextLine();
            System.out.println(line);
        }
    }

    @Test
    public void testName1() throws Exception {
        final SftpClient sftpClient = getSftpClient();
        final boolean fileExist = sftpClient.isFileExist("/LT/files/ZQC000003_20151204_03.txt");
        System.out.println(fileExist);
    }
}