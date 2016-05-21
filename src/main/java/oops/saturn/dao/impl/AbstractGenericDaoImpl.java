package oops.saturn.dao.impl;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Transient;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.CollectionType;
import org.hibernate.type.DateType;
import org.hibernate.type.MaterializedClobType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import oops.saturn.bean.ExtendedKeyValue;
import oops.saturn.bean.KeyValue;
import oops.saturn.dao.GenericDao;
import oops.saturn.model.BaseEntity;
import oops.saturn.other.PageQueryCondition;
import oops.saturn.other.PageQueryResult;
import oops.saturn.util.CommonUtil;

/**
 * 数据库操作层通用实现类
 * 
 * 
 * @param <T>
 *            实体类
 * @param <PK>
 *            实体主键类型
 */
@SuppressWarnings({ "unchecked", "deprecation", "rawtypes" })
public abstract class AbstractGenericDaoImpl<T, PK extends Serializable> implements GenericDao<T, PK> {
	public final Logger logger = LoggerFactory.getLogger(getClass());
	protected HibernateTemplate hibernateTemplate; // hibernate数据为操作模板
	protected SessionFactory sessionFactory; // hibernate会话工厂

	/**
	 * 设置hibernate会话工厂
	 * 
	 * @param sessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		this.hibernateTemplate = new HibernateTemplate(sessionFactory);
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	

	/**
	 * 获取hibernate会话工厂
	 * 
	 * @return hibernate会话工厂
	 */
	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

	/**
	 * 获取指定实体类的实体对象,如果没有实体对象则返回null
	 * @param entityClass 实体类
	 * @return 实体对象
	 */

