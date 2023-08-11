package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.ArticleDaoImp;
import dao.UserDaoImp;
import pojo.Article;
import pojo.Message;
import utils.DataBaseUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ArticleService implements ArticleInterface{




    @Override
    public Message getAllArticles(ObjectMapper objectMapper,Integer num,Integer categoryId) {
        Message message = new Message();
        ArticleDaoImp articleDaoImp = new ArticleDaoImp();
        Connection connection = DataBaseUtils.getConnection();
        List<Article> list = null;
        if(categoryId==7){
            list = articleDaoImp.getAllArticles(connection,num);
            message.setValue(getTotalNum()+"");
        }else if(categoryId==8){
//            list = articleDaoImp.getHotArtcles(num);
//            message.setValue(getTotalNum()+"");
        }else{
            list = articleDaoImp.getArticlesByCategory(connection,num,categoryId);
            message.setValue(articleDaoImp.getArticleNumByCategory(connection,categoryId)+"");
        }
        try {
            String JsonStr = objectMapper.writeValueAsString(list);
            System.out.println(JsonStr);
            message.setJStr(JsonStr);
            message.setStatue("200");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            message.setStatue("500");
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return message;
        }

    }

    @Override
    public Integer getTotalNum() {
        return new ArticleDaoImp().getTotalNum();
    }

    @Override
    public Message publishArticle(String userId, String articleId, String title, int[] category, InputStream in) {
        //回滚无效（dao层未抛出异常，可以在dao层抛出异常，不进行处理）
        Message message = new Message();
        Connection connection = DataBaseUtils.getConnection();
        byte[] data =null;
        String content = null;
        try {
            data = new byte[in.available()];
            in.read(data);
            content = new String(data,"utf-8");
            connection.setAutoCommit(false);
            ArticleDaoImp articleDaoImp = new ArticleDaoImp();
            articleDaoImp.insertArticle(connection,articleId,title,content);
            articleDaoImp.insertArticleUser(connection,userId,articleId);
            articleDaoImp.insertCategories(connection,articleId,category);
            message.setStatue("200");
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return message;
        }


    }




    @Override
    public Message getArticleDetails(ObjectMapper om, String articleId,String userId) {
        Message message = new Message();
        Connection connection = DataBaseUtils.getConnection();
        try {
            ArticleDaoImp articleDaoImp = new ArticleDaoImp();
            Article article = articleDaoImp.getArticleDetails(connection,articleId);
            article.setCollected(articleDaoImp.judgeIsCollect(connection,userId,articleId));
            String s = om.writeValueAsString(article);
            message.setJStr(s);
            message.setValue(articleDaoImp.getUserIdByArticleId(connection,articleId));
            message.setStatue("200");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            message.setStatue("500");
        }finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return message;
        }
    }

    @Override
    public Message getArticlesByAuthorId(String authorId, Integer pageNum,ObjectMapper om) {
        Message message = new Message();
        Connection connection = DataBaseUtils.getConnection();
        ArticleDaoImp articleDaoImp = new ArticleDaoImp();
        List<Article> articles = articleDaoImp.getArticlesByAuthorId(connection,authorId,pageNum);
        try {
            message.setJStr(om.writeValueAsString(articles));
            message.setValue(articleDaoImp.getUserNameById(connection,authorId));
            message.setStatue("200");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            message.setStatue("500");
        }
        return message;
    }



    @Override
    public Message collectOperate(String articleId, Boolean isCollected,String userId) {
        Message message = new Message();
        ArticleDaoImp articleDaoImp = new ArticleDaoImp();
        Connection connection = DataBaseUtils.getConnection();
        String statue = null;
        if(isCollected){
            statue = articleDaoImp.cancelCollect(connection,articleId,userId);
        }else{
            statue = articleDaoImp.addCollect(connection,articleId,userId);
        }
        message.setStatue(statue);
        return message;
    }



    @Override
    public Message getCollectedArticles(String userId, Integer pageNum,Integer category) {
        Message message = new Message();
        ArticleDaoImp articleDaoImp = new ArticleDaoImp();
        UserDaoImp userDaoImp = new UserDaoImp();
        Connection connection = DataBaseUtils.getConnection();
        List<Article> list = null;
        if(category==7){
            list = articleDaoImp.getCollectedArticles(connection,userId,pageNum);
        }else{
            list = articleDaoImp.getCollectedArticlesByCategory(connection,pageNum,category,userId);
        }
        try {
            message.setJStr(new ObjectMapper().writeValueAsString(list));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        message.setValue((int)Math.ceil((double)userDaoImp.getCollectionNum(connection,userId,message)/ArticleDaoImp.onePageNum)+"");
        return message;
    }



    @Override
    public Message searchArticles(String keyword) {
        Message message = new Message();
        ArticleDaoImp articleDaoImp = new ArticleDaoImp();
        Connection connection = DataBaseUtils.getConnection();
        List<Article> articles = articleDaoImp.searchArticles(connection,keyword);
        try{
            message.setJStr(new ObjectMapper().writeValueAsString(articles));
            message.setStatue("200");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return message;
    }

    @Override
    public Message deleteArticle(String articleId) {
        Message message = new Message();
        Connection connection = DataBaseUtils.getConnection();
        ArticleDaoImp articleDaoImp = new ArticleDaoImp();
        String statue = articleDaoImp.deleteArticle(connection,articleId);
        message.setStatue(statue);
        return message;
    }
}
