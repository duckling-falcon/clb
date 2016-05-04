/*
 * Copyright (c) 2008-2016 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
 * 
 * This file is part of Duckling project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 */
package cn.vlabs.clb.server.dao;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

public class DAOUtils<T> {

    private static final String QUERY_DELIMETER = " and ";
    private static final String COMMA_DELIMETER = ",";
    private static final Logger LOG = Logger.getLogger(DAOUtils.class);
    private static final int DELTA_FROM_UPPER_TO_LOWER = 32;
    private static final int UPPER_START_CODE = 64;
    private static final int UPPER_END_CODE = 91;
    public static final String FIELDS = "${field}";
    public static final String TABLE = "${table}";
    public static final String VALUES = "${value}";

    public static final String INSERT_SQL = "insert into " + TABLE + "(" + FIELDS + ") values(" + VALUES + ")";
    public static final String SELECT_SQL = "select * from " + TABLE + " where 1=1";
    public static final String UPDATE_SQL = "update " + TABLE + " set " + FIELDS + " where id=:id";
    public static final String UPDATE_SQL_BY_FIELDS = "update " + TABLE + " set " + FIELDS + " where 1=1";
    public static final String DELETE_SQL = "delete from " + TABLE + " where 1=1" + FIELDS;
    public static final String SELECT_MAX_ID_SQL = "select * from " + TABLE + " where id=(select max(id) from " + TABLE
            + " )";

    private Class<?> modelClazz;
    private Field[] fields;

    private String tableName;

    private NamingRule namingRule = NamingRule.CAMEL;

    public DAOUtils(Class<?> objClass, String tableName, NamingRule rule) {
        this.modelClazz = objClass;
        loadFiledFromClass(objClass);
        this.tableName = tableName;
        this.namingRule = rule;
    }

    private void loadFiledFromClass(Class<?> objClass) {
        Field[] allFields = objClass.getDeclaredFields();
        List<Field> privateFields = new ArrayList<Field>();
        for (int i = 0; i < allFields.length; i++) {
            if (isValidDBFiled(allFields[i])) {
                privateFields.add(allFields[i]);
            }
        }
        this.fields = new Field[privateFields.size()];
        int index = 0;
        for (Field field : privateFields) {
            this.fields[index++] = field;
        }
    }

    public String getQueryByMaxIdSQL() {
        return SELECT_MAX_ID_SQL.replace(TABLE, tableName);
    }

    public String getInsertSQL(String expectField) {
        return INSERT_SQL.replace(TABLE, tableName).replace(FIELDS, filterUnexpectedFileds(expectField))
                .replace(VALUES, getValueRepeat(expectField));
    }

    public String getSelectSQL(T t) {
        StringBuilder sb = choseAllFieldsAsQueryClause(t);
        return SELECT_SQL.replace(TABLE, tableName) + sb.toString();
    }

    public String getUpdateSQL(T t) {
        StringBuilder sb = getSetValuesClause(t, new String[] {}, false); // 更新字段为所有初始化过的字段
                                                                          // 以id=:id作为搜索条件
        return UPDATE_SQL.replace(TABLE, tableName).replace(FIELDS, format(sb, COMMA_DELIMETER));
    }

    public String getUpdateByFieldsSQL(T t, String[] queryFields) {
        StringBuilder sb = getSetValuesClause(t, queryFields, false); // 更新字段为所有不在queryFields中的且已初始化的字段
        StringBuilder qsb = choseSelectedFieldsAsQueryClause(t, queryFields); // 所有在queryFields都是查询条件
        return UPDATE_SQL_BY_FIELDS.replace(TABLE, tableName).replace(FIELDS, format(sb, COMMA_DELIMETER))
                + qsb.toString();
    }

    public String getDeleteSQL(T t) {
        StringBuilder sb = choseAllFieldsAsQueryClause(t);
        return DELETE_SQL.replace(TABLE, tableName).replace(FIELDS, sb);
    }

    public RowMapper<T> getRowMapper(final Map<String, String> map) {
        return new RowMapper<T>() {
            @Override
            public T mapRow(ResultSet rs, int index) throws SQLException {
                Object obj = null;
                try {
                    obj = modelClazz.newInstance();
                    for (int i = 0; i < fields.length; i++) {
                        Field field = fields[i];
                        field.getName();
                        setValueToObj(field, obj, rs);
                    }
                } catch (InstantiationException e) {
                    LOG.debug(e.getMessage(), e);
                } catch (IllegalAccessException e) {
                    LOG.debug(e.getMessage(), e);
                }
                revertMapping(map, obj, rs);
                return (T) obj;
            }
        };
    }

    private StringBuilder choseAllFieldsAsQueryClause(T t) {
        List<String> selectFields = getSelectedFields(t, new String[] {}, false);
        return buildClause(selectFields, QUERY_DELIMETER);
    }

