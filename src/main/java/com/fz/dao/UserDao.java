package com.fz.dao;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.fz.model.User;

@Repository("userDao")
public class UserDao {

	@Resource
	private  SessionFactory sessionFactory;
	
	private Session getSession(){
		return sessionFactory.getCurrentSession();
	}
	
	public Integer save(User user){
	/*	User user1 = new User();
		this.getSession().save(user);
		this.getSession().update(user);
		this.getSession().saveOrUpdate(user);
		this.getSession().delete(user);
		
		User user2 = (User)this.getSession().get(User.class, 1);
		User user3 = (User)this.getSession().load(User.class, 1);*/
		
		return (Integer)this.getSession().save(user);
	}
	
	
	public User getUserByName(String name){
		String hql = "from User where name=?";
		Query query = this.getSession().createQuery(hql);
		query.setString(0, name);
		return (User)query.uniqueResult();
	}
	
}
