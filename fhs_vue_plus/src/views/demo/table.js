/*
 * @Author: sky.li
 * @LastEditors: sky.li
 * @Description:
 * @可以输入预定的版权声明、个性签名、空行等
 */

import schema2component from "../../lib/utils/schema2component";

const schema = {
  type: "page",
  title: "会员列表",
  body: {
    type: "card",
    body: [
      {
        label: "新增",
        type: "button",
        actionType: "dialog",
        level: "primary",
        dialog: {
          title: "新增表单",
          body: {
            type: "form",
            api: "/form/add",
            data:{'sex':'1'},
            controls: [
              {
                type: "text",
                name: "name",
                label: "姓名",
                rule:'required',
                visibleOn:function(){
                  return this.sex !=2;
                },
                disabledOn:function(){
                  return this.sex ==1;
                }
              },
              {
                type: "select",
                name: "sex",
                label: "性别",
                url:'/dictList/index',
                labelField:'title',
                valueField:'id',
                rule:'required',
                filterable:true,
                clearable:true
              }
            ]
          }
        }
      },
      {
        type: "crud",
        title: "asd",
        api: "/list/index",
        methods:{

        },
        filter: {
          title: "条件搜索",
          controls: [
            {
              type: "text",
              label: "关键字",
              name: "keywords",
              placeholder: "通过关键字搜索"
            }
          ]
        },
        columns: [
          {
            name: "id",
            label: "ID"
          },
          {
            name: "title",
            label: "title"
          },
          {
            name: "content",
            label: "content"
          },
          {
            type: "formart",
            name: "content",
            label: "内容2",
            formart:"<a >你好世界88888</a>",
            click:function(_row){
              console.log(_row);
              console.log(this);
            }
          },
          {
            type: "operation",
            label: "操作",
            buttons: [
              {
                label: "删除",
                type: "button",
                actionType: "ajax",
                level: "danger",
                confirmText: "确认要删除？",
                api: "delete:https://houtai.baidu.com/api/sample/${id}"
              },
              {
                label: "修改",
                type: "button",
                actionType: "dialog",
                dialog: {
                  title: "修改表单",
                  body: {
                    type: "form",
                    initApi: "/api/sample/${id}",
                    api: "put:/api/sample/${id}",
                    controls: [
                      {
                        type: "text",
                        name: "engine",
                        label: "Engine"
                      },
                      {
                        type: "text",
                        name: "browser",
                        label: "Browser"
                      }
                    ]
                  }
                }
              }
            ]
          }
        ]
      }
    ]
  }
};

export default schema2component(schema);
