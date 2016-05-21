package oops.saturn.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

import oops.saturn.other.PageQueryCondition;
import oops.saturn.other.PageQueryResult;

/**
 * 数据库操作层通用接口
 *
 * @param <T> 实体类
 * @param <PK> 实体主键类型
 */
public interface GenericDao<T, PK extends Serializable> {
	
	/**
	 * 获取hibernate会话工厂
	 * @return hibernate会话工厂
	 */
    public SessionFactory getSessionFactory();
    
    /**
     * 获取hibernate数据库操作模板
     * @return hibernate数据库操作模板
     */
    public HibernateTemplate getHibernateTemplate();

    /**
     * 根据数据库编号删除某个实体
     * @param entityClass 待删除实体的类
     * @param id 数据库编号
     */
    public void delete(Class<? extends T> entityClass, PK id);
    
    /**
     * 删除某个实体
     * @param entity 待删除的实体对象
     */
    public void delete(T entity);
    
    /**
     * 根据数据库编号删除一组实体
     * @param entityClass 待删除实体的类
     * @param ids 数据库编号列表
     */
    public void delete(Class<? extends T> entityClass, List<PK> ids);
    
    /**
     * 删除一组实体
     * @param entities 待删除的实体对象列表
     */
    public void delete(List<? extends T> entities);
    
    /**
     * 根据数据库编号获取某个实体对象
     * @param entityClass 要获取的实体对象所属的类
     * @param id 数据库编号
     * @return 实体对象
     */
    public <E extends T> E get(Class<E> entityClass, PK id);
	
    /**
     * 根据查询条件获取模糊查询的实体对象列表
     * @param entityClass 实体类
     * @param conditions 查询条件
     * @return 实体对象列表
     */
    public <E extends T> List<E> get(Class<E> entityClass, Map<String, Object> conditions);
    
    /**
     * 根据查询条件获取实体对象列表
     * @param entityClass 实体类
     * @param conditions 查询条件
     * @param matchExact 是否精确匹配
     * @return 实体对象列表
     */
    public <E extends T> List<E> get(Class<E> entityClass, Map<String, Object> conditions, boolean matchExact);
    
    /**
     * 根据查询条件获取模糊查询的实体对象列表
     * @param entityClass 实体类
     * @param conditions 查询条件
     * @param orders 排序条件
     * @param matchExact 是否精确匹配
     * @return 实体对象列表
     */    
    public <E extends T> List<E> get(Class<E> entityClass, Map<String, Object> conditions, Map<String, Boolean> orders, boolean matchExact);
    
    /**
     * 获取实体类下的所有实体对象列表
     * @param entityClass 实体类
     * @return 实体对象列表
     */
    public <E extends T> List<E> getAll(Class<E> entityClass);
    
    /**
     * 根据实体对象的属性名值对进行模糊查询，返回查询到的实体对象列表
     * @param entityClass 实体类
     * @param key 实体对象的属性名称
     * @param value 实体对象的属性值
     * @return 实体对象列表
     */
    public <E extends T> List<E> get(Class<E> entityClass, String key, Object value);
    
    /**
     * 根据实体对象的属性名值对进行查询，返回查询到的实体对象列表
     * @param entityClass 实体类
     * @param key 实体对象的属性名称
     * @param value 实体对象的属性值
     * @param matchExact  是否精确匹配
     * @return 实体对象列表
     */
    public <E extends T> List<E> get(Class<E> entityClass, String key, Object value, boolean matchExact);
    
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
    public <E extends T> List<E> get(Class<E> entityClass, String propertyName, Object propertyValue, boolean matchExact, String orderPropertyName, boolean order);
   
    /**
     * 根据查询条件获取模糊查询结果
     * @param queryCondition 查询条件
     * @return 查询结果
     */
    public PageQueryResult get(PageQueryCondition queryCondition);
    
    /**
     * 根据查询条件获取查询结果
     * @param queryCondition 查询条件
     * @param matchExact 是否精确匹配
     * @return  查询结果
     */
    public PageQueryResult get(PageQueryCondition queryCondition, boolean matchExact);
    
	/**
	 * 根据查询条件获取查询结果，使用复合主键时调用
	 * @param queryCondition 查询条件
	 * @param matchExact 是否精确匹配
	 * @return 查询结果
	 */
	public PageQueryResult getComplexPK(PageQueryCondition queryCondition, boolean matchExact);
    
    /**
     * 新建或修改实体对象
     * @param entity 实体对象
     */
	public <E extends T> E saveOrUpdate(E entity);

    /**
     * 新建或修改实体对象,受影响的属性由第二个参数指定。
     * @param entity 实体对象
     * @param modifiedData 要保存的数据
     * @param isJson 表明第二个参数值是否为json数据
     * @return 保存后的实体对象
     */
    public <E extends T> E saveOrUpdate(E entity, Map<String, Object> modifiedData, boolean isJson);
    
