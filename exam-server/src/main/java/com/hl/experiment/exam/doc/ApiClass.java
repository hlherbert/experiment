package com.hl.experiment.exam.doc;

import java.util.List;

public class ApiClass {
    private String name;
    private String note = "note";
    private List<ApiEntry> apis;

    public ApiClass(String name) {
        this.name = name;
    }
    public List<ApiEntry> getApis() {
        return apis;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setApis(List<ApiEntry> apis) {
        this.apis = apis;
    }

    public String toString() {
        StringBuilder builder =new StringBuilder();
        builder.append("Note: "+note+"\n");
        builder.append("Controller: "+name+"\n\n");
        for (ApiEntry api : apis) {
            builder.append(api + "\n---------------------------\n");
        }
        builder.append("\n");
        return builder.toString();
    }
}
