package dao;

import pojo.Article;

import java.sql.Connection;
import java.util.List;

public interface ArticleDao {
   /**
    * 获取文章的基本信息
    * @param connection
    * @param num
    * @return
    */
   List<Article> getAllArticles(Connection connection,Integer num);

   /**
    * 获取文章总数
    * @return
    */
   Integer getTotalNum();

   /**
    *插入目录
    * @param connection
    * @param articleId
    * @param categories
    */
   void insertCategories(Connection connection,String articleId,int categories[]);

   /**
    *插入作者
    * @param connection
    * @param userId
    * @param articleId
    */
   void insertArticleUser(Connection connection,String userId,String articleId);

   /**
    *插入文章
    * @param connection
    * @param articleId
    * @param title
    * @param content
    */
   void insertArticle(Connection connection,String articleId,String title,String content);

   /**
    *获取文章细节
    * @param connection
    * @param article
    * @return
    */
   Article getArticleDetails(Connection connection,String article);

   /**
    *通过文章Id获取用户id
    * @param connection
    * @param articleId
    * @return
    */
   String getUserIdByArticleId(Connection connection,String articleId);

   /**
    *通过作者的id获取文章
    * @param connection
    * @param authorId
    * @param pageNum
    * @return
    */
   List<Article> getArticlesByAuthorId(Connection connection,String authorId,Integer pageNum);

   /**
    *通过文章id获得收藏数
    * @param connection
    * @param articleId
    * @return
    */
   Integer getCollectedNum(Connection connection,String articleId);

   /**
    *通过id获得作者名称
    * @param connection
    * @param userId
    * @return
    */
   String getUserNameById(Connection connection,String userId);

   /**
    *判断是否被收藏
    * @param connection
    * @param userId
    * @param articleId
    * @return
    */
   Boolean judgeIsCollect(Connection connection,String userId,String articleId);

   /**
    *取消收藏
    * @param connection
    * @param articleId
    * @param userId
    * @return
    */
   String cancelCollect(Connection connection,String articleId,String userId);

   /**
    *收藏
    * @param connection
    * @param articleId
    * @param userId
    * @return
    */
   String addCollect(Connection connection,String articleId,String userId);

   /**
    *获取自己收藏的文章
    * @param connection
    * @param userId
    * @param pageNum
    * @return
    */
   List<Article> getCollectedArticles(Connection connection,String userId,Integer pageNum);

   /**
    *获取对应标签的文章
    * @param connection
    * @param num
    * @param categoryId
    * @return
    */
   List<Article> getArticlesByCategory(Connection connection,Integer num,Integer categoryId);

   /**
    *
    * @param connection
    * @param pageNum
    * @param categoryId
    * @param userId
    * @return
    */
   List<Article> getCollectedArticlesByCategory(Connection connection,Integer pageNum,Integer categoryId,String userId);

   /**
    *
    * @param connection
    * @param categoryId
    * @return
    */
   Integer getArticleNumByCategory(Connection connection ,Integer categoryId);

   /**
    *
    * @param connection
    * @param categoryId
    * @return
    */
   Integer getCollectedArticleNumByCategory(Connection connection ,Integer categoryId);

   /**
    *搜索文章内容
    * @param connection
    * @param keyword
    * @return
    */
   List<Article> searchArticles(Connection connection,String keyword);

   /**
    *删除文章
    * @param connection
    * @param articleId
    * @return
    */
   String deleteArticle(Connection connection,String articleId);

}
