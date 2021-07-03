package com.johns.dynamicdatasource.utils;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 反射util测试工具类
 *
 * @author johns-li
 * @date 2021/06/23
 */
public class ReflectionUtilTest {

    static class  User1{
        private int id;
        private String userName;
        private String userPass;
        private Date createDate;
    }

    static class  DatabaseDetail{
        private long id;
        private String tenantId;
        private String url;
        private String username;
        private String password;
        private String driverClassName;
    }

    static class  User{
        private Long id;
        private String name;
        private String tenantId;
    }

    public static void main(String[] ages){
        String tableName = "user";
        StringBuffer stringBuffer = new StringBuffer ();
        List<Field> fields = getFields (User.class);
        stringBuffer.append ("CREATE TABLE `"+tableName+"` (");
        for (Field field : fields) {

            if (!Modifier.isStatic (field.getModifiers ()) && !"currentUser".equals (field.getName ())){
                stringBuffer.append ("\n").append (getName(field.getName ())+" ");
                stringBuffer.append (getMysqlColunmType (field.getType ()));
                stringBuffer.append (" NULL DEFAULT NULL ,");
            }
        }
        stringBuffer.delete (stringBuffer.length ()-1,stringBuffer.length ());
        stringBuffer.append ("\n").append (")ENGINE=InnoDB\n" +
                "DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci ;");

        System.out.println (stringBuffer);

    }


    /**
     * 获取所有字段
     * @param cls   实体类Class
     * @return
     */
    public  static List<Field> getFields(Class<?> cls){
        List<Field> fields = new ArrayList<> ();
        if (cls == Object.class){
            return  new ArrayList<> ();
        }
        fields.addAll (getFields (cls.getSuperclass ()));
        Field[] declaredFields = cls.getDeclaredFields ();
        for (Field declaredField : declaredFields) {
            fields.add (declaredField);
        }
        return  fields;
    }

    /**
     * 获取Mysql列属性
     * 有需要自己添加哟
     * @param cls
     * @return
     */
    public static String getMysqlColunmType(Class cls){
        String clsName = cls.toString ();
        if (cls == Date.class){
            return  "datetime";
        }
        if (cls == BigDecimal.class){
            return  "decimal(12,2)";
        }
        if (cls == Integer.class||cls == Long.class || clsName.equals ("int") || clsName.equals ("long")){
            return  "int";
        }
        return "varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci ";
    }

    /**
     * 获取属性名称转换数据库列明
     * 驼峰转换
     * @param name
     * @return
     */
    private  static String getName(String name){
        StringBuffer stringBuffer = new StringBuffer ();
        for (char c : name.toCharArray ()) {
            if (Character.isUpperCase (c)){
                stringBuffer.append ("_");
                stringBuffer.append (Character.toLowerCase (c));
            }else{
                stringBuffer.append (c);
            }
        }
        return stringBuffer.toString ();
    }


}
