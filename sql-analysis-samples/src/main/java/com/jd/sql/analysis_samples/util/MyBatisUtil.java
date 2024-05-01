package com.jd.sql.analysis_samples.util;

import com.jd.sql.analysis.util.GsonUtil;
import com.jd.sql.analysis_samples.mapper.TaskMapper;
import com.jd.sql.analysis_samples.po.Task;
import lombok.Getter;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Author huhaitao21
 * @Description Mybatis工具类
 * @Date 14:35 2022/11/11
 **/
public class MyBatisUtil {
		@Getter
		private static SqlSessionFactory sqlSessionFactory;

		static {
				InputStream fis = null;
				InputStream inputStream = null;
				try {
						//创建Properties对象
						Properties prop = new Properties();
						//创建输入流，指向配置文件,getResourceAsStream可以从classpath加载资源
						fis = Resources.getResourceAsStream("jdbc.properties");
						//加载属性文件
						prop.load(fis);

						// 从类路径下加载资源文件mybatis-config.xml
						String resource = "mybatis-config.xml";
						InputStream is = Resources.getResourceAsStream(resource);
						// 由 SqlSessionFactoryBuilder创建SqlSessionFactory
						setSqlSessionFactory(new SqlSessionFactoryBuilder().build(is, "dev", prop));
				} catch (IOException e) {
						e.printStackTrace();
				} finally {
						if (fis != null) {
								try {
										fis.close();
								} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
								}
						}
						if (inputStream != null) {
								try {
										inputStream.close();
								} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
								}
						}
				}
		}

		public static void main(String[] args) {
				SqlSession sqlSession = getSqlSession();
				System.out.println(sqlSession);
				TaskMapper mapper = sqlSession.getMapper(TaskMapper.class);

				Long id = 21L;
				Task task = mapper.selectByPrimaryKey(id);
				System.out.println(GsonUtil.bean2Json(task));
		}


		/**
		 * 由 SqlSessionFactory创建SqlSession
		 *
		 * @return
		 */
		public static SqlSession getSqlSession() {
				return getSqlSessionFactory().openSession();
		}

		/**
		 * 关闭SqlSession
		 *
		 * @param sqlSession
		 */
		public static void closeSqlSession(SqlSession sqlSession) {
				if (sqlSession != null) {
						sqlSession.close();
				}
		}

		public static void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
				MyBatisUtil.sqlSessionFactory = sqlSessionFactory;
		}
}
