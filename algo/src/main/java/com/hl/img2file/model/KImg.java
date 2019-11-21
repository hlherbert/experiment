package com.hl.img2file.model;

/**
 * 文件的图片模型
 */
public class KImg {
    /**
     * 头部
     */
    private KImgHeader header;

    /**
     * 数据
     */
    private byte[] data;

    public KImgHeader getHeader() {
        return header;
    }

    public void setHeader(KImgHeader header) {
        this.header = header;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
