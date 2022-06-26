package org.jeecg.modules.demo.school.service.impl;

import org.jeecg.modules.demo.school.entity.Student;
import org.jeecg.modules.demo.school.mapper.StudentMapper;
import org.jeecg.modules.demo.school.service.IStudentService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 学生信息
 * @Author: jeecg-boot
 * @Date:   2022-06-25
 * @Version: V1.0
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {

}
