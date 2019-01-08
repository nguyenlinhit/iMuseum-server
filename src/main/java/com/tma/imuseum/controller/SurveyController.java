package com.tma.imuseum.controller;

import com.tma.imuseum.model.dao.ArtifactDAO;
import com.tma.imuseum.model.dao.SurveyDAO;
import com.tma.imuseum.model.pojo.Artifact;
import com.tma.imuseum.model.pojo.Survey;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by nvulinh on 19/06/2017.
 * Survey Controller
 */
@RestController
@RequestMapping(value = "/api/survey")
public class SurveyController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SurveyDAO surveyDAO;
    private final ArtifactDAO artifactDAO;

    @Autowired
    public SurveyController(SurveyDAO surveyDAO, ArtifactDAO artifactDAO) {
        this.surveyDAO = surveyDAO;
        this.artifactDAO = artifactDAO;
    }

    //get Survey by id
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Survey findById(@PathVariable int id) {
        Survey survey = surveyDAO.find(id);
        if (null != survey) {
            return survey;
        } else {
            return null;
        }
    }

    //Get list of category and paging, default page is 1st, exp: domain/api/survey?page=1
    @RequestMapping(method = RequestMethod.GET)
    public List<Survey> listSurvey(@RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "num", defaultValue = "10") int num,
            @RequestParam(value = "a", defaultValue = "1") int active,
            @RequestParam(value = "s", defaultValue = "") String search) {
        return surveyDAO.getList(page, num, active, search);
    }

    //create Survey
    @RequestMapping(method = RequestMethod.POST)
    public String create(@RequestBody String request) {
        logger.info(request);

        Survey survey = new Survey();
        String message = "";
        try {
            JSONObject obj = new JSONObject(request);
            int id = obj.getInt("artifactSurvey");
            logger.info("Survey ar: '" );
            String comment = obj.getString("comment");
            logger.info("Survey cm: '");
            int rank = obj.getInt("rank");
            logger.info("Survey ra: '");
            // Check null or more, if not pass throw the message type of ERROR
            if (0 == id) {
                message += "idArtifact has null value;/r/n";
            } else {
                Artifact artifact = artifactDAO.find(id);
                survey.setArtifactSurvey(artifact);
            }
            survey.setComment(comment);
            survey.setRank(rank);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Survey: '" + message + "'");
        // Throw ERROR message to user side if message != ""
        if ("".equals(message)) {
            surveyDAO.create(survey);
            logger.info("Survey create: '" + survey.getComment() + "'");
            return JSONObject.quote("Success");
        } else {
            return JSONObject.quote("Create failed: " + message);
        }

    }

    //delete Survey by ID
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    public String delete(@PathVariable int id) {
        if (surveyDAO.find(id) != null) {
            surveyDAO.remove(id);
            return JSONObject.quote("Success");
        } else {
            return JSONObject.quote("Not found Survey with Id: " + id);
        }
    }

    //get Artifact by id
    @RequestMapping(value = "/{id}/artifact", method = RequestMethod.GET)
    public Artifact getArtifact(@PathVariable int id) {
        Artifact artifact = surveyDAO.find(id).getArtifactSurvey();
        if (null != artifact) {
            return artifact;
        } else {
            return null;
        }
    }
}
