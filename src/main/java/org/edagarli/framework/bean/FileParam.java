package org.edagarli.framework.bean;

import java.io.InputStream;

/**
 * User: lurou
 * Email: lurou@2dfire.com
 * Date: 16/2/12
 * Time: 20:34
 * Desc:
 */
public class FileParam {
    private String fieldName;
    private String fileName;
    private long fileSize;
    private String contentType;
    private InputStream inputStream;

    public FileParam(String fieldName, String fileName, long fileSize, String contentType, InputStream inputStream) {
        this.fieldName = fieldName;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.contentType = contentType;
        this.inputStream = inputStream;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFileName() {
        return fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getContentType() {
        return contentType;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}

