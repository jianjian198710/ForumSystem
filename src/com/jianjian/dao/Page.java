package com.jianjian.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Page implements Serializable{
	private static final long serialVersionUID = -5294023503623329849L;
	
	private static int DEFAULT_PAGE_SIZE = 20;
	private int pageSize = DEFAULT_PAGE_SIZE;
	private long start;//当前页第一条数据在整个List中的位置,从0开始
	private List data;//当前页中存放的记录
	private long totalCount;//总记录数
	
	public Page(){
		this(0,0,DEFAULT_PAGE_SIZE,new ArrayList());
	}
	
	public Page(long start, long totalCount, int pageSize,List data){
		this.pageSize = pageSize;
		this.start = start;
		this.totalCount = totalCount;
		this.data =data;
	}
	
	//总共有多少页
	public long getTotalPageCount(){
		if(totalCount % pageSize ==0){
			return totalCount/pageSize;
		}else{
			return totalCount/pageSize + 1;
		}
	}
	
	//当前是多少页
	public long getCurrentPageNum(){
		return start/pageSize + 1;
	}
	
	public boolean isHasNextPage(){
		return this.getCurrentPageNum()<this.getTotalPageCount();
	}
	
	public boolean isHasPreviousPage(){
		return this.getCurrentPageNum()>1;
	}
	
	/**
	 * 获取任一页第一条数据在数据集List中的位置(从0开始)，每页条数使用默认值.
	 *
	 * @see #getStartOfPage(int,int)
	 */
	protected static int getStartOfPage(int pageNo) {
		return getStartOfPage(pageNo, DEFAULT_PAGE_SIZE);
	}

	/**
	 * 获取任一页第一条数据在数据集的位置.
	 *
	 * @param pageNo   从1开始的页号
	 * @param pageSize 每页记录条数
	 * @return 该页第一条数据
	 */
	public static int getStartOfPage(int pageNo, int pageSize) {
		return (pageNo - 1) * pageSize;
	}
}
