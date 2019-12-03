package com.hl.experiment.security.xss;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TestXss {

    private static List<String> loadPayloads() {
        InputStream ins = null;
        URL url = TestXss.class.getResource("/xsspayload.txt");

        try (BufferedReader bufReader = new BufferedReader(new InputStreamReader(ins))) {
            return bufReader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<String>();
    }

    public static void test() {
        List<String> payloads = loadPayloads();
        XssDetector detector = XssDetector.getNormalDetector();

        for (String payload : payloads) {
            boolean pass = detector.passXssCheck(payload);
            if (pass) {
                System.out.println(payload);
            }
        }
    }

    public static void main(String[] args) {
        test();
    }
}
