<template>
  <span>
  <e-icon :icon-name="icon"/>
  <span v-if="title" >{{title}}</span>
  </span>
</template>
<script>
export default {
  name: 'MenuItem',
  props: {
    icon: {
      type: String,
      default: ''
    },
    title: {
      type: String,
      default: ''
    }
  }
}
</script>
