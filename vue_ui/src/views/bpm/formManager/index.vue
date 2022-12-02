<template>
  <formManagerVue :component="$route.params.type"></formManagerVue>
</template>
<script>
import formManagerVue from "./formManager.vue";
export default{
  components:{
    formManagerVue
  }
}
</script>