	public <E extends T> E getOne(Class<E> entityClass) {
		final StringBuilder hql =new StringBuilder("from ").append(entityClass.getName());
		List<E> dataSet = (List<E>) this.hibernateTemplate.executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query queryObject = session.createQuery(hql.toString());
					queryObject.setMaxResults(1);
				return queryObject.list();
			}
		});
		if(dataSet.size()>0){
			return dataSet.get(0);
		}else{
			return null;
		}
	}

	/**
     * 获取实体类下的所有实体对象列表
     * @param entityClass 实体类
     * @return 实体对象列表
     */
	public <E extends T> List<E> getAll(Class<E> entityClass) {
		return this.hibernateTemplate.loadAll(entityClass);
	}

	 /**
     * 根据查询条件获取实体对象,如果没有实体对象则返回null
     * @param entityClass 实体类
     * @param conditions 查询条件
     * @param matchExact 是否精确匹配
     * @return 实体对象
     */
    public <E extends T> E getOne(Class<E> entityClass, Map<String, Object> conditions, boolean matchExact){
		PageQueryCondition queryCondition = new PageQueryCondition()
				.from(entityClass).where(conditions).limit(1, 1);
		PageQueryResult result = this.get(queryCondition, matchExact);
		return CommonUtil.getOne(result.getRows(entityClass));
	}
	
    /**
     * 根据查询条件获取实体对象列表
     * @param entityClass 实体类
     * @param conditions 查询条件
     * @param matchExact 是否精确匹配
     * @return 实体对象列表
     */
    public <E extends T> List<E> get(Class<E> entityClass, Map<String, Object> conditions, boolean matchExact) {
		PageQueryCondition queryCondition = new PageQueryCondition()
				.from(entityClass).where(conditions);
		PageQueryResult result = this.get(queryCondition, matchExact);
		return result.getRows(entityClass);
	}

	/**
     * 根据实体对象的属性名值对进行模糊查询，返回查询到的实体对象,如果没有实体对象则返回null
     * @param entityClass 实体类
     * @param key 实体对象的属性名称
     * @param value 实体对象的属性值
     * @return 实体对象
     */
    public <E extends T> E getOne(Class<E> entityClass, String key, Object value) {
		return this.getOne(entityClass, key, value, false);
	}
	
    /**
     * 根据实体对象的属性名值对进行模糊查询，返回查询到的实体对象列表
     * @param entityClass 实体类
     * @param key 实体对象的属性名称
     * @param value 实体对象的属性值
     * @return 实体对象列表
     */
    public <E extends T> List<E> get(Class<E> entityClass, String key, Object value){
		return this.get(entityClass, key, value, false);
	}

	/**
     * 根据实体对象的属性名值对进行查询，返回查询到的实体对象,如果没有实体对象则返回null
     * @param entityClass 实体类
     * @param key 实体对象的属性名称
     * @param value 实体对象的属性值
     * @param matchExact  是否精确匹配
     * @return 实体对象
     */
    public <E extends T> E getOne(Class<E> entityClass, String key, Object value, boolean matchExact) {
		PageQueryCondition queryCondition = new PageQueryCondition()
				.from(entityClass).where(key, value).limit(1, 1);
		PageQueryResult result = this.get(queryCondition, matchExact);
		return CommonUtil.getOne(result.getRows(entityClass));
	}
	
    /**
     * 根据实体对象的属性名值对进行查询，返回查询到的实体对象列表
     * @param entityClass 实体类
     * @param key 实体对象的属性名称
     * @param value 实体对象的属性值
     * @param matchExact  是否精确匹配
     * @return 实体对象列表
     */
    public <E extends T> List<E> get(Class<E> entityClass, String key, Object value, boolean matchExact) {
		PageQueryCondition queryCondition = new PageQueryCondition()
				.from(entityClass).where(key, value);
		PageQueryResult result = this.get(queryCondition, matchExact);
		return result.getRows(entityClass);
	}

	 /**
     * 根据属性名值对、是否精确匹配及排序条件获取实体对象,如果没有实体对象则返回null
     * @param entityClass 实体类
     * @param propertyName 属性名
     * @param propertyValue 属性值
     * @param matchExact 是否精确匹配
     * @param orderPropertyName 排序属性名
     * @param order true为正序，false为逆序
     * @return 实体列表
     */
    public <E extends T> E getOne(Class<E> entityClass, String propertyName, Object propertyValue, boolean matchExact, String orderPropertyName, boolean order) {
		PageQueryCondition queryCondition = new PageQueryCondition()
				.from(entityClass).where(propertyName, propertyValue)
				.order(orderPropertyName, order).limit(1, 1);		
		PageQueryResult result = this.get(queryCondition, matchExact);
		return CommonUtil.getOne(result.getRows(entityClass));
	}
	
    /**
     * 根据属性名值对、是否精确匹配及排序条件获取实体列表
     * @param entityClass 实体类
     * @param propertyName 属性名
     * @param propertyValue 属性值
     * @param matchExact 是否精确匹配
     * @param orderPropertyName 排序属性名
     * @param order true为正序，false为逆序
     * @return 实体列表
     */
    public <E extends T> List<E> get(Class<E> entityClass, String propertyName, Object propertyValue, boolean matchExact, String orderPropertyName, boolean order){
		PageQueryCondition queryCondition = new PageQueryCondition()
				.from(entityClass).where(propertyName, propertyValue)
				.order(orderPropertyName, order);		
		PageQueryResult result = this.get(queryCondition, matchExact);
		return result.getRows(entityClass);
	}

	/**
     * 根据查询条件获取模糊查询的实体对象,如果没有实体对象则返回null
     * @param entityClass 实体类
     * @param conditions 查询条件
     * @param orders 排序条件
     * @param matchExact 是否精确匹配
     * @return 实体对象
     */    
    public <E extends T> E getOne(Class<E> entityClass, Map<String, Object> conditions, Map<String, Boolean> orders, boolean matchExact) {
		PageQueryCondition queryCondition = new PageQueryCondition()
				.from(entityClass).where(conditions).order(orders).limit(1, 1);
		PageQueryResult result = this.get(queryCondition, matchExact);
		return CommonUtil.getOne(result.getRows(entityClass));
	}
	
    /**
     * 根据查询条件获取模糊查询的实体对象列表
     * @param entityClass 实体类
     * @param conditions 查询条件
     * @param orders 排序条件
     * @param matchExact 是否精确匹配
     * @return 实体对象列表
     */    
    public <E extends T> List<E> get(Class<E> entityClass, Map<String, Object> conditions, Map<String, Boolean> orders, boolean matchExact){
		PageQueryCondition queryCondition = new PageQueryCondition()
				.from(entityClass).where(conditions).order(orders);
		PageQueryResult result = this.get(queryCondition, matchExact);
		return result.getRows(entityClass);
	}

	/**
     * 根据查询条件获取模糊查询的实体对象,如果没有实体对象则返回null
     * @param entityClass 实体类
     * @param conditions 查询条件
     * @return 实体对象
     */
    public <E extends T> E getOne(Class<E> entityClass, Map<String, Object> conditions ) {
		return this.getOne(entityClass, conditions, false);
	}
	
    /**
     * 根据查询条件获取模糊查询的实体对象列表
     * @param entityClass 实体类
     * @param conditions 查询条件
     * @return 实体对象列表
     */
    public <E extends T> List<E> get(Class<E> entityClass, Map<String, Object> conditions){
		return this.get(entityClass, conditions, false);
	}

    /**
     * 根据数据库编号删除一组实体
     * @param entityClass 待删除实体的类
     * @param ids 数据库编号列表
     */
    public void delete(Class<? extends T> entityClass, List<PK> ids) {
    	for (PK id : ids) {
    		this.delete(entityClass, id);
    	}
    		
	}

    /**
     * 删除一组实体
     * @param entities 待删除的实体对象列表
     */
	public void delete(List<? extends T> entities){
		String idField = "id";
		Object id = null;
		for (Object entity : entities) {
			try {
				id = PropertyUtils.getProperty(entity, idField);
				entity = this.hibernateTemplate.get(CommonUtil.getClassNoJavassist(entity.getClass()), (PK) id);
				if(entity!=null){
					this.hibernateTemplate.delete(entity);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new HibernateException(e);
			}
		}
	}

    /**
     * 根据数据库编号删除某个实体
     * @param entityClass 待删除实体的类
     * @param id 数据库编号
     */
    public void delete(Class<? extends T> entityClass, PK id){
    	try {
			this.hibernateTemplate.delete(this.hibernateTemplate.get(CommonUtil.getClassNoJavassist(entityClass), (PK) id));
		} catch (Exception e) {
			e.printStackTrace();
			throw new HibernateException(e);
		}
    }
    
    /**
     * 删除某个实体
     * @param entity 待删除的实体对象
     */
    public void delete(T entity) {
		List<T> entities = new ArrayList<T>();
		entities.add(entity);
		this.delete(entities);
	}


	 /**
     * 新建或修改一组实体对象
     * @param entities
     * @param modifiedData 要保存的数据
     * @param isJson 表明第二个参数值是否为json数据
     * @return 保存后的实体对象列表
     */
    public <E extends T> List<E> saveAll(List<E> entities, List<Map<String, Object>> modifiedData, boolean isJson) {
		E entity = null;
		List<E> result = new ArrayList<E>();
		for (int i = 0; i < entities.size(); i++) {
			entity = entities.get(i);
			entity = this.saveOrUpdate(entity, modifiedData==null?null:modifiedData.get(i), isJson);
			result.add(entity);
		}
		return result;
	}

    /**
     * 新建或修改实体对象
     * @param entity  实体对象
     * @param json 要保存的json数据
     * @return 保存后的实体对象
     */
	public <E extends T> E saveOrUpdate(E entity, Map<String, Object> json){
		try {
			if (json != null) {
				entity = (E)this.loadEntityByJson(entity, json);
			}
			this.hibernateTemplate.saveOrUpdate(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new HibernateException(e);
		}
		return entity;
	}

	/**
	 * 新建或修改实体对象
	 * 
	 * @param entity
	 *            实体对象
	 * @return 保存后的实体对象
	 */
	public <E extends T> E saveOrUpdate(E entity) {
		 return this.saveOrUpdate(entity, null, false);
	}

	/**
     * 新建或修改实体对象,受影响的属性由第二个参数指定。
     * @param entity 实体对象
     * @param modifiedData 要保存的数据
     * @param isJson 表明第二个参数值是否为json数据
     * @return 保存后的实体对象
     */
	public <E extends T> E saveOrUpdate(E entity, Map<String, Object> modifiedData, boolean isJson){
		try {
			if (modifiedData != null) {
				if (isJson)
					entity = (E) this.loadEntityByJson(entity, modifiedData);
				else
					entity = (E) this.loadEntityByParam(entity, modifiedData);
			}
			this.hibernateTemplate.saveOrUpdate(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new HibernateException(e);
		}
		return entity;
	}

    /**
     * 根据数据库编号获取某个实体对象
     * @param entityClass 要获取的实体对象所属的类
     * @param id 数据库编号
     * @return 实体对象
     */
	public <E extends T> E get(Class<E> entityClass, PK id){
		return (E)this.hibernateTemplate.get(entityClass, id);
	}
	
	/**
	 * 根据查询条件获取模糊查询结果
	 * 
	 * @param queryCondition
	 *            查询条件
	 * @return 查询结果
	 */
	public PageQueryResult get(final PageQueryCondition queryCondition) {
		return this.get(queryCondition, false);
	}


	/**
	 * 获取数据的条数 
	 * @param entityClass   实体类
	 * @param conditions   查询条件
	 * @param matchExact   是否精确匹配
	 * @return 数据的条数
	 */
	public Long getCount(Class<? extends T> entityClass, Map<String, Object> conditions, boolean matchExact){
		PageQueryCondition queryCondition = new PageQueryCondition()
				.from(entityClass).where(conditions);
		return this.getCount(queryCondition, matchExact);
	}
	
	
	/**
	 * 获取数据的条数 
	 * @param queryCondition 查询条件
	 * @param matchExact 是否精确匹配
	 * @return 数据的条数
	 */
	public Long getCount(final PageQueryCondition queryCondition, boolean matchExact){
		return this.get(queryCondition, matchExact,true,true).getRecords();
	}
	/**
	 * 根据查询条件获取查询结果
	 * 
	 * @param queryCondition
	 *            查询条件
	 * @param matchExact
	 *            是否精确匹配
	 * @return 查询结果
	 */
	public PageQueryResult get(final PageQueryCondition queryCondition, boolean matchExact){
		return this.get(queryCondition, matchExact,false,true);
	}
		
	/**
	 * 根据查询条件获取查询结果，使用复合主键时调用
	 * @param queryCondition 查询条件
	 * @param matchExact 是否精确匹配
	 * @param isNeedDistinct 使用复合主键时，需要去除Distinct
	 * @return 查询结果
	 */
	public PageQueryResult getComplexPK(final PageQueryCondition queryCondition, boolean matchExact){
		return this.get(queryCondition, matchExact,false,false);
	}


	  /**
     * 获取当前实体类下的实体对象总数
     * @param entityClass 实体类
     * @return 实体对象总数
     */
    public Long getCount(Class<? extends T> entityClass) {
		final String countHql = "select count(*) from " + entityClass.getSimpleName();
		Long count = this.executeCountHql(countHql);
		return count;
	}
    
    /**
     * 获取表的字段名称列表
     * @param tableName 表名
     * @return 表的字段名称列表
     */
	public Set<String> getColumnNames(final String tableName){
    	Set<String> columnNameSet = new HashSet<String>();
    	
    	JdbcTemplate jdbcTemplate =new JdbcTemplate(SessionFactoryUtils.getDataSource(this.hibernateTemplate.getSessionFactory()));
    	List<Map<String, Object>> columnNames = jdbcTemplate.queryForList("show columns from " + tableName);
    	for(int i = 0;i<columnNames.size();i++){
    		columnNameSet.add(((String) columnNames.get(i).get("Field")).toLowerCase());
		}
    	
    	return columnNameSet;
    }

	public int executeUpdate(final String ql, final boolean isNative, final Object... params) {
		return (Integer) this.hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = isNative ? session.createSQLQuery(ql) : session.createQuery(ql);
				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						query.setParameter(i, params[i]);
					}
				}
				return query.executeUpdate();
			}
		});
	}

	public int executeUpdate(final String ql, final boolean isNative, final Map<String, Object> params) {
		return (Integer) this.hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = isNative ? session.createSQLQuery(ql) : session.createQuery(ql);
				if (params != null) {
					for (String key : params.keySet()) {
						query.setParameter(key, params.get(key));
					}
				}
				return query.executeUpdate();
			}
		});
	}

	public <E> List<E> executeQuery(final String ql, final boolean isNative, final Object... params) {
		return (List<E>) this.hibernateTemplate.executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = isNative ? session.createSQLQuery(ql) : session.createQuery(ql);
				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						query.setParameter(i, params[i]);
					}
				}
				return query.list();
			}
		});
	}

	public <E> List<E> executeQuery(final String ql, final boolean isNative, final Map<String, Object> params) {
		return (List<E>) this.hibernateTemplate.executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = isNative ? session.createSQLQuery(ql) : session.createQuery(ql);
				if (params != null) {
					for (String key : params.keySet()) {
						query.setParameter(key, params.get(key));
					}
				}
				return query.list();
			}
		});
	}
	

   /**
    * 执行返回结果为数值的hql，
    * @param hql
    * @return 执行结果
    */
