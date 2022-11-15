package com.fhs.basics.constant;


/**
 * 后台返回异常code
 */
public interface ExceptionConstant {
    /**
     * 导入文件不能为空
     */
    Integer FILE_NOT_EMPTY_CODE = 100101;

    /**
     * 导出失败
     */
    Integer EXPORT_FAIL = 100102;

    /**
     * 语言信息不能重复
     */
    Integer LANGUAGE_MESSAGE_NOT_REPEAT = 100103;

    /**
     * 请选择需要删除的消息
     */
    Integer CHOICE_DELETE_MESSAGE = 100104;

    /**
     * 下载文件异常,可能是文件不存在
     */
    Integer DOWNLOAD_ABNORMAL_FILE_ABSENT = 100105;

    /**
     * 密码输入次数过多，账号已被锁定
     */
    Integer PASSWORD_TOO_MUCH = 100106;

    /**
     * 该账号已经注册了，请直接登录！
     */
    Integer ACCOUNT_NUMBER_EXISTENCE = 100107;

    /**
     * 验证码已过期，请重新发送
     */
    Integer VERIFICATION_CODE_OVERDUE = 100111;

    /**
     * 验证码不正确
     */
    Integer VERIFICATION_CODE_ERROR = 100112;

    /**
     * 验证码失效，请刷新验证码后重新输入
     */
    Integer VERIFICATION_CODE_INVALID = 100113;

    /**
     * 验证码错误，请重新输入
     */
    Integer VERIFICATION_CODE_WRONG = 100114;

    /**
     * 账号或者密码错误
     */
    Integer ACCOUNT_NUMBER_ERROR = 100115;

    /**
     * 账号未注册，请先注册账号！
     */
    Integer ACCOUNT_NUMBER_NOT_REGISTER = 100116;

    /**
     * 密码修改失败！
     */
    Integer PASSWORD_MODIFY_FAIL = 100117;

    /**
     * 密码修改成功！
     */
    Integer PASSWORD_MODIFY_SUCCESS = 100118;

    /**
     * token失效
     */
    Integer TOKEN_INVALID = 100119;

    /**
     * 登出成功
     */
    Integer LOG_OUT_SUCCESS = 100121;

    /**
     * 用户名重复
     */
    Integer USER_NAME_REPEAT = 100122;

    /**
     * 密码输入错误
     */
    Integer PASSWORD_INPUT_ERROR = 100123;

    /**
     * 该手机号已被使用！
     */
    Integer PHONE_NUMBER_USE = 100124;

    /**
     * 该邮箱已被使用！
     */
    Integer MAILBOX_USE = 100125;

    /**
     * 所更换的手机号号码不能与当前手机号码一致
     */
    Integer PHONE_NUMBER_NOT_AGREEMENT = 100126;

    /**
     * 所更换的邮箱号码不能与当前邮箱号码一致
     */
    Integer MAILBOX_USE_NOT_AGREEMENT = 100127;

    /**
     * 文件为空
     */
    Integer FILE_IS_NULL = 100128;

    /**
     * 导入的Excel数据格式不正确
     */
    Integer EXCEL_FORMAT_ERROR = 100129;

    /**
     * 拥有子结构不可禁用
     */
    Integer SUBSTRUCTURE_NOT_DISABLE = 100130;

    /**
     * 父机构处于禁用状态，不能添加子机构
     */
    Integer NOT_ADD_TO = 100131;

    /**
     * 该机构下拥有用户,不可禁用!
     */
    Integer MECHANISM_NOT_DISABLE = 100132;

    /**
     * 该机构拥有子机构,不可删除!
     */
    Integer MECHANISM_NOT_DELETE = 100133;

    /**
     * 该机构下拥有用户,不可删除!
     */
    Integer USER_NOT_DELETE = 100134;

    /**
     * 新密码不可为空
     */
    Integer NEW_PASSWORD_NOT_NULL = 100135;

    /**
     * 密码不正确
     */
    Integer PASSWORD_INCORRECT = 100136;

    /**
     * 邀请码不能为空！
     */
    Integer INVITATION_CODE_NOT_NULL = 100137;

    /**
     * 邀请码不正确，请联系管理员重新生成！
     */
    Integer INVITATION_CODE_INCORRECT = 100138;

    /**
     * 邀请码已经过期，请联系管理员重新生成！
     */
    Integer INVITATION_CODE_OVERDUE = 100139;

    /**
     * 邀请码已经被使用，请联系管理员重新生成！
     */
    Integer INVITATION_CODE_USE = 100140;

    /**
     * 当前邀请码所属家庭不存在！
     */
    Integer FAMILY_NOTHINGNESS = 100141;

    /**
     * 该账号已加入该家庭，不能重复加入！
     */
    Integer ACCOUNT_NUMBER_FAMILY = 100142;

    /**
     * 邀请成功
     */
    Integer INVITATION_SUCCESS = 100143;

    /**
     * 该账号不存在！
     */
    Integer ACCOUNT_NUMBER_NOTHINGNESS = 100144;

    /**
     * 自己不能邀请自己的账号！
     */
    Integer ONESELF_NOT_INVITATION = 100145;

    /**
     * 该账号已加入该家庭，不能重复邀请！
     */
    Integer ACCOUNT_NUMBER_INVITATION = 100146;

    /**
     * 家庭名称不能为空
     */
    Integer FAMILY_NAME_NULL = 100147;

    /**
     * 房间名称不能为空
     */
    Integer ROOM_NAME_NULL = 100148;

    /**
     * 当前用户不存在
     */
    Integer USER_NOTHINGNESS = 100149;

    /**
     * 撤销失败！
     */
    Integer REVOKE_FAIL = 100150;

    /**
     * 该分类下有子分类,不能删除
     */
    Integer CLASSIFICATION_NOT_DELETE = 100151;

    /**
     * 执行成功
     */
    Integer IMPLEMENT_SUCCESS = 100152;

    /**
     * 当前家庭不存在！
     */
    Integer CURRENT_FAMILY_NOTHINGNESS = 100153;

    /**
     * 当前用户下只有一个家庭时 不能删除！
     */
    Integer FAMILY_NUM_LESS_THAN_ONE = 100154;

    /**
     * 用户无操作当前设备权限
     */
    Integer USER_NOT_OPERATION_AUTHORITY = 100155;
}
