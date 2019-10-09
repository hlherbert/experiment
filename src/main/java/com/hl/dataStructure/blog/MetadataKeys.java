package com.hl.dataStructure.blog;

public enum MetadataKeys {
    AUTHOR("author"),
    SOURCE("source");

    private final String key;

    MetadataKeys(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }

    @Override
    public String toString() {
        return key;
    }
}
