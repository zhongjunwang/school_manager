package org.jeecg.modules.demo.school.service;

import org.jeecg.modules.demo.school.entity.Student;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * @Description: 学生信息
 * @Author: jeecg-boot
 * @Date:   2022-06-25
 * @Version: V1.0
 */
public interface IStudentService extends IService<Student> {

    void sendMsg(String id, String msg);

    BigDecimal getTotalAmount(String id);
}
