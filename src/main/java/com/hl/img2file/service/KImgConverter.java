package com.hl.img2file.service;

import com.hl.img2file.model.KImg;
import com.hl.img2file.model.KImgConvertParam;
import com.hl.img2file.model.KImgHeader;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 文件-图像转换器
 */
public class KImgConverter {

    public static KImg fileToKimg(File file, KImgConvertParam param) throws IOException {
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

        header.setDataByteSize((int)size);


        // data
        FileChannel channel = null;
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(file);
            channel = fs.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
            while ((channel.read(byteBuffer)) > 0) {
                // do nothing
                // System.out.println("reading");
            }
            byte[] data = byteBuffer.array();
            kImg.setData(data);
            return kImg;
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
    }

    public static File kimgToFile(KImg kimg) throws IOException,FileNotFoundException {
        KImgHeader header = kimg.getHeader();
        String filename = header.getFilename();
        File file = new File(filename);
        byte[] data = kimg.getData();

        // write fiel
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

        // TODO
        return null;
    }

    /**
     * 将kimg保存为图片
     */
    public static BufferedImage kimgToImg(KImg kimg) {
        KImgHeader header = kimg.getHeader();
        byte[] data = kimg.getData();

        BufferedImage image = new BufferedImage(header.getWidth(), header.getWidth(), BufferedImage.TYPE_BYTE_BINARY);
        writeKimgHeader(header, image);
        writeKimgData(data, image);
        return image;
    }

    /**
     * 将kimg头部写入图片
     * @param header 头部
     * @param image 目标图片
     */
    private static void writeKimgHeader(KImgHeader header, BufferedImage image) {
        // TODO
    }

    /**
     * 将kimg数据部分写入图片
     * @param data 数据部分
     * @param image 目标图片
     */
    private static void writeKimgData(byte[] data, BufferedImage image) {
        // TODO
    }
}
