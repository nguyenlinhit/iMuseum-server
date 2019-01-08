package com.tma.imuseum.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.tma.imuseum.model.pojo.Beacon;


public interface BeaconDAO {

    Beacon find(int id);

    void create(Beacon beacon);

    void remove(int id);

    void edit(Beacon beacon);

    // Get pagination
    // page: page number, page < 0 function will get all element
    // num: maximum element per page
    List<Beacon> getList(int page, int num, Integer active, String search);

    // need to add filter

    List<Beacon> callSP(String spName, Map<String, Object> inputValue);
}