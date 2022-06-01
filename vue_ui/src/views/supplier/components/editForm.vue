<!--
  模块名称：供应商资料管理详情
  开发人员：何静静
  创建时间: 2022-5-31
-->
<template>
  <base-container>
    <div class="content-box">
      <!-- 头部信息 -->
      <div class="card">
        <div class="title-style">供应商：【 {{ detailsInfo.supplierCode }} 】{{ detailsInfo.name }}</div>
        <div class="contenr-info">
          <span>创建人：{{ detailsInfo.transMap.createUserUserName }}</span>
          <span>创建时间：{{ detailsInfo.createTime }}</span>
          <span>最后修改人：{{ detailsInfo.transMap.updateUserUserName }}</span>
          <span>最后修改时间：{{ detailsInfo.updateTime }}</span>
        </div>
      </div>
      <!-- 中间审核按钮 拒绝 按钮 -->
      <div class="card m-t-20">
        <el-button size="small">审核</el-button>
        <el-button size="small">按钮</el-button>
      </div>
      <!-- tabs切换 -->
      <div class="card m-t-20">
        <!-- @submitClick="submitClickFn" -->
        <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
          <el-tab-pane label="基本信息" name="basic">
            <bacisEditForm v-if="showForm" ref="bacisEditForm" :detailsInfo="detailsInfo"></bacisEditForm>
          </el-tab-pane>
          <el-tab-pane label="证件信息" name="certificate">
            <licenseEditForm></licenseEditForm>
          </el-tab-pane>
          <el-tab-pane label="财务信息" name="finance">
            <financialEditForm v-if="showForm" ref="financialEditForm" :detailsInfo="detailsInfo"></financialEditForm>
          </el-tab-pane>
        </el-tabs>
        <div class="btn-style">
          <!-- <el-button size="small" @click='btnCancelFn'>取消</el-button> -->
          <el-button
            class="btn-submit-sty"
            size="small"
            @click="btnSubmitFn"
          >{{btnSubmit ? '保存并下一步' : '保存'}}</el-button>
        </div>
      </div>
    </div>
  </base-container>
</template>

<script>
import bacisEditForm from '@/views/supplier/components/bacisEditForm.vue';

import financialEditForm from '@/views/supplier/components/financialEditForm.vue';

import licenseEditForm from '@/views/supplier/components/licenseEditForm.vue';
export default {
  name: 'editForm',
  components: {
    bacisEditForm,
    financialEditForm,
    licenseEditForm,
  },
  data() {
    return {
      activeName: 'basic',
      btnSubmit: true,
      count: 0,
      id: null,
      showForm:false,
      detailsInfo: {
        supplierCode: '- -',
        name: '- -',
        createTime: '- -',
        updateTime: '- -',
        transMap: {
          createUserUserName: '- -',
          updateUserUserName: '- -',
        },
      }, // 详情信息
      basicTabCanter: {},
      financeTabCanter: {},
    };
  },
  created() {
    this.id = this.$route.query && this.$route.query.id;
    // this.querys[0].value = this.id;
    this.supplierList(this.id);
  },
  methods: {
    supplierList(id) {
      this.$pagexRequest({
        url: '/basic/ms/supplierSupplier/' + id,
        method: 'GET',
      })
        .then((res) => {
          console.log(res);
          this.detailsInfo = res;
          this.showForm = true;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    handleClick(tab, event) {
      console.log(tab, event);
      if (this.activeName === 'finance') {
        this.btnSubmit = false;
      } else {
        this.btnSubmit = true;
      }
    },
    // submitClickFn(val) {
    //   if (val === 'bacisEditForm') {
    //     this.btnSubmit = true;
    //     this.activeName = 'certificate';
    //   } else if (val === 'financialEditForm') {
    //     this.btnSubmit = false;
    //     this.SaveInterface();
    //   }
    // },
    btnSubmitFn() {
      if (this.activeName === 'basic') {
        const _that = this
        this.$refs.bacisEditForm.validate(function(_model){
          console.log(3343)
          _that.basicTabCanter = _model
          _that.btnSubmit = true;
          _that.activeName = 'certificate';
          
        });
        
      } else if (this.activeName === 'certificate') {
        this.activeName = 'finance';
        this.btnSubmit = false;
      } else if (this.activeName === 'finance') {
        const _that = this
        this.$refs.financialEditForm.validate(function(_model){
          console.log(_model)
          _that.financeTabCanter = _model
          _that.btnSubmit = false;
          _that.SaveInterface();
        });
      }
    },
    SaveInterface() {
      const Canter = Object.assign(this.basicTabCanter,this.financeTabCanter,{id: this.id})
      this.$pagexRequest({
        url: '/basic/ms/supplierSupplier/updateField',
        method: 'PUT',
        data: Canter
      })
        .then((res) => {
          this.msgSuccess(
            res.message || '修改成功'
          );
          this.$router.push({ path: '/supplier/supplierSupplier' });
          console.log(res);
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
};
</script>

<style lang="scss" scoped>
.card {
  border-radius: 4px;
  background-color: #ffffff;
  padding: 20px;
  .title-style {
    color: #0f9b9c;
    font-weight: 700;
    line-height: 30px;
  }
  .contenr-info {
    span {
      font-size: 14px;
      line-height: 30px;
      &:nth-child(1) {
        padding-right: 20px;
      }
      &:nth-child(n + 2) {
        padding: 0 20px;
      }
    }
  }
}
.m-t-20 {
  margin-top: 20px;
}
.btn-style {
  margin: 20px 0;
  display: flex;
  justify-content: center;
  .btn-submit-sty {
    background-color: #0f9b9c;
    color: #ffffff;
  }
}
</style>