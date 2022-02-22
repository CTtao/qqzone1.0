package com.ct.qqzone.service;

import com.ct.qqzone.pojo.Topic;
import com.ct.qqzone.pojo.UserBasic;

import java.util.List;

public interface TopicService {
    //查询用户的日志列表
    List<Topic> getTopicList(UserBasic userBasic);

    Topic getTopicById(Integer id);

    //获取带有author的Topic
    Topic getTopic(Integer id);

    //删除特定id的topic
    void delTopic(Integer id);
    //添加指定的topic
    void addTopic(Topic topic);
}
