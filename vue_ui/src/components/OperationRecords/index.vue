<!--
  模块名称：操作记录
  开发人员：姜伟
  创建时间:2021/7/27 14:00
-->
<template>
  <div class="block">
    <div class="operation">操作记录</div>
    <div class="main_opinion">
        <el-timeline>
          <el-timeline-item
            v-for="(item, index) in operationlist"
            :key="index"
            :icon="item.icon"
            type="danger"
            color="#53BD6C"
            size="normal"
            class="eltimelinefocus"
          >
            <div style="margin-bottom: 20px">
              <span class="opinion opininoTitle" :style="{marginRight: item.title.length > 2 ? '35px' : '63px'}">{{ item.title + ':' }}</span>
              <span class="agree" :style="{ color: item.color }">{{ item.result }}</span>
              <span class="opinion" :style="{ marginLeft: item.result ? '0px' : '-17px' }">{{ item.createTime }}</span>
            </div>
            <div class="content">
              <div class="recommendations">
                <div class="opinion">操&#8194;作&#8194;人：</div>
                <div>{{ item.transMap.updateUserUserName }}</div>
              </div>
              <!-- 添加意见字段 -->
              <div class="recommendations" v-if="item.content">
                <div class="opinion">意&#8195;&#8195;见：</div>
                <div>{{ item.content }}</div>
              </div>
              <!-- 添加备注字段 -->
              <div class="recommendations" v-if="item.descType != 1 && item.remark">
                <div class="opinion">备&#8195;&#8195;注：</div>
                <div>{{ item.remark }}</div>
              </div>
            </div>
          </el-timeline-item>
        </el-timeline>
      </div>
  </div>
</template>

<script>
export default {
  name: "OperationRecords",
  props: {
    operationapi: {
      type: String,
      default: "/ms/commonOperatorLog/findListAdvance"
    },
    fKey: {
      type: String,
      default: ''
    },
    namespace: {
      type: String,
      default: 'work_rules'
    },
    sorter: {
      type: Array,
      default: ()=> {
        return [{
          direction: "ASC",
          property: "createTime",
        }]
      }
    },
    operationQuerys: {
      type: Array,
      default: ()=> {
        return []
      }
    }
  },
  computed: {},
  created() {
    this.getfindList()
  },
  data() {
    return {
      operationlist: []
    }
  },
  methods: {
    getfindList() {
      if (this.operationapi && this.operationQuerys.length > 0) {
        this.$pagexRequest({
          url: this.operationapi,
          method: 'POST',
          data: {
            'sorter': this.sorter,
            'querys': this.operationQuerys
          }
        }).then((res) => {
          this.operationlist = res
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.el-timeline-item__wrapper {
  padding-left: 23px;
}

.el-timeline-item__tail {
  border-left: 1px solid #e0e0e0;
}

.block {
  background: #ffffff;
  border-radius: 10px;
  padding: 20px 30px;
  margin: 20px;
}

.operation {
  font-size: 16px;
  font-weight: 400;
  color: #333333;
  margin-bottom: 29px;
}

.opininoTitle {
  margin-left: -98px;
}

.agree {
  font-size: 14px;
  font-family: Microsoft YaHei;
  font-weight: 400;
  color: #0d57ff;
  margin-right: 17px;
}

.content {
  padding: 10px 12px 10px 12px;
  background: #f7f9fb;
  border-radius: 4px;
  margin: 20px 0;
}

.eltimelinefocus {
  ::v-deep .el-timeline-item__node {
    border: 2px solid hsla(0, 0%, 90%, 0.7) !important;
  }
}

::v-deep .el-timeline-item__tail {
  margin-top: 23px;
  height: 75%;
  background: #e0e0e0;
}

.opinion {
  font-size: 14px;
  font-weight: 400;
  color: #888888;
}

.main_opinion {
  margin-left: 70px;
}

.el-timeline {
  padding-left: 0px;
  margin-top: -10px;
}

.reviewer {
  line-height: 36px;
}

.recommendations {
  line-height: 36px;
  display: -webkit-inline-box;
  width: 670px;
}

.content {
  width: 760px;
}

::v-deep .eltimelinefocus {
  padding-bottom: 0px;
}
</style>
