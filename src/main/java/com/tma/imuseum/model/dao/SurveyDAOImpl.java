package com.tma.imuseum.model.dao;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;

import com.tma.imuseum.model.pojo.Survey;

@Repository("SurveyDAO")
public class SurveyDAOImpl extends DAO<Integer, Survey> implements SurveyDAO {

    private void proxyInitialize(Survey temp) {
        if (null != temp) {
            Hibernate.initialize(temp.getArtifactSurvey());
        }
    }

    @Override
    public Survey find(int id) {
        Survey temp = getByKey(id);
        proxyInitialize(temp);
        return temp;
    }

    @Override
    public void create(Survey survey) {
        persist(survey);
    }

    @Override
    public void remove(int id) {
        deleteById(id);
    }

    @Override
    public List<Survey> getList(int page, int num, int active, String search) {
        List<Survey> temps = createEntityCriteria(page, num, active);
        for (Survey temp : temps) {
            proxyInitialize(temp);
        }
        return temps;
    }
}