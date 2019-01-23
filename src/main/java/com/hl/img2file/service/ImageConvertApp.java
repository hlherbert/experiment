package com.hl.img2file.service;

import com.hl.img2file.exception.ImageHasNoBorderException;
import com.hl.img2file.model.KImgConvertParam;

import java.io.IOException;

public class ImageConvertApp {
    public static void main(String[] args) {
        KImgConvertParam param = new KImgConvertParam(256,256);

        try {
            ImageConverter.convertFileToImg("src/main/resources/a.txt","out/a.gif", param);
            ImageConverter.restoreFileFromImg("out/a.gif","out");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ImageHasNoBorderException e) {
            e.printStackTrace();
        }
    }
}
