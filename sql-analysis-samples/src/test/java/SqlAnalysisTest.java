import com.jd.sql.analysis_samples.mapper.TaskMapper;
import com.jd.sql.analysis_samples.po.Task;
import com.jd.sql.analysis_samples.util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

/**
 * @author huhaitao21
 *  SqlAnalysisTest
 *  19:13 2022/11/10
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

}
