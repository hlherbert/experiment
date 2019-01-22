package com.hl.img2file.service;

import com.hl.img2file.model.KImg;
import com.hl.img2file.model.KImgConvertParam;
import com.hl.img2file.model.KImgHeader;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.*;
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

    /** 方向 **/
    private enum Direction {
        VERTICAL,
        HORIZONTAL
    }

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

    /**
     * 检查转换参数是否合法
     * @param file
     * @param param
     */
    private static void checkKimgConvertParam(File file, KImgConvertParam param) throws InvalidParameterException {
        long filesize = file.length();
        if ((param.getHeight()-1)*(param.getWidth()-1) < filesize){
            throw new InvalidParameterException("img size is less than file size.");
        }
        if (param.getHeight()<KIMG_MINSIZE || param.getWidth()<KIMG_MINSIZE) {
            throw new InvalidParameterException(MessageFormat.format("img width or height is less than {1}.",KIMG_MINSIZE));
        }
    }

    public static File kimgToFile(KImg kimg) throws IOException,FileNotFoundException {
        KImgHeader header = kimg.getHeader();
        String filename = header.getFilename();
        File file = new File(filename);
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
        writeBorder(image);

        BufferedImage headerImg = image.getSubimage(1,1,image.getWidth()-1,1);
        writeKimgHeader(header, headerImg);

        BufferedImage dataImg = image.getSubimage(1,2,image.getWidth()-1,image.getWidth()-2);
        writeKimgData(data, dataImg);
        return image;
    }

    /**
     * 画边框
     * @param image
     */
    private static void writeBorder(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        int firstline = 0;
        int lastline = h-1;
        int firstcol =  0;
        int lastcol = w-1;

        // top
        drawBorder(image, Direction.HORIZONTAL, firstline, 0);
        // bottom
        drawBorder(image, Direction.HORIZONTAL, lastline, 0);
        // left
        drawBorder(image, Direction.VERTICAL, 0, firstcol);
        // right
        drawBorder(image, Direction.VERTICAL, 0, lastcol);
    }

    private static void drawBorder(BufferedImage image, Direction direction, int line, int col) {
        int w = image.getWidth();
        int h = image.getHeight();
        switch (direction) {
            case HORIZONTAL:
                for (int x=0;x<w;x++) {
                    image.setRGB(x,line,1);
                }
                break;
            case VERTICAL:
                for (int y=0;y<h;y++) {
                    image.setRGB(col,y,1);
                }
                break;
        }
    }
    /**
     * 将kimg头部写入图片
     * @param header 头部
     * @param image 目标图片(无边框)
     */
    private static void writeKimgHeader(KImgHeader header, BufferedImage image) {
        byte[] data = ((DataBufferByte) image.getData().getDataBuffer()).getData();
        ByteBuffer buf = ByteBuffer.wrap(data);
        buf.putInt(header.getHeight());
        buf.putInt(header.getWidth());
        buf.putInt(header.getDataByteSize());
        buf.put(header.getFilename().getBytes());
    }

    /**
     * 将kimg数据部分写入图片
     * @param data 数据部分
     * @param image 目标图片(无边框)
     */
    private static void writeKimgData(byte[] data, BufferedImage image) {
        // TODO
    }

    /**
     * 向字节区写入整数
     * @param val 整数值
     * @param buf 字节区
     * @param start 写入的起始位置
     */
    private static void writeInt(int val, byte[] buf, int start) {
        // 整数为32位，4个字节，从低到高依次写入4个字节

    }
}
