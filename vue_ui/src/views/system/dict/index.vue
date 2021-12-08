<template>

    <pagex-crudForm :namespace="namespace" :title="title" :crudSett="crudSett" :formSett="formSett" :idFieldName="idFieldName" >
    </pagex-crudForm>

</template>

<script>

export default {
  name: "Dict",
  data() {
    return {
      namespace:'dictGroup',
      title:'字典分组',
      idFieldName:'groupId',
      crudSett:{
        api: '/basic/ms/dictGroup/pagerAdvance',
        sortSett: [{
          "direction": "DESC",
          "property": "updateTime"
        }],
        buttons: [
          {
            title: '新增',
            name: 'add',
            code: "add",
            type: 'primary',
            size: 'mini',
            icon: 'el-icon-plus',
          }
        ],
        columns: [
          {label: '分组名称', name: 'groupName'},
          {
            label: '分组编码', name: 'groupCode', type: 'formart',
            formart: "<label style='cursor:pointer'>${groupCode}</label>",
            click: function (_row) {
              this.$router.push({path: '/dict/type/data/',query:{groupCode: _row.groupCode}});
            }
          },
          {
            label: '操作',
            name: 'operation',
            type: 'textBtn',
            textBtn: [
              {
                title: "编辑",
                type: "bottom",
                size: 'mini'
              },
              {
                title: "详情",
                type: "success",
                size: 'mini'
              },
              {
                title: "删除",
                type: "danger",
                size: 'mini',
                api: '/basic/ms/dictGroup/'
              }
            ],
          }
        ],
        filters: [
          {label: '分组名称:', name: 'groupName', placeholder: "分组名称", type: 'text', operation: 'like'},
          {label: '分组编码:', name: 'groupCode', placeholder: "分组编码", type: 'text', operation: 'like'}
        ],
      },
      formSett:{
        addApi: '/basic/ms/dictGroup/',
        updateApi: '/basic/ms/dictGroup/',
        data:{

        },
        controls:[
          {
            type: 'text',
            name: 'groupName',
            label: '分组名称',
            rule: 'required',
            placeholder: '请输入分组名称'
          }, {
            type: 'text',
            name: 'groupCode',
            label: '分组编码',
            rule: 'required',
            placeholder: '请输入分组编码'
          }
        ]
      },
    }
  },
  methods: {

  }
};
</script>
