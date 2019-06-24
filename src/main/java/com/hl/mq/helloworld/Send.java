package com.hl.mq.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.concurrent.TimeoutException;

public class Send {
    private final static String Q_NAME = "hello";

    public static void main(String[] args) {
        // connectionFactory
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");

        // connection
        try (Connection connection = connectionFactory.newConnection()){
            // channel
            Channel channel = connection.createChannel();
            channel.queueDeclare(Q_NAME, false, false, false, null);
            String msg = "hello world!";
            channel.basicPublish("", Q_NAME, null, msg.getBytes());
            System.out.println(MessageFormat.format("[x] Sent \"{0}\"",msg));
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
