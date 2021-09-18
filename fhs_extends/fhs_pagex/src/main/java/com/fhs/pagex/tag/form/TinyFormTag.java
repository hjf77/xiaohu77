package com.fhs.pagex.tag.form;

import com.fhs.core.config.EConfig;
import com.fhs.pagex.common.BeetlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * UM富文本编辑器
 *
 * @ProjectName: framework_v2_idea2
 * @Package: com.fhs.pagex.tag.form
 * @ClassName: SelectFormTag
 * @Author: JackWang
 * @CreateDate: 2018/12/4 0004 19:32
 * @UpdateUser: JackWang
 * @UpdateDate: 2018/12/4 0004 19:32
 * @Version: 1.0
 */
@Component
@Slf4j
public class TinyFormTag extends InputFormTag {
    static {
        FormTagFactory.regTag("tiny", TinyFormTag.class);
    }

    @Override
    public String getContentHtml() {
        request.setAttribute("tagSett", super.tagSett);
        try {
            return BeetlUtil.renderBeelt("/pagex/tags/tiny_tag.html", super.getBeetlParamMap());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }


    @Override
    public String loadSuccessJs() {
        return "umLoadSuccess_" + super.tagSett.get("name") + "(info)";
    }

    @Override
    public String saveJs() {
        return "umOnsave_" + super.tagSett.get("name") + "()";
    }

    @Override
    public boolean isNewRow() {
        return true;
    }

    @Override
    protected String[] getHandelKeys() {
        return new String[]{};
    }

    @Override
    public boolean isSupportAdvanceSearch() {
        return true;
    }
}
