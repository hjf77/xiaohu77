package com.fhs.influxdb.core.enums;


public enum Function {

    SUM("sum", "累加"),
    LAST("last", "最后一条数据"),
    MEAN("mean", "平均数")

    ;
    private final String tag;

    private final String content;

    Function(String tag, String content) {
        this.tag = tag;
        this.content = content;
    }

    public String getTag() {
        return tag;
    }

    public String getContent() {
        return content;
    }
}
