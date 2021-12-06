<template>
  <base-container>
    <base-crud :option="crudOption" :before-open="beforeOpen">
    </base-crud>
  </base-container>

</template>

<script>

export default {
  name: "Dict",
  data() {
    return {
      crudOption: {
        api: '/basic/ms/dictGroup/pagerAdvance',
        addApi: '/basic/ms/dictGroup/',
        updateApi: '/basic/ms/dictGroup/',
        delApi: '/basic/ms/dictGroup/',
        queryOneApi: '',
        isNeedPager: false,
        align: "center", //对齐方式
        sortSett: [{
          "direction": "DESC",
          "property": "updateTime"
        }],
        formOption: {
          group:
            [
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
            ],

        },
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
        filters: [
          {label: '分组名称:', name: 'groupName', placeholder: "分组名称", type: 'text', operation: 'like'},
          {label: '分组编码:', name: 'groupCode', placeholder: "分组编码", type: 'text', operation: 'like'}
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
                name: "编辑",
                code: "edit",
                type: "text",
                size: 'mini'
              },
              {
                name: "删除",
                code: "del",
                type: "text",
                api: '/basic/ms/dictGroup/',
                size: 'mini',
                idFieldName: 'groupId'
              }
            ],
          }
        ],
      },
    }
  },
  methods: {
    beforeOpen(done, type) {
      done()
    }
  }
};
</script>
