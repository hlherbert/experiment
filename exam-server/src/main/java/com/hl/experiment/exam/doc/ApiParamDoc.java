package com.hl.experiment.exam.doc;

import java.lang.reflect.Type;

public class ApiParamDoc {
    private String name;
    private String commentText;
    private Type type;
    private String example;

    public ApiParamDoc(Type type, String name, String commentText) {
        this.type = type;
        this.name = name;
        this.commentText = commentText;
        this.example =
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
}
