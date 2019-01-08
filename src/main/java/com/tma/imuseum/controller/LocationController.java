package com.tma.imuseum.controller;

import com.tma.imuseum.model.dao.LocationDAO;
import com.tma.imuseum.model.pojo.Artifact;
import com.tma.imuseum.model.pojo.Beacon;
import com.tma.imuseum.model.pojo.Location;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by nvulinh on 19/06/2017.
 * Location Controller
 */

@RestController
@RequestMapping(value = "/api/location")
public class LocationController {
    private final LocationDAO locationDAO;

    @Autowired
    public LocationController(LocationDAO locationDAO) {
        this.locationDAO = locationDAO;
    }

    //get Location by id
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Location findById(@PathVariable int id) {
        Location location = locationDAO.find(id);
        if (null != location) {
            return location;
        } else {
            return null;
        }
    }

    //Get list of category and paging, default page is 1st, exp: domain/api/beacon?page=1
    @RequestMapping(method = RequestMethod.GET)
    public List<Location> listLocation(@RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "num", defaultValue = "10") int num,
            @RequestParam(value = "a", defaultValue = "1") int active,
            @RequestParam(value = "s", defaultValue = "") String search) {
        return locationDAO.getList(page, num, active, search);
    }

    //create Location
    @RequestMapping(method = RequestMethod.POST)
    public String create(@RequestBody String request) {
        String message = "";
        Location location = new Location();

        try {
            JSONObject obj = new JSONObject(request);
            String name = obj.getString("nameLocation");
            int parent = obj.getInt("idParent");
            String type = obj.getString("type");
            // Check null or more, if not pass throw the message type of ERROR
            if (null == name) {
                message += "Name has null value;/r/n";
            } else {
                location.setNameLoc(name);
            }
            if (null == type) {
                message += "Type has null value;/r/n";
            } else {
                location.setType(type);
            }
            if (parent < 0) {
                message += "Parent has null value;/r/n";
            } else {
                location.setIdParent(parent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if ("".equals(message)) {
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("nameLocation", location.getNameLoc());
            parameters.put("type", location.getType());
            parameters.put("idParent", location.getIdParent());

            return JSONObject
                    .quote(Integer.toString(locationDAO.callSP("insertLocation", parameters).get(0).getIdLocation()));
        } else {
            // return JSONObject.quote("Create failed: " + message);
            return JSONObject.quote("Create failed");
        }
    }

    //update Location by ID
    @RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
    public String edit(@PathVariable int id, @RequestBody String request) {
        String message = "";
        Location temp = locationDAO.find(id);
        if (null != temp) {
            try {
                JSONObject obj = new JSONObject(request);
                String name = obj.getString("nameLoc");
                String type = obj.getString("type");
                // Check null or more, if not pass throw the message type of ERROR
                if (null == name) {
                    message += "Name has null value;/r/n";
                } else {
                    temp.setNameLoc(name);
                }
                if (null == type) {
                    message += "Type has null value;/r/n";
                } else {
                    temp.setType(type);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Throw ERROR message to user side if message != ""
            if ("".equals(message)) {
                locationDAO.create(temp);
                return JSONObject.quote("Success");
            } else {
                return JSONObject.quote("Update failed: " + message);
            }
        } else {
            return JSONObject.quote("Update failed");
        }
    }

    //delete Location by ID
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    public String delete(@PathVariable int id) {
        if (locationDAO.find(id) != null) {
            locationDAO.remove(id);
            return JSONObject.quote("Success");
        } else {
            return JSONObject.quote("Not found Location with Id: " + id);
        }
    }

    //get articfact by location
    @RequestMapping(value = "/{id}/artifact", method = RequestMethod.GET)
    public Set<Artifact> getArtifact(@PathVariable int id) {
        Set<Artifact> artifacts = (locationDAO.find(id).getArtifacts());
        if (null != artifacts) {
            return artifacts;
        } else {
            return null;
        }
    }

    //get beacon by location
    @RequestMapping(value = "/{id}/beacon", method = RequestMethod.GET)
    public Set<Beacon> getBeaaon(@PathVariable int id) {
        Set<Beacon> beacons = (locationDAO.find(id).getBeacons());
        if (null != beacons) {
            return beacons;
        } else {
            return null;
        }
    }
}
