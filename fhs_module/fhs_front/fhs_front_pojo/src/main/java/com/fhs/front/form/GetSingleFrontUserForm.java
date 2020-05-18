package com.fhs.front.form;

import com.fhs.core.base.form.BaseForm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 获取单个用户form
 * @author user
 * @since 2019-05-18 11:47:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetSingleFrontUserForm extends BaseForm {
    /**
     * accessToken 访问令牌
     */
    private String accessToken;

    /**
     * 用户id
     */
    private String userId;
}
