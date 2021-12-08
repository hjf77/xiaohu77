<template>


<!--  <div id="full-screen-container" :ref="ref" class="login-container">-->
<!--      <div class="login-wrap">-->
<!--          <div class="login-content">-->

<!--          </div>-->
<!--      </div>-->

<!--  </div>-->

  <div class="login">
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
      <h3 class="title">FHS-DEMO</h3>
      <el-form-item prop="username">
        <el-input v-model="loginForm.username" type="text" auto-complete="off" placeholder="账号">
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="loginForm.password"
          type="password"
          auto-complete="off"
          placeholder="密码"
          @keyup.enter.native="handleLogin"
        >
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="code">
        <el-input
          v-model="loginForm.identifyCode"
          auto-complete="off"
          placeholder="验证码"
          style="width: 63%"
          @keyup.enter.native="handleLogin"
        >
          <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
        </el-input>
        <div class="login-code">
          <img :src="codeUrl" @click="getCode" />
        </div>
      </el-form-item>
      <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox>
      <el-form-item style="width:100%;">
        <el-button
          :loading="loading"
          size="medium"
          type="primary"
          style="width:100%;"
          @click.native.prevent="handleLogin"
        >
          <span v-if="!loading">登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
      </el-form-item>
    </el-form>
    <!--  底部  -->
    <div class="el-login-footer">
      <span>Copyright © 2018-2021 fhs-opensource All Rights Reserved.</span>
    </div>
  </div>
</template>

<script>
import { getCodeImg } from "@/api/login";
import Cookies from "js-cookie";
import { encrypt, decrypt } from '@/utils/jsencrypt'
import md5 from "js-md5";
export default {
  name: "Login",
  data() {
    return {

      ref: 'full-screen-container',
      allWidth: 0,
      scale: 0,
      datavRoot: '',
      ready: false,


      codeUrl: "",
      cookiePassword: "",
      loginForm: {
        username: "",
        password: "",
        rememberMe: false,
        identifyCode: "",
        uuid: ""
      },
      loginRules: {
        username: [
          { required: true, trigger: "blur", message: "用户名不能为空" }
        ],
        password: [
          { required: true, trigger: "blur", message: "密码不能为空" }
        ],
        identifyCode: [{ required: true, trigger: "change", message: "验证码不能为空" }]
      },
      loading: false,
      redirect: undefined
    };
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true
    }
  },
  beforeCreate() {
    // 当前页面宽度相对于 1920 宽的缩放比例，可根据自己需要修改。
    const scale = document.documentElement.clientWidth / 1920

    // 设置页面根节点字体大小
    document.documentElement.style.fontSize = (14 * Math.min(scale, 2)) + 'px'
  },
  created() {
    this.getCode();
    this.getCookie();
  },
  methods: {
    getCode() {

      getCodeImg().then(res => {
        this.codeUrl = "data:image/gif;base64," + res.data.img;
        this.loginForm.uuid = res.data.uuid;
      });
    },
    getCookie() {
      const username = Cookies.get("username");
      const password = Cookies.get("password");
      const rememberMe = Cookies.get('rememberMe')
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password: password === undefined ? this.loginForm.password : decrypt(password),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
      };
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true;
          if (this.loginForm.rememberMe) {
            Cookies.set("username", this.loginForm.username, { expires: 30 });
            Cookies.set("password", encrypt(this.loginForm.password), { expires: 30 });
            Cookies.set('rememberMe', this.loginForm.rememberMe, { expires: 30 });
          } else {
            Cookies.remove("username");
            Cookies.remove("password");
            Cookies.remove('rememberMe');
          }
          let payload = {
            username: this.loginForm.username ,
            password:  md5(this.loginForm.password),
            uuid: this.loginForm.uuid,
            identifyCode: this.loginForm.identifyCode
          };
          this.$store
            .dispatch("Login", payload)
            .then(() => {
              this.$router.push({ path: this.redirect || "/" });
            })
            .catch(() => {
              this.loading = false;
              this.getCode();
            });
        }
      });
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss">
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-image: url("../assets/image/login-background.jpg");
  background-size: cover;
}
.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #707070;
}

.login-form {
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 25px 25px 5px 25px;
  .el-input {
    height: 38px;
    input {
      height: 38px;
    }
  }
  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }
}
.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.login-code {
  width: 33%;
  height: 38px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}

.login-container {
  background: linear-gradient(0deg, #0A7D84, #00CBCB);
  position: fixed;
  top: 0px;
  left: 0px;
  overflow: hidden;
  transform-origin: left top;
  z-index: 999;
  height: 100%;
  width: 100%;
  box-sizing: border-box;
  padding:70px 140px;

}
.login-wrap{
  width:100%;
  height:940px;
  border-radius: 4px;
  background: rgba(255, 255, 255, 0.2);
  border: 1px solid rgba(249,249,249,0.5);
  box-shadow: 0px 0px 40px 0px rgba(0, 84, 89, 0.2);
  box-sizing: border-box;
  padding:20px;

}
.login-container:after {
  content:"";
  display: block;
  width:708px;
  height:503px;
  background-image: url("/static/login-bg.png");
  background-size: 100%;
  position: absolute;
  bottom: 0;
  left: 0;
}
.login-content{
  width: 100%;
  height: 100%;
  background: #fff;
  border-radius: 4px;
}
</style>
