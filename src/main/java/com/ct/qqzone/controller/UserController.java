package com.ct.qqzone.controller;

import com.ct.qqzone.pojo.Topic;
import com.ct.qqzone.pojo.UserBasic;
import com.ct.qqzone.service.TopicService;
import com.ct.qqzone.service.UserBasicService;

import javax.servlet.http.HttpSession;
import java.util.List;

public class UserController {

    private UserBasicService userBasicService =null;
    private TopicService topicService = null;

    public String login(String loginId, String pwd, HttpSession session){
        //1.登陆验证
        UserBasic userBasic = userBasicService.login(loginId, pwd);
        if(userBasic!=null){
            List<UserBasic> friendList = userBasicService.getFriendList(userBasic);
            List<Topic> topicList = topicService.getTopicList(userBasic);

            userBasic.setFriendList(friendList);
            userBasic.setTopicList(topicList);

            session.setAttribute("userBasic",userBasic);
            session.setAttribute("friend",userBasic);
            return "index";
        }else {
            return "login";
        }
    }

    public String friend(Integer id , HttpSession session){
        UserBasic currFriend = userBasicService.getUserBasicById(id);
        List<Topic> topicList = topicService.getTopicList(currFriend);
        currFriend.setTopicList(topicList);

        session.setAttribute("friend",currFriend);

        return "index";
    }
}
