package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import pojo.Message;

import java.io.InputStream;

public interface ArticleInterface {
    Message getAllArticles(ObjectMapper objectMapper,Integer num,Integer categoryId);
    Integer getTotalNum();
    Message publishArticle(String userId, String articleId, String title, int category[], InputStream in);
    Message getArticleDetails(ObjectMapper om,String articleId,String userId);
    Message getArticlesByAuthorId(String authorId,Integer pageNum,ObjectMapper om);
    Message collectOperate(String articleId,Boolean isCollected,String userId);
    Message getCollectedArticles(String userId,Integer pageNum,Integer category);

    Message searchArticles(String keyword);
    Message deleteArticle(String articleId);
}
