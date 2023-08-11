
//登录
var boxVue = new Vue({
    el:"#loginbox",
    data(){
        return {
            usernameErrorMessage:"",
            passwordErrorMessage:"",
            username:"",
            password:""
        }
    },
    methods: {
        //使用axios获取登录状态码，如果是0表示用户不存在，如果是1表示密码错误，如果是2表示账号被冻结，如果是3表示登陆成功进入首页
        submitData() {
            if (boxVue.usernameErrorMessage == "" && boxVue.passwordErrorMessage == "") {
                axios.get("http://localhost:8080/ArticlePublishingPlatform/login?username=" + boxVue.username + "&password=" + boxVue.password).then(function (resp) {
                    let message = resp.data;
                    let code = message.statue;
                    //alert(code);
                    //接受后端响应
                    if(code=="n"){
                        boxVue.usernameErrorMessage="用户名不规范";
                    }else if(code=="p"){
                        boxVue.passwordErrorMessage="密码不规范";
                    }else if (code == '0') {
                        boxVue.usernameErrorMessage = "用户不存在";
                    } else if (code == '1') {
                        boxVue.usernameErrorMessage = "密码错误";
                    } else if (code == '2') {
                        boxVue.usernameErrorMessage = "用户被冻结";
                    } else {
                        // alert("开始跳转");
                        //普通用户/管理员 登陆成功 首页相同，不进行区分
                        sessionStorage.setItem("role",message.value);
                        location.href = "http://localhost:8080/ArticlePublishingPlatform/index.html";
                        //http://localhost:8080/ArticlePublishingPlatform/index.html
                    }
                });
            }
        },
        //用户名输入框失去焦点后调用的方法
        usernameErrorTip() {
            if (!/^\w{2,10}$/.test(boxVue.username)) {
                boxVue.usernameErrorMessage = "用户名不合法";
            } else {
                this.usernameErrorMessage = "";
            }
        },
        //密码输入框失去焦点后调用的方法
        passwordErrorTip() {
            if (!/^[a-zA-Z0-9]{6,20}$/.test(boxVue.password)) {
                boxVue.passwordErrorMessage = "密码不合法";
            } else {
                boxVue.passwordErrorMessage = "";
            }
        }
    }

});