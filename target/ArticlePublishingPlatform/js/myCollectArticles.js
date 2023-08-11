

let articleBox = new Vue({
    el:"#articleBox",
    data(){
        return {
            articles:[],
            totalPageNum:0,
            currentPage:1,
            currentCategory:7,
            userId:this.getUserId()
        }
    },
    methods:{
        getUserId(){
            let str = location.href;
            let index = str.lastIndexOf('?');
            let authorId = str.substring(index+1);
            // alert(authorName);
            return authorId;
        },
        //获取指定页数的文章
        getArticleByPage(pageNum){
            axios.get("http://localhost:8080/ArticlePublishingPlatform//afterLogin/getCollectedArticles?pageNum="+pageNum+"&userId="+this.userId+"&category="+this.currentCategory).then(resp=>{
                //虽然后端返回的数据是message对象的json字符串，但是前端接收到后会自动转换成message对象
                let message = resp.data;//获取到message对象
                if(message.statue=="200"){//如果成功响应,我们就把message对象里的jStr给手动转成我们的数组对象
                    //之所以要手动转，是因为这里这个存储aricle的数组的json字符串是作为message的属性Json发过来的，
                    articleBox.articles = JSON.parse(message.jstr);
                    articleBox.currentPage=pageNum;
                    articleBox.totalPageNum = parseInt(message.value);
                    console.log("当前收藏类型为:"+articleBox.currentCategory+" 文章页数为:"+articleBox.totalPageNum);
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
            this.currentCategory = categoryId;
            this.getArticleByPage(1);
        },
        cancelCollect(artcleId,userId){
            axios.get("http://localhost:8080/ArticlePublishingPlatform/afterLogin/cancelCollect?articleId="+artcleId+"&userId="+userId).then(resp=> {
                let message = resp.data;
                if (message.statue == "200") {
                    articleBox.getArticleByPage(1);
                }else{
                    alert("取消失败，稍后再试");
                }
            });
        },
        toPersonalPage() {
            location.href="http://localhost:8080/ArticlePublishingPlatform/html/PersonalInformation.html";
        }
    },
    mounted() {

        //从后端获取文章的数组
        this.getArticleByPage(1);
        document.getElementById("personal").onclick=function () {
            articleBox.toPersonalPage();
        }
        // axios.get("http://localhost:8080/ArticlePublishingPlatform/collectPageNum?userId="+this.userId).then(resp=>{
        //     //alert("获取文章数"+resp.data);
        //     articleBox.totalPageNum=resp.data;
        // });
    }

});
