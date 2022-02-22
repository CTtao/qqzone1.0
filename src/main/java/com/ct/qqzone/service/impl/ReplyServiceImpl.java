package com.ct.qqzone.service.impl;

import com.ct.qqzone.dao.ReplyDAO;
import com.ct.qqzone.pojo.HostReply;
import com.ct.qqzone.pojo.Reply;
import com.ct.qqzone.pojo.Topic;
import com.ct.qqzone.pojo.UserBasic;
import com.ct.qqzone.service.HostReplyService;
import com.ct.qqzone.service.ReplyService;
import com.ct.qqzone.service.UserBasicService;

import java.util.List;

public class ReplyServiceImpl implements ReplyService {
    private ReplyDAO replyDAO;
    private HostReplyService hostReplyService;
    private UserBasicService userBasicService;
    @Override
    public List<Reply> getReplyListByTopicId(Integer topicId) {
        List<Reply> replyList = replyDAO.getReplyList(new Topic(topicId));
        for(int i = 0;i<replyList.size();i++){
            Reply reply = replyList.get(i);
            UserBasic author = userBasicService.getUserBasicById(reply.getAuthor().getId());
            reply.setAuthor(author);
            //2.将关联的hostReply加入进来
            HostReply hostReply = hostReplyService.getHostReplyByReplyId(reply.getId());
            reply.setHostReply(hostReply);
        }
        return replyList;
    }

    @Override
    public void addReply(Reply reply) {
        replyDAO.addReply(reply);
    }

    @Override
    public void delReply(Integer id) {
        //如果reply有关联的hostReply则先删除hostReply
        Reply reply = replyDAO.getReply(id);
        if(reply!=null){
            HostReply hostReply = hostReplyService.getHostReplyByReplyId(reply.getId());
            if(hostReply!=null){
                hostReplyService.delHostReply(hostReply.getId());
            }
            replyDAO.delReply(id);
        }
    }

    @Override
    public void delReplyList(Topic topic) {
        List<Reply> replyList = replyDAO.getReplyList(topic);
        if(replyList!=null){
            for(Reply reply : replyList){
                delReply(reply.getId());
            }
        }

    }

}
