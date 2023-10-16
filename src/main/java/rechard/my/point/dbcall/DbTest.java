package rechard.my.point.dbcall;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * 数据库的调用和mybatis log的定义化处理
 */
public class DbTest {

    public static void main(String[] args) {
        new DbTest().test();
    }

    public void test(){
        String jdbcURL="jdbc:mysql://localhost:3307/sparkdb?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT";
        String jdbcUser="root";
        String jdbcPassword="devuser";
        //DataSource ds = new SingleConnectionDataSource(jdbcURL,jdbcUser,jdbcPassword,false);
        DataSource dataSource = new DriverManagerDataSource(jdbcURL, jdbcUser, jdbcPassword);
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        org.apache.ibatis.logging.LogFactory.useCustomLogging(MyLog.class);
        configuration.addMapper(TestMapper.class);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);


        try (SqlSession session = sqlSessionFactory.openSession()) {
            TestMapper mapper = session.getMapper(TestMapper.class);
            mapper.save("1", "12");
        }
    }
}
