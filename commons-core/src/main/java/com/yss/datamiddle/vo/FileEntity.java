package com.yss.datamiddle.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @description: 文件类
 * @author: fangzhao
 * @create: 2020/3/24 13:09
 * @update: 2020/3/24 13:09
 */
@ApiModel("文件实体")
public class FileEntity {

    /**
     * 源文件名
     */
    @ApiModelProperty("源文件名")
    private String srcName;

    /**
     * 目标文件名
     */
    @ApiModelProperty("源文件名")
    private String destName;

    /**
     * 文件类型，包括img,video,html,preview_html等
     */
    @ApiModelProperty("文件类型")
    private String fileType;

    /**
     * 文件实际路径，用于提示文件服务器将文件存储到何种路径之下
     */
    @ApiModelProperty("实际路径")
    private String realPath;

    /**
     * 文件映射路径
     */
    @ApiModelProperty("映射路径")
    private String mappingPath;

    /**
     * 文件大小
     */
    @ApiModelProperty("文件大小")
    private long fileSize;

    public String getSrcName() {
        return srcName;
    }

    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }

    public String getDestName() {
        return destName;
    }

    public void setDestName(String destName) {
        this.destName = destName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getRealPath() {
        return realPath;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }

    public String getMappingPath() {
        return mappingPath;
    }

    public void setMappingPath(String mappingPath) {
        this.mappingPath = mappingPath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public String toString() {
        return "FileUploadEntity{" +
                "srcName='" + srcName + '\'' +
                ", destName='" + destName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", realPath='" + realPath + '\'' +
                ", mappingPath='" + mappingPath + '\'' +
                ", fileSize=" + fileSize +
                '}';
    }
}
