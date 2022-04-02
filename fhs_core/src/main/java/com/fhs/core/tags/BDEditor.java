package com.fhs.core.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;

/**
 * 百度编辑器
 *
 * @Filename: BDEditor.java
 * @Description:
 * @Version: 1.0
 * @Author: jackwang
 * @Email: wanglei@sxpartner.com
 * @History:<br> 陕西小伙伴网络科技有限公司
 * Copyright (c) 2017 All Rights Reserved.
 */
public class BDEditor extends BaseFormTag {
    @Override
    public void doTag()
            throws JspException, IOException {
        super.setClassName(super.getClassName() + " ueEditorText");
        write(" <div class=\"fitemDiv\">");
        write(" <div class=\"bigLabelDiv\">");
        write("   <label>" + super.getTitle() + ":</label>");
        write(" </div> <div class=\"bigContent\">");
        write(" <textarea WordWrap=\"false\" " + super.getHtml());
        write(" </div> </div>");
        write(" </div> </div>");

    }
}
