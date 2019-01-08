package com.tma.imuseum.controller;

import com.tma.imuseum.model.dao.ApiDAO;
import com.tma.imuseum.model.pojo.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.json.JSONObject;
import java.util.List;

@RestController
@RequestMapping(value = "/api/api")
public class ApiController {
    @Autowired
    private ApiDAO apiDAO;

    //get Api by id
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Api findById(@PathVariable int id) {
        return apiDAO.find(id);
    }

    //Get list of category and paging, default page is 1st, exp: domain/api/api?page=1
    @RequestMapping(method = RequestMethod.GET)
    public List<Api> lstArtifact(@RequestParam(value = "page", defaultValue = "1") int page) {
        List<Api> artifacts = apiDAO.getList(page, 5);
        return artifacts;
    }

    //create Api
    @RequestMapping(method = RequestMethod.POST)
    public String create(@RequestBody Api api) {
        String message = "";

        // Check null or more, if not pass throw the message type of ERROR
        if (null == api.getGroup()) {
            message += "Group has null value;/r/n";
        }
        if (null == api.getApiName()) {
            message += "API name has null value;/r/n";
        }
        if (null == api.getApiLink()) {
            message += "API link has null value;/r/n";
        }
        if (null == api.getApiExample()) {
            message += "API exemple has null value;/r/n";
        }

        // Throw ERROR message to user side if message != ""
        if ("".equals(message)) {
            apiDAO.create(api);
           return JSONObject.quote("Success");
        } else {
            return JSONObject.quote("Create failed: " + message);
        }
    }

    //update Api by ID
    @RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
    public String edit(@PathVariable int id, @RequestBody Api api) {
        String message = "";
        Api temp = apiDAO.find(id);

        // Check null or more, if not pass throw the message type of ERROR
        if (null == api.getGroup()) {
            message += "Group has null value;/r/n";
        }
        if (null == api.getApiName()) {
            message += "API name has null value;/r/n";
        }
        if (null == api.getApiLink()) {
            message += "API link has null value;/r/n";
        }
        if (null == api.getApiExample()) {
            message += "API exemple has null value;/r/n";
        }
        if (null == temp) {
            message += "Api not exist";
        }
        // Throw ERROR message to user side if message != ""
        if ("".equals(message)) {
            apiDAO.edit(api);
            return JSONObject.quote("Success");
        } else {
            return JSONObject.quote("Update failed: " + message);
        }
    }

    //delete Api by ID
    public String delete(@PathVariable int id) {
        String message = "";
        Api temp = apiDAO.find(id);
        //int status = 0;
        try {
            Boolean status = false;
            temp.setIsActive(status);
            // Check null or more, if not pass throw the message type of ERROR
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Throw ERROR message to user side if message != ""
        if ("".equals(message)) {
            apiDAO.edit(temp);
            return JSONObject.quote("Success");
        } else {
            return JSONObject.quote("Delete failed: ");
        }
    }
    // @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    // public String delete(@PathVariable int id) {
    //     if (apiDAO.find(id) != null) {
    //         apiDAO.remove(id);
    //         return JSONObject.quote("Success");
    //     } else {
    //         return JSONObject.quote("Not found Api with Id: " + id);
    //     }
    // }
}