package com.ct.qqzone.dao.impl;

import com.ct.myssm.basedao.BaseDAO;
import com.ct.qqzone.dao.HostReplyDAO;
import com.ct.qqzone.pojo.HostReply;

public class HostReplyDAOImpl extends BaseDAO<HostReply> implements HostReplyDAO {
    @Override
    public HostReply getHostReplyByReplyId(Integer replyId) {
        return load("select * from t_host_reply where reply = ?",replyId);
    }

    @Override
    public void delHostReply(Integer id) {
        executeUpdate("delete from t_host_reply where id = ?",id);
    }
}
