/**
 模块：Element组件重写
 作者：SunXinHao
 邮箱：393108203@qq.com
 时间：2020年1月15日 16:47:20
 版本：v1.0
 修改记录：
 修改内容：
 修改人员：
 修改时间：
 */
import Element from 'element-ui'

/**
 * 重写Dialog默认属性和方法。
 * @param {closeOnClickModal} 禁止点击Dialog遮罩层关闭Dialog
 * @param {closeOnPressEscape} 禁止点击Esc关闭Dialog
 */

Element.Dialog.props.width.default = "960px"
Element.Dialog.props.closeOnClickModal.default = false
Element.Dialog.props.closeOnPressEscape.default = false


