package com.tma.imuseum.model.dao;

import java.util.Date;
import java.util.List;

import com.tma.imuseum.model.pojo.Request;


public interface RequestDAO {

    Request find(Date id);

    void create(Request request);

    void remove(Date id);

    void edit(Request request);


    // Get pagination
    // page: page number, page < 0 function will get all element
    // num: maximum element per page
    List<Request> getList(int page, int num, int active, String search);

    // need to add filter
}