package com.jianjian.dao;

import java.util.Iterator;

import org.springframework.stereotype.Repository;

import com.jianjian.domain.Board;

@Repository
public class BoardDao extends BaseDao<Board>{
	private final String GET_BOARD_NUM = "select count(f.boardId) from Board f";
	
	@SuppressWarnings("rawtypes")
	public long getBoardNum(){
		Iterator it = getHibernateTemplate().iterate(GET_BOARD_NUM);
		return ((Long)it.next());
	}
}
