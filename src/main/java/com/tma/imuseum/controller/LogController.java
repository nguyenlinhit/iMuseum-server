package com.tma.imuseum.controller;

import com.tma.imuseum.model.dao.LogDAO;
import com.tma.imuseum.model.pojo.Log;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by nvulinh on 19/06/2017.
 * Log Controller
 */
@RestController
@RequestMapping(value = "/api/log")
public class LogController {
    private final LogDAO logDAO;

    @Autowired
    public LogController(LogDAO logDAO) {
        this.logDAO = logDAO;
    }

    //get Log by id
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Log findById(@PathVariable int id) {
        Log log = logDAO.find(id);
        if (null != log) {
            return log;
        } else {
            return null;
        }
    }

    //Get list of category and paging, default page is 1st, exp: domain/api/log?page=1
    @RequestMapping(method = RequestMethod.GET)
    public List<Log> listLog(@RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "num", defaultValue = "10") int num,
            @RequestParam(value = "a", defaultValue = "1") int active,
            @RequestParam(value = "s", defaultValue = "") String search) {
        return logDAO.getList(page, num, active, search);
    }

    //create Log
    @RequestMapping(method = RequestMethod.POST)
    public String create(@RequestBody String request) {
        String message = "";
        Log log = new Log();
        try {
            JSONObject obj = new JSONObject(request);
            String description = obj.getString("description");
            String input = obj.getString("input");
            // Check null or more, if not pass throw the message type of ERROR
            if (null == description) {
                message += "description has null value;/r/n";
            } else {
                log.setDescription(description);
            }
            if (null == input) {
                message += "input has null value;/r/n";
            } else {
                log.setInput(input);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Throw ERROR message to user side if message != ""
        if ("".equals(message)) {
            logDAO.create(log);
            return JSONObject.quote("Success");
        } else {
            return JSONObject.quote("Create failed: " + message);
        }
    }

    //update Log by ID
    @RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
    public String edit(@PathVariable int id, @RequestBody String request) {
        String message = "";
        Log temp = logDAO.find(id);
        if (null != temp) {
            try {
                JSONObject obj = new JSONObject(request);
                String description = obj.getString("description");
                String input = obj.getString("input");
                // Check null or more, if not pass throw the message type of ERROR
                if (null == description) {
                    message += "description has null value;/r/n";
                } else {
                    temp.setDescription(description);
                }
                if (null == input) {
                    message += "input has null value;/r/n";
                } else {
                    temp.setInput(input);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Throw ERROR message to user side if message != ""
            if ("".equals(message)) {
                logDAO.edit(temp);
                return JSONObject.quote("Success");
            } else {
                return JSONObject.quote("Update failed: " + message);
            }
        } else {
            return JSONObject.quote("Update failed");
        }
    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    public String delete(@PathVariable int id) {
        String message = "";
        Log temp = logDAO.find(id);
        //int status = 0;
        if (null != temp) {
            try {
                Boolean status = false;
                temp.setIsActive(status);
                // Check null or more, if not pass throw the message type of ERROR
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Throw ERROR message to user side if message != ""
            if ("".equals(message)) {
                logDAO.edit(temp);
                return JSONObject.quote("Success");
            } else {
                return JSONObject.quote("Delete failed: ");
            }
        } else {
            return JSONObject.quote("Delete failed");
        }
    }
    //delete Log by ID
    // @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    // public String delete(@PathVariable int id) {
    //     if (logDAO.find(id) != null) {
    //         logDAO.remove(id);
    //         return "Success";
    //     } else {
    //         return "Not found Log with Id: " + id;
    //     }
    // }
}
