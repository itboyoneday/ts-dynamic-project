package com.tsingsoft.common.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * ========================
 *
 * @author bask
 * @Description: mybatis工具类
 * @date : 2022/7/8 17:29
 * Version: 1.0
 * ========================
 */
public class MybatisUtil {

    private static SqlSessionFactory build ;
    private static  SqlSession sqlSession;
    //    静态代码块进行mybatis配置文件的读取并且获取出来sqlsession
    static {
        InputStream rs = null;
        try {
            rs = Resources.getResourceAsStream("mybatis-config.xml");
            build = new SqlSessionFactoryBuilder().build(rs);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (rs!=null){
                    rs.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }//end finally

    }
    //    未使用代理的获取对象
    public static Object getMapper(Class clazz){
        sqlSession =build.openSession();
        Object mapper = sqlSession.getMapper(clazz);
        return mapper;
    }
    public static void commit(){
        sqlSession.commit();
    }
    public static void rollback(){
        sqlSession.rollback();
    }
}
