/*
 * 文 件 名:  ServiceFile.java
 * 版    权:  sxpartner Technology International Ltd.
 * 描    述:  &lt;描述&gt;.
 * 修 改 人:  wanglei
 * 修改时间:  ${date}
 * 跟踪单号:  &lt;跟踪单号&gt;
 * 修改单号:  &lt;修改单号&gt;
 * 修改内容:  &lt;修改内容&gt;
 */
package com.fhs.pub.dox;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.core.base.dox.BaseDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * &lt;文件&gt;
 *
 * @author 王磊
 * @version [版本号, 2015/08/14 11:34:23]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_pub_file")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "PubFileDO", description = "PubFile参数")
public class PubFileDO extends BaseDO<PubFileDO> implements Serializable
{
    private static final long serialVersionUID = 7096338027750227520L;

    /**
     * id
     */
    @TableId("file_id")
    @ApiModelProperty("主键id")
    private String fileId;

    /**
     * 文件名
     */
    @TableField("file_name")
    @ApiModelProperty("文件名")
    private String fileName;

    /**
     * 后缀
     */
    @TableField("file_suffix")
    @ApiModelProperty("后缀")
    private String fileSuffix;

    /**
     * 文件上传时间
     */
    @TableField("upload_date")
    @ApiModelProperty("文件上传时间")
    private String uploadDate;

    @TableField("file_size")
    @ApiModelProperty("文件大小byte")
    private Long fileSize;


    @TableField(exist = false)
    @ApiModelProperty("文件大小描述")
    private String fileSizeDesc;

    /**
     * 时间长度
     */
    @ApiModelProperty("时间长度")
    @TableField(exist = false)
    private Long timeLength;


    /**
     * 格式化文件大小描述
     * @return
     */
    public String getFileSizeDesc(){
        if(fileSize==null){
            return "";
        }
        DecimalFormat formater = new DecimalFormat("####.00");

        if (fileSize < 1024) {
            return fileSize + "bytes";

        } else if (fileSize < 1024 * 1024) {
            float kbsize = fileSize / 1024f;

            return formater.format(kbsize) + "KB";

        } else if (fileSize < 1024 * 1024 * 1024) {
            float mbsize = fileSize / 1024f / 1024f;

            return formater.format(mbsize) + "MB";

        } else if (fileSize < 1024 * 1024 * 1024 * 1024) {
            float gbsize = fileSize / 1024f / 1024f / 1024f;

            return formater.format(gbsize) + "GB";

        } else {
            return "size: error";

        }
    }

}
