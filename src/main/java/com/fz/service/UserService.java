package com.fz.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fz.dao.UserDao;
import com.fz.model.User;

@Service("userService")
public class UserService {

	@Resource
	private UserDao userDao;
	
	public int insertUser(User user){
		
		//先判断当前的用户名是否注册
		if(userDao.getUserByName(user.getName()) !=null){
			
			return -1;
		}
		
		
		return userDao.save(user);
	}
	
	
}
