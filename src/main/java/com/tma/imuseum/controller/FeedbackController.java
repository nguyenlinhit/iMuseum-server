package com.tma.imuseum.controller;

import com.tma.imuseum.model.dao.FeedbackDAO;
import com.tma.imuseum.model.pojo.Feedback;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nvulinh on 19/06/2017.
 * Feedback Controller
 */
@RestController
@RequestMapping(value = "/api/feedback")
public class FeedbackController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FeedbackDAO feedbackDAO;

    @Autowired
    public FeedbackController(FeedbackDAO feedbackDAO) {
        this.feedbackDAO = feedbackDAO;
    }

    //get Feedback by id
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Feedback findById(@PathVariable int id) {
        Feedback feedback = feedbackDAO.find(id);
        if (null != feedback) {
            return feedback;
        } else {
            return null;
        }
    }

    //Get list of category and paging, default page is 1st, exp: domain/api/beacon?page=1
    @RequestMapping(method = RequestMethod.GET)
    public List<Feedback> lstFeedback(@RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "num", defaultValue = "10") int num,
            @RequestParam(value = "a", defaultValue = "1") int active,
            @RequestParam(value = "s", defaultValue = "") String search) {
        return feedbackDAO.getList(page, num, active, search);
    }

    @RequestMapping(value = "/feedbackNew" ,method = RequestMethod.GET)
    public List<Feedback> lstFeedbackNew(@RequestParam(value = "page", defaultValue = "-1") int page,
            @RequestParam(value = "num", defaultValue = "10") int num,
            @RequestParam(value = "status", defaultValue = "1") int status,
            @RequestParam(value = "category", defaultValue = "all") String category) {
        
        return feedbackDAO.getList(page, num, status, category);
    }

    @RequestMapping(value = "/{id}/changeStatus", method = RequestMethod.POST)
    public String changeStatus(@PathVariable int id, @RequestBody String request) {
        String message = "";
        Feedback feedback = feedbackDAO.find(id);

        if (null == feedback) {
            message += "Feedback not exist!";
        } else {
            try {
                JSONObject obj = new JSONObject(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Throw ERROR message to user side if message != ""
        if ("".equals(message)) {
            assert feedback != null;
            Boolean status = feedback.getIsActive();
            status = null == status || !status;
            feedback.setIsActive(status);
            feedbackDAO.edit(feedback);
            return JSONObject.quote("Success");
        } else {
            return JSONObject.quote("Change status failed: " + message);
        }
    }
    //create Feedback
    @RequestMapping(method = RequestMethod.POST)
    public String create(@RequestBody String request) {
        logger.info("Feedback create: " + request);
        String message = "";
        Feedback feedback = new Feedback();
        feedback.setIsActive(true);
        try {
            JSONObject obj = new JSONObject(request);
            String error = obj.getString("error");
            String idea = obj.getString("idea");
            // Check null or more, if not pass throw the message type of ERROR
            if (null == error && null == idea) {
                message += "Error and idea has null value;/r/n";
            } else {
                feedback.setError(error);
                feedback.setIdea(idea);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Throw ERROR message to user side if message != ""
        if ("".equals(message)) {
            feedbackDAO.create(feedback);
            return JSONObject.quote("Success");
        } else {
            return JSONObject.quote("Create failed: " + message);
        }
    }

    //delete Feedback by ID
    // public String delete(@PathVariable int id) {
    //     String message = "";
    //     Feedback temp = feedbackDAO.find(id);
    //     //int status = 0;
    //     try {
    //         Boolean status = false;
    //         temp.setIsActive(status);
    //         // Check null or more, if not pass throw the message type of ERROR
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }

    //     // Throw ERROR message to user side if message != ""
    //     if ("".equals(message)) {
    //         feedbackDAO.edit(temp);
    //         return JSONObject.quote("Success");
    //     } else {
    //         return JSONObject.quote("Delete failed: ");
    //     }
    // }
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    public String delete(@PathVariable int id) {
        if (feedbackDAO.find(id) != null) {
            feedbackDAO.remove(id);
            return JSONObject.quote("Success");
        } else {
            return JSONObject.quote("Not found Feedback with Id: " + id);
        }
    }


}
