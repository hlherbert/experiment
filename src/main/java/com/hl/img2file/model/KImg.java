package com.hl.img2file.model;

/**
 * 文件的图片模型
 */
public class KImg {
    private KImgHeader header;
    private byte[]     data;

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
