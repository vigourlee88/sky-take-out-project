package com.bjpowernode.crm.settings.service;

import java.util.List;
import java.util.Map;

import com.bjpowernode.crm.settings.domain.User;

public interface UserService {
	User queryUserByLoginActAndPwd(Map<String, Object> map);

	List<User> queryAllUsers();
}
