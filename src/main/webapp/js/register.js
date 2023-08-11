
var registerBox = new Vue({
    el:"#registerbox",
    data(){
        return{
            usernameErrorMessage:"",
            passwordErrorMessage:"",
            sureErrorMessage:"",
            registerErrorMessage:"",
            username:"",
            password:"",
            surePassword:""
        }
    },
    methods:{
        //发送请求，若返回0则代表用户已存在，不能注册
        judgeUserName(){
            if(!/^\w{2,10}$/.test(this.username)){
                this.usernameErrorMessage = "用户名不合法";
            }else{
                axios.get("http:////localhost:8080/ArticlePublishingPlatform/judgeUserName?username="+registerBox.username).then(function (resp) {
                    let message=resp.data;//这是后端发来的message对象
                    // if(message.statue=="300"){
                    //     registerBox.usernameErrorMessage = message.description;
                    // }else{
                    //     registerBox.usernameErrorMessage="";
                    // }
                    registerBox.usernameErrorMessage = message.description;//将message的对象的描述信息显示
                });
            }
        },
        judgePassword(){
            if(!/^[a-zA-Z0-9]{6,20}$/.test(this.password)){
                this.passwordErrorMessage = "密码不合法";
            }else{
                this.passwordErrorMessage = "";
            }
        },
        surePasswordTip(){
            // alert(this.password+"   "+this.surePassword);
            if(this.password == this.surePassword){
                this.sureErrorMessage = "";
            }else{
                this.sureErrorMessage = "密码不相同";
            }
        },
        register(){
            if(this.usernameErrorMessage==""&&this.passwordErrorMessage==""&&this.sureErrorMessage==""){
                axios.get("http:////localhost:8080/ArticlePublishingPlatform/register?username="+registerBox.username+"&password="+registerBox.password).then(resp =>{
                    // alert("请求发送");
                    let message=resp.data;

                    if(message.statue=="500"){
                        this.registerErrorMessage = "注册失败！稍后再试";
                    }else{
                        location.href = "http://localhost:8080/ArticlePublishingPlatform/login.html";//注册成功，跳转首页
                    }
                });
            }
        }
    }
});