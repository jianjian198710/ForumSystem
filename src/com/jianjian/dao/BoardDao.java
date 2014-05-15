package com.jianjian.dao;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.jianjian.domain.Board;

@Repository
public class BoardDao extends BaseDao<Board>{
	private final String GET_BOARD_NUM = "select count(f.boardId) from Board f";
	private final String GET_ALL_BOARDS = "from Board";
	
	@SuppressWarnings("rawtypes")
	public long getBoardNum(){
		Iterator it = getHibernateTemplate().iterate(GET_BOARD_NUM);
		return ((Long)it.next());
	}
	
	@SuppressWarnings("unchecked")
	public List<Board> getAllBoards(){
		return (List<Board>)getHibernateTemplate().find(GET_ALL_BOARDS);
	}
}

