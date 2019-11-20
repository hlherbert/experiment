package com.hl.img2file.model;

/**
 * 图片转换参数
 */
public class KImgConvertParam {
    private int width;
    private int height;

    public KImgConvertParam(int w, int h) {
        width = w;
        height = h;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
