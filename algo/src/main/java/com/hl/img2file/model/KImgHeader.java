package com.hl.img2file.model;

/**
 * 文件的图片模型-头部
 */
public class KImgHeader {
    /**
     * 宽
     */
    private int width;

    /**
     * 高
     */
    private int height;

    /**
     * 原始文件名
     */
    private String filename;

    /**
     * 原始文件大小 bytes
     */
    private int dataByteSize;

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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getDataByteSize() {
        return dataByteSize;
    }

    public void setDataByteSize(int dataByteSize) {
        this.dataByteSize = dataByteSize;
    }
}
