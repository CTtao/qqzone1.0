package com.ct.qqzone.controller;

import com.ct.qqzone.pojo.Reply;
import com.ct.qqzone.pojo.Topic;
import com.ct.qqzone.pojo.UserBasic;
import com.ct.qqzone.service.ReplyService;

import javax.servlet.http.HttpSession;
import java.util.Date;

public class ReplyController {
    private ReplyService replyService;

    public String addReply(String content,Integer topicId, HttpSession session){
        UserBasic author = (UserBasic) session.getAttribute("userBasic");
        Reply reply = new Reply(content,new Date(),author,new Topic(topicId));
        replyService.addReply(reply);

        return "redirect:topic.do?operate=topicDetail&id="+topicId;
    }
    public String delReply(Integer replyId,Integer topicId){
        replyService.delReply(replyId);
        return "redirect:topic.do?operate=topicDetail&id="+topicId;
    }
}
