package org.jeecg.modules.demo.school.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.demo.school.entity.Student;
import org.jeecg.modules.demo.school.service.IStudentService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 学生信息
 * @Author: jeecg-boot
 * @Date:   2022-06-25
 * @Version: V1.0
 */
@Api(tags="学生信息")
@RestController
@RequestMapping("/school/student")
@Slf4j
public class StudentController extends JeecgController<Student, IStudentService> {
	@Autowired
	private IStudentService studentService;
	
	/**
	 * 分页列表查询
	 *
	 * @param student
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "学生信息-分页列表查询")
	@ApiOperation(value="学生信息-分页列表查询", notes="学生信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Student>> queryPageList(Student student,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Student> queryWrapper = QueryGenerator.initQueryWrapper(student, req.getParameterMap());
		Page<Student> page = new Page<Student>(pageNo, pageSize);
		IPage<Student> pageList = studentService.page(page, queryWrapper);
		pageList.getRecords().stream().map(record->{
			record.setTotalAmount(studentService.getTotalAmount(record.getId()));
			return record;
		}).collect(Collectors.toList());
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param student
	 * @return
	 */
	@AutoLog(value = "学生信息-添加")
	@ApiOperation(value="学生信息-添加", notes="学生信息-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Student student) {
		studentService.save(student);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param student
	 * @return
	 */
	@AutoLog(value = "学生信息-编辑")
	@ApiOperation(value="学生信息-编辑", notes="学生信息-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Student student) {
		studentService.updateById(student);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "学生信息-通过id删除")
	@ApiOperation(value="学生信息-通过id删除", notes="学生信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		studentService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "学生信息-批量删除")
	@ApiOperation(value="学生信息-批量删除", notes="学生信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.studentService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "学生信息-通过id查询")
	@ApiOperation(value="学生信息-通过id查询", notes="学生信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Student> queryById(@RequestParam(name="id",required=true) String id) {
		Student student = studentService.getById(id);
		if(student==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(student);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param student
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Student student) {
        return super.exportXls(request, student, Student.class, "学生信息");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Student.class);
    }

}
