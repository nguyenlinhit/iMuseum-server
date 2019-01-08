package com.tma.imuseum.model.dao;

import java.util.List;
import java.util.Map;

import com.tma.imuseum.model.pojo.Feedback;


public interface FeedbackDAO {

    Feedback find(int id);

    void create(Feedback feedback);

    void remove(int id);

    void edit(Feedback feedback);

    // Get pagination
    // page: page number, page < 0 function will get all element
    // num: maximum element per page
    List<Feedback> getList(int page, int num, int active, String search);

    // need to add filter

     List<Feedback> callSP(String spName, Map<String, Object> inputValue);
}