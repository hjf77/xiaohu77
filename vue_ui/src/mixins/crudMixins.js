/**
 *
 * formData 默认值
 * org组织机构信息
 * crud:控制crud组件加载和销毁
 * isEdit：是否编辑
 * open:默认表单弹窗状态
 * init:编辑的时候的初始化数据
 * textMap Dialog标题栏
 * dialogStatus Dialog状态
 *
 *
 *
 *
 */


export default {

  data(){
    return{
      formData:{},
      org:undefined,
      crud:false,
      isEdit:false,
      open:false,
      init: {},
      title:undefined,
      dialogStatus:"",
      textMap:{
        add:"新增",
        edit:"编辑",
        detail:"详情",
        revision: '修订',
        audit: '审核',
        enable: '启用',
        discard: '废弃',
        report: '信息上报',
        relieve: '解除',
        upgrade: '调级',
        issued: '下达',
        builImport:'批量导入',
        issuance: '签发',
        roster: '花名册'
      }
    }
  },


  methods:{


    /**
     * 获取当前组织机构信息
     * @param data
     */
    getOrg(data){
      this.$set(this,'org',data)
    },


    /**
     * Dialog弹窗关闭取消按钮
     */
    cancel(){
      this.$set(this,"open",false)
    },

  }







}
