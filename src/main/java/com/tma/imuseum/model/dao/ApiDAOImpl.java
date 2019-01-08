package com.tma.imuseum.model.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tma.imuseum.model.pojo.Api;

@Repository("ApiDAO")
public class ApiDAOImpl extends DAO<Integer, Api> implements ApiDAO {

    @Override
    public Api find(int id) {
        return getByKey(id);
    }

    @Override
    public void create(Api api) {
        persist(api);
    }

    @Override
    public void remove(int id) {
        deleteById(id);
    }

    @Override
    public void edit(Api api) {
        update(api);
    }

    @Override
    public List<Api> getList(int page, int num, int active, String search) {
        List<Api> temp = createEntityCriteria(page, num, active);
        return temp;
    }
}