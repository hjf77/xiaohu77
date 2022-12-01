<template>
  <div ref="asyncCompontent">
    <component v-if="componentName" v-bind:is="componentName"></component>
  </div>
</template>
<script>
const modules = import.meta.glob('/src/views/**/*.vue')
export default {
    props:{
      component:{
        type:String,
        default:""
      }
    },
    data(){
      return {
        componentName:null,
      }
    },
    methods: {
      initCompontent(){
        this.componentName = modules['/src/views/bpm/processInstance/components/' + this.component + '.vue'] 
      }
    },
    mounted() {
      this.initCompontent()
    }
}
</script>