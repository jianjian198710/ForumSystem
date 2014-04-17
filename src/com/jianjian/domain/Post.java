package com.jianjian.domain;

import java.util.Date;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name="t_post")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="post_type",discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("2")
public class Post extends BaseDomain{
	
	private int postId;
	private String postTitle;
	private String postText;
	private int boardId;
	private Date craeteTime;
	private User user;
	
	
}
