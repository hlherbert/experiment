package com.hl.dataStructure.blog;

import com.alibaba.fastjson.JSON;

public class Blog {

    private Metadata metadata = new Metadata();

    public static void main(String[] args) {
        Blog blog = new Blog();
        blog.setMeta(MetadataKeys.AUTHOR.key(), "黄历");
        blog.setMeta(MetadataKeys.SOURCE.key(), "新浪网");

        String author = blog.getMeta(MetadataKeys.AUTHOR.key());
        System.out.println("blog author = " + author);

        blog.setMeta(MetadataKeys.AUTHOR.key(), "笨蛋");

        String metadata = blog.getMetadata();
        System.out.println("blog metadata = " + metadata);

        blog.setMetadata(metadata);
        System.out.println("blog metadata = " + metadata);
    }

    public String getMeta(String key) {
        return metadata.get(key);
    }

    public void setMeta(String key, String val) {
        metadata.put(key, val);
    }

    public String getMetadata() {
        if (metadata == null) {
            return null;
        }
        return JSON.toJSONString(metadata);
    }

    public void setMetadata(String meta) {
        metadata = new Metadata();
        metadata = JSON.parseObject(meta, Metadata.class);
    }
}
