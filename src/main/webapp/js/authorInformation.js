
var authorPageVue = new Vue({
    el:"#authorPage",
    data(){
        return {
            user:{}
        }
    },
   methods:{
        //请求后端user对象
        loadUser(){
            axios.get("http://localhost:8080/ArticlePublishingPlatform/getAuthorInformation?authorName="+this.getAuthorName()).then(resp=>{

                let message = resp.data;
                if(message.statue=="401"){
                    location.href="http://localhost:8080/ArticlePublishingPlatform/login.html";
                }
                else if(message.statue=="200"){
                    authorPageVue.user = JSON.parse(message.jstr);
                    let followButton = document.getElementById("follow");
                    //防止异步
                    followButton.onclick=function (){
                        // alert("asdads");
                        axios.get("http://localhost:8080/ArticlePublishingPlatform/afterLogin/follow?toUserId="+authorPageVue.user.userId+"&isFollow="+authorPageVue.user.flag).then(resp=>{
                            let meesage = resp.data;
                            if(meesage.statue=="401"){
                                //权限不够，跳转到登录页
                                location.href="http://localhost:8080/ArticlePublishingPlatform/login.html";
                            }else if(meesage.statue=="200") {
                                if (authorPageVue.user.flag == 1) {
                                    authorPageVue.user.flag = 2;
                                    authorPageVue.user.fansNum-=1;
                                    followButton.innerText = "关注";
                                } else if (authorPageVue.user.flag == 2) {
                                    authorPageVue.user.flag = 1;
                                    authorPageVue.user.fansNum+=1;
                                    followButton.innerText = "已关注";
                                }
                            }});
                        }

                    if(authorPageVue.user.flag==1){
                        followButton.innerText="已关注";
                    }
                    else if(authorPageVue.user.flag==2){
                        followButton.innerText="关注";
                    }}else{
                    alert(message.description);
                }

            })
        },
        getAuthorName(){
            let str = location.href;
            let index = str.lastIndexOf('?');
            let authorName = str.substring(index+1);
            // alert(authorName);
            return authorName;
        },
       toMyArticles(userId){
            location.href = "http://localhost:8080/ArticlePublishingPlatform/myArticles.html?"+userId;
       }

    },
    mounted(){
        this.loadUser();
    }
})

document.getElementById("index").onclick=function (){
    location.href="http://localhost:8080/ArticlePublishingPlatform/index.html";
}
