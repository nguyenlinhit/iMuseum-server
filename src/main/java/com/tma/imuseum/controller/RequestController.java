package com.tma.imuseum.controller;

import com.tma.imuseum.model.dao.BeaconDAO;
import com.tma.imuseum.model.dao.RequestDAO;
import com.tma.imuseum.model.pojo.Beacon;
import com.tma.imuseum.model.pojo.Request;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by nvulinh on 19/06/2017.
 * Request Controller
 */
@RestController
@RequestMapping(value = "/api/request")
public class RequestController {
    private final RequestDAO requestDAO;
    private final BeaconDAO beconDAO;

    @Autowired
    public RequestController(RequestDAO requestDAO, BeaconDAO beconDAO) {
        this.requestDAO = requestDAO;
        this.beconDAO = beconDAO;
    }

    //get Request by id
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Request findById(@PathVariable String id) {
        Request request = requestDAO.find(new Date(Long.parseLong(id)));
        if (null != request) {
            return request;
        } else {
            return null;
        }
    }

    //Get list of category and paging, default page is 1st, exp: domain/api/request?page=1
    @RequestMapping(method = RequestMethod.GET)
    public List<Request> listRequest(@RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "num", defaultValue = "10") int num,
            @RequestParam(value = "a", defaultValue = "1") int active,
            @RequestParam(value = "s", defaultValue = "") String search) {
        return requestDAO.getList(page, num, active, search);
    }

    //create Request
    @RequestMapping(method = RequestMethod.POST)
    public String create(@RequestBody String request) {
        Request request2 = new Request();
        String message = "";

        try {
            JSONObject obj = new JSONObject(request);
            int id = obj.getInt("beaconRequest");
            String idDevice = obj.getString("idDevice");
            // Check null or more, if not pass throw the message type of ERROR
            if (0 == id) {
                message += "idBeacon has null value/r/n";
            } else {
                Beacon beacon = beconDAO.find(id);
                if (null != beacon) {
                    request2.setBeanconRequest(beacon);
                }
            }
            if (null == idDevice) {
                message += "IdDevice has null value;/r/n";
            } else {
                request2.setIdDevice(idDevice);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Throw ERROR message to user side if message != ""
        if ("".equals(message)) {
            requestDAO.create(request2);
            return JSONObject.quote("Success");
        } else {
            return JSONObject.quote("Create failed: " + message);
        }
    }

    //delete Request by ID
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    public String delete(@PathVariable String id) {
        if (requestDAO.find(new Date(Long.parseLong(id))) != null) {
            requestDAO.remove(new Date(Long.parseLong(id)));
            return JSONObject.quote("Success");
        } else {
            return JSONObject.quote("Not found Request with Id: " + id);
        }
    }

    //get becon by id
    @RequestMapping(value = "/{id}/beacon", method = RequestMethod.GET)
    public Beacon getBeacon(@PathVariable String id) {
        Beacon beacon = requestDAO.find(new Date(Long.parseLong(id))).getBeaconRequest();
        if (null != beacon) {
            return beacon;
        } else {
            return null;
        }
    }
}