private Long executeCountHql(final String hql) {
		Long count = (Long) this.hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query queryObject = session.createQuery(hql);
				return (Long) queryObject.uniqueResult();
			}
		});
		return count;
	}
   
	/**
	 * 获取实体对象的属性类型
	 * 
	 * @param entity
	 *            实体对象
	 * @param propertyName
	 *            属性名
	 * @return 属性类型
	 */
	private Type getPropertyType(Object entity, String propertyName) {
		ClassMetadata metadata = this.hibernateTemplate.getSessionFactory().getClassMetadata(CommonUtil.getClassNoJavassist(entity.getClass()));
		Type type = metadata.getPropertyType(propertyName);
		return type;
	}

	/**
	 * 将格式为yyyy-MM-dd的日期字符串解析为java日期对象,若解析出错则返回null
	 * 
	 * @param dateString
	 *            日期字符串
	 * @return java日期对象
	 */
	private Date parseDate(String dateString) {
		Date date = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = dateFormat.parse(dateString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 将格式 为yyyy-MM-dd HH:mm:ss时间戳字符串解析为java日期对象，若解析出错则返回null
	 * 
	 * @param timestampString
	 *            时间戳字符串
	 * @return java日期对象
	 */
	private Date parseTimestamp(String timestampString) {
		Date date = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = dateFormat.parse(timestampString);
		} catch (Exception e) {
			date=parseDate(timestampString);
		}
		return date;
	}

	/**
	 * 设置实体对象的属性值
	 * 
	 * @param targetBean
	 *            实体对象
	 * @param typeName
	 *            属性类型名称
	 * @param key
	 *            属性名
	 * @param value
	 *            属性值
	 * @throws Exception
	 *             异常
	 */
	private void setCustomPropertyValue(Object targetBean, String typeName, String key, Object value) throws Exception {
		if (value.getClass().isArray()) {
			value = ((String[]) value)[0];
		}
		if (typeName.equals("date")) {
			if (((String) value).trim().isEmpty())
				value = null;
			else
				value = parseDate((String) value);
		} else if (typeName.equals("timestamp")) {
			if (((String) value).trim().isEmpty())
				value = null;
			else
				value = parseTimestamp((String) value);
		} else if (typeName.equals("long")) {
			if (((String) value).trim().isEmpty())
				value = null;
			else
				value = Long.parseLong((String) value);
		} else if (typeName.equals("double")) {
			if (((String) value).trim().isEmpty())
				value = null;
			else
				value = Double.parseDouble((String) value);
		} else if (typeName.equals("boolean")) {
			if (((String) value).equals("1"))
				value = true;
			else
				value = false;
		}
		PropertyUtils.setProperty(targetBean, key, value);
	}
	/**
	 * 设置实体对象的属性值
	 * 
	 * @param targetBean
	 *            实体对象
	 * @param type
	 *            属性类型
	 * @param key
	 *            属性名
	 * @param value
	 *            属性值
	 * @throws Exception
	 *             异常
	 */
	private void setCustomPropertyValue(Object targetBean, Type type, String key, Object value) throws Exception {
		String typeName = type.getName();
		this.setCustomPropertyValue(targetBean, typeName, key, value);
	}

	/**
	 * 设置实体对象的自定义属性值
	 * 
	 * @param targetBean
	 *            实体对象
	 * @param key
	 *            属性名
	 * @param value
	 *            属性值
	 * @throws Exception
	 *             异常
	 */
	private void setPropertyValue(Object targetBean, String key, Object value) throws Exception {
		if (targetBean instanceof BaseEntity && key.startsWith("cp.")) {
			Type type = getPropertyType(targetBean, key);
			String typeName = type.getName();
			if (typeName.equals("date")) {
				if (((String) value).trim().isEmpty())
					value = null;
				else
					value = parseDate((String) value);
			} else if (typeName.equals("timestamp")) {
				if (((String) value).trim().isEmpty())
					value = null;
				else
					value = parseTimestamp((String) value);
			} else if (typeName.equals("long")) {
				if (((String) value).trim().isEmpty())
					value = null;
				else
					value = Long.parseLong((String) value);
			} else if (typeName.equals("double")) {
				if (((String) value).trim().isEmpty())
					value = null;
				else
					value = Double.parseDouble((String) value);
			} else if (typeName.equals("boolean")) {
				if (((String) value).equals("1"))
					value = true;
				else
					value = false;
			}
		}
		PropertyUtils.setProperty(targetBean, key, value);
	}
	
	/**
	 * 根据客户端传递过来的参数列表，获取对象的属性
	 * 
	 * @param parameters
	 *            原始参数列表
	 * @return 对象的属性
	 */
	private Map<String, Map<String, Object>> filterParameters(Map<String, Object> parameters) {
		Map<String, Map<String, Object>> tempResult = new HashMap<String, Map<String, Object>>();
		Map<String, Map<String, Object>> result = new HashMap<String, Map<String, Object>>();
		Object value = null;
		for (String param : parameters.keySet()) {
			int index = param.indexOf(".");
			value = parameters.get(param);
			if (index == -1) {
				if (value instanceof String && ((String) value).isEmpty())
					continue;
				tempResult.put(param, null);
			} else {
				String key = param.substring(0, index);
				Map<String, Object> temp = tempResult.get(key);
				if (temp == null) {
					temp = new HashMap<String, Object>();
					tempResult.put(key, temp);
				}
				if (value instanceof String && ((String) value).isEmpty())
					continue;
				temp.put(param.substring(index + 1), value);
			}
		}
		for (Map.Entry<String, Map<String, Object>> entity : tempResult.entrySet()) {
			Map<String, Object> v1 = entity.getValue();
			Object v2 = null;
			Class<?> c = null;
			Class<?> elementType = null;
			if (v1 != null && v1.size() == 1) {
				v2 = v1.values().iterator().next();
				c = v2.getClass();
				if (c.isArray()) {
					elementType = c.getComponentType();
					if (elementType == String.class && ((String[]) v2).length == 1 && ((String[]) v2)[0].isEmpty())
						continue;
				}
			}
			result.put(entity.getKey(), entity.getValue());
		}
		return result;
	}

	/**
	 * 根据实体类名和实体对象主键获取对应的实体对象
	 * 
	 * @param entityName
	 *            实体类名
	 * @param id
	 *            实体对象主键
	 * @return 实体对象
	 */
	private T get(String entityName, PK id) {
		return (T) this.hibernateTemplate.get(entityName, id);
	}
	
	/**
	 * 将json数据转换成对应的实体对象
	 * 
	 * @param srcBean
	 *            json数据对应的临时对象
	 * @param json
	 *            json数据
	 * @return 转换后的实体对象
	 */
	private Object loadEntityByJson(Object srcBean, Object json) {
		Object targetBean = null, tempObject;

		try {

			// 如果临时对象不是集合对象或map对象则将目标对象设置为相应的持久化对象
			if (!(srcBean instanceof Collection) && !(srcBean instanceof Map)) {
				ClassMetadata metadata = this.hibernateTemplate.getSessionFactory().getClassMetadata(CommonUtil.getClassNoJavassist(srcBean.getClass()));
				String idField = "id";
				Object id = PropertyUtils.getProperty(srcBean, idField);
				if (id != null) {
					if (id.equals(new Long(-1)))
						return null;
					else
						targetBean = this.get(metadata.getEntityName(), (PK) id);
				}
			}

			// 如果目标对象还未设置则创建目标对象
			if (targetBean == null)
				targetBean = srcBean.getClass().newInstance();

			// 如果目标对象为map对象则循环处理每个键值对
			if (targetBean instanceof Map) {
				Iterator<String> iterator = ((Map<String,?>) json).keySet().iterator();
				Map jsonMap = (Map) json;
				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					Object subJsons = jsonMap.get(key);
					tempObject = ((Map) srcBean).get(key);
					if (tempObject != null && !(tempObject instanceof String)) {
						Object value = loadEntityByJson(tempObject, subJsons);
						((Map) targetBean).put(key, value);
					}
				}
			} else if (targetBean instanceof Collection) { // 如果目标对象为集合对象则循环处理每个集合元素
				Iterator iterator = ((Collection) json).iterator();
				for (Object value : ((Collection) srcBean)) {
					Object subBean = loadEntityByJson(value, iterator.next());
					((Collection) targetBean).add(subBean);
				}
			} else { // 否则循环处理json数据中的每个键值对
				Map jsonMap = (Map) json;
				Iterator ite = jsonMap.keySet().iterator();
				while (ite.hasNext()) {
					String key = (String) ite.next();
					Object subJsons = jsonMap.get(key);
					Object value = null;
					try {
						value = PropertyUtils.getProperty(srcBean, key);
					} catch (NoSuchMethodException e) {
						continue;
					}

					// 如果属性值为集合对象或map对象则需进一步处理
					if (subJsons instanceof Collection || subJsons instanceof Map) {
						Object subBean = loadEntityByJson(value, subJsons);
						Collection originalSet = null, originalList = null;
						Map originalMap = null;
						if (subBean instanceof Set) {
							originalSet = (Collection) PropertyUtils.getProperty(targetBean, key);
							if (originalSet == null) { // 如果原始集合值为空则设置相应的属性值
								PropertyUtils.setProperty(targetBean, key, subBean);
							} // 如果存在fixation属性则替换原始集合
							else if ((jsonMap.get("fixation") != null && ((Map) jsonMap.get("fixation")).get(key) != null)) {
								originalSet.clear();
								this.hibernateTemplate.getSessionFactory().getCurrentSession().flush();
								originalSet.addAll((Set) subBean);
							} else
								// 否则将现有集合元素添加到原集合对象
								originalSet.addAll((Collection) subBean);

						} else if (subBean instanceof Map) {
							originalMap = (Map) PropertyUtils.getProperty(targetBean, key);

							// 如果不存在internalState属性则将现有键值对添加到原map对象
							if (originalMap != null && ((Map) subJsons).get("internalState") == null)
								originalMap.putAll((Map) subBean);
							else {

								// 如果原map不为空则先清除原map对象
								if (originalMap != null) {
									originalMap.clear();
									originalMap.putAll((Map) subBean);
								} else
									setPropertyValue(targetBean, key, subBean);
							}
						} else if (subBean instanceof List) {
							originalList = (Collection) PropertyUtils.getProperty(targetBean, key);
							originalList.clear();
							originalList.addAll((Collection) subBean);
						} else
							setPropertyValue(targetBean, key, subBean);

					} else { // 否则设置相应的属性值
						setPropertyValue(targetBean, key, value);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new HibernateException(e);
		}
		return targetBean;
	}

	/**
	 * 将请求参数转换成对应的实体对象
	 * 
	 * @param srcBean
	 *            请求参数对应的临时对象
	 * @param parameters
	 *            请求参数
	 * @return 转换后的实体对象
	 */
	private Object loadEntityByParam(Object srcBean, Map<String, Object> parameters) {
		Object targetBean = null, temp;
		String[] idArray;

		try {

			// 如果临时对象不是集合对象或map对象则将目标对象设置为相应的持久化对象
			if (!(srcBean instanceof Collection) && !(srcBean instanceof Map)) {
				ClassMetadata metadata = this.hibernateTemplate.getSessionFactory().getClassMetadata(CommonUtil.getClassNoJavassist(srcBean.getClass()));
				String idField = "id";
				Type type = metadata.getIdentifierType();
				Object id = PropertyUtils.getProperty(srcBean, idField);
				if (id == null) {
					temp = parameters.get(idField);
					if (temp != null) {
						if (temp.getClass().isArray()) {
							idArray = (String[]) temp;
							if (idArray.length > 0 && (!(idArray[0].isEmpty()))) {
								if (type.getName().equals("long")) {
									id = Long.parseLong(idArray[0]);
								} else
									id = idArray[0];
							}
						}
					}
				}
				if (id != null) {
					if (id.equals(new Long(-1)))
						return null;
					else
						targetBean = this.get(metadata.getEntityName(), (PK) id);
				}
			}
			if (targetBean == null)
				targetBean = srcBean.getClass().newInstance();

			Map<String, Map<String, Object>> filtedParams = filterParameters(parameters);
			if (targetBean instanceof Collection) { // 如果目标对象为集合对象则循环处理每个集合元素
				Map<String, Object> subParams = filtedParams.values().iterator().next();
				for (Object value : ((Collection<?>) srcBean)) {
					Object subBean = loadEntityByParam(value, subParams);
					((Collection<Object>) targetBean).add(subBean);
				}

			} else { // 循环处理每个请求参数
				for (Map.Entry<String, Map<String, Object>> entry : filtedParams.entrySet()) {
					String key = entry.getKey();
					Map<String, Object> subParams = entry.getValue();
					Object value = null;
					try {
						value = PropertyUtils.getProperty(srcBean, key);
					} catch (NoSuchMethodException e) {
						continue;
					}
					if (subParams == null) { // 如果没有下级参数则直接设置相应的属性值
						if (value instanceof Set)
							value = new HashSet<Object>();
						else if (value instanceof List)
							value = new ArrayList<Object>();
						if (value != null && value.getClass().isArray()) {
							String[] tempValue = (String[]) value;
							if (tempValue.length == 1) {
								PropertyUtils.setProperty(targetBean, key, tempValue[0]);
								continue;
							}
						}

						PropertyUtils.setProperty(targetBean, key, value);
					} else {

						// 如果是自定义属性则直接设置相应的属性值
						if (targetBean instanceof BaseEntity && key.equals("cp")) {
							for (Map.Entry<String, Object> entity : subParams.entrySet()) {
								String customPropertyKey = entity.getKey();
								Object customPropertyValue = entity.getValue();
								this.setCustomPropertyValue(PropertyUtils.getProperty(targetBean, "cp"), getPropertyType(targetBean, "cp." + customPropertyKey), customPropertyKey, customPropertyValue);
							}
						} else {
							Object subBean = loadEntityByParam(value, subParams);
							PropertyUtils.setProperty(targetBean, key, subBean);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new HibernateException(e);
		}
		return targetBean;
	}

	/**
	 * 获取实体属性类型
	 * 
	 * @param fieldClass
	 *            实体类
	 * @param fieldName
	 *            属性名
	 * @return 属性类型
	 */
	private Type getFieldClass(Class<?> fieldClass, String fieldName) {
		ClassMetadata metadata = this.hibernateTemplate.getSessionFactory().getClassMetadata(CommonUtil.getClassNoJavassist(fieldClass));
		Type type = null;
		int index = fieldName.indexOf("."), lastIndex = 0;
		String tempName = null, identityFieldName = null;
		StringBuilder tempPropertyName=new StringBuilder();
		if (index != -1) {
			while (index != -1) {
				tempName = fieldName.substring(lastIndex, index);
				tempPropertyName.append(tempName);
				type = metadata.getPropertyType(tempName);
				if (type.isAssociationType()) {
					metadata = this.hibernateTemplate.getSessionFactory().getClassMetadata(CommonUtil.getClassNoJavassist(type.getReturnedClass()));
					tempPropertyName.setLength(0);
				}
				lastIndex = index + 1;
				index = fieldName.indexOf(".", lastIndex);
				if(tempPropertyName.length()>0)
					tempPropertyName.append(".");
			}
			fieldName = fieldName.substring(lastIndex);
			tempPropertyName.append(fieldName);
		}else{
			tempPropertyName.append(fieldName);
		}
		fieldName=tempPropertyName.toString();
		identityFieldName = "id";
		if (identityFieldName != null && identityFieldName.equals(fieldName))
			type = metadata.getIdentifierType();
		else
			type = metadata.getPropertyType(fieldName);
		return type;
	}

	
	/**
	 * 根据属性名值对生成hql查询条件
	 * 
	 * @param entityClass
	 *            实体类
	 * @param aliasName
	 *            实体类别名
	 * @param lstField
	 *            属性名值对列表
	 * @param matchExact
	 *            是否精确匹配
	 * @return hql查询条件
	 */
	private String generatorConditionStatement(Class<?> entityClass, String aliasName, List<KeyValue<String, Object>> lstField, boolean matchExact) {
		StringBuffer content = new StringBuffer();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timestampFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date tempDate;
		if (lstField == null)
			return content.toString();
		Iterator<KeyValue<String, Object>> iterator = lstField.iterator();

		// 循环处理属性名值对列表
		while (iterator.hasNext()) {
			KeyValue<String, Object> field = iterator.next();
			try {
				String srcFieldName = field.getName(), fieldName = aliasName + "." + srcFieldName;
				Object value = field.getValue();
				Type propertyType = getFieldClass(entityClass, srcFieldName);

				// 属性类型为日期或时间戳
				if (propertyType instanceof DateType || propertyType instanceof TimestampType) {

					// 属性值为map对象时，属性值包含开始和结束日期
					if (value instanceof Map) {
						Calendar endCal = null;
						Map<String,?> dateConditions = (Map<String,?>) value;
						String startDateString = (String) dateConditions.get("startDate");
						String endDateString = (String) dateConditions.get("endDate");
						if (endDateString != null) {
							endCal = Calendar.getInstance();
							if (propertyType instanceof TimestampType && endDateString.trim().length() > "yyyy-MM-dd".length()) {
								tempDate = parseTimestamp((String) endDateString);
								endCal.setTime(tempDate);
							} else {
								tempDate = parseDate((String) endDateString);
								endCal.setTime(tempDate);
								endCal.add(Calendar.DAY_OF_MONTH, 1);
							}
						}
						if (startDateString != null) {
							content.append(fieldName);
							content.append(" >='");
							content.append(startDateString);
							content.append("'");
						}
						if (endDateString != null) {
							if (startDateString != null)
								content.append(" and ");
							content.append(fieldName);
							if (propertyType instanceof TimestampType && endDateString.trim().length() > "yyyy-MM-dd".length()) {
								content.append(" <='");
								content.append(timestampFormatter.format(endCal.getTime()));
							} else {
								content.append(" <'");
								content.append(dateFormatter.format(endCal.getTime()));
							}
							content.append("'");
						}
					} else {
						Calendar startCal = Calendar.getInstance();
						Calendar endCal = Calendar.getInstance();
						if(value==null){
							content.append(fieldName);
							content.append("  is null ");
						} else if (propertyType instanceof DateType) { // 属性类型为日期
							tempDate = parseDate((String) value);
							startCal.setTime(tempDate);
							endCal.setTime(tempDate);
							endCal.add(Calendar.DAY_OF_MONTH, 1);
							content.append(fieldName);
							content.append(" >='");
							content.append(dateFormatter.format(startCal.getTime()));
							content.append("'");
							content.append(" and ");
							content.append(fieldName);
							content.append(" <'");
							content.append(dateFormatter.format(endCal.getTime()));
							content.append("'");
						} else { // 属性类型为时间戳
							tempDate = parseTimestamp((String) value);
							startCal.setTime(tempDate);
							tempDate = parseDate((String) value);
							endCal.setTime(tempDate);
							endCal.add(Calendar.DAY_OF_MONTH, 1);

							content.append(fieldName);
							content.append(" >='");
							content.append(timestampFormatter.format(startCal.getTime()));
							content.append("'");
							content.append(" and ");
							content.append(fieldName);
							content.append(" <'");
							content.append(timestampFormatter.format(endCal.getTime()));
							content.append("'");
						}
					}

				} else if (propertyType instanceof StringType || propertyType instanceof MaterializedClobType) { // 属性类型为字符串或clob
					if (value instanceof Collection) { // 属性值为列表时构造in语句
						content.append("(");
						Iterator<String> iteValues = ((List<String>) value).iterator();
						while (iteValues.hasNext()) {
							String temp = iteValues.next();
							content.append(fieldName);
							content.append(" like ");
							content.append("'%,");
							content.append(temp);
							content.append(",%'");
							if (iteValues.hasNext())
								content.append(" or ");
						}
						content.append(")");
					} else if (value == null) { // 属性值为空时构造 is null语句
						content.append(fieldName);
						content.append(" is null");
					} else {
						content.append(fieldName);
						if (matchExact) {
							content.append(" = '");
							content.append(value.toString().replaceAll("'", "''").replaceAll("\\\\", "\\\\\\\\"));
							content.append("'");
						} else {
							content.append(" like '%");
							content.append(value.toString().replaceAll("'", "''").replaceAll("\\\\", "\\\\\\\\\\\\\\\\").replaceAll("%", "\\\\%").replaceAll("_", "\\\\_"));
							content.append("%'");
						}
					}
				} else {
					content.append(fieldName);
					if (value instanceof Collection) { // 属性值为列表时构造in语句
						content.append(" in (");
						Iterator<?> iteValues = ((List<?>) value).iterator();
						while (iteValues.hasNext()) {
							String temp = iteValues.next() + "";
							content.append(temp);
							if (iteValues.hasNext())
								content.append(",");
						}
						content.append(")");
					} else if (value == null) { // 属性值为空时构造 is null语句
						content.append(" is null");
					} else {
						content.append("=");
						content.append(value);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new HibernateException(e);
			}
			if (iterator.hasNext())
				content.append(" and ");
		}

		return content.toString();
	}
	
	
	/**
	 * 根据查询条件获取查询结果
	 * @param queryCondition  查询条件
	 * @param matchExact 是否精确匹配
	 * @param justQueryDataCount 是否仅获取数据的条数
	 * @param isNeedDistinct 是否需要Distinct，使用复合主键时，需要去除
	 * @return 查询结果
	 */
	private PageQueryResult get(final PageQueryCondition queryCondition, boolean matchExact,boolean justQueryDataCount,boolean isNeedDistinct) {
		String propertyPrefix = queryCondition.getPropertyPrefix();
		PageQueryResult result = new PageQueryResult();
		List<String> fields = queryCondition.getFields();
		Map<String,Object> conditions = queryCondition.getConditions();
		Map<String, Boolean> orders = queryCondition.getOrders();
		Class<?> entityClass = queryCondition.getEntityClass();
		String extendedHql = queryCondition.getExtendedHql();
		String classAlias = null;
		StringBuffer query = new StringBuffer();
		StringBuffer fromStatementBuffer = new StringBuffer(" from ");
		StringBuffer conditionStatement = new StringBuffer(" where ");
		;

		// 去除属性前缀
		if (propertyPrefix != null && !propertyPrefix.trim().isEmpty()) {
			int length = propertyPrefix.length() + 1;

			// 去除字段中的属性前缀
			if (fields != null) {
				for (int index = 0; index < fields.size(); index++) {
					fields.set(index, fields.get(index).substring(length));
				}
			}

			// 去除查询条件中的属性前缀
			if (conditions != null) {
				Map<String,Object> tempConditions = new HashMap<String,Object>();
				for (Map.Entry<String,Object> entry : conditions.entrySet()) {
					tempConditions.put((entry.getKey()).substring(length), entry.getValue());
				}
				conditions = tempConditions;
			}

			// 去除排序条件中的属性前缀
			if (orders != null) {
				Map<String, Boolean> tempOrders = new HashMap<String, Boolean>();
				for (Map.Entry<String, Boolean> entry : orders.entrySet()) {
					tempOrders.put((entry.getKey()).substring(length), entry.getValue());
				}
				orders = tempOrders;
			}
		}

		// 构造查询条件
		if (conditions != null && conditions.size() > 0) {
			Map<ExtendedKeyValue<Class<?>,String>, Map<String, Object>> class2Fields = new HashMap<ExtendedKeyValue<Class<?>,String>, Map<String, Object>>();
			Map<ExtendedKeyValue<Class<?>,String>, String> class2Alias = new HashMap<ExtendedKeyValue<Class<?>,String>, String>();
			List<KeyValue<String, Object>> collectionFields;
			Iterator<Map.Entry<String,Object>> ite = conditions.entrySet().iterator();
			ExtendedKeyValue<Class<?>,String> rootClassFieldKeyValue=new ExtendedKeyValue<Class<?>,String>(entityClass,null);
			class2Fields.put(rootClassFieldKeyValue, new HashMap<String, Object>());

			// 循环处理每个查询条件，解析出属性类型是集合的元素类型及元素类型下的查询字段
			while (ite.hasNext()) {
				Class<?> elementClass;
				Map.Entry<String,Object> entity = ite.next();
				Object value = entity.getValue();
				String srcFieldName = (String) entity.getKey(), subCollectionName = srcFieldName, fieldName;
				int beginIndex = srcFieldName.indexOf(".");
				if (beginIndex != -1)
					subCollectionName = srcFieldName.substring(0, beginIndex);
				Type propertyType = getFieldClass(entityClass, subCollectionName);
				if (propertyType.isCollectionType()) { // 如果属性类型为集合则取集合元素的类型
					CollectionType collectionType = (CollectionType) propertyType;
					Type elementType = collectionType.getElementType((SessionFactoryImplementor) this.sessionFactory);
					elementClass = elementType.getReturnedClass();
					fieldName = srcFieldName.substring(beginIndex + 1);
				} else {
					elementClass = entityClass;
					fieldName = srcFieldName;
				}
				ExtendedKeyValue<Class<?>,String> classSubCollectionNameKeyValue=new ExtendedKeyValue<Class<?>,String>(elementClass,null);
				if(propertyType.isCollectionType()){
					classSubCollectionNameKeyValue.setValue(subCollectionName);
				}
				Map<String, Object> classProperties = class2Fields.get(classSubCollectionNameKeyValue);
				if (classProperties == null) {
					classProperties = new HashMap<String, Object>();
					classProperties.put("subCollectionName", subCollectionName);
					class2Fields.put(classSubCollectionNameKeyValue, classProperties);
				}
				collectionFields = (List<KeyValue<String, Object>>) classProperties.get("fields");
				if (collectionFields == null) {
					collectionFields = new ArrayList<KeyValue<String, Object>>();
					classProperties.put("fields", collectionFields);
				}
				collectionFields.add(new KeyValue<String,Object>(fieldName, value));
			}

			// 构造from语句
			List<ExtendedKeyValue<Class<?>,String>> lstClass = new ArrayList<ExtendedKeyValue<Class<?>,String>>(class2Fields.keySet());
			lstClass.remove(rootClassFieldKeyValue);
			lstClass.add(0, rootClassFieldKeyValue);
			Iterator<ExtendedKeyValue<Class<?>,String>> classIterator = lstClass.iterator();
			
			// 循环构造left join语句
			Map<Class<?>,Integer> classCountMap=new HashMap<Class<?>,Integer>();
			while (classIterator.hasNext()) {
				ExtendedKeyValue<Class<?>,String> classFieldKeyValue= classIterator.next();
				Class<?> tempClass =classFieldKeyValue.getName();
				Integer classCount = classCountMap.get(tempClass);
				if(classCount==null){
					classCount=0;
				}else{
					classCount++;
				}
				classCountMap.put(tempClass, classCount);	
				String simpleClassName = tempClass.getSimpleName();
				String aliasName = simpleClassName + "_"+classCount;
				String tempCondition;
				class2Alias.put(classFieldKeyValue, aliasName);
				if (tempClass == entityClass) {
					fromStatementBuffer.append(simpleClassName);
				} else {
					fromStatementBuffer.append(class2Alias.get(rootClassFieldKeyValue));
					fromStatementBuffer.append(".");
					fromStatementBuffer.append(class2Fields.get(classFieldKeyValue).get("subCollectionName"));
				}
				fromStatementBuffer.append(" as ");
				fromStatementBuffer.append(aliasName);
				if (classIterator.hasNext())
					fromStatementBuffer.append(" left join ");
				tempCondition = generatorConditionStatement(tempClass, aliasName, (List<KeyValue<String, Object>>) class2Fields.get(classFieldKeyValue).get("fields"), matchExact);
				conditionStatement.append(" ");
				conditionStatement.append(tempCondition);
				if (!tempCondition.trim().isEmpty() && classIterator.hasNext())
					conditionStatement.append(" and ");
			}
			classAlias = class2Alias.get(rootClassFieldKeyValue);

			query.append(fromStatementBuffer);
			query.append(conditionStatement);
		} else {
			classAlias = entityClass.getSimpleName() + "_0";
			query.append(" from ");
			query.append(entityClass.getSimpleName());
			query.append(" as ");
			query.append(classAlias);
		}

		// 附加查询条件
		if (extendedHql != null && !extendedHql.trim().isEmpty()) {
			if (conditions != null && conditions.size() > 0) {
				query.append(" and ");
			} else {
				query.append(" where ");
			}
			query.append(extendedHql);
		}

		

		// 查询数据总条数
		if (queryCondition.getPage() != null||justQueryDataCount) {
			String countHql="";
			if(isNeedDistinct==true){
				countHql= "select count(distinct " + classAlias + ") " + query.toString();
			}else{
				//使用复合主键时，hibernate生成的查询语句为distinct(key1,key2)，Mysql报错：Operand should contain 1 column(s),modified by:czhang
				countHql= "select count(*) " + query.toString();
			}			
			Long count = this.executeCountHql(countHql);
			result.setRecords(count);
		}
		if(justQueryDataCount)
			return result;
		
		// 构造投影查询的字段列表
		boolean canUseProjectQuery=true;
		StringBuilder fieldStringBuilder = new StringBuilder("select distinct ");
		if(fields!=null){
			for(int index=0;index<fields.size();index++){
				String field = fields.get(index);
				if(field.contains(".")){
					canUseProjectQuery=false;
					break;
				}
				if(this.isTransient(entityClass, field)||this.getFieldClass(entityClass, field).isAssociationType())
					canUseProjectQuery=false;
				fieldStringBuilder.append(classAlias).append(".").append(field);
				if(index!=fields.size()-1){
					fieldStringBuilder.append(",");
				}
			}
		}
		
		
		String orderByHql="";
		if(fields==null|| fields.isEmpty()||!canUseProjectQuery){// 使用子查询查询出符合条件的对象
			String tempAlias = classAlias+"_temp";
			StringBuilder tempHql=new StringBuilder("select ");
			tempHql.append(tempAlias).append(" from ").append(entityClass.getSimpleName()).append(" as ").append(tempAlias).append(" where ").append(tempAlias).append(".id in (select distinct ")
			.append(classAlias).append(".id ");
			query.insert(0, tempHql.toString());
			query.append(") ");
			orderByHql=this.generateOrderByHql(orders, tempAlias,entityClass);
			query.append(orderByHql);
			
		}else{ // 投影查询
			query.insert(0, fieldStringBuilder.toString());
			orderByHql=this.generateOrderByHql(orders, classAlias,entityClass);
			query.append(orderByHql);
		}
		final String dataHql = query.toString();
		List<?> dataSet = this.hibernateTemplate.executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query queryObject = session.createQuery(dataHql);
				Integer page = queryCondition.getPage();
				Integer numPerPage = queryCondition.getNumPerPage();
				if (page != null && page > 0 && numPerPage != null && numPerPage > 0) {
					queryObject.setFirstResult((page - 1) * numPerPage);
					queryObject.setMaxResults(numPerPage);
				}
				return queryObject.list();
			}
		});
		List<Object> resultData = new ArrayList<Object>();
		if (fields == null || fields.size() == 0) { // 返回实体对象列表
			result.setRows(dataSet);
		} else { 
			if(canUseProjectQuery){// 仅查询指定的字段
				for (Object tempRowData : dataSet) {
					List<String> data = new ArrayList<String>();
					if(tempRowData instanceof Object[]){
						for(Object tempFieldData:(Object[])tempRowData){
							data.add(CommonUtil.formatData(tempFieldData));
						}
					}else{
						data.add(tempRowData==null?null:tempRowData.toString());
					}
					resultData.add(data);
				}
			}else{
				for (Object object : dataSet) {
					List<String> data = new ArrayList<String>();
					for (String key : queryCondition.getFields()) {
						try {
							if(key.contains(".")){
								int index = key.lastIndexOf(".");
								Object tempObject = PropertyUtils.getProperty(object, key.substring(0, index));
								if(tempObject instanceof Set){
									StringBuffer sb = new StringBuffer();
									for(Object o:(Set<?>)tempObject){
										if(!sb.toString().isEmpty())
											sb.append(",");
										sb.append(CommonUtil.formatData(PropertyUtils.getProperty(o, key.substring(index+1))));
									}
									data.add(sb.toString());
								}else{
									data.add(CommonUtil.formatData(PropertyUtils.getProperty(object, key)));
								}
							}else{
								data.add(CommonUtil.formatData(PropertyUtils.getProperty(object, key)));
							}
						} catch (Exception e) {
							// e.printStackTrace();
							data.add(null);
						}
					}
					resultData.add(data);
				}
			}
			result.setRows(resultData);
		}

		result.setPage(queryCondition.getPage());
		return result;
	}
	
	private String generateOrderByHql(Map<String, Boolean> orders,String classAlias,Class<?> entityClass){
		boolean isMysql=((SessionFactoryImplementor)this.sessionFactory).getSettings().getDialect() instanceof MySQLDialect;
		// 构造排序语句
		StringBuffer orderBy = new StringBuffer();
		if (orders != null && orders.size() > 0) {
			orderBy.append(" order by ");
			Iterator<Map.Entry<String, Boolean>> ite = orders.entrySet().iterator();
			while (ite.hasNext()) {
				Map.Entry<String, Boolean> entity = ite.next();
				String propertyName=entity.getKey();
				Type propertyType=this.getFieldClass(entityClass, propertyName);
				boolean isString=propertyType instanceof StringType || propertyType instanceof MaterializedClobType;
				if(isMysql&&isString){
					orderBy.append("gbk(");
				}
				if (classAlias != null) {
					orderBy.append(classAlias);
					orderBy.append(".");
				}
				orderBy.append(propertyName);
				if(isMysql&&isString){
					orderBy.append(")");
				}
				orderBy.append(entity.getValue() ? " asc" : " desc");
				if (ite.hasNext())
					orderBy.append(",");
			}
		}
		return orderBy.toString();
		
	}
	
	private boolean isTransient(Class<?> entityClass,String propertyName){
		try{
			PropertyDescriptor propertyDescriptor =PropertyUtils.getPropertyDescriptor(entityClass.newInstance(), propertyName);
			if(propertyDescriptor==null){
				return false;
			}
			Method method = PropertyUtils.getReadMethod(propertyDescriptor);
			if(method.getAnnotation(Transient.class)!=null)
				return true;
			else
				return false;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	

}
