

let myArticleVue = new Vue({
    el:"#myArticleVue",
    data(){
        return {
            articles:[],
            totalPageNum:0,
            currentPage:1,
            authorName:"",
            isAuthorSelf:false
        }
    },
    methods:{
        //获取指定页数的文章
        getArticleByPage(pageNum){
            axios.get("http://localhost:8080/ArticlePublishingPlatform/getArticlesByUserId?pageNum="+pageNum+"&userId="+this.getAuthorId()).then(resp=>{
                //虽然后端返回的数据是message对象的json字符串，但是前端接收到后会自动转换成message对象
                let message = resp.data;//获取到message对象
                if(message.statue=="200"){//如果成功响应,我们就把message对象里的jStr给手动转成我们的数组对象
                    //之所以要手动转，是因为这里这个存储aricle的数组的json字符串是作为message的属性Json发过来的，
                    myArticleVue.articles = JSON.parse(message.jstr);
                    myArticleVue.currentPage=pageNum;
                    myArticleVue.authorName = message.value;
                }else{
                    alert("服务器出错，请稍后再试！！！");
                }

            });
        },
        backPage(){
            if(myArticleVue.currentPage>1){
                // myArticleVue.currentPage=myArticleVue.currentPage-1;
                this.getArticleByPage(myArticleVue.currentPage-1);
            }
        },
        nextPage(){
            if(myArticleVue.currentPage<myArticleVue.totalPageNum){
                this.getArticleByPage(myArticleVue.currentPage+1);
            }
        },
        toArticleDetails(article){
            alert(article.isFreezing)
            if(article.isFreezing == 0) {
                location.href = "http://localhost:8080/ArticlePublishingPlatform/articleDetails.html?" + article.articleId;
            }else if(this.isAuthorSelf){
                location.href = "http://localhost:8080/ArticlePublishingPlatform/articleDetails.html?" + article.articleId;
            }else{
                alert("文章被冻结，不可查看");
            }
        },
        toAuthorInformation(authorName){
            location.href="http://localhost:8080/ArticlePublishingPlatform/authorInformation.html?"+authorName;
        },
        getAuthorId(){
            let str = location.href;
            let index = str.lastIndexOf('?');
            let authorId = str.substring(index+1);
            // alert(authorName);
            return authorId;
        },
        judgeIsAuthorSelf(){
            axios.get("http://localhost:8080/ArticlePublishingPlatform/afterLogin/judgeIsSelf?authorId="+this.getAuthorId()).then(resp=> {
                if (resp.data == "1") {
                    this.isAuthorSelf = true;
                }
            });
        }
    },
    mounted() {
        //从后端获取文章的数组
        this.getArticleByPage(1);
        axios.get("http://localhost:8080/ArticlePublishingPlatform/getPageNumByAuthorId?userId="+this.getAuthorId()).then(resp=>{
            //alert("获取文章数"+resp.data);
            myArticleVue.totalPageNum=resp.data;
        });
        this.judgeIsAuthorSelf();
    }
});
