var articleDetailvue = new Vue({
    el:"#artileVue",
    data(){
        return {
            article: {},
            authorId:"",
            editPower:false,
            isEditing:false,
            userRole:sessionStorage.getItem("role")
        }

    },
    methods:{
        toAuthor(){
            location.href = "http://localhost:8080/ArticlePublishingPlatform/";
        },
        //收藏或者取消收藏的方法
        collect(){
            axios.get("http://localhost:8080/ArticlePublishingPlatform/afterLogin/collectOperate?articleId="+articleDetailvue.article.articleId+"&isCollected="+this.article.collected).then(resp=>{
                let message = resp.data;
                if(message.statue=="401"){
                    location.href="http://localhost:8080/ArticlePublishingPlatform/login.html"
                }else if(message.statue=="200"){
                    if(articleDetailvue.article.collected){
                        articleDetailvue.article.collected = false;
                        articleDetailvue.article.collectNum-=1;
                    }else{
                        articleDetailvue.article.collected = true;
                        articleDetailvue.article.collectNum+=1;
                    }
                }
            });
        },
        getArticleId(){
            let str = location.href;
            let index = str.lastIndexOf('?');
            let articleId = str.substring(index+1);
            return articleId;
        },
        loadArticle(articleId){//请求servlet
            // alert("articleId="+articleId);
            axios.get("http://localhost:8080/ArticlePublishingPlatform/getArticleDetail?articleId="+articleId).then(resp=>{
                let message = resp.data;
                if(message.statue=="200"){
                    articleDetailvue.authorId = message.value;
                    articleDetailvue.article=JSON.parse(message.jstr);
                    articleDetailvue.judgeIsAuthorSelf();
                }
            })
        },
        enterEdit(){
            document.getElementById("articleContent").contentEditable = "true";
            this.isEditing = true;
        },
        save(){
            let formData = new FormData();
            let content = document.getElementById("articleContent").textContent;
            formData.append("content",content);
            axios.post(
                "http://localhost:8080/ArticlePublishingPlatform/afterLogin/changeText?articleId="+this.article.articleId,
                formData,
                {"Content-Type": "multipart/form-data;boundary="+new Date().getTime()}
            ).then(resp=>{
               let message = resp.data;
               if(message.statue=="401"){
                   location.href="http://localhost:8080/ArticlePublishingPlatform/login.html"
               }else if(message.statue=="200"){
                   document.getElementById("articleContent").contentEditable = "false";
                   articleDetailvue.article.content=content;
                   this.isEditing = false;
               }
            });
        },
        cancel(){
            document.getElementById("articleContent").contentEditable = "false";
            this.isEditing = false;
            window.flush();
        },
        judgeIsAuthorSelf(){
            axios.get("http://localhost:8080/ArticlePublishingPlatform/afterLogin/judgeIsSelf?authorId="+this.authorId).then(resp=> {
                if (resp.data == "1") {
                    articleDetailvue.editPower = true;
                }
            });
        },
        deleteArticle(articleId){
            axios.get("http://localhost:8080/ArticlePublishingPlatform/afterLogin/deleteArticle?articleId="+articleId).then(resp=>{
                let message = resp.data;
                if(message.statue=="200"){
                    location.href="http://localhost:8080/ArticlePublishingPlatform/index.html";
                }else{
                    alert("删除失败，稍后再试");
                }
            });
        }
    },
    mounted(){
        //给article对象赋值
        this.loadArticle(this.getArticleId());
    }

})
