package com.hl.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelTest {
    public static void main(String[] args) throws IOException {
        FileChannelTest test = new FileChannelTest();
        test.test();
    }

    public void test() throws IOException {
        RandomAccessFile aFile = new RandomAccessFile("data/nio-data.txt", "rw");
        FileChannel inChannel = aFile.getChannel();
        ByteBuffer buf = ByteBuffer.allocate(48);
        int bytesRead = inChannel.read(buf);

//Hello! This is a hello world test. | 34
//great! I am very well. |23
//How about you? |15


        while (bytesRead != -1) {
            //System.out.println("Read " + bytesRead);
            //buf.flip();
            buf.rewind();
            System.out.print("||:");
            while (buf.hasRemaining()) {
                System.out.print((char) buf.get());
            }
            System.out.println();


            buf.clear();
            bytesRead = inChannel.read(buf);
//            bytesRead = -1;
        }
        aFile.close();
    }
}
