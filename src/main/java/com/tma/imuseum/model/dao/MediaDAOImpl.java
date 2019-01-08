package com.tma.imuseum.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;

import com.tma.imuseum.model.pojo.Media;
import com.tma.imuseum.utils.Search;

@Repository("MediaDAO")
public class MediaDAOImpl extends DAO<Integer, Media> implements MediaDAO {

    Search searchTool = new Search();

    private void proxyInitialize(Media temp) {
        if (null != temp) {
            Hibernate.initialize(temp.getArtifactMedia());
        }
    }

    @Override
    public Media find(int id) {
        Media temp = getByKey(id);
        proxyInitialize(temp);
        return temp;
    }

    @Override
    public void create(Media media) {
        persist(media);
    }

    @Override
    public void remove(int id) {
        deleteById(id);
    }

    @Override
    public void edit(Media media) {
        update(media);
    }

    @Override
    public List<Media> getList(int page, int num, Integer active, String search) {
        if (active < 0) {
            active = null;
        } else if (active > 1)
            active = 1;

        Map<String, String> searchKey = searchTool.searchOptimize(search);
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("page", page - 1);
        parameters.put("numb", num);
        parameters.put("normal", searchKey.get("normal"));
        parameters.put("beacon", searchKey.get("beacon"));
        parameters.put("artifact", searchKey.get("artifact"));

        List<Media> temps = callSP("spListMediaAdvance", parameters);
        return temps;
    }

    @Override
    public List<Media> callSP(String spName, Map<String, Object> inputValue) {
        List<Media> temps = callSPDAO(spName, inputValue);
        for (Media temp : temps) {
            proxyInitialize(temp);
        }
        return temps;
    }
}