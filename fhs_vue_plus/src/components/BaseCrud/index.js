import BaseCrud from './src/main.vue'

import BaseForm from './src/form.vue'
import Editor from './src/Editor.vue'
import formDetail from './src/formDetail.vue'

BaseCrud.install = (Vue)=>{
    Vue.component(BaseCrud.name,BaseCrud)
  Vue.component(BaseForm.name,BaseForm)
  Vue.component(Editor.name,Editor)
  Vue.component(formDetail.name,formDetail)

}

export default BaseCrud
