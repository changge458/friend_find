package com.fz.action;

import java.io.IOException;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.fz.model.User;
import com.fz.service.UserService;

@Controller("userAction")
@Scope("prototype")
public class UserAction {

	private User user;
	
	@Resource
	private UserService userService;
	
	public void register(){
		
		System.out.println("用户名："+user.getName());
		System.out.println("密码："+user.getPass());
		
		int result = userService.insertUser(user);
		System.out.println("返回值："+result);
		
		if(result == -1){
			
			try {
				ServletActionContext.getResponse().getWriter().write("用戶名已注冊");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}



	
	
	
}
