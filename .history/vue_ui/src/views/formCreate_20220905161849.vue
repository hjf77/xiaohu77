<template>
  <base-container>
  
  <fc-designer ref="designer"/>
  </base-container>
</template>
  
<script>
import FcDesigner from '@/formCreate/index.js';

export default {
  name: "Index",
  components: {
    FcDesigner
  },
  methods: {
    handleClick(row) {
      console.log(row);
    },
  },
};
</script>
  