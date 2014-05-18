package com.jianjian.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Page implements Serializable{
	private static final long serialVersionUID = -5294023503623329849L;
	
	private static int DEFAULT_PAGE_SIZE = 20;
	private int pageSize = DEFAULT_PAGE_SIZE;
	private long start;//��ǰҳ��һ������������List�е�λ��,��0��ʼ
	private List data;//��ǰҳ�д�ŵļ�¼
	private long totalCount;//�ܼ�¼��
	
	public Page(){
		this(0,0,DEFAULT_PAGE_SIZE,new ArrayList());
	}
	
	public Page(long start, long totalCount, int pageSize,List data){
		this.pageSize = pageSize;
		this.start = start;
		this.totalCount = totalCount;
		this.data =data;
	}
	
	//�ܹ��ж���ҳ
	public long getTotalPageCount(){
		if(totalCount % pageSize ==0){
			return totalCount/pageSize;
		}else{
			return totalCount/pageSize + 1;
		}
	}
	
	//��ǰ�Ƕ���ҳ
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
	 * ��ȡ��һҳ��һ�����������ݼ�List�е�λ��(��0��ʼ)��ÿҳ����ʹ��Ĭ��ֵ.
	 *
	 * @see #getStartOfPage(int,int)
	 */
	protected static int getStartOfPage(int pageNo) {
		return getStartOfPage(pageNo, DEFAULT_PAGE_SIZE);
	}

	/**
	 * ��ȡ��һҳ��һ�����������ݼ���λ��.
	 *
	 * @param pageNo   ��1��ʼ��ҳ��
	 * @param pageSize ÿҳ��¼����
	 * @return ��ҳ��һ������
	 */
	public static int getStartOfPage(int pageNo, int pageSize) {
		return (pageNo - 1) * pageSize;
	}
}
