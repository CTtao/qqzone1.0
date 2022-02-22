package com.ct.qqzone.dao;

import com.ct.qqzone.pojo.HostReply;

public interface HostReplyDAO {
    //根据id查询关联的hostReply
    HostReply getHostReplyByReplyId(Integer replyId);

    void delHostReply(Integer id);
}
