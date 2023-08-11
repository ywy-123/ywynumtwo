var selectFile = document.createElement("input");
selectFile.type= "file";
selectFile.setAttribute("style",'visibility:hidden');
document.body.appendChild(selectFile);
selectFile.onchange=function () {
    let file = selectFile.files[0];
    let formData = new FormData;
    formData.append("excelFile",file);
    axios.post(
        "http://localhost:8080/ArticlePublishingPlatform/afterLogin/manager/uploadExcel",
        formData
    ).then(resp=>{
        let message = resp.data;
        if(message.statue=="200"){
            alert("导入数据成功");
        }else{
            alert(message.description);
        }
    });
}


var personalPageVue = new Vue({
    el:"#personalPage",
    data(){
        return {
            user:{},
            editFlag:false,
            usernameCopy:"",
            userRole:sessionStorage.getItem("role"),
            articleTotalNum:0
        }
    },
    methods:{
        //请求后端user对象
        loadUser(){
            axios.get("http://localhost:8080/ArticlePublishingPlatform/afterLogin/getUser").then(resp=>{
                let message = resp.data;
                if(message.statue=="401"){
                    location.href="http://localhost:8080/ArticlePublishingPlatform/login.html";
                }
                else if(message.statue=="200"){
                    personalPageVue.user = JSON.parse(message.jstr);
                }else{
                    alert(message.description);
                }
            })
        },
        editor(){
            this.usernameCopy=this.user.userName;
            this.editFlag=true;
            document.getElementById("username").removeAttribute("readonly");//取消readonly
        },
        save(){
            if(confirm("保存？")){
                // let name = document.getElementById("username").textContent;
                axios.get("http://localhost:8080/ArticlePublishingPlatform/afterLogin/saveUserInfor?updateName="+personalPageVue.user.userName).then(resp=>{
                let message=resp.data;
                    if(message.statue=="401"){
                        location.href="http://localhost:8080/ArticlePublishingPlatform/login.html";
                    } else if(message.statue=="200"){
                        this.editFlag=false;
                        document.getElementById("username").setAttribute("readonly","");//取消readonly
                    }else{
                        alert("保存失败！")
                    }
                })
            }
        },
        Cancle(){
            if(confirm("不保存？")){
                this.user.userName=this.usernameCopy;
                this.editFlag=false;
                document.getElementById("username").setAttribute("readonly","");//取消readonly
            }
        },
        toPublishing(){
            location.href="http://localhost:8080/ArticlePublishingPlatform/html/publishing.html";
        },
        zhuxiao(){
            axios.get("http://localhost:8080/ArticlePublishingPlatform/afterLogin/zhuxiao").then(resp=>{
                let message = resp.data;
                if(message.statue=="200"){
                    location.href="http://localhost:8080/ArticlePublishingPlatform/login.html";
                }else{
                    alert("服务器出错，注销失败");
                }
            });
        },
        toCollectPage(){
            location.href="http://localhost:8080/ArticlePublishingPlatform/html/myCollectArticles.html?"+this.user.userId;
        },
        toMyArticles(userId){
            location.href = "http://localhost:8080/ArticlePublishingPlatform/myArticles.html?"+userId;
        },
        uploadExcel(){
            selectFile.click();
        },
        downloadExcel(){
            axios({
                url:"http://localhost:8080/ArticlePublishingPlatform/downloadExcel",
                method:"get",
                responseType:"blob"
            }).then(resp=>{
                const data = resp.data;
                saveAs(data,"articles.xlsx");//"articles.xlsx"为指定的excel文件名
                //这个data是通过后端获取的，就是说是后端将表格生成为二进制数据发给前端，就是resp.data的内容
            });
        },
        getTotalNum(){//获得总文章数
            axios.get("http://localhost:8080/ArticlePublishingPlatform/afterLogin/manager/getArticleTotalNum").then(resp=>{
              personalPageVue.articleTotalNum=resp.data;
            })
        }
    },
    mounted(){
        this.loadUser();
        this.getTotalNum();
    }
})

document.getElementById("index").onclick=function (){
    location.href="http://localhost:8080/ArticlePublishingPlatform/index.html";
}
// document.getElementById("username").removeAttribute("readonly");//取消readonly

function saveAs(data, name) {//将得到的二进制数据保存为本地文件
    //data：得到的Blob数据，name:保存在本地的文件的名字
    const urlObject = window.URL || window.webkitURL || window;
    const export_blob = new Blob([data]);
    const save_link = document.createElementNS('http://www.w3.org/1999/xhtml', 'a');
    save_link.href = urlObject.createObjectURL(export_blob);
    save_link.download = name;
    save_link.click();
    let formData3 = new FormData()
    formData3.append('taskid', id);
    postAction(_that.downloadOver, formData3)
}