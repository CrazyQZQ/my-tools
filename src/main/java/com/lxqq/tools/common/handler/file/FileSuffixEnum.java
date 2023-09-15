package com.lxqq.tools.common.handler.file;

/**
 * @Description:
 * @Author QinQiang
 * @Date 2023/9/14
 **/
public enum FileSuffixEnum {

    PDF("pdf"),
    DOCX("docx"),
    HTML("html"),
    XML("xml"),
    XLSX("xlsx"),
    PNG("png"),
    JPG("jpg"),
    JPEG("jpeg"),
    ;

    public String suffix;

    FileSuffixEnum(String suffix) {
        this.suffix = suffix;
    }
}
