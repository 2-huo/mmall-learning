package com.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by geely
 */
public class Const {

    public static final String CURRENT_USER = "currentUser";

    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    public interface ProductListOrderBy{
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc","price_asc");
        Set<String> TIME_ASC_DESC = Sets.newHashSet("update_time_desc","update_time_asc");
    }

    public interface adminId {
        Integer ID_ADMIN = new Integer(1);
    }

    public interface Role{
        String ROLE_ADMIN = "1";//管理员
        String ROLE_PIFA = "2"; // 批发商
        String ROLE_ST = "3"; // 实体店用户
        String ROLE_CUSTOMER = "4"; //普通用户
    }

    public enum ProductStatusEnum{
        ON_SALE(1,"在线");
        private String value;
        private int code;
        ProductStatusEnum(int code,String value){
            this.code = code;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }


}