    private StringBuilder choseSelectedFieldsAsQueryClause(T t, String[] fieldNames) {
        List<String> selectFields = getSelectedFields(t, fieldNames, true);
        return buildClause(selectFields, QUERY_DELIMETER);
    }

    private StringBuilder getSetValuesClause(T t, String[] fieldNames, boolean isWhiteList) {
        List<String> selectFields = getSelectedFields(t, fieldNames, isWhiteList);
        selectFields.remove("id"); //update sql中过滤掉id=:id的语句
        return buildClause(selectFields, COMMA_DELIMETER);
    }

    private StringBuilder buildClause(List<String> selectFields, String delimeter) {
        StringBuilder sb = new StringBuilder();
        for (String f : selectFields) {
            sb.append(delimeter);
            if (namingRule == NamingRule.UNDERSCORE) {
                sb.append(mappingCamelToUnderscore(f));
            } else {
                sb.append(f);
            }
            sb.append("=:");
            sb.append(f);
        }
        return sb;
    }

    private List<String> getSelectedFields(T t, String[] fieldNames, boolean isWhiteList) {
        Set<String> selected = new HashSet<String>();
        if (fieldNames != null) {
            for (String filedName : fieldNames) {
                selected.add(filedName);
            }
        }
        List<String> fieldList = new ArrayList<String>();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String name = field.getName();
            Object value = getFieldValue(t, field);
            if (!isUninitField(value)) {
                if (isWhiteList) { // 白名单
                    if (selected.contains(name)) {
                        fieldList.add(name);
                    }
                } else { // 黑名单
                    if (!selected.contains(name)) {
                        fieldList.add(name);
                    }
                }
            }
        }
        return fieldList;
    }

    private String format(StringBuilder sb, String delimeter) {
        return sb.substring(delimeter.length());
    }

    private void setValueToObj(Field field, Object obj, ResultSet rs) {
        String methodName = getSetMethodName(field.getName());
        Method method = null;
        try {
            method = modelClazz.getMethod(methodName, field.getType());
        } catch (NoSuchMethodException | SecurityException e) {
            LOG.debug("can not find this method:" + methodName, e);
        }
        String columnName = getDBField(field.getName());
        readValueFromResultSet(field.getType(), columnName, obj, rs, method);
    }

    private void readValueFromResultSet(Class fieldClazz, String columnName, Object obj, ResultSet rs, Method method) {
        try {
            if (isString(fieldClazz)) {
                method.invoke(obj, new Object[] { rs.getString(columnName) });
            } else if (isInt(fieldClazz)) {
                method.invoke(obj, new Object[] { rs.getInt(columnName) });
            } else if (isLong(fieldClazz)) {
                method.invoke(obj, new Object[] { rs.getLong(columnName) });
            } else if (isDouble(fieldClazz)) {
                method.invoke(obj, new Object[] { rs.getDouble(columnName) });
            } else if (isDate(fieldClazz)) {
                method.invoke(obj, new Object[] { rs.getTimestamp(columnName) });
            } else if (isBoolean(fieldClazz)) {
                method.invoke(obj, new Object[] { rs.getBoolean(columnName) });
            } else if (isByteArray(fieldClazz)) {
                // TODO support read byte array
                LOG.debug("Not support");
                //method.invoke(obj, new Object[] { lobHandler.getBlobAsBytes(rs, columnName) });
            } else {
                LOG.debug("Unsupported type");
            }
        } catch (ReflectiveOperationException | SQLException e) {
            LOG.debug("对象属性命名不规范" + obj.getClass() + "@" + fieldClazz, e);
        }
    }
    
    private void revertMapping(Map<String, String> map, Object obj, ResultSet rs) {
        if (map != null) {
            Set<Entry<String, String>> set = map.entrySet();
            for (Iterator<Entry<String, String>> it = set.iterator(); it.hasNext();) {
                Entry<String, String> entry = it.next();
                try {
                    Field field = modelClazz.getDeclaredField(entry.getKey());
                    field = modelClazz.getDeclaredField(entry.getKey());
                    Method method = modelClazz.getMethod(getSetMethodName(field.getName()), field.getType());
                    readValueFromResultSet(field.getType(), entry.getValue(), obj, rs, method);
                } catch (ReflectiveOperationException e) {
                    LOG.debug("对象属性命名不规范" + entry, e);
                }
            }
        }
    }

    public PreparedStatement setValues(PreparedStatement pst, final T t, String expectField) throws SQLException {
        int index = 0;
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (expectField.contains(field.getName())) {
                continue;
            }
            Object obj = null;
            String exceptionMsg = "属性名字和get方法不匹配";
            try {
                Method method = t.getClass().getMethod(getGetMethodName(field.getName()));
                obj = method.invoke(t);
            } catch (InvocationTargetException e) {
                LOG.debug(field.getName() + exceptionMsg, e);
                continue;
            } catch (NoSuchMethodException e) {
                LOG.debug(field.getName() + exceptionMsg, e);
                continue;
            } catch (SecurityException e) {
                LOG.debug(field.getName() + exceptionMsg, e);
                continue;
            } catch (IllegalAccessException e) {
                LOG.debug(field.getName() + exceptionMsg, e);
                continue;
            } catch (IllegalArgumentException e) {
                LOG.debug(field.getName() + exceptionMsg, e);
                continue;
            }
            if (isString(field.getType())) {
                pst.setString(++index, (String) obj);
            } else if (isInt(field.getType())) {
                pst.setInt(++index, (int) obj);
            } else {
                LOG.debug("Unsupported Type");
            }
        }
        return pst;
    }

    public Map<String, Object> getParamMap(T t) {
        return useModelToFillParamMap(t, false);
    }

    public Map<String, Object> useModelToFillParamMap(T t, boolean isAll) {
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            Object value = getFieldValue(t, field);
            if (!isAll && isUninitField(value)) {
                continue;
            }
            if (isDate(field.getType()) && value != null) {
                value = new Timestamp(((Date) value).getTime());
            } else if (isBoolean(field.getType())) {
                if (value == null) {
                    value = "0";
                } else {
                    value = (((boolean) value) ? "1" : "0");
                }
            }
            map.put(field.getName(), value);
        }
        return map;
    }

    private boolean isValidDBFiled(Field field) {
        return (!Modifier.isStatic(field.getModifiers())) && (Modifier.isPrivate(field.getModifiers()))
                && (field.getAnnotation(TempField.class) == null);
    }

    private Object getFieldValue(T t, Field field) {
        Object obj = null;
        String exceptionMsg = "取值异常：";
        try {
            Method method = modelClazz.getDeclaredMethod(getGetMethodName(field.getName()));
            if (t == null) {
                return null;
            }
            obj = method.invoke(t);
        } catch (ReflectiveOperationException e) {
            LOG.debug(exceptionMsg + field.getName());
        }
        return obj;
    }

    private String filterUnexpectedFileds(String unexpectedFileds) {
        StringBuilder sb = new StringBuilder();
        for (Field field : fields) {
            if (unexpectedFileds.contains(field.getName()) || isUninitField(field)) {
                continue;
            }
            sb.append(COMMA_DELIMETER);
            sb.append(getDBField(field.getName()));
        }
        return format(sb, COMMA_DELIMETER);
    }

    private String getDBField(String filedName) {
        switch (namingRule) {
        case CAMEL:
            return filedName;
        case UNDERSCORE:
            return mappingCamelToUnderscore(filedName);
        default:
            return null;
        }
    }

    private String mappingCamelToUnderscore(String fieldName) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fieldName.length(); i++) {
            char c = fieldName.charAt(i);
            if (isUpperCase(c)) {
                if (i == 0) {
                    sb.append(up2low(c));
                } else {
                    sb.append("_").append(up2low(c));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private String getValueRepeat(String expectField) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (expectField.contains(field.getName()) || isUninitField(field)) {
                continue;
            }
            sb.append(COMMA_DELIMETER);
            sb.append(":");
            sb.append(field.getName());
        }
        return format(sb, COMMA_DELIMETER);
    }

    private boolean isString(Class clazz) {
        return clazz.equals(String.class);
    }

    private boolean isInt(Class clazz) {
        return clazz.equals(int.class) || clazz.equals(Integer.class);
    }

    private boolean isLong(Class clazz) {
        return clazz.equals(long.class) || clazz.equals(Long.class);
    }

    private boolean isDouble(Class clazz) {
        return clazz.equals(double.class) || clazz.equals(Double.class);
    }

    private boolean isBoolean(Class clazz) {
        return clazz.equals(boolean.class) || clazz.equals(Boolean.class);
    }

    private boolean isDate(Class clazz) {
        return clazz.equals(Date.class);
    }

    private boolean isUpperCase(char c) {
        return c < UPPER_END_CODE && c > UPPER_START_CODE;
    }
    
    private boolean isByteArray(Class clazz){
        return clazz.equals(byte[].class);
    }

    private String getGetMethodName(String fieldName) {
        return "get" + low2up(fieldName.charAt(0)) + fieldName.substring(1);
    }

    private String getSetMethodName(String fieldName) {
        return "set" + low2up(fieldName.charAt(0)) + fieldName.substring(1);
    }

    private char up2low(char c) {
        return (char) (c + DELTA_FROM_UPPER_TO_LOWER);
    }

    private char low2up(char c) {
        return (char) (c - DELTA_FROM_UPPER_TO_LOWER);
    }

    private boolean isUninitField(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj instanceof Integer) {
            return (int) obj == 0;
        } else if (obj instanceof Long) {
            return (long) obj == 0;
        } else if (obj instanceof Double) {
            return (double) obj == 0.0;
        }
        return false;
    }
}
