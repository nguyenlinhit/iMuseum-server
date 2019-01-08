package com.tma.imuseum.model.dao;

import java.util.List;

import com.tma.imuseum.model.pojo.Survey;


public interface SurveyDAO {

    Survey find(int id);

    void create(Survey survey);

    void remove(int id);


    // Get pagination
    // page: page number, page < 0 function will get all element
    // num: maximum element per page
    List<Survey> getList(int page, int num, int active, String search);

    // need to add filter
}