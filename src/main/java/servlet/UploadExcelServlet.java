package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import pojo.Article;
import pojo.Message;
import utils.DataBaseUtils;
import utils.ExcelImportSheet;

import javax.xml.crypto.Data;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/afterLogin/manager/uploadExcel")
@MultipartConfig
public class UploadExcelServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Part part = req.getPart("excelFile");

        String fileName = part.getSubmittedFileName();
        InputStream in = part.getInputStream();

        Message message = new Message();

        Connection connection = DataBaseUtils.getConnection();

        Statement pInsertArticleCategory = null;
        PreparedStatement pInsertArticles = null;
        PreparedStatement pInsertArticleUser = null;

        String insertArticleCategory="insert into article_category(article_id,category_id) values";
        String insertArticles="insert into articles(article_id,title,is_freezing,create_time,content,update_time) values(?,?,?,?,?,?)";
        String insertAtrticleUser="insert into user_article(user_id,article_id) values(?,?)";
        try {

            connection.setAutoCommit(false);


            Workbook workbook = ExcelImportSheet.getTypeFromExtends(in,fileName);
            Sheet sheet = workbook.getSheetAt(0);
            for(int i = sheet.getFirstRowNum()+1;i<sheet.getLastRowNum();i++){
                insertArticles+=",(?,?,?,?,?,?)";
                insertAtrticleUser+=",(?,?)";
            }
//            Row row = sheet.getRow(sheet.getFirstRowNum());
            pInsertArticleCategory = connection.createStatement();
            pInsertArticles = connection.prepareStatement(insertArticles);
            pInsertArticleUser = connection.prepareStatement(insertAtrticleUser);

            for(int num =0,i = sheet.getFirstRowNum()+1;i<=sheet.getLastRowNum();i++,num++){

                Row row = sheet.getRow(i);

                String title = row.getCell(0).getStringCellValue();
                String articleId = row.getCell(2).getStringCellValue();
                String categoryName = row.getCell(3).getStringCellValue();
                Integer isFreezing = (int)row.getCell(4).getNumericCellValue();
                java.sql.Date uploadTime = new java.sql.Date(row.getCell(5).getDateCellValue().getTime());
                java.sql.Date changeTime = new java.sql.Date(row.getCell(6).getDateCellValue().getTime());

                String authorId = row.getCell(7).getStringCellValue();
                String content = row.getCell(8).getStringCellValue();

                String[] categories = categoryName.split(",");
                for(int x = 0;x<categories.length;x++){
                    insertArticleCategory+="('"+articleId+"',"+categories[x]+"),";
                }
                pInsertArticles.setString(1+num*6,articleId);
                pInsertArticles.setString(2+num*6,title);
                pInsertArticles.setInt(3+num*6,isFreezing);
                pInsertArticles.setDate(4+num*6,uploadTime);
                pInsertArticles.setString(5+6*num,content);
                pInsertArticles.setDate(6+6*num,changeTime);
                pInsertArticleUser.setString(1+num*2,authorId);
                pInsertArticleUser.setString(2+num*2,articleId);
            }
            insertArticleCategory = insertArticleCategory.substring(0,insertArticleCategory.length()-1);
            System.out.println("插入文章"+pInsertArticles.executeUpdate());
            System.out.println("插入类别"+pInsertArticleUser.executeUpdate());
            System.out.println("插入文章与作者"+pInsertArticleCategory.executeUpdate(insertArticleCategory));
            connection.commit();
            message.setStatue("200");
        } catch (Exception e) {
            message.setStatue("500");
            message.setDescription("插入数据出错");
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        ObjectMapper om = new ObjectMapper();
        resp.getWriter().write(om.writeValueAsString(message));
    }
}