package org.jeecg.modules.demo.school.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.demo.school.entity.Student;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 学生信息
 * @Author: jeecg-boot
 * @Date:   2022-06-25
 * @Version: V1.0
 */
public interface StudentMapper extends BaseMapper<Student> {

    BigDecimal getTotalAmount(String id);
}
