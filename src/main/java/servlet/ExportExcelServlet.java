package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utils.DataBaseUtils;

import javax.xml.crypto.Data;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/downloadExcel")
public class ExportExcelServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("articles");//相当于创建惟一的默认表
        Row row = sheet.createRow(0);
        String[] columns = new String[]{"标题","作者","类别","上传时间"};//表头
        for(int i = 0;i<columns.length;i++){
            Cell cell = row.createCell(i);
            cell.setCellValue(columns[i]);
        }
        Connection connection = DataBaseUtils.getConnection();

        String getArticles = "select title,category_names,createTime,authorName from (select articles.title as title,articles.article_id as article_id,GROUP_CONCAT( categories.category_name SEPARATOR '|' ) As category_names,articles.create_time as createTime,articles.update_time as updateTime,users.user_name as authorName,users.user_id as userId,articles.is_freezing as isFreezing" +
                "            from articles " +
                "            inner JOIN user_article on articles.article_id=user_article.article_id " +
                "            INNER join users ON users.user_id=user_article.user_id " +
                "            inner join article_category on article_category.article_id = articles.article_id " +
                "            inner join categories on article_category.category_id=categories.id " +
                "            GROUP BY users.user_id,users.user_name,articles.title,articles.article_id,articles.create_time,articles.update_time) as articles";

        ResultSet resultSet = null;
        PreparedStatement preparedStatement=null;
        try {
            preparedStatement = connection.prepareStatement(getArticles);
            resultSet = preparedStatement.executeQuery();
            int rowNum = 1;
            String[] tableColumns = new String[]{"title","authorName","category_names","createTime"};
            while(resultSet.next()){
                Row row1 = sheet.createRow(rowNum);
                rowNum++;
                for(int i = 0;i<tableColumns.length;i++){
                    row1.createCell(i).setCellValue(resultSet.getString(tableColumns[i]));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                resultSet.close();
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        OutputStream os = resp.getOutputStream();
        resp.setContentType("application/x-download");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Content-disposition", "attachment;filename=articles.xlsx");
        workbook.write(os);
        os.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}