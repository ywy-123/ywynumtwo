var searchUserVue = new Vue({
    el:"#app",
    data(){
        return{
            usersArray:[],
            totalPageNum:0,
            keyword:"",
            userRole:sessionStorage.getItem("role")
        }
    },
    methods:{
        search(){
            if(this.keyword==""){
                return;
            }
            axios.get("http://localhost:8080/ArticlePublishingPlatform/search?instance=1&keyword="+this.keyword).then(resp=>{
                let message = resp.data;
                console.log("查询用户:  "+JSON.stringify(message));
                searchUserVue.usersArray = JSON.parse(message.jstr);
            });
        },
        toIndex(){
            location.href="http://localhost:8080/ArticlePublishingPlatform/index.html";
        },
        toPersonalPage(){
            location.href="http://localhost:8080/ArticlePublishingPlatform/html/PersonalInformation.html";
        },
        toAuthorInformation(username){
            location.href="http://localhost:8080/ArticlePublishingPlatform/authorInformation.html?"+username;
        },
        deleteUser(userId){
            axios.get("http://localhost:8080/ArticlePublishingPlatform/afterLogin/manager/deleteUser?userId="+userId).then(resp=>{
                let message = resp.data;
                if(message.statue=="200"){
                    location.reload(true);
                }else{
                    alert("删除失败，稍后再试");
                }
            });
        }
    },
    mounted() {

    }
});

