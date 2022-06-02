<template>
     <div>
         <pagex-fileItem v-for="(file,i) in files" :key="i" :is-delete="isDelete" :is-download="isDownload" :file="file"></pagex-fileItem>
     </div>
</template>

<script>
export default {
  name: "FileDetail",
  props: {
    fileIds: {
      type: String || Array,
      default: null,
    },
    isDelete:{
      type: Boolean,
      default: false,
    },
    isDownload:{
      type: Boolean,
      default: true,
    }
  },
  computed: {},
  data() {
    return {
        files:[],
    }
  },
  created() {
    if(this.fileIds && this.fileIds.length !== 0){
      this.getFileInfoByids();
    }
  },
  methods: {
     getFileInfoByids(){
        //调用后台接口，并且初始化组件的文件列表
      this.$pagexRequest({
        method: "get",
        url: "/basic/downLoad/listData?fileIds=" + this.fileIds,
      })
        .then((res) => {
           this.files = res;
        })
        .catch((res) => {
          console.log(res);
        });
    }


  }
}
</script>
