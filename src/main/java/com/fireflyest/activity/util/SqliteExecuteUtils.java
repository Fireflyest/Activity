package com.fireflyest.activity.util;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author Fireflyest
 * 2022/1/5 17:40
 */

public class SqliteExecuteUtils {

    private SqliteExecuteUtils(){}

    public static String createTable(Class<?> clazz){
        //建表指令
        String tableName = getTable(clazz);
        StringBuilder builder = new StringBuilder("create table if not exists ").append(tableName).append("(");
        List<Field> fields = ReflectUtils.getClassFields(clazz);

        //获取所有成员变量
        for (int i = 0; i < fields.size(); i++) {
            String fieldName = String.format("`%s`", fields.get(i).getName());
            String type = javaType2SQLType(fields.get(i).getType().getTypeName());
            builder.append(fieldName).append(" ").append(type);
            if ("id".equals(getPriKey(clazz))){
                if(fieldName.equals("`id`")){
                    builder.append(" NOT NULL PRIMARY KEY AUTOINCREMENT");
                }
            }else {
                if(i == 0){
                    builder.append(" primary key not null");
                }
            }
            if(i != fields.size()-1)builder.append(",");
        }
        builder.append(");");
        return builder.toString();
    }

    public static <T> String query(Class<T> clazz){
        String table = getTable(clazz);
        return String.format("select * from %s", table);
    }

    public static <T> String query(Class<T> clazz, String condition){
        String table = getTable(clazz);
        String sql = String.format("select * from %s", table);
        return addCondition(sql, condition);
    }

    public static <T> String query(Class<T> clazz, String key, Object value){
        String table = getTable(clazz);
        String sql = String.format("select * from %s", table);
        return addCondition(sql, key, value);
    }

    public static <T> String query(Class<T> clazz, int start, int amount){
        String table = getTable(clazz);
        String sql = String.format("select * from %s", table);
        return addLimit(sql, start, amount);
    }

    public static <T> String query(Class<T> clazz, String condition, int start, int amount){
        String table = getTable(clazz);
        String sql = String.format("select * from %s", table);
        sql = addCondition(sql, condition);
        return addLimit(sql, start, amount);
    }

    public static <T> String query(Class<T> clazz, String key, Object value, int start, int amount){
        String table = getTable(clazz);
        String sql = String.format("select * from %s", table);
        sql = addCondition(sql, key, value);
        return addLimit(sql, start, amount);
    }

    public static <T> String update(T t){
        Class<?> clazz = t.getClass();
        StringBuilder update = new StringBuilder();
        List<Field> fields = ReflectUtils.getClassFields(clazz);
        String priKey = getPriKey(clazz);
        int amount = 0;
        for(Field field : fields){
            if(amount > 0) update.append(",");
            if(priKey.equalsIgnoreCase(field.getName()))continue;
            update.append(String.format("`%s`", field.getName())).append("=").append("'").append(ReflectUtils.invokeGet(t, field.getName())).append("'");
            amount++;
        }
        String table = getTable(clazz);
        String value = String.valueOf(ReflectUtils.invokeGet(t, priKey));
        String data = update.toString().replace("'true'", "1").replace("'false'", "0");
        return String.format("update %s set %s where %s='%s'", table, data, priKey, value);
    }

    public static <T> String delete(Class<T> clazz, String key, Object value){
        String table = getTable(clazz);
        String sql = String.format("delete from %s", table);
        return addCondition(sql, key, value);
    }

    public static <T> String delete(Class<T> clazz, String condition){
        String table = getTable(clazz);
        String sql = String.format("delete from %s", table);
        return addCondition(sql, condition);
    }

    public static String delete(Object obj){
        Class<?> clazz = obj.getClass();
        String priKey = getPriKey(clazz);
        String value = String.valueOf(ReflectUtils.invokeGet(obj, priKey));
        return delete(clazz, priKey, value);
    }

    public static String insert(Object obj){
        //拼接sql指令
        Class<?> clazz = obj.getClass();
        StringBuilder fieldString = new StringBuilder();
        StringBuilder dataString = new StringBuilder();
        int amount = 0;
        for(Field field : ReflectUtils.getClassFields(clazz)){
            if(field.getType().equals(int.class) && getPriKey(clazz).equals(field.getName())) continue;
            if(amount > 0){
                fieldString.append(",");
                dataString.append(",");
            }
            fieldString.append(String.format("`%s`", field.getName()));
            dataString.append("'").append(ReflectUtils.invokeGet(obj, field.getName())).append("'");
            amount++;
        }
        String data = dataString.toString().replace("'true'", "1").replace("'false'", "0");
        String table = getTable(clazz);
        return String.format("insert into %s (%s) values (%s)", table, fieldString, data);
    }

    private static String addCondition(String sql, String key, Object value) {
        String data = value.toString();
        if(data.equals("true") || data.equals("false")){
            data = data.replace("true", "1").replace("false", "0");
        }
        return sql + String.format(" where `%s`='%s'", key, data);
    }

    private static String addCondition(String sql, String condition) {
        return sql + condition;
    }

    private static String addLimit(String sql, int start, int amount){
        return sql + String.format(" limit %d,%d", start, amount);
    }

    private static String getPriKey(Class<?> clazz){
        if (clazz.getSuperclass() != null && !clazz.getSuperclass().getTypeName().equals("java.lang.Object")){
            return getPriKey(clazz.getSuperclass());
        }
        return clazz.getDeclaredFields()[0].getName().toLowerCase();
    }

    private static String getTable(Class<?> clazz){
        return clazz.getSimpleName().toLowerCase();
    }

    /**
     * 将java数据类型转化为sql数据类型
     * @param type java数据类型
     * @return sql数据类型
     */
    private static String javaType2SQLType(String type){
        switch (type){
            case "int":
            case "long":
            case "java.lang.Long":
            case "java.lang.Integer":
            case "boolean":
            case "java.lang.Boolean":
            case "short":
                return "integer";
            case "java.lang.String":
                return "text(255)";
            case "java.lang.Double":
            case "java.lang.Float":
                return "real";
            default:
        }
        return "text(31)";
    }

}
