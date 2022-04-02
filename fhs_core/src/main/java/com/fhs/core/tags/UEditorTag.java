package com.fhs.core.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;

/**
 * 百度编辑器
 *
 * @Filename: BDEditor.java
 * @Description:
 * @Version: 1.0
 * @Author: Lins
 * @Email: wanglei@sxpartner.com
 * @History:<br> 陕西小伙伴网络科技有限公司 Copyright (c) 2017 All Rights Reserved.
 */
public class UEditorTag extends BaseFormTag {

    // 默认class
    private final String defaultClassName = "ueEditorText";

    @Override
    public void doTag()
            throws JspException, IOException {

        defaultClassName(defaultClassName);
        write("     <div class=\"bigLabelDiv\">");
        write("         <label>" + super.getTitle() + ":</label>");
        write("     </div>");
        write("     <div class=\"bigContent\">");
        write("         <div id=" + super.getName() + " tag=\"UEditor\" name=" + super.getName() + "></div>");
        write("     </div> ");

    }
}
