project: img2file
info: 办公电脑不能访问外网，但能通过远程连接，连接到虚拟机中访问外网。我需要将外网中的歌曲传入下载到办公电脑上
      所以设计本软件。
作用： 由于通过远程连接，唯一通过的数据，只有图像通道。故需在虚拟机中将任意文件转换为图片，然后在办公电脑上，将图片
      再转换为文件。
      由于不能保证图片质量和颜色质量，故只能使用二值图片，即黑白图片，只有黑白两种颜色，每个像素点代表0或者1.
过程： 分为如下几个阶段：
      （1）VM上，用户使用file2img，文件转为图片。
      （2）VM上，用户打开转换的图片。
      （3）PC上, 用户将VM的屏幕中的图片截屏，使用img2file，将图片还原为文件。
设计：
      图片为黑白二值图片，用像素点记录值，像素点为白色，则表示0，黑色表示1。
      扫描每一行，每一行分为若干段，每段由8个连续像素组成，编码一个字节。

      file2img (文件转为图片）:
        用户定义图片宽（8的倍数）、高（8的倍数）
        Kimg kimg = convertFileToKimg(file, file2KImgParams, kimg)
        写入头部
        写入文件数据
        将Kimg转为图片文件。

      KImg: (文件的图像表示, 下图中*号表示边框, 即1像素点)
      ******************************************************
      *  高（4*8),宽(4*8),文件数据大小（4*8）,文件名(20*8) *
      *  文件数据                                          *
      ******************************************************

      文件头部256个字节，为了能在单独一行保存头部，因此图片宽度至少是256

      img2file(图片转为文件）:
        读取图片像素
        二值化为黑白图片
        匹配边框，划分图片范围
        读取头部
        读取文件数据