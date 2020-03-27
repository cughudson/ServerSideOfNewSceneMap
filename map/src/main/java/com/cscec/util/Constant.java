package com.cscec.util;

import com.alibaba.fastjson.JSONObject;
import com.cscec.model.ext.UserExt;
import com.cscec.service.CommonService;
import com.cscec.util.response.ErrorCode;
import com.cscec.util.response.MyException;
import com.cscec.util.response.ResponseFormat;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.ServletContext;
import java.util.*;

public class Constant {
    public static Map<String,JSONObject> tokens=Maps.newHashMap();
    public static final String accept_message="审核状态  0待审核  1审核通过  2审核失败  3重提待审核  4(待审核和重提待审核)";
    public static final String admin ="admin";
    private static Logger logger = LoggerFactory.getLogger(Constant.class);
    public static Random random = new Random();
    public static final String user = "user";
    public static final String userExt = "userExt";
    public static final String id = "id";
    public static final String count = "count";
    public static final String userId = "userId";
    public static final String result = "result";
    public static final String cadFilePath = "cadFilePath";
    public static final String imgFilePath = "imgFilePath";
    public static final String cadFilename = "cadFilename";

    public static final String name = "name";
    public static final String username = "username";
    public static final String extra = "extra";
    public static final String datetime = "datetime";
    public static final String project = "project";
    public static final String projectId = "projectId";

    public static final String realName = "realName";

    public static final String alias = "alias";
    public static final String password = "password";
    public static final String loginTime = "loginTime";
    public static final String headImg = "headImg";
    public static final String token = "token";


    public static final String ip = "ip";
    public static final String code = "code";
    public static final String value="value";
    public static final String message = "message";
    public static final String data = "data";


    public static final String fileName = "fileName";
    public static final String imgFilename = "imgFilename";
    public static final String fileSuffix = "fileSuffix";
    public static final String md5 = "md5";
    public static final String filePath = "filePath";
    public static final String cadFileVersion = "cadFileVersion";
    public static final String error = "error";

    public static final String type = "type";
    public static final String TYPE_xcmt_cad = "xcmt/cad";
    public static final String TYPE_xcmt_img = "xcmt/img";
    public static final String TYPE_headImg = "headImg";
    public static final String TYPE_software = "software";


    public static final String createTime = "createTime";
    public static final String companyId = "companyId";
    public static final String company = "company";

    public static final String enable = "enable";
    public static final String useable = "useable";
    public static final String del = "del";
    public static final String delTime = "delTime";

    public static final String dimission = "dimission";


    public static final String pageNum = "pageNum";

    public static final String pageSize = "pageSize";
    public static final String accept = "accept";
    public static final String companyGs = "companyGs";
    public static final String companyGc = "companyGc";
    public static final String mttypeId = "mttypeId";
    public static final String remark = "remark";
    public static final String desp = "desp";

    public static int defaultPageSize = 4;//4 40

    public static final int POWER_super_admin = 0;
    public static final int POWER_normal_admin = 1;
    public static final int POWER_normal_user = 2;
    public static final String projectName = "cscec";


    public static final int ACCEPT_CHECK = 0;// 待审核
    public static final int ACCEPT_SUCCESS = 1;//通过
    public static final int ACCEPT_REJECT = 2;//审核拒绝
    public static final int ACCEPT_RECHECK = 3; //重提 待审核

    public static final int ACCEPT_All_UNCHECK = 4;// 包括 1和3


    public static final int company_typ_gc = 0;//工厂
    public static final int company_typ_gs = 1;//公司

    public static void applicationInit() {
        reloadHeadImg();
    }

   public static String defaultPassword="123456";
    public static Long tokenExpire=60*5L;


    public static List<JSONObject> map2JSONObject(List<Map<String, Object>> queryForList) {
        List<JSONObject> result=new ArrayList<>();
        for (Map<String, Object> map:queryForList) {
            result.add(map2JSONObject(map));
        }
        return result;
    }
    public static JSONObject map2JSONObject(Map<String, Object> map) {
        JSONObject json=new JSONObject();
        for (String key:map.keySet()) {
            json.put(key,map.get(key));
        }
        return json;
    }


    public static Map<Long, UserExt> userRandomMap = Maps.newConcurrentMap();

    public static List<String> headImgs = new ArrayList<>();

    public static void reloadHeadImg() {
        ServletContext context = SpringUtil.getBean(ServletContext.class);
        headImgs = SpringUtil.getBean(CommonService.class).selectEnableHeadImg();
        context.setAttribute("headImgs", headImgs);
        logger.info("加载默认头像成功");
    }

    public class Sysconfig {
        public static final String userTimedDeletion = "user_timed_deletion";
        public static final String xcmtDefaultPageSize = "xcmt_default_page_size";
        public static final String defaultPassword="default_password";
        public static final String tokenExpire="token_expire";
    }

}
