package com.tma.imuseum.model.dao;

import com.tma.imuseum.model.pojo.Notification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("NotificationDAO")
public class NotificationDAOImpl extends DAO<String, Notification> implements NotifiationDAO{

    @Override
    public Notification find(String id) {
        Notification temp = getByKey(id);
        return temp;

    }

    @Override
    public void create(Notification notification) {
        persist(notification);
    }

    @Override
    public void remove(String id) {
        deleteById(id);
    }

    @Override
    public void edit(Notification notification) {
        update(notification);
    }

    @Override
    public List<Notification> getList(int page, int num, int active, String search) {
        List<Notification> temps = createEntityCriteria(page, num, active);

        return temps;
    }

    @Override
    public List<Notification> callSP(String spName, Map<String, Object> inputValue) {
        return callSPDAO(spName,inputValue);
    }
}
