package com.ct.qqzone.service;

import com.ct.qqzone.pojo.Reply;
import com.ct.qqzone.pojo.Topic;

import java.util.List;

public interface ReplyService {
    //根据Topic的id获得回复列表
    List<Reply> getReplyListByTopicId(Integer topicId);

    void addReply(Reply reply);

    void delReply(Integer id);
    //删除指定日志下的所有回复
    void delReplyList(Topic topic);
}
