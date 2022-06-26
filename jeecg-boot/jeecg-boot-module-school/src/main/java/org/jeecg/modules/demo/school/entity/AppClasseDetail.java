package org.jeecg.modules.demo.school.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.checkerframework.checker.units.qual.C;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 上课情况
 * @Author: jeecg-boot
 * @Date:   2022-06-25
 * @Version: V1.0
 */
@Data
@TableName(value = "app_classe_detail",autoResultMap = true)
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="app_classe_detail对象", description="上课情况")
public class AppClasseDetail implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
	/**学生*/
	@Excel(name = "学生", width = 15, dictTable = "student", dicText = "name", dicCode = "id")
	@Dict(dictTable = "student", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "学生")
    private java.lang.String name;
	/**课程类型*/
	@Excel(name = "课程类型", width = 15, dicCode = "classes_type")
	@Dict(dicCode = "classes_type")
    @ApiModelProperty(value = "课程类型")
    private java.lang.String classesType;
	/**缴费金额*/
	@Excel(name = "缴费金额", width = 15)
    @ApiModelProperty(value = "缴费金额")
    private java.lang.String amount;
	/**总课时*/
	@Excel(name = "总课时", width = 15)
    @ApiModelProperty(value = "总课时")
    private java.lang.Integer totalClassHour;
	/**已上课时*/
	@Excel(name = "已上课时", width = 15)
    @ApiModelProperty(value = "已上课时")
    private java.lang.Integer nowClassHour;
	/**剩余课时*/
	@Excel(name = "剩余课时", width = 15)
    @ApiModelProperty(value = "剩余课时")
    private java.lang.Integer leftClassHour;
	/**签到详情*/
    @ApiModelProperty(value = "签到详情")
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<CheckIn> checkInList;
	/**最近签到时间*/
	@Excel(name = "最近签到时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "最近签到时间")
    private java.util.Date lastCheckInTime;

    @TableField(exist = false)
    private Boolean todayCheckIn;

    public void addCheckIn() {
        if (this.checkInList==null){
            this.checkInList=new ArrayList<>();
        }
        this.checkInList.add(new CheckIn(DateTime.now()));
    }

    public void initData() {
        this.setNowClassHour(this.getCheckInList()==null?0:this.getCheckInList().size());
        this.setLeftClassHour(this.getTotalClassHour()-this.getNowClassHour());
        this.setTodayCheckIn(this.getLastCheckInTime()==null?false: DateUtil.isSameDay(DateTime.now(),this.getLastCheckInTime()));
    }
}
