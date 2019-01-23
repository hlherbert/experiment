package com.hl.img2file.service;

import com.hl.img2file.model.KImg;
import com.hl.img2file.model.KImgConvertParam;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageConverter {

    /** 文件到转换为图片文件 */
    public static void convertFileToImg(String srcFilename, String dstImgFilename, KImgConvertParam param) throws IOException {
        File srcFile = loadFile(srcFilename);
        KImg kImg = KImgConverter.fileToKimg(srcFile, param);
        BufferedImage bufImg = KImgConverter.kimgToImg(kImg);

        File destFile = new File(dstImgFilename);
        ImageIO.write(bufImg,"gif",destFile);
    }

    /** 图片还原为文件 */
    public static void restoreFileFromImg(String imgFilename, String destFilePath) throws IOException {
        BufferedImage img = ImageIO.read(new File(imgFilename));
        KImg kimg = KImgConverter.imgToKimg(img);
        File file = KImgConverter.kimgToFile(kimg,destFilePath);
    }

    /**
     * 加载文件
     * @param srcFilename 文件全路径
     * @return 文件对象
     */
    private static File loadFile(String srcFilename) {
        return new File(srcFilename);
    }
}
