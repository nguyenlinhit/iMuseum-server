package com.tma.imuseum.model.dao;

import java.util.List;
import java.util.Map;

import com.tma.imuseum.model.pojo.Media;


public interface MediaDAO {

    Media find(int id);

    void create(Media media);

    void remove(int id);

    void edit(Media media);

    // Get pagination
    // page: page number, page < 0 function will get all element
    // num: maximum element per page
    List<Media> getList(int page, int num, Integer active, String search);

    // need to add filter
    List<Media> callSP(String spName, Map<String, Object> inputValue);
}