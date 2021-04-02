<template>
  <pagex-form api="/form/add"
            :data="{'sex':'1','hobby':'2','hobbys':['1','2'],'whether':1,'count':3,'range':5,'pages':10}"
            :controls= "[
              {
                type: 'text',
                name: 'ame',
                label: '姓名',
                rule:'required'
              },
              {
                type: 'select',
                name: 'sex',
                label: '性别',
                url:'/dictList/index',
                labelField:'title',
                valueField:'id',
                rule:'required',
                filterable:true,
                clearable:true
              },
              {
                type: 'radio',
                name: 'hobby',
                label: '爱好',
                options:[{'value':'1',label:'打篮球'},{'value':'2',label:'打王者'}]
              },
              {
                type: 'checkbox',
                name: 'hobbys',
                label: '爱好',
                options:[{'value':'1',label:'打篮球'},{'value':'2',label:'打王者'}]
              },
              {
                type: 'switch',
                name: 'whether',
                label: '付费',
                options:{label:'开关说明'}
              },
              {
                type: 'inputNumber',
                name: 'count',
                label: '计数',
                options:{label:'衣服',min:0, max:100}
              },
              {
                type: 'slider',
                name: 'range',
                label: '音量',
                options:{label:'音量',min:0, max:100}
              },
              {
                type: 'pagination',
                name: 'pages',
                label: '页数',
                options:{total:500}
              },
            ]"
          ></pagex-form>

</template>

<script>
export default {
  data () {
    return {
    }
  },
  computed: {

  },
  methods:{
    
  }
}
</script>
