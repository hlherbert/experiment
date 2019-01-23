package com.hl.img2file.service;

import com.hl.img2file.model.KImgConvertParam;

import java.io.IOException;

public class ImageConvertApp {
    public static void main(String[] args) {
        KImgConvertParam param = new KImgConvertParam(256,256);

        try {
            ImageConverter.convertFileToImg("src/main/resources/a.txt","a.gif", param);
            ImageConverter.restoreFileFromImg("a.gif",".");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
