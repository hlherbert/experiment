package com.hl.experiment.exam.doc;

import java.text.MessageFormat;
import java.util.Arrays;

public class ApiEntry {
    private String method;
    private String note;
    private String httpMethod;
    private String path;
    private String[] queryParams;
    private ApiParamDoc[] queryParamDocs;
    private String body;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String method) {
        this.httpMethod = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String[] getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(String[] queryParams) {
        this.queryParams = queryParams;
    }

    public ApiParamDoc[] getQueryParamDocs() {
        return queryParamDocs;
    }

    public void setQueryParamDocs(ApiParamDoc[] queryParamDocs) {
        this.queryParamDocs = queryParamDocs;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String toString() {
        return MessageFormat.format(
                "Note: {0}\n"
                        + "Method: {1}\n"
                        + "Path: {2}  {3}\n"
                        + "Params: {4}\n"
                        + "Body: {5}", note, method, httpMethod, path, Arrays.asList(queryParams), body);
    }
}
