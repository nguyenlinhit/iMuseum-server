package com.tma.imuseum.model.dao;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;

import com.tma.imuseum.model.pojo.Category;

@Repository("CategoryDAO")
public class CategoryDAOImpl extends DAO<Integer, Category> implements CategoryDAO {

    private void proxyInitialize(Category temp) {
        if (null != temp) {
            Hibernate.initialize(temp.getArtifacts());
        }
    }

    @Override
    public Category find(int id) {
        Category temp = getByKey(id);
        proxyInitialize(temp);
        return temp;
    }

    @Override
    public void create(Category category) {
        persist(category);
    }

    @Override
    public void remove(int id) {
        deleteById(id);
    }

    @Override
    public void edit(Category category) {
        update(category);
    }

    @Override
    public List<Category> getList(int page, int num, int active, String search) {
        List<Category> temps = createEntityCriteria(page, num, active);
        for (Category temp : temps) {
            proxyInitialize(temp);
        }
        return temps;
    }
}