package com.tma.imuseum.model.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;

import com.tma.imuseum.model.pojo.Request;

@Repository("RequestDAO")
public class RequestDAOImpl extends DAO<Date, Request> implements RequestDAO {

    private void proxyInitialize(Request temp) {
        if (null != null) {
            Hibernate.initialize(temp.getBeaconRequest());
        }
    }

    @Override
    public Request find(Date id) {
        Request temp = getByKey(id);
        proxyInitialize(temp);
        return temp;
    }

    @Override
    public void create(Request request) {
        persist(request);
    }

    @Override
    public void remove(Date id) {
        deleteById(id);
    }

    @Override
    public void edit(Request request) {
        update(request);
    }

    @Override
    public List<Request> getList(int page, int num, int active, String search) {
        List<Request> temps = createEntityCriteria(page, num, active);
        for (Request temp : temps) {
            proxyInitialize(temp);
        }
        return temps;
    }
}