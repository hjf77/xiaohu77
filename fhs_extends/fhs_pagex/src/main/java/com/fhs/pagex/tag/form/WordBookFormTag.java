package com.fhs.pagex.tag.form;

import com.fhs.core.config.EConfig;
import org.springframework.stereotype.Component;

/**
 * combobox
 *
 * @ProjectName: framework_v2_idea2
 * @Package: com.fhs.pagex.tag.form
 * @ClassName: WordBookFormTag
 * @Author: JackWang
 * @CreateDate: 2018/12/4 0004 19:38
 * @UpdateUser: JackWang
 * @UpdateDate: 2018/12/4 0004 19:38
 * @Version: 1.0
 */
@Component
public class WordBookFormTag extends SelectFormTag {

    static {
        FormTagFactory.regTag("book", WordBookFormTag.class);
        FormTagFactory.regOne2XTag("book", WordBookFormTag.class);
    }

    @Override
    public String getContentHtml() {
        //设置这几个就搞定啦，其他的爸爸干
        tagSett.put("valueField", "wordbookCode");
        tagSett.put("textField", "wordbookDesc");
        tagSett.put("url", EConfig.getPathPropertiesValue("fhs_basics_url") + "/webApi/wordbook/getData?wordbookGroupCode=" +
                tagSett.get("code") + "&jsonpCallback=?");
        return super.getContentHtml();
    }

    @Override
    protected String[] getHandelKeys() {
        return new String[]{"code"};
    }

    @Override
    public String getAdvanceSearchSett() {
        return "{name:'" + this.tagSett.get("name") + "',title:'" + this.tagSett.get("title") + "',type:'book',code:'" + tagSett.get("code") + "'}";
    }
}