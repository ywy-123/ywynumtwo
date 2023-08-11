package dao;

import pojo.Article;
import utils.DataBaseUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticleDaoImp extends BaseDao<Article> implements ArticleDao{


    public static  final int onePageNum =10;
    @Override
    public List<Article> getAllArticles(Connection connection,Integer num) {
        List<Article> articles = new ArrayList<>();
        Statement statement  =null;
        try {
            statement = connection.createStatement();
            String query = "select articles.title,articles.article_id,GROUP_CONCAT( categories.category_name SEPARATOR \"#\" ) As category_names,articles.create_time,articles.update_time,users.user_name \n" +
                    "from articles \n" +
                    "inner JOIN user_article on articles.article_id=user_article.article_id \n" +
                    "INNER join users ON users.user_id=user_article.user_id \n" +
                    "inner join article_category on article_category.article_id = articles.article_id \n" +
                    "inner join categories on article_category.category_id=categories.id \n" +
                    "GROUP BY articles.title,articles.article_id,articles.create_time,articles.update_time,users.user_name order by create_time desc limit "+(num-1)*onePageNum+","+onePageNum;
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                Article article = new Article();
                article.setTitle(rs.getString("articles.title"));
                article.setAuthor(rs.getString("users.user_name"));
                article.setArticleId(rs.getString("articles.article_id"));
                article.setCategoryName(rs.getString("category_names"));
                article.setCreateTime(rs.getDate("articles.create_time").toString());
                article.setCollectNum(getCollectedNum(connection,article.getArticleId()));
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DataBaseUtils.closeSource(connection,statement);
        }
        return articles;
    }


    public static final String GETARTICLESNUM = "select count(*) as totalNum from articles where is_freezing=0";
    @Override
    public Integer getTotalNum() {
        Connection connection = DataBaseUtils.getConnection();
        double totalNum =getTotalNum(GETARTICLESNUM,connection);
        totalNum = Math.ceil(totalNum/onePageNum);
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (int)totalNum;
    }

    //插入标签
    public static final String INSERTCATEGORY="insert into article_category(article_id,category_id) values(?,?)";
    @Override
    public void insertCategories(Connection connection,String articleId, int[] categories) {
        System.out.println(categories.length);
        for(int i = 0 ;i<categories.length;i++){
            System.out.println("categories:"+categories[i]);
        }


        String insertcategories = INSERTCATEGORY;
        for(int i = 0;i<categories.length-1;i++){
            insertcategories+=",(?,?)";
        }

        System.out.println(insertcategories);

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(insertcategories);
            for(int i=0;i<categories.length;i++){
                preparedStatement.setString(i*2+1,articleId);
                preparedStatement.setInt(i*2+2,categories[i]);
            }

            System.out.println(preparedStatement);

            preparedStatement.executeUpdate();//一定要记得执行！！！！！
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    //插入文章与用户的关系表
    public static final String INSERTARTICLEUSER="insert into user_article(user_id,article_id) value(?,?) ";
    @Override
    public void insertArticleUser(Connection connection, String userId, String articleId) {
        int update = update(connection, INSERTARTICLEUSER, userId, articleId);//影响的行数
    }

    //插入文章表
    public static final String INSERTARTICLE="insert into articles(article_id,title,content) value(?,?,?)";
    @Override
    public void insertArticle(Connection connection, String articleId, String title,String content) {
        int update = update(connection, INSERTARTICLE, articleId, title,content);
    }


    
    //获取一篇文章的所有信息
    public static final String GETARTICLEDETAIL = "select author,update_time,title,article_id,category_names,create_time,content from (select articles.title as title,articles.article_id as article_id,GROUP_CONCAT( categories.category_name SEPARATOR \",\") As category_names,articles.create_time as create_time,articles.update_time as update_time,users.user_name as author,articles.content as content \n" +
            "            from articles \n" +
            "            inner JOIN user_article on articles.article_id=user_article.article_id \n" +
            "            INNER join users ON users.user_id=user_article.user_id \n" +
            "            inner join article_category on article_category.article_id = articles.article_id \n" +
            "            inner join categories on article_category.category_id=categories.id \n" +
            "            GROUP BY articles.title,articles.article_id,articles.create_time,articles.update_time,users.user_name) as articles where article_id = ?";
    @Override
    public Article getArticleDetails(Connection connection, String articleId) {
        Article article = null;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(GETARTICLEDETAIL);
            preparedStatement.setString(1,articleId);
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) {
                article = new Article();
                article.setTitle(rs.getString("title"));
                article.setArticleId(articleId);
                article.setAuthor(rs.getString("author"));
                article.setCategoryName(rs.getString("category_names"));
                article.setCreateTime(rs.getDate("create_time").toString());
                article.setCollectNum(getCollectedNum(connection,articleId));
                article.setUpdateTime(rs.getDate("update_time").toString());
                article.setContent(rs.getString("content"));
            }else{

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return article;
        }
    }

    //根据文章id获取用户Id
    public static final String GETUSERIDBYARTICLEID = "select user_id from user_article where article_id = ?";
    @Override
    public String getUserIdByArticleId(Connection connection, String articleId) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement  = connection.prepareStatement(GETUSERIDBYARTICLEID);
            preparedStatement.setString(1,articleId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getString("user_id");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static final String GETARTICLESBYAUTHORID = "select isFreezing,author,update_time,title,article_id,category_names,create_time from (select articles.title as title,articles.article_id as article_id,GROUP_CONCAT( categories.category_name SEPARATOR ',') As category_names,articles.create_time as create_time,articles.update_time as update_time,users.user_name as author,users.user_id as authorId,articles.is_freezing as isFreezing \n" +
            "            from articles \n" +
            "            inner JOIN user_article on articles.article_id=user_article.article_id \n" +
            "            INNER join users ON users.user_id=user_article.user_id \n" +
            "            inner join article_category on article_category.article_id = articles.article_id \n" +
            "            inner join categories on article_category.category_id=categories.id \n" +
            "            GROUP BY users.user_id,users.user_name,articles.title,articles.article_id,articles.create_time,articles.update_time,users.user_name) as articles where authorId=? limit ?,"+onePageNum;
    @Override
    public List<Article> getArticlesByAuthorId(Connection connection, String authorId, Integer pageNum) {
        List<Article> list = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(GETARTICLESBYAUTHORID);
            preparedStatement.setString(1,authorId);
            preparedStatement.setInt(2,(pageNum-1)*onePageNum);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Article article = new Article();
                article.setTitle(resultSet.getString("title"));
                article.setAuthor(resultSet.getString("author"));
                article.setArticleId(resultSet.getString("article_id"));
                article.setCategoryName(resultSet.getString("category_names"));
                article.setCreateTime(resultSet.getDate("create_time").toString());
                article.setIsFreezing(resultSet.getInt("isFreezing"));
                article.setCollectNum(getCollectedNum(connection,article.getArticleId()));
                list.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                preparedStatement.close();
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    //
    public static final String cancelCollect="delete from collect where article_id=? and user_id=?";
    @Override
    public String cancelCollect(Connection connection, String articleId, String userId) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(cancelCollect);
            preparedStatement.setString( 1,articleId);
            preparedStatement.setString(2,userId);
            int roll = preparedStatement.executeUpdate();
            if(roll == 1){
                return "200";
            }else{
                return "500";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "500";
        }
    }


    //
    public static final String addCollect = "insert into collect(user_id,article_id,author_id) value(?,?,?)";
    @Override
    public String addCollect(Connection connection, String articleId, String userId) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(addCollect);
            preparedStatement.setString(1,userId);
            preparedStatement.setString(2,articleId);
            preparedStatement.setString(3,getUserIdByArticleId(connection,articleId));
            int roll = preparedStatement.executeUpdate();
            if (roll==1) {
                return "200";
            }else{
                return "500";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "500";
        }

    }

    //
    public static final String getCollectedArticles= "select isFreezing,userId,title,article_id,category_names,createTime,updateTime,authorName from (select articles.title as title,articles.article_id as article_id,GROUP_CONCAT( categories.category_name SEPARATOR '|' ) As category_names,articles.create_time as createTime,articles.update_time as updateTime,users.user_name as authorName,users.user_id,articles.is_freezing as isFreezing,collect.user_id as userId\n" +
            "            from articles \n" +
            "            inner JOIN user_article on articles.article_id=user_article.article_id\n" +
            "            INNER join users ON users.user_id=user_article.user_id \n" +
            "\t\t\t\t\t\tinner join article_category on article_category.article_id = articles.article_id\n" +
            "            inner join categories on article_category.category_id=categories.id \n" +
            "            inner join collect on articles.article_id = collect.article_id\n" +
            "\t\t\t\t\t\tGROUP BY users.user_id,users.user_name,articles.title,articles.article_id,articles.create_time,articles.update_time,collect.user_id) as articles where userId = ? limit ?,"+onePageNum;
    @Override
    public List<Article> getCollectedArticles(Connection connection, String userId, Integer pageNum) {
        List<Article> list = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            preparedStatement = connection.prepareStatement(getCollectedArticles);
            preparedStatement.setString(1,userId);
            preparedStatement.setInt(2,(pageNum-1)*onePageNum);
            rs = preparedStatement.executeQuery();
            while(rs.next()){

//                System.out.println("开始创建Article对象");
                Article article = new Article();
                article.setTitle(rs.getString("title"));
                article.setAuthor(rs.getString("authorName"));
                article.setArticleId(rs.getString("article_id"));
                article.setCategoryName(rs.getString("category_names"));
                article.setCreateTime(rs.getDate("createTime").toString());
                article.setUpdateTime(rs.getString("updateTime").toString());
                article.setIsFreezing(rs.getInt("isFreezing"));
                article.setCollectNum(getCollectedNum(connection,article.getArticleId()));
                list.add(article);
//                System.out.println("加入一个Article对象");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Article> getArticlesByCategory(Connection connection,Integer num, Integer category) {
        List<Article> articles = new ArrayList<>();
        Statement statement  =null;
        try {
            statement = connection.createStatement();
            String query = "select articles.title,articles.article_id,GROUP_CONCAT( categories.category_name SEPARATOR \"#\" ) As category_names,articles.create_time,articles.update_time,users.user_name \n" +
                    "from articles \n" +
                    "inner JOIN user_article on articles.article_id=user_article.article_id \n" +
                    "INNER join users ON users.user_id=user_article.user_id \n" +
                    "inner join article_category on article_category.article_id = articles.article_id \n" +
                    "inner join categories on article_category.category_id=categories.id where category_id="+category+" \n" +
                    "GROUP BY articles.title,articles.article_id,articles.create_time,articles.update_time,users.user_name limit "+(num-1)*onePageNum+","+onePageNum;
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                Article article = new Article();
                article.setTitle(rs.getString("articles.title"));
                article.setAuthor(rs.getString("users.user_name"));
                article.setArticleId(rs.getString("articles.article_id"));
                article.setCategoryName(rs.getString("category_names"));
                article.setCreateTime(rs.getDate("articles.create_time").toString());
                article.setCollectNum(getCollectedNum(connection,article.getArticleId()));
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DataBaseUtils.closeSource(null,statement);
        }
        return articles;
    }




    public static final String getCollectedArticlesByCategory= "select isFreezing,userId,title,article_id,category_names,createTime,updateTime,authorName from (select articles.title as title,articles.article_id as article_id,GROUP_CONCAT( categories.category_name SEPARATOR '|' ) As category_names,articles.create_time as createTime,articles.update_time as updateTime,users.user_name as authorName,users.user_id,articles.is_freezing as isFreezing,collect.user_id as userId\n" +
            "            from articles \n" +
            "            inner JOIN user_article on articles.article_id=user_article.article_id\n" +
            "            INNER join users ON users.user_id=user_article.user_id \n" +
            "\t\t\t\t\t\tinner join article_category on article_category.article_id = articles.article_id\n" +
            "            inner join categories on article_category.category_id=categories.id \n" +
            "            inner join collect on articles.article_id = collect.article_id where category_id=? \n" +
            "\t\t\t\t\t\tGROUP BY users.user_id,users.user_name,articles.title,articles.article_id,articles.create_time,articles.update_time,collect.user_id) as articles where userId = ? limit ?,"+onePageNum;
    @Override
    public List<Article> getCollectedArticlesByCategory(Connection connection, Integer pageNum, Integer categoryId, String userId) {
        List<Article> list = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            preparedStatement = connection.prepareStatement(getCollectedArticlesByCategory);
            preparedStatement.setInt(1,categoryId);
            preparedStatement.setString(2,userId);
            preparedStatement.setInt(3,(pageNum-1)*onePageNum);
            rs = preparedStatement.executeQuery();
            while(rs.next()){
                Article article = new Article();
                article.setTitle(rs.getString("title"));
                article.setAuthor(rs.getString("authorName"));
                article.setArticleId(rs.getString("article_id"));
                article.setCategoryName(rs.getString("category_names"));
                article.setCreateTime(rs.getDate("createTime").toString());
                article.setUpdateTime(rs.getString("updateTime").toString());
                article.setIsFreezing(rs.getInt("isFreezing"));
                article.setCollectNum(getCollectedNum(connection,article.getArticleId()));
                list.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }



    public static final String getArticlesNumByCategory = "select count(*) as totalNum from articles inner join article_category on articles.article_id=article_category.article_id where is_freezing=0 and category_id=?";
    @Override
    public Integer getArticleNumByCategory(Connection connection ,Integer categoryId) {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        int totalNum = 0;
        try {
            preparedStatement = connection.prepareStatement(getArticlesNumByCategory);
            preparedStatement.setInt(1,categoryId);
            rs = preparedStatement.executeQuery();
            rs.next();
            totalNum = rs.getInt("totalNum");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        totalNum = (int)Math.ceil((double)totalNum/onePageNum);
        return totalNum;
    }


    public static final String getCollectedArticlesNumByCategory = "select count(*) as totalNum from articles inner join article_category on articles.article_id=article_category.article_id inner join user_article on user_article.article_id=articles.article_id where is_freezing=0 and category_id=? and ";
    @Override
    public Integer getCollectedArticleNumByCategory(Connection connection ,Integer categoryId) {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        int totalNum = 0;
        try {
            preparedStatement = connection.prepareStatement(getCollectedArticlesNumByCategory);
            preparedStatement.setInt(1,categoryId);
            rs = preparedStatement.executeQuery();
            rs.next();
            totalNum = rs.getInt("totalNum");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        totalNum = (int)Math.ceil((double)totalNum/onePageNum);
        return totalNum;
    }


    public static final String searchArticles = "select content,isFreezing,userId,title,article_id,category_names,createTime,updateTime,authorName from (select articles.title as title,articles.article_id as article_id,GROUP_CONCAT( categories.category_name SEPARATOR '|' ) As category_names,articles.create_time as createTime,articles.update_time as updateTime,users.user_name as authorName,users.user_id as userId,articles.is_freezing as isFreezing,articles.content as content" +
            "            from articles " +
            "            inner JOIN user_article on articles.article_id=user_article.article_id " +
            "            INNER join users ON users.user_id=user_article.user_id " +
            "            inner join article_category on article_category.article_id = articles.article_id " +
            "            inner join categories on article_category.category_id=categories.id " +
            "            GROUP BY users.user_id,users.user_name,articles.title,articles.article_id,articles.create_time,articles.update_time) as articles where title like ? OR authorName like ? OR content like ?";
    @Override
    public List<Article> searchArticles(Connection connection, String keyword) {
        keyword = "%"+keyword+"%";
        byte[] bytes = keyword.getBytes(StandardCharsets.UTF_8);
        try {
            keyword=new String(bytes,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        List<Article> articles =  new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(searchArticles);
            preparedStatement.setString(1,keyword);
            preparedStatement.setString(2,keyword);
            preparedStatement.setString(3,keyword);
            rs = preparedStatement.executeQuery();
            while(rs.next()){
                Article article = new Article();
                article.setTitle(rs.getString("title"));
                article.setAuthor(rs.getString("authorName"));
                article.setArticleId(rs.getString("article_id"));
                article.setCategoryName(rs.getString("category_names"));
                article.setCreateTime(rs.getDate("createTime").toString());
                article.setUpdateTime(rs.getString("updateTime").toString());
                article.setIsFreezing(rs.getInt("isFreezing"));
                article.setCollectNum(getCollectedNum(connection,article.getArticleId()));
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                preparedStatement.close();
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return articles;
        }
    }


    public static final String deleteArticle="delete article_category,articles,user_article from article_category inner join articles on articles.article_id=article_category.article_id inner join  user_article on user_article.article_id=articles.article_id where articles.article_id=?";
    public static final  String deleteArticleFromCollect = "delete from collect where article_id=?";
    @Override
    public String deleteArticle(Connection connection, String articleId) {
        PreparedStatement preparedStatement = null;
        try{
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(deleteArticle);
            preparedStatement.setString(1,articleId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            preparedStatement = connection.prepareStatement(deleteArticleFromCollect);
            preparedStatement.setString(1,articleId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.commit();
            return "200";
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return "500";
        }
    }


    //根据文章Id获取收藏数
    public static final String GETCOLLECTEDNUM = "select count(*) as collectNum from collect where article_id=?";
    public Integer getCollectedNum(Connection connection,String articleId){
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = connection.prepareStatement(GETCOLLECTEDNUM);
            preparedStatement.setString(1,articleId);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            return rs.getInt("collectNum");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    //根据用户id获取用户名
    public static final String getUserNameById = "select user_name from users where user_id=?";
    public String getUserNameById(Connection connection,String userId){
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            preparedStatement = connection.prepareStatement(getUserNameById);
            preparedStatement.setString(1,userId);
            rs = preparedStatement.executeQuery();
            rs.next();
            return rs.getString("user_name");
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }


    public static final String judgeIsCollect="select article_id from collect where user_id=? and article_id=?";
    public Boolean judgeIsCollect(Connection connection,String userId,String articleId){
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            preparedStatement = connection.prepareStatement(judgeIsCollect);
            preparedStatement.setString(1,userId);
            preparedStatement.setString(2,articleId);
            rs = preparedStatement.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }finally {
            DataBaseUtils.closeSource(null,preparedStatement,rs);
        }
    }
}
