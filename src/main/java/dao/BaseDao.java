package dao;

import utils.DataBaseUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDao<T>  {
    private Class<T> clazz = null;

    {
        Type genericSuperclass = this.getClass().getGenericSuperclass();//得到父类的泛型
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();//获取了父类的泛型参数
        clazz= (Class<T>) actualTypeArguments[0];
    }

    /**
     * 获取多个对象即多条查询
     */
    public List<T> getForList (Connection connection, String sql, Object ...args){
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            //1.链接
            connection = DataBaseUtils.getConnection();
            //2.sql操作
            ps = connection.prepareStatement(sql);
            //3.填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }
            //4.执行获取结果集（存数据）
            resultSet = ps.executeQuery();
            //5.获取封装的元数据(列数，列名在元数据中)
            ResultSetMetaData metaData = resultSet.getMetaData();
            //6.通过元数据由反射获得列名
            int columnCount = metaData.getColumnCount();//列数
            //创建集合对象
            ArrayList<T> list = new ArrayList<>();
            while (resultSet.next()){
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    //获取列值（通过结果集）
                    Object columValue = resultSet.getObject(i + 1);
                    //获取列名
                    //获取列的列名：getColumnName()
                    //获取列的别名(原表名也可以)：getColumnLabel() 推荐
                    String columnLabel = metaData.getColumnLabel(i+1);
                    //通过反射将对象指定名columnName 的指定属性进行赋值columValue
                    Field field = clazz.getDeclaredField(columnLabel);//指定字段名
                    field.setAccessible(true);
                    field.set(t,columValue);//为指定的对象的字段赋值
                }
                list.add(t);

            }
            return list;//这里要在while外面返回（1）创建就是在外面, （2）如果在while里面返回，那么只会有第一次的记录，就直接被返回了
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseUtils.closeSource(null,ps,resultSet);
        }
        return null;
    }


    /**
     * 返回一条数据 只有一hang
     * @param connection
     * @param sql
     * @param args
     * @param <E>
     * @return
     * @throws Exception
     */

    public <E>E getValue(Connection connection,String sql, Object ...args){
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            ps = connection.prepareStatement(sql);
            for(int i=0;i<args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            resultSet = ps.executeQuery();
            if(resultSet.next()){
                return (E)resultSet.getObject(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseUtils.closeSource(null,ps,resultSet);
        }

        return null;
    }

    /**
     * 返回一条数据 只有一列
     * @param connection
     * @param sql
     * @param args
     * @param <E>
     * @return
     * @throws Exception
     */

    public <E>E getValues(Connection connection,String sql, Object ...args){
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            ps = connection.prepareStatement(sql);
            for(int i=0;i<args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            resultSet = ps.executeQuery();
            if(resultSet.next()){
                return (E)resultSet.getObject(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseUtils.closeSource(null,ps,resultSet);
        }

        return null;
    }

    /**
     * 通用的查询操作，用于返回数据表中的一条数据(version2.0)
     * 获取一个对象即一条记录
     * 考虑上事务
     */
    public  T getInstance(Connection connection, String sql,Object ...args){
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            //1.链接
            connection = DataBaseUtils.getConnection();
            //2.sql操作
            ps = connection.prepareStatement(sql);
            //3.填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }
            //4.执行获取结果集（存数据）
            resultSet = ps.executeQuery();
            //5.获取封装的元数据(列数，列名在元数据中)
            ResultSetMetaData metaData = resultSet.getMetaData();
            //6.通过元数据由反射获得列名
            int columnCount = metaData.getColumnCount();//列数
            if(resultSet.next()){
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    //获取列值（通过结果集）
                    Object columValue = resultSet.getObject(i + 1);
                    //获取列名
                    //获取列的列名：getColumnName()
                    //获取列的别名(原表名也可以)：getColumnLabel() 推荐
                    String columnLabel = metaData.getColumnLabel(i+1);
                    //通过反射将对象指定名columnName 的指定属性进行赋值columValue
                    Field field = clazz.getDeclaredField(columnLabel);//指定字段名
                    field.setAccessible(true);
                    field.set(t,columValue);//为指定的对象的字段赋值
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseUtils.closeSource(null,ps,resultSet);
        }
        return null;
    }

    /**
     * 更新数据
     * @param connection
     * @param sql
     * @param args
     * @return
     */

    public int update(Connection connection, String sql, Object... args) {
        //sql中的占位符的个数，应该与可变符的长度一致
        PreparedStatement ps = null;
        try {
            //2,预编译sql语句，返回preparedStatement的实例
            ps = connection.prepareStatement(sql);
            //3,填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);//i+1是因为这个标号是从1开始的,后面从i开始是因为那是数组，数组是从0开始的
            }
            //4,执行
            return ps.executeUpdate();//返回的是影响了几行数据
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5.关闭资源
            DataBaseUtils.closeSource(null, ps);//connection是外部传入的，不要把它关闭了，方便后面操作可以继续使用
        }
        return 0;
    }


    public static Integer getTotalNum(String sql,Connection connection){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            return rs.getInt("totalNum");
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }

    }



}
