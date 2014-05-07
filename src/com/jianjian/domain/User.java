package com.jianjian.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name="t_user")
public class User extends BaseDomain{
	private static final long serialVersionUID = 2723153790296038635L;
    /**
     *�����û���Ӧ��״ֵ̬ 
     */
    public static final int USER_LOCK = 1;
    /**
     * �û�������Ӧ��״ֵ̬
     */
    public static final int USER_UNLOCK = 0;
    /**
     * ����Ա����
     */
    public static final int FORUM_ADMIN = 1;
    /**
     * ��ͨ�û�����
     */
    public static final int NORMAL_USER = 0;
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="user_id")
	private int userId;
	@Column(name="user_name")
	private String userName;
	@Column(name="password")
	private String password;
	@Column(name="user_type")
	private int userType;
	@Column(name="locked")
	private int locked;
	@Column(name="credit")
	private int credit;
	@Column(name="last_visit")
	private Date lastVisit;
	@Column(name="last_ip")
	private String lastIp;
	
	//����İ��
    @ManyToMany(fetch = FetchType.LAZY)
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinTable(name = "t_board_manager", joinColumns = {@JoinColumn(name ="user_id" )},inverseJoinColumns = {@JoinColumn(name = "board_id")})
	private Set<Board> manBoards = new HashSet<Board>();
    
	public int getUserId(){
		return userId;
	}
	public void setUserId(int userId){
		this.userId = userId;
	}
	public String getUserName(){
		return userName;
	}
	public void setUserName(String userName){
		this.userName = userName;
	}
	public String getPassword(){
		return password;
	}
	public void setPassword(String password){
		this.password = password;
	}
	public int getUserType(){
		return userType;
	}
	public void setUserType(int userType){
		this.userType = userType;
	}
	public int getLocked(){
		return locked;
	}
	public void setLocked(int locked){
		this.locked = locked;
	}
	public int getCredit(){
		return credit;
	}
	public void setCredit(int credit){
		this.credit = credit;
	}
	public Date getLastVisit(){
		return lastVisit;
	}
	public void setLastVisit(Date lastVisit){
		this.lastVisit = lastVisit;
	}
	public String getLastIp(){
		return lastIp;
	}
	public void setLastIp(String lastIp){
		this.lastIp = lastIp;
	}
	public Set<Board> getManBoards(){
		return manBoards;
	}
	public void setManBoards(Set<Board> manBoards){
		this.manBoards = manBoards;
	}
}

