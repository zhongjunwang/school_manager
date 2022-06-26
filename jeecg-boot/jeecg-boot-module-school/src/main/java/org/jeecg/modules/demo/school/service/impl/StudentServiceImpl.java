package org.jeecg.modules.demo.school.service.impl;

import org.jeecg.common.util.DyDuanXinSmsHelper;
import org.jeecg.common.util.dynamic.db.DynamicDBUtil;
import org.jeecg.modules.demo.school.entity.Student;
import org.jeecg.modules.demo.school.mapper.StudentMapper;
import org.jeecg.modules.demo.school.service.IStudentService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;

/**
 * @Description: 学生信息
 * @Author: jeecg-boot
 * @Date:   2022-06-25
 * @Version: V1.0
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {

    @Override
    public void sendMsg(String id, String msg) {
        Student student = getById(id);
        DyDuanXinSmsHelper.sendSms(student.getPhone(),msg);
    }

    @Override
    public BigDecimal getTotalAmount(String id) {
        return baseMapper.getTotalAmount(id);
    }
}
