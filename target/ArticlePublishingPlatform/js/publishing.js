
var filePublishing = new  Vue({
    el:"#filePublish",
    data(){
        return{
            title:""
        }
    },
    methods:{
        publish() {
            if (confirm("确认发布")) {
                var formData = new FormData();
                var content = document.getElementById("File").files[0];
                var checkboxes = document.querySelectorAll("input[type='checkbox']");//获取标签
                let category = "";
                for (let i = 0; i < checkboxes.length; i++) {
                    if (checkboxes[i].checked) {
                        category += "," + checkboxes[i].value;
                    }
                }
                formData.append("file", content);
                axios.post("http://localhost:8080/ArticlePublishingPlatform/afterLogin/filePublished?title=" + filePublishing.title + "&categories=" + category, formData).then(resp => {
                    let message = resp.data;
                    if (message.statue == "200") {
                        location.href = "http://localhost:8080/ArticlePublishingPlatform/html/PersonalInformation.html";
                    } else {
                        alert("发布失败，稍后再试");
                    }
                })
            }
        }
    }
})