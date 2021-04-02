<template>
  <el-form ref="form"
           :model="model"
           :rules="rules"
           driver="vee"
           label-width="120px"
           class="demo-form"
  >

    <el-form-item label="姓名" prop="name">
      <el-input v-model="model.name"/>
    </el-form-item>

    <el-form-item label="备注" prop="comment">
      <el-input type="textarea" v-model="model.comment"/>
    </el-form-item>

    <el-form-item label="时间" >
      <el-col :span="11">
        <el-form-item prop="when.date">
          <el-date-picker type="date" placeholder="Date" v-model="model.when.date" style="width: 100%;"/>
        </el-form-item>
      </el-col>
      <el-col class="line" :span="2" style="text-align: center">-</el-col>
      <el-col :span="11">
        <el-form-item prop="when.time">
          <el-time-picker type="fixed-time" placeholder="Time" v-model="model.when.time" style="width: 100%;"/>
        </el-form-item>
      </el-col>
    </el-form-item>

    <el-form-item label="区域" prop="region">
      <el-select v-model="model.region">
        <el-option label="North" value="north"/>
        <el-option label="South" value="south"/>
      </el-select>
    </el-form-item>

    <el-form-item label="源" prop="resource">
      <el-radio-group v-model="model.resource">
        <el-radio label="Sponsorship"/>
        <el-radio label="Venue"/>
      </el-radio-group>
    </el-form-item>

    <el-form-item label="类型" prop="type" required>
      <el-checkbox-group v-model="model.type">
        <el-checkbox label="Online activities" name="type"/>
        <el-checkbox label="Offline activities" name="type"/>
        <el-checkbox label="Indoor activities" name="type"/>
        <el-checkbox label="Outdoor activities" name="type"/>
      </el-checkbox-group>
    </el-form-item>
    <el-form-item>
      <el-button @click="submit" type="primary">Submit</el-button>
      <el-button @click="reset">Reset</el-button>
    </el-form-item>
  </el-form>

</template>

<script>
export default {
  data () {
    return {
      model: {
        name: '',
        comment: '',
        when: {
          date: '',
          time: '',
        },
        region: '',
        resource: '',
        type: [],
      },
      rules : {
        name: 'required|alpha|min:2|max:5',
        comment: { required: true, alpha: true },
        when: {
          date: 'required',
          time: 'required',
        },
        region: 'required',
        resource: 'required',
        type: 'picked:2,3',
      }
    }
  },
  computed: {

  },
  methods:{
    submit () {
      this.$refs.form
          .validate((valid, errors) => {
            if (valid) {
              this.reset()
              this.status = 'Your data has been submitted, thank you!'
              this.errors = null
            } else {
              this.status = 'Your data has errors; please correct them and submit again'
              this.errors = Object.keys(errors).map(key => errors[key][0].message)
              // eslint-disable-next-line
              console.info(this.errors)
              return false
            }
          })
    },
    reset () {
      this.$refs.form.resetFields()
      this.errors = null
      this.status = ''
    }
  }
}
</script>

<style scoped>
</style>