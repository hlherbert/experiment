package com.hl.experiment.exam.doc;


import java.text.MessageFormat;

public class ApiParamDoc {
    private String name;
    private String commentText;
    private String type;
    private String example;

    private String requestParamName;
    private boolean isBody = false;

    public ApiParamDoc(String type, String name, String commentText) {
        this.type = type;
        this.name = name;
        this.requestParamName = name;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getRequestParamName() {
        return requestParamName;
    }

    public void setRequestParamName(String requestParamName) {
        this.requestParamName = requestParamName;
    }

    public boolean isBody() {
        return isBody;
    }

    public void setBody(boolean body) {
        isBody = body;
    }

    public String toString() {
        if (isBody) {
            return MessageFormat.format("Name: {0} \t Note: {1}\t Type: {2} \n\tExample:\n {3}", requestParamName, commentText, type, example);
        }
        return MessageFormat.format("Name: {0} \t  Note: {1}\t Type: {2} \n\tExample:\n {3}", requestParamName, commentText, type, example);
    }
}
