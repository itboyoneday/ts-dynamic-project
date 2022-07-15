package com.tsingsoft.common.utils;

import cn.hutool.core.util.NumberUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.*;

/**
 * <p>数据工具类</p>
 *
 * @author:bask
 * @create 2018-06-27 14:54
 */
public final class DataUtil {

    public static final int TWO_VALUE = 2;
    public static final String JAR = "jar";
    public static final String FILE = "file";
    public static final String DOT_JAR = ".jar";
    public static final String DOT_ZIP = ".zip";

    private DataUtil() {
    }

    public static final String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder(b.length * 2);
        String stmp = "";
        byte[] var3 = b;
        int var4 = b.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            byte element = var3[var5];
            stmp = Integer.toHexString(element & 255);
            if (stmp.length() == 1) {
                hs.append("0").append(stmp);
            } else {
                hs.append(stmp);
            }
        }

        return hs.toString();
    }

    public static final byte[] hex2byte(String hs) {
        byte[] b = hs.getBytes();
        if (b.length % TWO_VALUE != 0) {
            throw new IllegalArgumentException("长度不是偶数");
        } else {
            byte[] b2 = new byte[b.length / TWO_VALUE];

            for(int n = 0; n < b.length; n += TWO_VALUE) {
                String item = new String(b, n, TWO_VALUE);
                b2[n / 2] = (byte)Integer.parseInt(item, 16);
            }

            return b2;
        }
    }

    public static final String getFullPathRelateClass(String relatedPath, Class<?> cls) {
        String path = null;
        if (relatedPath == null) {
            throw new NullPointerException();
        } else {
            String clsPath = getPathFromClass(cls);
            File clsFile = new File(clsPath);
            String tempPath = clsFile.getParent() + File.separator + relatedPath;
            File file = new File(tempPath);

            try {
                path = file.getCanonicalPath();
            } catch (IOException var8) {
                var8.printStackTrace();
            }

            return path;
        }
    }

    public static final String getPathFromClass(Class<?> cls) {
        String path = null;
        if (cls == null) {
            throw new NullPointerException();
        } else {
            URL url = getClassLocationURL(cls);
            if (url != null) {
                path = url.getPath();
                if (JAR.equalsIgnoreCase(url.getProtocol())) {
                    try {
                        path = (new URL(path)).getPath();
                    } catch (MalformedURLException var6) {
                        ;
                    }

                    int location = path.indexOf("!/");
                    if (location != -1) {
                        path = path.substring(0, location);
                    }
                }

                File file = new File(path);

                try {
                    path = file.getCanonicalPath();
                } catch (IOException var5) {
                    var5.printStackTrace();
                }
            }

            return path;
        }
    }

    public static final boolean isEmpty(Object pObj) {
        if (pObj != null && !"".equals(pObj)) {
            if (pObj instanceof String) {
                if (((String)pObj).trim().length() == 0) {
                    return true;
                }
            } else if (pObj instanceof Collection) {
                if (((Collection)pObj).size() == 0) {
                    return true;
                }
            } else if (pObj instanceof Map && ((Map)pObj).size() == 0) {
                return true;
            }

            return false;
        } else {
            return true;
        }
    }

    public static final boolean isNotEmpty(Object pObj) {
        if (pObj != null && !"".equals(pObj)) {
            if (pObj instanceof String) {
                if (((String)pObj).trim().length() == 0) {
                    return false;
                }
            } else if (pObj instanceof Collection) {
                if (((Collection)pObj).size() == 0) {
                    return false;
                }
            } else if (pObj instanceof Map && ((Map)pObj).size() == 0) {
                return false;
            }

            return true;
        } else {
            return false;
        }
    }

    public static final String[] trim(String[] paramArray) {
        if (ArrayUtils.isEmpty(paramArray)) {
            return paramArray;
        } else {
            String[] resultArray = new String[paramArray.length];

            for(int i = 0; i < paramArray.length; ++i) {
                String param = paramArray[i];
                resultArray[i] = StringUtils.trim(param);
            }

            return resultArray;
        }
    }

    private static URL getClassLocationURL(Class<?> cls) {
        if (cls == null) {
            throw new IllegalArgumentException("null input: cls");
        } else {
            URL result = null;
            String clsAsResource = cls.getName().replace('.', '/').concat(".class");
            ProtectionDomain pd = cls.getProtectionDomain();
            if (pd != null) {
                CodeSource cs = pd.getCodeSource();
                if (cs != null) {
                    result = cs.getLocation();
                }

                if (result != null && FILE.equals(result.getProtocol())) {
                    try {
                        if (!result.toExternalForm().endsWith(DOT_JAR) && !result.toExternalForm().endsWith(DOT_ZIP)) {
                            if ((new File(result.getFile())).isDirectory()) {
                                result = new URL(result, clsAsResource);
                            }
                        } else {
                            result = new URL("jar:".concat(result.toExternalForm()).concat("!/").concat(clsAsResource));
                        }
                    } catch (MalformedURLException var6) {
                        ;
                    }
                }
            }

            if (result == null) {
                ClassLoader clsLoader = cls.getClassLoader();
                result = clsLoader != null ? clsLoader.getResource(clsAsResource) : ClassLoader.getSystemResource(clsAsResource);
            }

            return result;
        }
    }

    public static final <K> K ifNull(K k, K defaultValue) {
        return k == null ? defaultValue : k;
    }

    /**
     * 功能描述:<br/>
     * 负荷分析模块校验数据是否合法<br/>
     * 说明：<br/>
     * true：合法<br/>
     * false：非法  非法情况为null，"undefined", ""
     * @Author: liyu
     * @Date: 2019/11/20 18:11
     * @param obj
     * @Return: java.lang.Boolean
     */
    public static Boolean objCheck(Object obj) {
        //如果没有内容，返回原有对象
        if (obj == null || "".equalsIgnoreCase(obj.toString()) || "undefined".equalsIgnoreCase(obj.toString())){
            return false;
        }
        return true;
    }

    /**
     * 功能描述:<br/>
     * 小数格式化工具类<br/>
     * 说明：<br/>
     * (1)如果传入对象没有内容，返回原对象<br/>
     * 例：null,""<br/>
     * (2)四舍五入保留，小数位不足时补0<br/>
     * 例：保留两位小数   2.1  -->  2.10<br/>
     * (3)如果保留位数<=0，则保留整数（四舍五入）<br/>
     * @Author: liyu
     * @Date: 2019/11/20 14:14
     * @param obj  操作对象
     * @param scale  保留小数位数
     * @Return: java.lang.Object  返回实际类型为BigDecimal
     */
    public static Object numberFormat(Object obj, Integer scale) {
        //如果没有内容，返回原有对象
        if (obj == null || "".equals(obj.toString())){
            return "";
        }
        return NumberUtil.round(obj.toString(),scale);
    }

    /**
     * 功能描述:<br/>
     * 小数格式化工具类<br/>
     * 说明：<br/>
     * (1)如果传入对象没有内容，返回空字符串""<br/>
     * 例：如果传入对象为null,""，则返回""<br/>
     * (2)四舍五入保留，小数位不足时补0<br/>
     * 例：保留两位小数   2.1  -->  2.10<br/>
     * (3)如果保留位数<=0，则保留整数（四舍五入）<br/>
     * @Author: liyu
     * @Date: 2019/11/20 14:14
     * @param obj  操作对象
     * @param scale  保留小数位数
     * @Return: java.lang.String
     */
    public static String numberFormatReturnString(Object obj, Integer scale) {
        //如果没有内容，返回""
        if (obj == null || "".equals(obj.toString())){
            return "";
        }
        return NumberUtil.round(obj.toString(),scale).toString();
    }

    /**
     * 功能描述:<br/>
     * 转换风向度数为汉字风向<br/>
     * @Author: liyu
     * @Date: 2019/11/29 10:53
     * @param fengXiangData  风向度数
     * @Return: 风向  例：东南风<br/>
     * 说明：如果风向度数为null，返回""
     */
    public static String getFengXiang(Double fengXiangData) {
        String fengXiangStr = "-";
        if (fengXiangData == null) {

        }else if (fengXiangData >= 348.76d || fengXiangData <= 11.25d) {
            fengXiangStr = "北";
        }else if (fengXiangData >= 11.26d && fengXiangData <= 33.75d) {
            fengXiangStr = "北东北";
        }else if (fengXiangData >= 33.76d && fengXiangData <= 56.25d) {
            fengXiangStr = "东北";
        }else if (fengXiangData >= 56.26d && fengXiangData <= 78.75d) {
            fengXiangStr = "东东北";
        }else if (fengXiangData >= 78.76d && fengXiangData <= 101.25d) {
            fengXiangStr = "东";
        }else if (fengXiangData >= 101.26d && fengXiangData <= 123.75d) {
            fengXiangStr = "东东南";
        }else if (fengXiangData >= 123.76d && fengXiangData <= 146.25d) {
            fengXiangStr = "东南";
        }else if (fengXiangData >= 146.26d && fengXiangData <= 168.75d) {
            fengXiangStr = "南东南";
        }else if (fengXiangData >= 168.76d && fengXiangData <= 191.25d) {
            fengXiangStr = "南";
        }else if (fengXiangData >= 191.26d && fengXiangData <= 213.75d) {
            fengXiangStr = "南西南";
        }else if (fengXiangData >= 213.76d && fengXiangData <= 236.25d) {
            fengXiangStr = "西南";
        }else if (fengXiangData >= 236.26d && fengXiangData <= 258.75d) {
            fengXiangStr = "西西南";
        }else if (fengXiangData >= 258.76d && fengXiangData <= 281.25d) {
            fengXiangStr = "西";
        }else if (fengXiangData >= 281.26d && fengXiangData <= 303.75d) {
            fengXiangStr = "西西北";
        }else if (fengXiangData >= 303.76d && fengXiangData <= 326.25d) {
            fengXiangStr = "西北";
        }else if (fengXiangData >= 326.26d && fengXiangData <= 348.75d) {
            fengXiangStr = "北西北";
        }

        return fengXiangStr;
    }

    /**
     * 功能描述:<br/>
     * 根据AQI值获得对应的等级<br/>
     * @Author: liyu
     * @Date: 2019/11/29 10:53
     * @param AQIValue  AQI值
     * @Return: AQI等级  例：优<br/>
     * 说明：如果AQI值为null，返回""
     */
    public static String getAQILevel(Double AQIValue) {
        String AQILevel = "";
        if (AQIValue == null) {

        }else if (AQIValue >= 0d && AQIValue <= 50d) {
            AQILevel = "优";
        }else if (AQIValue >= 51d && AQIValue <= 100d) {
            AQILevel = "良";
        }else if (AQIValue >= 101d && AQIValue <= 150d) {
            AQILevel = "轻度污染";
        }else if (AQIValue >= 151d && AQIValue <= 200d) {
            AQILevel = "中度污染";
        }else if (AQIValue >= 201d && AQIValue <= 300d) {
            AQILevel = "重度污染";
        }else if (AQIValue >= 301d) {
            AQILevel = "严重污染";
        }
        return AQILevel;
    }

    /**
     * 功能描述:<br/>
     * 获取最大值<br/>
     * 如果传入map为null或者空或者map中的值都为null，则返回null；其他情况返回map中值的最大值
     * @Author: liyu
     * @Date: 2020/3/6 19:01
     * @param data
     * @Return: java.lang.Object
     */
    public static Double getMaxValue(Map<String, Double> data) {
        Double result = null;
        if (data == null || data.size() < 1) {
            return result;
        }
        Double max = Double.MIN_VALUE;
        int count = 0;
        for (String key : data.keySet()) {
            Double value = data.get(key);
            if (value == null) {
                continue;
            }else {
                if (value > max) {
                    max = value;
                }
                count++;
            }
        }
        //如果所有都为null，则返回null
        if (count == 0) {
            return result;
        }else {
            result = max;
        }
        return result;
    }

    /**
     * 功能描述:<br/>
     * 获取最小值<br/>
     * 如果传入map为null或者空或者map中的值都为null，则返回null；其他情况返回map中值的最大值
     * @Author: liyu
     * @Date: 2020/3/6 19:01
     * @param data
     * @Return: java.lang.Object
     */
    public static Double getMinValue(Map<String, Double> data) {
        Double result = null;
        if (data == null || data.size() < 1) {
            return result;
        }
        Double min = Double.MAX_VALUE;
        int count = 0;
        for (String key : data.keySet()) {
            Double value = data.get(key);
            if (value == null) {
                continue;
            }else {
                if (value < min) {
                    min = value;
                }
                count++;
            }
        }
        //如果所有都为null，则返回null
        if (count == 0) {
            return result;
        }else {
            result = min;
        }
        return result;
    }

    /**
     * 功能描述:<br/>
     * 获取平均值<br/>
     * 如果传入map为null或者空或者map中的值都为null，则返回null；其他情况返回map中值的平均值
     * @Author: liyu
     * @Date: 2020/3/6 19:01
     * @param data
     * @Return: java.lang.Object
     */
    public static Object getAvgValue(Map<String, Double> data) {
        Object result = null;
        if (data == null || data.size() < 1) {
            return result;
        }
        Double total = 0d;
        int count = 0;
        for (String key : data.keySet()) {
            Double value = data.get(key);
            if (value == null) {
                continue;
            }else {
                total += value;
                count++;
            }
        }
        //如果所有都为null，则返回null
        if (count == 0) {
            return result;
        }else {
            result = numberFormat(total/count,2);
        }
        return result;
    }

    /**
     * 功能描述:<br/>
     * 对map按照值进行升序排序，返回n个结果<br/>
     * （1）如果map为null或者里面没有内容，则返回nulStHIl<br/>
     * （2）如果map有值，返回值是list集合，list的元素内容是   源map的key:value
     * @Author: liyu
     * @Date: 2020/3/6 20:04
     * @param map  源map
     * @param n  返回结果数量
     * @param flag  取前几个结果还是后几个结果的<br/>
     * 标志位  true：取前几个结果     false：取后几个结果
     * @Return list<String>  list的元素内容是   源map的key:value
     */
    public static List<String> mapSort(Map<String,Double> map, Integer n, Boolean flag){
        if (map == null || map.size() < 1){
            return null;
        }
        List<String> result = new ArrayList<String>();

        List<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(
                map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            // 升序排序
            @Override
            public int compare(Map.Entry<String, Double> o1,
                               Map.Entry<String, Double> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }

        });
        if(flag){
            //取排名前n的结果（从小到大）
            int i = 0;
            for (Map.Entry<String, Double> mapping : list) {
                if(i < n){
                    result.add(mapping.getKey()+":"+mapping.getValue());
                }
                i++;
            }
        }else{
            //取排名后n的结果（从大到小）
            for (int i = list.size()-1; i >= list.size()-n; i--) {
                Map.Entry<String, Double> mapping = list.get(i);
                result.add(mapping.getKey()+":"+mapping.getValue());
            }
        }
        return result;
    }

    /**
     * 取一段日期的负荷数据每个点平均
     * @param ymdDataMap
     * @param ymdList
     * @return
     */
    public static TreeMap<String, Double> getAvgData(Map<String, TreeMap<String, Double>> ymdDataMap, List<String> ymdList){
        //转换为时刻点-日期-负荷值的map结构
        TreeMap<String, Map<String, Double>> pointAndYmdDataMap = new TreeMap<>();
        for (String ymd : ymdList){
            TreeMap<String, Double> tmpMap = ymdDataMap.get(ymd);
            if (tmpMap == null){
                continue;
            }

            tmpMap.forEach((point,value) -> {
                Map<String, Double> ymdData = pointAndYmdDataMap.get(point);
                if (ymdData == null){
                    ymdData = new HashMap<>();
                }
                ymdData.put(ymd, value);
                pointAndYmdDataMap.put(point,ymdData);
            });
        }

        TreeMap<String, Double> avgMap = new TreeMap<>();
        pointAndYmdDataMap.forEach((point,v) -> {
            Object obj = DataUtil.getAvgValue(v);
            avgMap.put(point, obj == null ? null : new Double(obj.toString()));
        });

        return avgMap;
    }

    /**
     * @Description JavaBean转换为Map<br/>
     *    <br/>
     * @Author Mark
     * @Date 2022/4/20 10:23
     * @param bean
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    public static Map<String, Object> beanToMap(Object bean){
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Class<?> cl = bean.getClass();
            //获取指定类的BeanInfo 对象
            BeanInfo beanInfo = Introspector.getBeanInfo(cl, Object.class);
            //获取所有的属性描述器
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor pd : pds) {
                String key = pd.getName();
                Method getter = pd.getReadMethod();
                Object value = getter.invoke(bean);
                map.put(key, value);
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return map;
    }
}
