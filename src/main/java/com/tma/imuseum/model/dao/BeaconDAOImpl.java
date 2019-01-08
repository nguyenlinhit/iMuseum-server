package com.tma.imuseum.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tma.imuseum.model.pojo.Beacon;
import com.tma.imuseum.utils.Search;

@Transactional
@Repository("BeaconDAO")
public class BeaconDAOImpl extends DAO<Integer, Beacon> implements BeaconDAO {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    Search searchTool = new Search();

    private void proxyInitialize(Beacon beacon) {
        if (null != beacon) {
            Hibernate.initialize(beacon.getArtifacts());
            Hibernate.initialize(beacon.getLocationBeacon());
            Hibernate.initialize(beacon.getRequest());
            Hibernate.initialize(beacon.getUserBeacon());
        }
    }

    @Override
    public Beacon find(int id) {
        Beacon beacon = getByKey(id);
        if (null != beacon) {
            proxyInitialize(beacon);
        }
        return beacon;
    }

    @Override
    public void create(Beacon beacon) {
        persist(beacon);
    }

    @Override
    public void remove(int id) {
        deleteById(id);
    }

    @Override
    public void edit(Beacon beacon) {
        update(beacon);
    }

    @Override
    public List<Beacon> getList(int page, int num, Integer active, String search) {
        // Get all list        
        if (active < 0) {
            active = null;
        } else if (active > 1)
            active = 1;

        Map<String, String> searchKey = searchTool.searchOptimize(search);
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("numb", num);
        parameters.put("page", page - 1);
        parameters.put("isActive", active);
        parameters.put("normal", searchKey.get("normal"));
        parameters.put("location", searchKey.get("location"));
        parameters.put("artifact", searchKey.get("artifact"));
        parameters.put("user", searchKey.get("user"));

        List<Beacon> temps = callSP("spListBeaconAdvance", parameters);
        return temps;
    }

    @Override
    public List<Beacon> callSP(String spName, Map<String, Object> inputValue) {
        List<Beacon> temps = callSPDAO(spName, inputValue);

        for (Beacon temp : temps) {
            proxyInitialize(temp);
        }
        return temps;
    }
}
