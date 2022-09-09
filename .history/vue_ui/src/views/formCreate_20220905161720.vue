<template>
  <base-container> </base-container>
</template>
  
<script>
import FcDesigner from '@/formCreate/index.js';

export default {
  name: "Index",
  methods: {
    handleClick(row) {
      console.log(row);
    },
  },
};
</script>
  