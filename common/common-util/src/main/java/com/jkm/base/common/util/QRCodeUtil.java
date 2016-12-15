package com.jkm.base.common.util;

import com.google.common.base.Preconditions;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.sun.imageio.plugins.jpeg.JPEGImageWriter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Element;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yulong.zhang on 2016/11/30.
 *
 * 二维码
 */
@Slf4j
@UtilityClass
public class QRCodeUtil {

    /**
     * 二维码宽度
     */
    private static final int QR_CODE_WIDTH = 700;

    /**
     * 二维码高度
     */
    private static final int QR_CODE_HEIGHT = 700;

    /**
     * 默认后缀
     */
    public static final String DEFAULT_IMAGE_SUFFIX = "jpg";

    /**
     * 判断图片后缀是否允许
     *
     * @param suffix
     * @return
     */
    private static boolean isImageSuffix(final String suffix) {
        List<String> allowType =
                Arrays.asList("png", "gif", "jpg", "jpeg", "x-png", "pjpeg", "svg");
        return allowType.contains(suffix);
    }

    /**
     * 生成二维码图像
     *
     * @param directFile  目录
     * @param url         二维码指定的url
     * @return
     */
    public static byte[] generateCodeByte(final String directFile, final String url, final String code){
        final String filePath = generateCode(directFile, url, code);
        final byte[] bytes = FileUtil.readBinary(new File(filePath));
        FileUtils.deleteQuietly(new File(filePath));
        return bytes;
    }


    /**
     * 生成二维码图像
     *
     * @param directFile  目录
     * @param url         二维码指定的url
     * @return
     */
    public static String generateCode(final String directFile, final String url, final String code){
        return generateCode(directFile, url, code, DEFAULT_IMAGE_SUFFIX);
    }

    /**
     * 生成二维码图像
     *
     * @param directFile  目录
     * @param url         二维码指定的url
     * @param suffix      二维码image后缀
     * @return
     */
    public static String generateCode(final String directFile, final String url, final String code, final String suffix){
        FileOutputStream fops = null;
        try {
            final Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 0);
//            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            final BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, QR_CODE_WIDTH, QR_CODE_HEIGHT, hints);
            final BufferedImage image = new BufferedImage(QR_CODE_WIDTH, QR_CODE_HEIGHT, BufferedImage.TYPE_INT_RGB);
            final File qrCodeFile = new File(directFile + File.separator + code + "." + suffix);
            ImageIO.write(image, suffix, qrCodeFile);
            fops = new FileOutputStream(qrCodeFile);
            MatrixToImageWriter.writeToStream(bitMatrix, suffix, fops);
            modifyDPI(qrCodeFile.getAbsolutePath(), suffix);
            return qrCodeFile.getAbsolutePath();
        } catch (final Exception e) {
            log.error("generate QR code error", e);
            e.printStackTrace();
        } finally {
            if (fops != null) {
                try {
                    fops.close();
                } catch (final IOException e) {
                    log.error("close fileOutputStream error", e);
                    e.printStackTrace();
                }
            }
        }
        log.warn("generate QR code exception");
        return "";
    }

    /**
     * 解析
     *
     * @param filePath
     * @return
     */
    public static String  parseCode(final String filePath) {
        Preconditions.checkState(filePath.length() != 0, "图片路径为空!");
        Preconditions.checkState(isImageSuffix(filePath.substring(filePath.lastIndexOf("."))), "图片格式错误!");
        try {
            final FileInputStream fileInputStream = new FileInputStream(filePath);
            final BufferedImage bufferedImage = ImageIO.read(fileInputStream);
            final LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            final Binarizer binarizer = new HybridBinarizer(source);
            final BinaryBitmap bitmap = new BinaryBitmap(binarizer);
            final Map<DecodeHintType, Object> hints = new HashMap<>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            final Result result = new MultiFormatReader().decode(bitmap, hints);
            fileInputStream.close();
            return result.getText();
        } catch (final Exception e) {
            log.error("parse QR code error", e);
            e.printStackTrace();
        }
        log.warn("parse QR code exception");
        return "";
    }

    /**
     * 获取临时路径
     *
     * @return
     */
    public static String getTempDir() {
        final String dir = System.getProperty("java.io.tmpdir") + "hss" + File.separator + "qr" + File.separator + "code";
        final File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dir;
    }

    /**
     * 将文件删除
     */
    public static void deleteFile(final File directFile) {
        FileUtils.deleteQuietly(directFile);
    }


    /**
     * 修改dpi
     *
     * @param filePath
     * @param suffix
     */
    private static void modifyDPI(final String filePath, final String suffix) {
        ImageOutputStream ios = null;
        FileOutputStream fops = null;
        try {

            final BufferedImage image = ImageIO.read(new File(filePath));
            final JPEGImageWriter imageWriter = (JPEGImageWriter) ImageIO.getImageWritersBySuffix(suffix).next();
            fops = new FileOutputStream(new File(filePath));
            ios = ImageIO.createImageOutputStream(fops);
            imageWriter.setOutput(ios);
            final JPEGImageWriteParam jpegParams = (JPEGImageWriteParam) imageWriter.getDefaultWriteParam();
            IIOMetadata imageMetaData = imageWriter.getDefaultImageMetadata(new ImageTypeSpecifier(image), jpegParams);
            final Element tree = (Element) imageMetaData.getAsTree("javax_imageio_jpeg_image_1.0");
            final Element jfif = (Element)tree.getElementsByTagName("app0JFIF").item(0);
            jfif.setAttribute("Xdensity", Integer.toString(2 * QR_CODE_WIDTH));
            jfif.setAttribute("Ydensity", Integer.toString(2 * QR_CODE_HEIGHT));
            jfif.setAttribute("resUnits", "1");
            imageMetaData.mergeTree("javax_imageio_jpeg_image_1.0", tree);
            imageWriter.write(imageMetaData, new IIOImage(image, null, imageMetaData), jpegParams);
            imageWriter.dispose();
        } catch (final IOException e) {
            log.error("modify qr code dpi error", e);
            e.printStackTrace();
        } finally {
            if (ios != null) {
                try {
                    ios.close();
                } catch (final IOException e) {
                    log.error("close ImageOutputStream error", e);
                    e.printStackTrace();
                }
            }
            if (fops != null) {
                try {
                    fops.close();
                } catch (final IOException e) {
                    log.error("close fileOutputStream error", e);
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        final String tempDir = getTempDir();
        System.out.println(generateCode(tempDir, "http://hss.qianbaojiajia.com/code/scanCode?code=100010060147&sign=e62fd5b945978ca18db19b7d05074fb26c225643d0472662c206bc529b0dab0a", "2"));
//        System.out.println(parseCode("C:\\Users\\YULONG~1.ZHA\\AppData\\Local\\Temp\\hss\\qr\\code\\1.jpg"));
    }
}
