package com.ct.qqzone.service;

import com.ct.qqzone.pojo.HostReply;

public interface HostReplyService {
    HostReply getHostReplyByReplyId(Integer replyId);

    void delHostReply(Integer id);
}
