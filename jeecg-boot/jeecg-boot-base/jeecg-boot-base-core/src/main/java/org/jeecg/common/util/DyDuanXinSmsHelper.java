package org.jeecg.common.util;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on 17/6/7.
 * 短信API产品的DEMO程序,工程中包含了一个SmsDemo类，直接通过
 * 执行main函数即可体验短信产品API功能(只需要将AK替换成开通了云通信-短信产品功能的AK即可)
 * 工程依赖了2个jar包(存放在工程的libs目录下)
 * 1:aliyun-java-sdk-core.jar
 * 2:aliyun-java-sdk-dysmsapi.jar
 *
 * 备注:Demo工程编码采用UTF-8
 * 国际短信发送请勿参照此DEMO
 * @author: jeecg-boot
 */
public class DyDuanXinSmsHelper {
	
	private final static Logger logger=LoggerFactory.getLogger(DyDuanXinSmsHelper.class);

    /**产品名称:云通信短信API产品,开发者无需替换*/
    static final String URL = "http://api.duanxin.com:7891/mt";
   static final String APIKEY    = "2326250010";
   static final String APISECRET = "7050621a5f3be83b5c57";


    
    
    public static boolean sendSms(String phone,String msg) {
        try {
            Map map=new HashMap<>();
            map.put("dc",15);
            map.put("sm", "【环境督查】"+msg);
            map.put("da",phone);
            map.put("un",APIKEY);
            map.put("pw",APISECRET);
            map.put("tf",3);
            map.put("rd",1);
            map.put("rf",2);
            String result = HttpUtil.get(URL, map);
            logger.info("短信内容：{}",map.get("sm"));
            logger.info("短信发送结果："+result);
        } catch (Exception e) {
            logger.error("短信发送异常："+e.getMessage());
        }
        return true;
        
    }
    
    private static void validateParam(JSONObject templateParamJson,DySmsEnum dySmsEnum) {
    	String keys = dySmsEnum.getKeys();
    	String [] keyArr = keys.split(",");
    	for(String item :keyArr) {
    		if(!templateParamJson.containsKey(item)) {
    			throw new RuntimeException("模板缺少参数："+item);
    		}
    	}
    }
    

    public static void main(String[] args) throws ClientException, InterruptedException, UnsupportedEncodingException {
    	JSONObject obj = new JSONObject();
    	obj.put("code", "1234");
    	sendSms("18641781590", "您的验证码是：1234");
    }
}
