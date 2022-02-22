package com.ct.qqzone.controller;

import com.ct.qqzone.pojo.Topic;
import com.ct.qqzone.pojo.UserBasic;
import com.ct.qqzone.service.TopicService;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

public class AddController {
    private TopicService topicService;

    public String addTopic(String title, String content, HttpSession session){
        UserBasic userBasic = (UserBasic) session.getAttribute("userBasic");
        Topic topic = new Topic(title,content,new Date(),userBasic);
        topicService.addTopic(topic);
        List<Topic> topicList = topicService.getTopicList(userBasic);
        Topic topic1 = topicList.get(topicList.size()-1);
        Integer topicId = topic1.getId();
        return "redirect:topic.do?operate=topicDetail&id="+topicId;
    }
}
