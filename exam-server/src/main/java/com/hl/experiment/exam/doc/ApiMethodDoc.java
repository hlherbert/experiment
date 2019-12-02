package com.hl.experiment.exam.doc;

import java.util.Map;

public class ApiMethodDoc {
    private String name;
    private String commentText;

    private Map<String, ApiParamDoc> paramMap;

    public ApiMethodDoc(String name, String commentText) {
        this.name = name;
        this.commentText = commentText;
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

    public Map<String, ApiParamDoc> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, ApiParamDoc> paramMap) {
        this.paramMap = paramMap;
    }
}
