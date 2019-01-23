package com.hl.img2file.service;

import com.hl.img2file.model.KImg;
import com.hl.img2file.model.KImgConvertParam;
import com.hl.img2file.model.KImgHeader;
import javafx.geometry.Pos;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
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

    // 1
    private static final int COLOR_WHITE = 0xffffff;

    // 0
    private static final int COLOR_BLACK = 0x0;

    /** 方向 */
    private enum Direction {
        VERTICAL,
        HORIZONTAL
    }

    /** 位置 */
    private static class Position {
        int x;
        int y;

        Position(int x, int y) {
            this.x=x;
            this.y=y;
        }

        Position(Position pos) {
            this.x = pos.x;
            this.y = pos.y;
        }
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
        writeBorder(image);

        BufferedImage headerImg = image.getSubimage(1,1,image.getWidth()-1,1);
        writeKimgHeader(header, headerImg);

        BufferedImage dataImg = image.getSubimage(1,2,image.getWidth()-1,image.getWidth()-2);
        writeKimgData(data, dataImg);
        return image;
    }

    /**
     * 从图片转成kimg
     * @param img 图片
     * @return kimg模型
     */
    public static KImg imgToKimg(BufferedImage img) {

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
                    image.setRGB(x,line,COLOR_WHITE);
                }
                break;
            case VERTICAL:
                for (int y=0;y<h;y++) {
                    image.setRGB(col,y,COLOR_WHITE);
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
        Position pos = new Position(0,0);
        pos = drawInt(image, pos, header.getHeight());
        pos = drawInt(image, pos, header.getWidth());
        pos = drawInt(image, pos, header.getDataByteSize());
        pos = drawBytes(image, pos, header.getFilename().getBytes());
    }

    /**
     * 将kimg数据部分写入图片
     * @param data 数据部分
     * @param image 目标图片(无边框)
     */
    private static void writeKimgData(byte[] data, BufferedImage image) {
        Position pos = new Position(0,0);
        pos = drawBytes(image, pos, data);
    }

    /**
     * 在指定位置用像素画数据
     * @param image 图像
     * @param pos 起始位置
     * @param val 数据
     * @return 新的位置
     */
    private static Position drawInt(BufferedImage image, Position pos, int val) {
        int nByte = Integer.BYTES;
        byte[] bytes = new byte[nByte];
        // read int into bytes, from lowest bit to highest bit
        for (int i=0;i<nByte;i++) {
            bytes[i] = (byte)((val >> (8*i)) & 0x8);
        }
        return drawBytes(image, pos, bytes);
    }

    /**
     * 在指定位置用像素画数据
     * @param image 图像
     * @param pos 起始位置
     * @param data 数据
     * @return 新的位置
     */
    private static Position drawBytes(BufferedImage image, Position pos, byte[] data) {
        int nByte = data.length;
        for (int i=0;i<nByte;i++) {
            byte byteVal = data[i];
            for (int j=0;j<8;j++) {
                int bit = (byteVal >> j) & 1;
                pos = drawBit(image,pos,bit);
            }
        }
        return pos;
    }

    /**
     * 在指定位置用像素画数据
     * 每个像素表示一个bit
     * @param image 图像
     * @param pos 起始位置
     * @param bit 0/1
     * @return 新的位置
     */
    private static Position drawBit(BufferedImage image, Position pos, int bit) {
        checkPositionOutOfBound(image, pos.x, pos.y);
        int color = COLOR_BLACK; //bit=0, black
        if (bit == 1) {
            color = COLOR_WHITE; //bit=1, white
        }
        image.setRGB(pos.x, pos.y, color);

        // goto next position
        Position newPos = new Position(pos.x+1, pos.y);
        if (newPos.x >= image.getWidth()) {
            newPos.x = 0;
            newPos.y += 1;
        }
        return newPos;
    }

    private static void checkPositionOutOfBound(BufferedImage image, int x, int y) {
        if (x < 0 || y <0 || x >= image.getWidth() || y >= image.getHeight()) {
            throw new IndexOutOfBoundsException("position out of image bound.");
        }
    }
}
