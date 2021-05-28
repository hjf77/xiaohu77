
<template>
  <div id="app">
    <viewer :images="images"
            @inited="inited"
            class="viewer" ref="viewer"
    >
      <template scope="scope">
        <img v-for="src in scope.images" :src="src" :key="src" width="100%">
      </template>
    </viewer>
  </div>
</template>

<script>
import 'viewerjs/dist/viewer.css'
import Viewer from "v-viewer/src/component.vue"
export default {
  components: {
    Viewer
  },
  props:{
    images:{
      type:Array,
      default:()=>{}
    }
  },
  data() {
    return{

    }
  },
  mounted() {
  },
  methods: {
    inited (viewer) {
      this.$viewer = viewer
    }
  }
}
</script>
