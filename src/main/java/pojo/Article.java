package pojo;

public class Article {
    private String title;
    private String author;
    private String ArticleId;
    private String categoryName;
    private int isFreezing;
    private int isChanging;
    private String CreateTime;
    private String UpdateTime;
    private Integer collectNum;
    private Integer likeNum;
    private Boolean collected;//判断被查看的该文章是否被查看其的用户所收藏
    private String content;

    public Boolean getCollected() {
        return collected;
    }

    public void setCollected(Boolean collected) {
        this.collected = collected;
    }

    public Article() {
    }

    public Article(String title, String author, String articleId, String categoryName, int isFreezing, int isChanging) {
        this.title = title;
        this.author = author;
        ArticleId = articleId;
        this.categoryName = categoryName;
        this.isFreezing = isFreezing;
        this.isChanging = isChanging;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getArticleId() {
        return ArticleId;
    }

    public void setArticleId(String articleId) {
        ArticleId = articleId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getIsFreezing() {
        return isFreezing;
    }

    public void setIsFreezing(int isFreezing) {
        this.isFreezing = isFreezing;
    }

    public int getIsChanging() {
        return isChanging;
    }

    public void setIsChanging(int isChanging) {
        this.isChanging = isChanging;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String updateTime) {
        UpdateTime = updateTime;
    }

    public Integer getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(Integer collectNum) {
        this.collectNum = collectNum;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }



    @Override
    public String toString() {
        return "Article{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", ArticleId='" + ArticleId + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", isFreezing=" + isFreezing +
                ", isChanging=" + isChanging +
                ", CreateTime='" + CreateTime + '\'' +
                ", UpdateTime='" + UpdateTime + '\'' +
                ", collectNum=" + collectNum +
                ", likeNum=" + likeNum +
                ", collected=" + collected +
                '}';
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
