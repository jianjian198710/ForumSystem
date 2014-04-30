package com.jianjian.dao;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.jianjian.domain.User;

/**
 * User����Dao
 */
@Repository
public class UserDao extends BaseDao<User> {
	private final String GET_USER_BY_USERNAME = "from User u where u.userName = ?";
	
    /**
     * �����û�����ѯUser����
     * @param userName �û���
     * @return ��ӦuserName��User������������ڣ�����null��
     */
	public User getUserByUserName(String userName){
		@SuppressWarnings("unchecked")
		List<User> users = (List<User>) getHibernateTemplate().find(GET_USER_BY_USERNAME, userName);
		if(users.size()==0){
			return null;
		}else{
			return users.get(0);
		}
    }
}
