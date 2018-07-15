package com.cqfangxin.domain;

public class FileMeta {
    private Integer fileId;
    /**
     * 1 stands for small image, 2 stands for large image
     */
    private Integer fileType;
    private String fileName;
    private String fileLocation;
    private Long fileSize;

    public FileMeta(){

    }

    public FileMeta(Integer fileId, String fileName,
                    String fileLocation, Long fileSize){
        this.fileId = fileId;
        this.fileName = fileName;
        this.fileLocation = fileLocation;
        this.fileSize = fileSize;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
}
