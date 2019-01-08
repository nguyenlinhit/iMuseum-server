package com.tma.imuseum.model.dao;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;

import com.tma.imuseum.model.pojo.User;
import com.tma.imuseum.utils.Search;

@Repository("UserDAO")
public class UserDAOImpl extends DAO<Integer, User> implements UserDAO {
    Search searchTool = new Search();

    private void proxyInitialize(User temp) {
        if (null != null) {
            Hibernate.initialize(temp.getArtifacts());
            Hibernate.initialize(temp.getBeacons());
        }
    }

    @Override
    public User find(int id) {
        User temp = getByKey(id);
        proxyInitialize(temp);
        return temp;
    }

    @Override
    public void create(User User) {
        persist(User);
    }

    @Override
    public void remove(int id) {
        deleteById(id);
    }

    @Override
    public void edit(User User) {
        update(User);
    }

    @Override
    public List<User> getList(int page, int num, Integer active, String search) {
        if (active < 0) {
            active = null;
        } else if (active > 1)
            active = 1;

        Map<String, String> searchKey = searchTool.searchOptimize(search);
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("numb", num);
        parameters.put("page", page -1);
        parameters.put("isActive", active);
        parameters.put("normal", searchKey.get("normal"));

        List<User> temps = callSP("spListUserAdvance", parameters);

        return temps;
    }

    public List<User> callSP(String spName, Map<String, Object> inputValue) {
        List<User> temps = callSPDAO(spName, inputValue);

        // Add proxy to object
        for (User temp : temps) {
            proxyInitialize(temp);
        }
        return temps;
    }
}