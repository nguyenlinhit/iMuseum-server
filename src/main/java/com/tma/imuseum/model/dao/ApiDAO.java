package com.tma.imuseum.model.dao;

import java.util.List;

import com.tma.imuseum.model.pojo.Api;


public interface ApiDAO {

    Api find(int id);

    void create(Api api);

    void remove(int id);

    void edit(Api api);


    // Get pagination
    // page: page number, page < 0 function will get all element
    // num: maximum element per page
    List<Api> getList(int page, int num, int active, String search);

    // need to add filter
}