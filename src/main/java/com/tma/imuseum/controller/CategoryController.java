package com.tma.imuseum.controller;

import com.tma.imuseum.model.dao.CategoryDAO;
import com.tma.imuseum.model.pojo.Artifact;
import com.tma.imuseum.model.pojo.Category;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryDAO dao;

    @Autowired
    public CategoryController(CategoryDAO dao) {
        this.dao = dao;
    }

    // Get list of category and paging, default page is 1st, exp: domain/api/category?page=1
    @RequestMapping(method = RequestMethod.GET)
    public List<Category> listCategory(@RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "num", defaultValue = "10") int num,
            @RequestParam(value = "a", defaultValue = "1") int active,
            @RequestParam(value = "s", defaultValue = "") String search) {
        // If want to get all list, set page < 0
        return dao.getList(page, num, active, search);
    }

    // Create new category
    @RequestMapping(method = RequestMethod.POST)
    public String create(@RequestBody String request) {
        String message = "";
        Category category = new Category();

        try {
            JSONObject obj = new JSONObject(request);
            String name = obj.getString("nameCategory");
            // Check null or more, if not pass throw the message type of ERROR
            if (null == name) {
                message += "Category name has null value;/r/n";
            } else {
                category.setNameCategory(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Throw ERROR message to user side if message != ""
        if ("".equals(message)) {
            dao.create(category);
            //return "Success";
            return JSONObject.quote("Success");
        } else {
            return JSONObject.quote("Create failed: " + message);
        }
    }

    // Get category by Id
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Category findById(@PathVariable int id) {
        Category category = dao.find(id);
        if (null != category) {
            return category;
        } else {
            return null;
        }
    }

    // Delete category by Id
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    public String remove(@PathVariable int id) {
        // Check the category is exist
        Category temp = dao.find(id);
        if (null != temp) {
            dao.remove(id);
            return JSONObject.quote("Success");
        } else {
            return JSONObject.quote("Not found category with Id: " + id);
        }
    }
    //create category

    // Edit category, need to find category by Id then save all attribute to ignore save wrong Id 
    @RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
    public String edit(@PathVariable int id, @RequestBody String request) {
        // To save message return for validation
        String message = "";
        Category temp = dao.find(id);
        if (null != temp) {
            try {
                JSONObject obj = new JSONObject(request);
                String name = obj.getString("nameCategory");
                // Check null or more, if not pass throw the message type of ERROR
                if (null == name) {
                    message += "Category name has null value;/r/n";
                } else {
                    temp.setNameCategory(name);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Throw ERROR message to user side if message != ""
            if ("".equals(message)) {
                dao.edit(temp);
                return JSONObject.quote("Success");
            } else {
                return JSONObject.quote("Update failed: " + message);
            }
        } else {
            return JSONObject.quote("Update failed");
        }
    }

    //get artifact by id
    @RequestMapping(value = "/{id}/artifact", method = RequestMethod.GET)
    public Set<Artifact> getArtifacts(@PathVariable int id) {
        Set<Artifact> artifacts = dao.find(id).getArtifacts();
        if (artifacts != null){
            return artifacts;
        }else{
            return null;
        }
    }

}
