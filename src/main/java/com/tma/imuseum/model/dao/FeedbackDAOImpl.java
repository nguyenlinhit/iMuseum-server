package com.tma.imuseum.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.tma.imuseum.model.pojo.Feedback;

@Repository("FeedbackDAO")
public class FeedbackDAOImpl extends DAO<Integer, Feedback> implements FeedbackDAO {

    @Override
    public Feedback find(int id) {
        return getByKey(id);
    }

    @Override
    public void create(Feedback feedback) {
        persist(feedback);
    }

    @Override
    public void remove(int id) {
        deleteById(id);
    }

    public void edit(Feedback feedback) {
        update(feedback);
    }

    @Override
    public List<Feedback> getList(int page, int num, int active, String search) {
        Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put("p", page);
        parameters.put("n", num);
        parameters.put("status", active);
        parameters.put("category", search);

        return callSP("spGetListFeedback", parameters);
    }
    
    @Override
    public List<Feedback> callSP(String spName, Map<String, Object> inputValue){
        return callSPDAO(spName, inputValue);
    }
}