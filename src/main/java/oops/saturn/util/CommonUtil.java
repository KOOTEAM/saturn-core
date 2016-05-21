package oops.saturn.util;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Collection;


/**
 * 常用操作工具类
 */
public abstract class CommonUtil {
	
	/**
	 * 获取代理的目标类
	 * @param className 代理类 
	 * @return  目标类
	 */
	public static Class<?> getClassNoJavassist(Class<?> className) {
		Class<?> classNameNoJavassist = className;
		int javassist = className.getName().indexOf("_$$");
		if(javassist==-1){
			javassist=className.getName().indexOf("$$");
		}
		if(javassist != -1){
		   try {
			classNameNoJavassist = Class.forName(className.getName().substring(0, javassist));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		}
		return classNameNoJavassist;
	}
	
	/**
	 * 格式化数据，小数保留3位
	 * @param originalData
	 * @return 格式化后的数据
	 */
	public static String formatData(Object originalData){
		String convertedValue=null;
		if(originalData==null){
			return null;
		}
		if(originalData instanceof Double||originalData instanceof Float){
			NumberFormat a=NumberFormat.getNumberInstance();
			a.setGroupingUsed(false);
			a.setMinimumFractionDigits(0);
			a.setMaximumFractionDigits(3);
			a.setMinimumIntegerDigits(1);
			a.setRoundingMode(RoundingMode.HALF_UP);
			convertedValue=a.format(originalData);
		}else{
			convertedValue=originalData.toString();
		}
		return convertedValue;
	}
	
	/**
	 * 从集合中获取单个对象，如果集合为null或空则返回null,否则返回首个对象
	 * @param <T> 
	 * @param collection 对象集合
	 * @return 单个对象
	 */
	public static <T> T getOne(Collection<T> collection){
		if(collection==null||collection.isEmpty())
			return null;
		return collection.iterator().next();
	}
}
