import com.jd.sql.analysis.util.GsonUtil;
import com.jd.sql.analysis_samples.mapper.TaskMapper;
import com.jd.sql.analysis_samples.po.Task;
import com.jd.sql.analysis_samples.util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author huhaitao21
 * @Description SqlAnalysisTest
 * @Date 19:13 2022/11/10
 **/
public class SqlAnalysisTest {

    private TaskMapper mapper;

    @Test
    public void testFastSql(){
        //走索引
        SqlSession  sqlSession = MyBatisUtil.getSqlSession();
        mapper = sqlSession.getMapper(TaskMapper.class);
        Task task = new Task();
        task.setCreateUser("zhangsan1");
        List<Task> list = mapper.queryAll(task);

    }

    @Test
    public void testSimpleSlowSql(){
        //不走索引
        SqlSession  sqlSession = MyBatisUtil.getSqlSession();
        mapper = sqlSession.getMapper(TaskMapper.class);
        Task task = new Task();
        task.setTitle("测试");
        List<Task> list = mapper.queryAll(task);
    }

    @Test
    public void testSimpleUpdateSql(){
        SqlSession  sqlSession = MyBatisUtil.getSqlSession();
        mapper = sqlSession.getMapper(TaskMapper.class);

        Task task = new Task();
        task.setContent("测试内容");
        task.setTitle("测试");
        task.setId(1l);
        mapper.updateByPrimaryKey(task);
    }
    @Test
    public void testSqlReplace(){
        SqlSession  sqlSession = MyBatisUtil.getSqlSession();
        mapper = sqlSession.getMapper(TaskMapper.class);

    }
    @Test
    public void testManyParaSql(){
        SqlSession  sqlSession = MyBatisUtil.getSqlSession();
        mapper = sqlSession.getMapper(TaskMapper.class);
        Task task = new Task();
        task.setTitle("测试");
        List<Task> list = mapper.queryAllM(task,1l);
        System.out.println(GsonUtil.bean2Json(list));
    }

    @Test
    public void testBindSql(){
        SqlSession  sqlSession = MyBatisUtil.getSqlSession();
        mapper = sqlSession.getMapper(TaskMapper.class);
        Task task = new Task();
        task.setTitle("测试");
        List<Task> list = mapper.queryLike(task);
        System.out.println(GsonUtil.bean2Json(list));
    }

}
