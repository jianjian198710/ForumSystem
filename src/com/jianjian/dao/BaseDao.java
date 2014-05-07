package com.jianjian.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.util.Assert;

public class BaseDao<T>{
	
	private Class<T> entityClass;
	@Autowired
	private HibernateTemplate hibernateTemplate;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BaseDao(){
		Type genType = getClass().getGenericSuperclass();
		//返回T的实际类型
		Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
		entityClass = (Class)params[0];
	}
	
	public T load(Serializable id){
		return (T)getHibernateTemplate().load(entityClass, id);
	}
	
	public T get(Serializable id){
		return (T)getHibernateTemplate().get(entityClass, id);
	}

	public List<T>loadAll(){
		return getHibernateTemplate().loadAll(entityClass);
	}
	
	public void save(T entity){
		getHibernateTemplate().save(entity);
	}
	
	public void remove(T entity){
		getHibernateTemplate().delete(entity);
	}
	
	public void update(T entity){
		getHibernateTemplate().update(entity);
	}
	
	@SuppressWarnings("rawtypes")//压制List缺泛型的警告
	public List find(String hql){
		return this.getHibernateTemplate().find(hql);
	}
	
	@SuppressWarnings("rawtypes")
	public List find(String hql, Object...params){
		return this.getHibernateTemplate().find(hql, params);
	}
	
	//对延迟加载的实体PO执行初始化
	public void initialize(Object entity){
		this.getHibernateTemplate().initialize(entity);
	}
	
	public Page pagedQuery(String hql, int pageNo, int pageSize, Object... values) {
		Assert.hasText(hql);
		Assert.isTrue(pageNo >= 1, "pageNo should start from 1");
		// Count查询,获取所有数量
		String countQueryString = " select count (*) " + removeSelect(removeOrders(hql));
		@SuppressWarnings("rawtypes")
		List countlist = getHibernateTemplate().find(countQueryString, values);
		long totalCount = (Long) countlist.get(0);

		if (totalCount < 1)
			return new Page();
		// 实际查询返回分页对象
		int startIndex = Page.getStartOfPage(pageNo, pageSize);
		Query query = createQuery(hql, values);
		//获取当前页的数据
		@SuppressWarnings("rawtypes")
		List list = query.setFirstResult(startIndex).setMaxResults(pageSize).list();

		return new Page(startIndex, totalCount, pageSize, list);
	}

	public Query createQuery(String hql, Object... values) {
		Assert.hasText(hql);
		Query query = getSession().createQuery(hql);
		for (int i = 0; i < values.length; i++) {
			query.setParameter(i, values[i]);
		}
		return query;
	}
	
	private static String removeSelect(String hql){
		Assert.hasText(hql);
		int beginPos = hql.toLowerCase().indexOf("from");
		Assert.isTrue(beginPos!=1,"hql:"+hql+"must has a keyword from");
		return hql.substring(beginPos);
	}
	
	private static String removeOrders(String hql){
		Assert.hasText(hql);
		Pattern pattern = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*",Pattern.CASE_INSENSITIVE);
		Matcher m = pattern.matcher(hql);
		StringBuffer sb = new StringBuffer();
		while(m.find()){
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}
	
	public HibernateTemplate getHibernateTemplate(){
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate){
		this.hibernateTemplate = hibernateTemplate;
	}
	
    public Session getSession() {
    	return getHibernateTemplate().getSessionFactory().openSession();
    }
}
