package oops.saturn.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Repository;

import oops.saturn.dao.EntityDao;
import oops.saturn.model.BaseEntity;

@Repository("entityDao")
public class EntityDaoImpl extends AbstractGenericDaoImpl<BaseEntity, Long> implements EntityDao {
	
	/**
	 * 设置hibernate会话工厂
	 * 
	 * @param sessionFactory
	 */
	@Resource(name = "sessionFactory")
	@Required
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
}
