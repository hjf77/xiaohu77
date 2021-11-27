package com.fhs.pub.vo;

import com.fhs.core.trans.vo.VO;
import com.fhs.pub.po.PubFilePO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

/**
 * 公共文件
 *
 * @author user
 * @since 2019-05-18 11:20:18
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "PubFileVO", description = "PubFile参数")
public class PubFileVO extends PubFilePO implements VO {
    // 文件相对路径, 用于nginx代理文件
    private String filePath;

    public String getFilePath() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getUploadDate()).append("/");
        sb.append(StringUtils.replace(this.getFileSuffix(), ".", "")).append("/");
        sb.append(this.getFileId()).append(this.getFileSuffix());
        return sb.toString();
    }
}
