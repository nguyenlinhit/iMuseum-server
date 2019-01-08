package com.tma.imuseum.model.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tma.imuseum.model.pojo.Log;

@Repository("LogDAO")
public class LogDAOImpl extends DAO<Integer, Log> implements LogDAO {

    @Override
    public Log find(int id) {
        return getByKey(id);
    }

    @Override
    public void create(Log log) {
        persist(log);
    }

    @Override
    public void remove(int id) {
        deleteById(id);
    }

    @Override
    public void edit(Log log) {
        update(log);
    }

    @Override
    public List<Log> getList(int page, int num, int active, String search) {
        List<Log> temp = createEntityCriteria(page, num, active);
        return temp;
    }
}