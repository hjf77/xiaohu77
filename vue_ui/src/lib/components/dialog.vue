<template>
  <el-dialog   v-bind="$attrs" v-on="$listeners" :visible.sync="visible" >
    <slot></slot>
  </el-dialog>
</template>

<script>
export default {
    props:{
        visible:{
            type:Boolean,
            default:false,
        },
        eventBusName: {
          type: String,
          default: () => null,
        }
    },
    computed:{

    },
    mounted() {
      this.$nextTick(()=>{
        if(this.eventBusName){
          this.$EventBus.$on(this.eventBusName,() => {
            this.$emit('update:visible', false);
          })
        }
      })
    },
    methods:{

    }
}
</script>

<style lang="scss" scoped>

</style>
