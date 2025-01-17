package org.jeecg.modules.demo.school.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.demo.school.entity.AppClasseDetail;
import org.jeecg.modules.demo.school.service.IAppClasseDetailService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.demo.school.service.IStudentService;
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
 * @Description: 上课情况
 * @Author: jeecg-boot
 * @Date:   2022-06-25
 * @Version: V1.0
 */
@Api(tags="上课情况")
@RestController
@RequestMapping("/school/appClasseDetail")
@Slf4j
public class AppClasseDetailController extends JeecgController<AppClasseDetail, IAppClasseDetailService> {
	@Autowired
	private IAppClasseDetailService appClasseDetailService;
	@Resource
	private IStudentService studentService;
	@Resource
	private ISysBaseAPI baseAPI;
	
	/**
	 * 分页列表查询
	 *
	 * @param appClasseDetail
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "上课情况-分页列表查询")
	@ApiOperation(value="上课情况-分页列表查询", notes="上课情况-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<AppClasseDetail>> queryPageList(AppClasseDetail appClasseDetail,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<AppClasseDetail> queryWrapper = QueryGenerator.initQueryWrapper(appClasseDetail, req.getParameterMap());
		Page<AppClasseDetail> page = new Page<AppClasseDetail>(pageNo, pageSize);
		IPage<AppClasseDetail> pageList = appClasseDetailService.page(page, queryWrapper);
		pageList.getRecords().stream().map(record->{
			record.initData();
			return record;
		}).collect(Collectors.toList());
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param appClasseDetail
	 * @return
	 */
	@AutoLog(value = "上课情况-添加")
	@ApiOperation(value="上课情况-添加", notes="上课情况-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody AppClasseDetail appClasseDetail) {
		appClasseDetailService.save(appClasseDetail);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param appClasseDetail
	 * @return
	 */
	@AutoLog(value = "上课情况-编辑")
	@ApiOperation(value="上课情况-编辑", notes="上课情况-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody AppClasseDetail appClasseDetail) {
		appClasseDetailService.updateById(appClasseDetail);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "上课情况-通过id删除")
	@ApiOperation(value="上课情况-通过id删除", notes="上课情况-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		appClasseDetailService.removeById(id);
		return Result.OK("删除成功!");
	}

	 @AutoLog(value = "上课情况-签到")
	 @ApiOperation(value="上课情况-签到", notes="上课情况-签到")
	 @GetMapping(value = "/checkIn")
	 public Result<String> checkIn(@RequestParam(name="id",required=true) String id) {
		 AppClasseDetail appClasseDetail = appClasseDetailService.getById(id);
		 appClasseDetail.setLastCheckInTime(DateTime.now());
		 appClasseDetail.addCheckIn();
		 appClasseDetail.initData();
		 appClasseDetailService.updateById(appClasseDetail);
		 String classType=baseAPI.translateDict("classes_type",appClasseDetail.getClassesType());
		 String student=baseAPI.translateDict("student,name,id",appClasseDetail.getName());
		 studentService.sendMsg(appClasseDetail.getName(), StrUtil.format("尊敬的家长您好，您的孩子{}的{}课程今天已来签到,当前总课时{},已上课时{},剩余课时{}",student,classType,appClasseDetail.getTotalClassHour(),appClasseDetail.getNowClassHour(),appClasseDetail.getLeftClassHour()));
		 return Result.OK("签到成功!");
	 }
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "上课情况-批量删除")
	@ApiOperation(value="上课情况-批量删除", notes="上课情况-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.appClasseDetailService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "上课情况-通过id查询")
	@ApiOperation(value="上课情况-通过id查询", notes="上课情况-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<AppClasseDetail> queryById(@RequestParam(name="id",required=true) String id) {
		AppClasseDetail appClasseDetail = appClasseDetailService.getById(id);
		if(appClasseDetail==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(appClasseDetail);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param appClasseDetail
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AppClasseDetail appClasseDetail) {
        return super.exportXls(request, appClasseDetail, AppClasseDetail.class, "上课情况");
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
        return super.importExcel(request, response, AppClasseDetail.class);
    }

}
