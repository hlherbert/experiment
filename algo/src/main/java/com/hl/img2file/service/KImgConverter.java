package com.hl.img2file.service;

import com.hl.img2file.exception.ImageHasNoBorderException;
import com.hl.img2file.model.ColorConstant;
import com.hl.img2file.model.KImg;
import com.hl.img2file.model.KImgConvertParam;
import com.hl.img2file.model.KImgHeader;
import com.hl.img2file.model.Position;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.InvalidParameterException;
import java.text.MessageFormat;

/**
 * 文件-图像转换器
 */
public class KImgConverter {

    /**
     * 图片最小大小，由头部决定
     */
    public static final int KIMG_MINSIZE = 256;


    public static KImg fileToKimg(File file, KImgConvertParam param) throws IOException {
        // check
        checkKimgConvertParam(file, param);

        KImg kImg = new KImg();
        // header
        KImgHeader header = new KImgHeader();
        header.setFilename(file.getName());
        header.setWidth(param.getWidth());
        header.setHeight(param.getHeight());
        long size = file.length();
        if (size > Integer.MAX_VALUE) {
            throw new IOException("file too large");
        }
        header.setDataByteSize((int) size);
        kImg.setHeader(header);

        // data
        try (FileInputStream fs = new FileInputStream(file);
             FileChannel channel = fs.getChannel()) {
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
            while ((channel.read(byteBuffer)) > 0) {
                // do nothing
                // System.out.println("reading");
            }
            byte[] data = byteBuffer.array();
            kImg.setData(data);
            return kImg;
        }
    }

    /**
     * 检查转换参数是否合法
     *
     * @param file
     * @param param
     */
    private static void checkKimgConvertParam(File file, KImgConvertParam param) throws InvalidParameterException {
        long filesize = file.length();
        if ((param.getHeight() - 1) * (param.getWidth() - 1) < filesize) {
            throw new InvalidParameterException("img size is less than file size.");
        }
        if (param.getHeight() < KIMG_MINSIZE || param.getWidth() < KIMG_MINSIZE) {
            throw new InvalidParameterException(MessageFormat.format("img width or height is less than {1}.", KIMG_MINSIZE));
        }
    }

    public static File kimgToFile(KImg kimg, String destFilePath) throws IOException {
        KImgHeader header = kimg.getHeader();
        String filename = header.getFilename();
        File file = new File(destFilePath + File.pathSeparator + filename);
        byte[] data = kimg.getData();

        // write file
        FileChannel channel = null;
        FileOutputStream fs = null;
        try {
            fs = new FileOutputStream(file);
            channel = fs.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.wrap(data);

            while ((channel.write(byteBuffer)) > 0) {
                // do nothing
                // System.out.println("reading");
            }
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

    /**
     * 将kimg保存为图片
     */
    public static BufferedImage kimgToImg(KImg kimg) {
        KImgHeader header = kimg.getHeader();
        byte[] data = kimg.getData();

        BufferedImage image = new BufferedImage(header.getWidth(), header.getWidth(), BufferedImage.TYPE_BYTE_BINARY);
        ImageDrawer.drawBorders(image);

        BufferedImage headerImg = image.getSubimage(1, 1, image.getWidth() - 1, 1);
        writeKimgHeader(header, headerImg);

        BufferedImage dataImg = image.getSubimage(1, 2, image.getWidth() - 1, image.getWidth() - 2);
        writeKimgData(data, dataImg);
        return image;
    }

    /**
     * 从图片转成kimg
     *
     * @param img 图片.已经处理为规范大小，且有边框
     * @return kimg模型
     */
    public static KImg imgToKimg(BufferedImage img) throws ImageHasNoBorderException {
        checkBorder(img);
        BufferedImage headerImg = img.getSubimage(1, 1, img.getWidth() - 1, 1);
        KImgHeader header = readKimgHeader(headerImg);

        BufferedImage dataImg = img.getSubimage(1, 2, img.getWidth() - 1, img.getHeight() - 2);
        byte[] data = readKimgData(dataImg, header.getDataByteSize());

        KImg kImg = new KImg();
        kImg.setHeader(header);
        kImg.setData(data);
        return kImg;
    }


    /**
     * 从图片读kimg的头部
     *
     * @param img 图片
     * @return 头部
     */
    private static KImgHeader readKimgHeader(BufferedImage img) {
        // todo
        KImgHeader header = new KImgHeader();

        return header;
    }

    /**
     * 从图片读数据
     *
     * @param img          图片
     * @param dataByteSize 数据长度（字节数）
     * @return 数据
     */
    private static byte[] readKimgData(BufferedImage img, int dataByteSize) {
        // todo
        byte[] data = new byte[dataByteSize];
        return data;
    }

    /**
     * 检查图片是否有边框
     *
     * @param img
     */
    private static void checkBorder(BufferedImage img) throws ImageHasNoBorderException {
        int w = img.getWidth();
        int h = img.getHeight();
        for (int x = 0; x < w; x++) {
            if ((img.getRGB(x, 0) != ColorConstant.COLOR_WHITE)
                    || (img.getRGB(x, h - 1) != ColorConstant.COLOR_WHITE)) {
                throw new ImageHasNoBorderException();
            }
        }
        for (int y = 0; y < h; y++) {
            if ((img.getRGB(0, y) != ColorConstant.COLOR_WHITE)
                    || (img.getRGB(w - 1, y) != ColorConstant.COLOR_WHITE)) {
                throw new ImageHasNoBorderException();
            }
        }
    }

    /**
     * 将kimg头部写入图片
     *
     * @param header 头部
     * @param image  目标图片(无边框)
     */
    private static void writeKimgHeader(KImgHeader header, BufferedImage image) {
        Position pos = new Position(0, 0);
        pos = ImageDrawer.drawInt(image, pos, header.getHeight());
        pos = ImageDrawer.drawInt(image, pos, header.getWidth());
        pos = ImageDrawer.drawInt(image, pos, header.getDataByteSize());
        pos = ImageDrawer.drawBytes(image, pos, header.getFilename().getBytes());
    }

    /**
     * 将kimg数据部分写入图片
     *
     * @param data  数据部分
     * @param image 目标图片(无边框)
     */
    private static void writeKimgData(byte[] data, BufferedImage image) {
        Position pos = new Position(0, 0);
        pos = ImageDrawer.drawBytes(image, pos, data);
    }

}
