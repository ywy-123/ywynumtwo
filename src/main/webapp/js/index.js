let articleBox = new Vue({
    el:"#articleBox",
    data(){
        return {
            articles:[],
            totalPageNum:0,
            currentPage:1,
            currentCategory:7
        }
    },
    methods:{

        //这个方法我改了！！！！！！！！！！！！！
        //由于最新最热数据里面没有，但是也不能有，所以我用7表示最新，8表示最热（我们现在的实现不太好做最热，所以就没有做）
        //获取指定页数的文章
        getArticleByPage(pageNum){
            axios.get("http://localhost:8080/ArticlePublishingPlatform/getArticles?pageNum="+pageNum+"&categoryId="+this.currentCategory).then(resp=>{
                //虽然后端返回的数据是message对象的json字符串，但是前端接收到后会自动转换成message对象
                let message = resp.data;//获取到message对象
                if(message.statue=="200"){//如果成功响应,我们就把message对象里的jStr给手动转成我们的数组对象
                    //之所以要手动转，是因为这里这个存储aricle的数组的json字符串是作为message的属性Json发过来的，
                    articleBox.articles = JSON.parse(message.jstr);
                    articleBox.totalPageNum = parseInt(message.value);
                    articleBox.currentPage=pageNum;
                }else{
                    alert("服务器出错，请稍后再试！！！");
                }
            });
        },
        backPage(){
            if(articleBox.currentPage>1){
                // articleBox.currentPage=articleBox.currentPage-1;
                this.getArticleByPage(articleBox.currentPage-1);
            }
        },
        nextPage(){
            if(articleBox.currentPage<articleBox.totalPageNum){
                this.getArticleByPage(articleBox.currentPage+1);
            }
        },
        toArticleDetails(articleId){
            location.href="http://localhost:8080/ArticlePublishingPlatform/articleDetails.html?"+articleId;
        },
        toAuthorInformation(authorName){
            location.href="http://localhost:8080/ArticlePublishingPlatform/authorInformation.html?"+authorName;
        },
        getArticleBycatgory(categoryId){
            //数据库中并没有存最新最热，所以最新我用7表示，最热我用8表示
            this.currentCategory=categoryId;
            this.getArticleByPage(1);
        }
    },
    mounted() {

        //从后端获取文章的数组
        this.getArticleByPage(1);
        // axios.get("http://localhost:8080/ArticlePublishingPlatform/pageNum").then(resp=>{
        //     //alert("获取文章数"+resp.data);
        //     articleBox.totalPageNum=resp.data;
        // });
    }

});

function search(keyword,instance){
    if(keyword==""){
        alert("未输入关键词");
        return;
    }
    axios.get("http://localhost:8080/ArticlePublishingPlatform/search?instance="+instance+"&keyword="+keyword).then(resp=>{
        let message = resp.data;
        if(message.statue=="200"){
            console.log(message.jstr);
            articleBox.articles = JSON.parse(message.jstr);
            articleBox.totalPageNum=1;
        }else{
            alert("查询出现异常，稍后再试");
        }
    });
}

var keyword = document.getElementById("search");

document.getElementById("searchClick").onclick = function () {
    search(keyword.value,2);
}

var buttonVue = new Vue({
    el:"#displayButton",
    data(){
        return{

        }
    },
    methods:{
        judgeLogin(){
            let personnalCenter = document.getElementById("personal");
            let loginAndRegister = document.getElementById("loginandRegister");
            // 把上面两句代码挪出去就无法更改个人中心和登录注册是否显示
            personnalCenter.onclick=function () {
                location.href="http://localhost:8080/ArticlePublishingPlatform/html/PersonalInformation.html";
            }
            loginAndRegister.onclick=function () {
                location.href="http://localhost:8080/ArticlePublishingPlatform/login.html";
            }
            document.getElementById("searchUser").onclick=function () {
                location.href="http://localhost:8080/ArticlePublishingPlatform/searchUsers.html";
            }
            axios.get("http://localhost:8080/ArticlePublishingPlatform/judgeLogin").then(resp=> {
                let message = resp.data;
                if (message.value == "1") {
                    personnalCenter.style.display="";//显示
                    loginAndRegister.style.display="none";//不显示
                } else {
                    personnalCenter.style.display="none";
                    loginAndRegister.style.display="";
                }
            });
        }
    },
    mounted(){
        this.judgeLogin();
    }
});

