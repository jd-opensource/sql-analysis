import com.jd.sql.analysis.analysis.SqlAnalysis;
import com.jd.sql.analysis.extract.SqlExtractResult;
import com.jd.sql.analysis_samples.mapper.TaskMapper;
import com.jd.sql.analysis_samples.po.Task;
import com.jd.sql.analysis_samples.util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNull;

/**
 * @Author huhaitao21
 * @Description SqlAnalysisTest
 * @Date 19:13 2022/11/10
 **/
public class SqlAnalysisTest {

		private TaskMapper mapper;
		private SqlSession sqlSession;

		@Before
		public void setUp() {
				sqlSession = MyBatisUtil.getSqlSession();
				mapper = sqlSession.getMapper(TaskMapper.class);
		}

		@Test
		public void testAnalysisWithNullConnection() {
				SqlExtractResult sqlExtractResult = new SqlExtractResult();
				sqlExtractResult.setSourceSql("SELECT * FROM task");
				assertNull(SqlAnalysis.analysis(sqlExtractResult, null));
		}

		@Test
		public void testFastSql() {
				Task task = new Task();
				task.setCreateUser("zhangsan1");
				List<Task> list = mapper.queryAll(task);
				Assert.assertNotNull(list);

		}

		@Test
		public void testSimpleSlowSql() {
				Task task = new Task();
				task.setTitle("测试");
				List<Task> list = mapper.queryAll(task);
				Assert.assertNotNull(list);
		}


		@Test
		public void testNewSql1() {
				SqlExtractResult sqlExtractResult = new SqlExtractResult();
				sqlExtractResult.setSourceSql("SELECT * FROM task WHERE create_user = 'zhangsan1'");
				Assert.assertNotNull(SqlAnalysis.analysis(sqlExtractResult, sqlSession.getConnection()));
		}

		@Test
		public void testNewSql2() {
				SqlExtractResult sqlExtractResult = new SqlExtractResult();
				sqlExtractResult.setSourceSql("SELECT * FROM task WHERE title = '测试'");
				Assert.assertNotNull(SqlAnalysis.analysis(sqlExtractResult, sqlSession.getConnection()));
		}
}