    /**
     * 新建或修改实体对象
     * @param entity  实体对象
     * @param modifiedData 要保存的json数据
     * @return 保存后的实体对象
     */
    public <E extends T> E saveOrUpdate(E entity, Map<String, Object> modifiedData);
    
    /**
     * 新建或修改一组实体对象
     * @param entities
     * @param modifiedData 要保存的数据
     * @param isJson 表明第二个参数值是否为json数据
     * @return 保存后的实体对象列表
     */
    public <E extends T> List<E> saveAll(List<E> entities, List<Map<String, Object>> modifiedData, boolean isJson);
    
      /**
     * 获取当前实体类下的实体对象总数
     * @param entityClass 实体类
     * @return 实体对象总数
     */
    public Long getCount(Class<? extends T> entityClass);
    
    /**
	 * 获取数据的条数 
	 * @param entityClass   实体类
	 * @param conditions   查询条件
	 * @param matchExact   是否精确匹配
	 * @return 数据的条数
	 */
	public Long getCount(Class<? extends T> entityClass, Map<String, Object> conditions, boolean matchExact);
	
	/**
	 * 获取数据的条数 
	 * @param queryCondition 查询条件
	 * @param matchExact 是否精确匹配
	 * @return 数据的条数
	 */
	public Long getCount(PageQueryCondition queryCondition, boolean matchExact);
	
	/**
	 * 获取指定实体类的实体对象,如果没有实体对象则返回null
	 * @param entityClass 实体类
	 * @return 实体对象
	 */
	public <E extends T> E getOne(Class<E> entityClass);
	 
    /**
     * 根据查询条件获取模糊查询的实体对象,如果没有实体对象则返回null
     * @param entityClass 实体类
     * @param conditions 查询条件
     * @return 实体对象
     */
    public <E extends T> E getOne(Class<E> entityClass, Map<String, Object> conditions );
    
    /**
     * 根据查询条件获取实体对象,如果没有实体对象则返回null
     * @param entityClass 实体类
     * @param conditions 查询条件
     * @param matchExact 是否精确匹配
     * @return 实体对象
     */
    public <E extends T> E getOne(Class<E> entityClass, Map<String, Object> conditions, boolean matchExact);

    /**
     * 根据查询条件获取模糊查询的实体对象,如果没有实体对象则返回null
     * @param entityClass 实体类
     * @param conditions 查询条件
     * @param orders 排序条件
     * @param matchExact 是否精确匹配
     * @return 实体对象
     */    
    public <E extends T> E getOne(Class<E> entityClass, Map<String, Object> conditions, Map<String, Boolean> orders, boolean matchExact);
    
    /**
     * 根据实体对象的属性名值对进行模糊查询，返回查询到的实体对象,如果没有实体对象则返回null
     * @param entityClass 实体类
     * @param key 实体对象的属性名称
     * @param value 实体对象的属性值
     * @return 实体对象
     */
    public <E extends T> E getOne(Class<E> entityClass, String key, Object value);
    
    /**
     * 根据实体对象的属性名值对进行查询，返回查询到的实体对象,如果没有实体对象则返回null
     * @param entityClass 实体类
     * @param key 实体对象的属性名称
     * @param value 实体对象的属性值
     * @param matchExact  是否精确匹配
     * @return 实体对象
     */
    public <E extends T> E getOne(Class<E> entityClass, String key, Object value, boolean matchExact);
    
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
    public <E extends T> E getOne(Class<E> entityClass, String propertyName, Object propertyValue, boolean matchExact, String orderPropertyName, boolean order);
    
    /**
     * 执行更新语句
     * @param qs hql或sql
     * @param isNative true为原生sql，false为hql
     * @param params 参数
     * @return 执行结果
     */
    public int executeUpdate(String ql, boolean isNative, Object... params);
    
    /**
     * 执行更新语句
     * @param qs hql或sql
     * @param isNative true为原生sql，false为hql
     * @param params 参数
     * @return 执行结果
     */
    public int executeUpdate(String ql, boolean isNative, Map<String, Object> params);
    
	/**
     * 执行查询语句
     * @param qs hql或sql
     * @param isNative true为原生sql，false为hql
     * @param params 参数
     * @return 数据列表
     */
    public <E> List<E> executeQuery(String ql, boolean isNative, Object... params);
    
	/**
     * 执行查询语句
     * @param qs hql或sql
     * @param isNative true为原生sql，false为hql
     * @param params 参数
     * @return 数据列表
     */
    public <E> List<E> executeQuery(String ql, boolean isNative, Map<String, Object> params);
    
    /**
     * 获取表的字段名称列表
     * @param tableName 表名
     * @return 表的字段名称列表
     */
    public Set<String> getColumnNames(final String tableName);

    
}
