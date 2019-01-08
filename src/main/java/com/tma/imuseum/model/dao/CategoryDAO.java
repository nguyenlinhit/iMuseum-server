package com.tma.imuseum.model.dao;

import java.util.List;

import com.tma.imuseum.model.pojo.Category;


public interface CategoryDAO {

    Category find(int id);

    void create(Category category);

    void remove(int id);

    void edit(Category category);

    // Get pagination
    // page: page number, page < 0 function will get all element
    // num: maximum element per page
    List<Category> getList(int page, int num, int active, String search);


    // need to add filter
}