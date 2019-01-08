package com.tma.imuseum.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;

import com.tma.imuseum.model.pojo.Location;

@Repository("LocationDAO")
public class LocationDAOImpl extends DAO<Integer, Location> implements LocationDAO {
    private void proxyInitialize(Location temp) {
        if (null != temp) {
            Hibernate.initialize(temp.getBeacons());
            Hibernate.initialize(temp.getArtifacts());
        }
    }

    @Override
    public Location find(int id) {
        Location temp = getByKey(id);
        proxyInitialize(temp);
        return temp;
    }

    @Override
    public void create(Location location) {
        persist(location);
    }

    @Override
    public void remove(int id) {
        deleteById(id);
    }

    @Override
    public void edit(Location location) {
        update(location);
    }

    @Override
    public List<Location> getList(int page, int num, int active, String search) {
        List<Location> temps = createEntityCriteria(page, num, active);
        for (Location temp : temps) {
            proxyInitialize(temp);
        }
        return temps;
    }

    @Override
    public List<Location> callSP(String spName, Map<String, Object> inputValue) {
        return callSPDAO(spName, inputValue);
    }
}