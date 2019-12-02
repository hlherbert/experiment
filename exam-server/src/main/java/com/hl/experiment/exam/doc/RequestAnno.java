package com.hl.experiment.exam.doc;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RequestAnno {
    private String[] path;
    private String method;

    public RequestAnno(String[] value, String method) {
        this.path = value;
        this.method = method;
    }

    public String getPath() {
        return path.length > 0 ? path[0] : null;
    }

    public String getMethod() {
        return method;
    }

    public static RequestAnno from(RequestMapping anno) {
        String method = "GET";
        if (anno.method() != null) {
            List<String> methodLst = Arrays.stream(anno.method())
                    .map(x -> x.name())
                    .collect(Collectors.toList());
            String[] methodsArr = new String[methodLst.size()];
            method = methodsArr.length > 0 ? methodsArr[0] : "GET";
        }
        return new RequestAnno(anno.value(), method);
    }

    public static RequestAnno from(GetMapping anno) {
        return new RequestAnno(anno.value(), "GET");
    }

    public static RequestAnno from(PutMapping anno) {
        return new RequestAnno(anno.value(), "PUT");
    }

    public static RequestAnno from(PostMapping anno) {
        return new RequestAnno(anno.value(), "POST");
    }

    public static RequestAnno from(DeleteMapping anno) {
        return new RequestAnno(anno.value(), "DELETE");
    }
}
