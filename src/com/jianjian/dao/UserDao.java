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
	private final String GET_ALL_USERS = "from User";
	private final String QUERY_USERS = "from User u where u.userName like ?";
	
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
	
	@SuppressWarnings("unchecked")
	public List<User> getAllUsers(){
		return (List<User>) getHibernateTemplate().find(GET_ALL_USERS);
	}
	
	@SuppressWarnings("unchecked")
	public List<User> queryAllUsers(String userName){
		StringBuffer sb = new StringBuffer();
		sb.append("%").append(userName).append("%");
		return (List<User>) getHibernateTemplate().find(QUERY_USERS, sb.toString());
	}
}
