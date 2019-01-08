package com.tma.imuseum.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.tma.imuseum.model.pojo.Location;

public interface LocationDAO {

    Location find(int id);

    void create(Location location);

    void remove(int id);

    void edit(Location location);

    // Get pagination
    // page: page number, page < 0 function will get all element
    // num: maximum element per page
    List<Location> getList(int page, int num, int active, String search);

    // need to add filter
    List<Location> callSP(String spName, Map<String, Object> inputValue);
}