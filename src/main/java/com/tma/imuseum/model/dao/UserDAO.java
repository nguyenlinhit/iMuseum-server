package com.tma.imuseum.model.dao;

import java.util.List;
import java.util.Map;
import com.tma.imuseum.model.pojo.User;


public interface UserDAO {

    User find(int id);

    void create(User user);

    void remove(int id);

    void edit(User user);

    // Get pagination
    // page: page number, page < 0 function will get all element
    // num: maximum element per page
    List<User> getList(int page, int num, Integer active, String search);

    List<User> callSP(String spName, Map<String, Object> inputValue);

    // need to add filter
}