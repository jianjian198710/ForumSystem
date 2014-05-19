package com.jianjian.dao;


import org.springframework.stereotype.Repository;

import com.jianjian.domain.Topic;


/**
 * topic ��DAO��
 *
 */
@Repository
public class TopicDao extends BaseDao<Topic> {
	
	private final String GET_BOARD_DIGEST_TOPICS = "from Topic t where t.boardId = ? and digest > 0 order by t.lastPost desc,digest desc";
	
	private final String GET_PAGED_TOPICS = "from Topic where board_id = ? order by lastPost desc";
	
	private final String QUERY_TOPIC_BY_TITILE = "from Topic  where topicTitle like ? order by lastPost desc";

	/**
	 * ��ȡ��̳���ĳһҳ�ľ����������������ظ�ʱ���Լ��������� ��������
	 * @param boardId ��̳���ID
	 * @return ����̳�µ����о���������
	 */
	public Page getBoardDigestTopics(int boardId,int pageNo,int pageSize){
	    return pagedQuery(GET_BOARD_DIGEST_TOPICS,pageNo,pageSize,boardId);
	}  

	/**
	 * ��ȡ��̳����ҳ����������
	 * @param boardId ��̳���ID
	 * @param pageNo ҳ�ţ���1��ʼ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @return ������ҳ��Ϣ��Page����
	 */
	public Page getPagedTopics(int boardId,int pageNo,int pageSize) {
		return pagedQuery(GET_PAGED_TOPICS,pageNo,pageSize,boardId);
	}
   
	/**
	 * ���������������ѯ����ģ��ƥ���������
	 * @param title ����Ĳ�ѯ����
	 * @param pageNo ҳ�ţ���1��ʼ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @return ������ҳ��Ϣ��Page����
	 */
	public Page queryTopicByTitle(String title, int pageNo, int pageSize) {
		return pagedQuery(QUERY_TOPIC_BY_TITILE,pageNo,pageSize,title);
	}
}
