package com.tma.imuseum.model.dao;

import com.tma.imuseum.model.pojo.Notification;

import java.util.List;
import java.util.Map;

public interface NotifiationDAO {
    Notification find(String id);

    void create(Notification notification);

    void remove(String id);

    void edit(Notification notification);

    // Get pagination
    // page: page number, page < 0 function will get all element
    // num: maximum element per page
    List<Notification> getList(int page, int num, int active, String search);

    // need to add filter
    List<Notification> callSP(String spName, Map<String, Object> inputValue);
}
