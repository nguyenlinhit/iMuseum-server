package com.tma.imuseum.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;

import com.tma.imuseum.model.pojo.Artifact;
import com.tma.imuseum.utils.Search;

@Repository("ArtifactDAO")
public class ArtifactDAOImpl extends DAO<Integer, Artifact> implements ArtifactDAO {

    private static List<Artifact> artifacts;
    Search searchTool = new Search();

    private void proxyInitialize(Artifact temp) {
        if (null != temp) {
            Hibernate.initialize(temp.getsurveys());
            Hibernate.initialize(temp.getBeaconArtifact());
            Hibernate.initialize(temp.getCategoryArtifact());
            Hibernate.initialize(temp.getLocationArtifact());
            Hibernate.initialize(temp.getMedias());
            Hibernate.initialize(temp.getUserArtifact());
        }
    }

    @Override
    public Artifact find(int id) {
        Artifact temp = getByKey(id);
        proxyInitialize(temp);
        return temp;
    }

    @Override
    public Artifact create(Artifact artifact) {
        persist1(artifact);
        //artifact.getIdArtifact();
        return artifact;
    }

    @Override
    public void remove(int id) {
        deleteById(id);
    }

    @Override
    public void edit(Artifact artifact) {
        update(artifact);
    }

    @Override
    public void flush() {
        flushSession();
    }

    @Override
    public void close() {
        closeSession();
    }

    @Override
    public List<Artifact> getList(int page, int num, Integer active, String search) {
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
        parameters.put("beacon", searchKey.get("beacon"));
        parameters.put("user", searchKey.get("user"));
        parameters.put("category", searchKey.get("category"));

        List<Artifact> temps = callSP("spListArtifactAdvance", parameters);

        return temps;
    }

    @Override
    public List<Artifact> findAllArtifact() {
        return artifacts;
    }

    @Override
    public List<Artifact> callSP(String spName, Map<String, Object> inputValue) {
        List<Artifact> temps = callSPDAO(spName, inputValue);

        // Add proxy to object
        for (Artifact temp : temps) {
            proxyInitialize(temp);
        }
        return temps;
    }
}