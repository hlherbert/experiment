package com.hl.experiment.exam.doc;

import java.util.Map;

public class ApiClassDoc {
    private String name;
    private String commentText;
    private Map<String, ApiMethodDoc> methodDocMap;

    public ApiClassDoc(String name, String commentText) {
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

    public Map<String, ApiMethodDoc> getMethodDocMap() {
        return methodDocMap;
    }

    public void setMethodDocMap(Map<String, ApiMethodDoc> methodDocMap) {
        this.methodDocMap = methodDocMap;
    }
}
