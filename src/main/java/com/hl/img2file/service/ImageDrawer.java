package com.hl.img2file.service;

import com.hl.img2file.model.ColorConstant;
import com.hl.img2file.model.Direction;
import com.hl.img2file.model.Position;

import java.awt.image.BufferedImage;

/**
 * 绘图神器
 */
public class ImageDrawer {

    /**
     * 画边框
     * @param image
     */
    public static void drawBorders(BufferedImage image) {
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
                    image.setRGB(x,line, ColorConstant.COLOR_WHITE);
                }
                break;
            case VERTICAL:
                for (int y=0;y<h;y++) {
                    image.setRGB(col,y, ColorConstant.COLOR_WHITE);
                }
                break;
        }
    }

    /**
     * 在指定位置用像素画数据
     * @param image 图像
     * @param pos 起始位置
     * @param val 数据
     * @return 新的位置
     */
    public static Position drawInt(BufferedImage image, Position pos, int val) {
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
    public static Position drawBytes(BufferedImage image, Position pos, byte[] data) {
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
        int color = ColorConstant.COLOR_BLACK; //bit=0, black
        if (bit == 1) {
            color = ColorConstant.COLOR_WHITE; //bit=1, white
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
