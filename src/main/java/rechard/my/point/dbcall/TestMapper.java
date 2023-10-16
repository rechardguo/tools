package rechard.my.point.dbcall;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TestMapper {
    @Insert("insert into test (COL,COL1) values (#{col}, #{col1})")
    public void save(@Param("col") String col, @Param("col1")String col1);
}
