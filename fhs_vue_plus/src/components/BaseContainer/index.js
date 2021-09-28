import BaseContainer from './src/main.vue'


BaseContainer.install = (Vue)=>{
    Vue.component(BaseContainer.name,BaseContainer)
}

export default BaseContainer