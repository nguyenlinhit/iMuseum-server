package com.tma.imuseum.model.dao;

import java.util.List;
import java.util.Map;

import com.tma.imuseum.model.pojo.Artifact;


public interface ArtifactDAO {

    Artifact find(int id);

    Artifact create(Artifact artifact);

    void remove(int id);

    void edit(Artifact artifact);

    void flush();

    void close();

    // Get pagination
    // page: page number, page < 0 function will get all element
    // num: maximum element per page
    List<Artifact> getList(int page, int num, Integer active, String search);

    List<Artifact> findAllArtifact();
    // need to add filter
    List<Artifact> callSP(String spName, Map<String, Object> inputValue);
}