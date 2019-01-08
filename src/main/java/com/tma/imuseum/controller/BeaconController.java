package com.tma.imuseum.controller;

import com.tma.imuseum.model.dao.ArtifactDAO;
import com.tma.imuseum.model.dao.BeaconDAO;
import com.tma.imuseum.model.dao.LocationDAO;
import com.tma.imuseum.model.dao.UserDAO;
import com.tma.imuseum.model.pojo.Artifact;
import com.tma.imuseum.model.pojo.Beacon;
import com.tma.imuseum.model.pojo.Location;
import com.tma.imuseum.model.pojo.Request;
import com.tma.imuseum.model.pojo.User;

import org.hibernate.Hibernate;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by nguye on 6/16/2017.
 * BeaconController
 */
@RestController
@RequestMapping(value = "/api/beacon")
public class BeaconController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BeaconDAO beaconDAO;
    private final LocationDAO locationDAO;
    private final UserDAO userDAO;
    private final ArtifactDAO artifactDAO;

    @Autowired
    public BeaconController(BeaconDAO beaconDAO, LocationDAO locationDAO, UserDAO userDAO, ArtifactDAO artifactDAO) {
        this.beaconDAO = beaconDAO;
        this.locationDAO = locationDAO;
        this.userDAO = userDAO;
        this.artifactDAO = artifactDAO;
    }

    //get Beacon by id
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Beacon findById(@PathVariable int id) {
        Beacon beacon = beaconDAO.find(id);
        if (null != beacon) {
            beacon.getLocationBeacon();
            return beacon;
        }
        return null;
    }
    @RequestMapping(value = "check/{id}", method = RequestMethod.GET)
    public String checkById(@PathVariable int id) {
        Beacon beacon = beaconDAO.find(id);
        if (null != beacon) {
            return JSONObject.quote("Exist");
        }
        return JSONObject.quote("New");
    }

    //Get list of category and paging, default page is 1st, exp: domain/api/beacon?page=1
    @RequestMapping(method = RequestMethod.GET)
    public List<Beacon> listBeacon(@RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "num", defaultValue = "10") int num,
            @RequestParam(value = "a", defaultValue = "1") int active,
            @RequestParam(value = "s", defaultValue = "") String search) {

        List<Beacon> beacons = beaconDAO.getList(page, num, active, search);
        for (Beacon beacon : beacons) {
            beacon.getLocationBeacon();
            logger.info("Beacon list: " + beacon.toString());
        }

        return beacons;
    }

    //create Beacon
    @RequestMapping(method = RequestMethod.POST)
    public String create(@RequestBody String request) {
        logger.info(request);

        String message = "";
        Beacon beacon = new Beacon();

        try {
            JSONObject obj = new JSONObject(request);
            Integer idBeacon = obj.getInt("idBeacon");
            int idUser = obj.getInt("userBeacon");

            User user = userDAO.find(idUser);
            // Check null or more, if not pass throw the message type of ERROR
            if (idBeacon < 10000 && idBeacon > 99999) {
                message += "Id Location has wrong value;/r/n";
            } else {
                beacon.setId(idBeacon);
            }
            if (null == user) {
                message += "Please login to use this method;/r/n";
            } else {
                beacon.setUserBeacon(user);
            }
        } catch (

        Exception e) {
            e.printStackTrace();
        }

        // Throw ERROR message to user side if message != ""
        if ("".equals(message)) {
            beacon.setDateCreated(new Date());
            beaconDAO.create(beacon);
            return JSONObject.quote("Success");
        } else {
            return JSONObject.quote("Create failed: " + message);
        }
    }

    //update Beacon by ID
    @RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
    public String edit(@PathVariable int id, @RequestBody String request) {
        String message = "";
        Beacon beacon = beaconDAO.find(id);
        if (null != beacon) {
            try {
                JSONObject obj = new JSONObject(request);
                Boolean isActive = (1 == obj.getInt("isActive"));
                int idLocation = obj.getInt("idLocation");
                String cookies = obj.getString("idUser");

                List<User> users = userDAO.getList(0, -1, 1, "");
                User user = new User();
                for (User user2 : users) {
                    if (user2.getCookies().equals(cookies)) {
                        user = user2;
                    }
                }

                Location location = locationDAO.find(idLocation);
                beacon.setActive(isActive);
                // Check null or more, if not pass throw the message type of ERROR
                if (user.getIdUser() == 0) {
                    message += "Please login to use this method;/r/n";
                } else {
                    beacon.setUserBeacon(user);
                }
                if (null == location) {
                    message += "Id Location has null value;/r/n";
                } else {
                    beacon.setLocationBeacon(location);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if ("".equals(message)) {
            assert beacon != null;
            beacon.setDateEdit(new Date());
            beaconDAO.edit(beacon);
            return JSONObject.quote("Success");
        } else {
            return JSONObject.quote("Update failed: " + message);
        }
    }

    //Add Artifact to Beacon
    @RequestMapping(value = "/{id}/addArtifact", method = RequestMethod.POST)
    public Artifact addArtifactToBeacon(@PathVariable int id, @RequestBody String request) {
        int idArtifact = 0;
        try {
            JSONObject obj = new JSONObject(request);
            idArtifact = obj.getInt("idArtifact");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Beacon beacon = beaconDAO.find(id);
        Artifact artifact = artifactDAO.find(idArtifact);

        if (null == artifact || null == beacon) {
            return null;
        } else {
            Map<String, Object> parameters = new HashMap<String, Object>();

            parameters.put("idArtifact", artifact.getIdArtifact());
            parameters.put("idBeacon", beacon.getId());

            return artifactDAO.callSP("spMap", parameters).get(0);
        }
    }

    //Remove Artifact out of Beacon
    @RequestMapping(value = "/{id}/removeArtifact", method = RequestMethod.POST)
    public String addArtifactOutOfBeacon(@PathVariable int id, @RequestBody String request) {
        int idArtifact = 0;
        try {
            JSONObject obj = new JSONObject(request);
            idArtifact = obj.getInt("idArtifact");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Beacon beacon = beaconDAO.find(id);
        if (null != beacon) {
            Map<String, Object> parameters = new HashMap<String, Object>();

            parameters.put("idArtifact", idArtifact);
            parameters.put("idBeacon", null);
            artifactDAO.callSP("spMap", parameters);
            return JSONObject.quote("Success");

            // Set<Artifact> artifacts = beacon.getArtifacts();
            // Artifact artifact = artifactDAO.find(idArtifact);

            // if (null == artifact) {
            //     return JSONObject.quote("Remove Failed");
            // } else {
            //     artifacts.remove(artifact);
            //     return JSONObject.quote("Success");
            // }
        } else {
            return JSONObject.quote("Remove Failed");
        }
    }

    //change status Beacon by ID
    @RequestMapping(value = "/{id}/changeStatus", method = RequestMethod.POST)
    public String changeStatus(@PathVariable int id, @RequestBody String request) {
        logger.info("Change status beacon: id - " + id + " - request: " + request);
        String message = "";
        Beacon beacon = beaconDAO.find(id);

        // if (null == beacon) {
        //     message += "Beacon not exist!";
        // } else {
        //     try {
        //         JSONObject obj = new JSONObject(request);
        //         int idUser = obj.getInt("userArtifact");
        //         // Check null or more, if not pass throw the message type of ERROR
        //         User user = userDAO.find(idUser);
        //         if (null == user) {
        //             message += "Please login to use this method;/r/n";
        //         } else {
        //             beacon.setUserBeacon(user);
        //         }
        //     } catch (Exception e) {
        //         e.printStackTrace();
        //     }
        // }

        // Throw ERROR message to user side if message != ""
        if ("".equals(message)) {
            Boolean status = beacon.getActive();
            status = null == status || !status;
            beacon.setActive(status);
            beaconDAO.edit(beacon);
            return JSONObject.quote("Success");
        } else {
            return JSONObject.quote("Change status failed: " + message);
        }
    }

    //get locaion by id
    @RequestMapping(value = "/{id}/location", method = RequestMethod.GET)
    public List<Location> getLocation(@PathVariable int id) {
        List<Location> list = new ArrayList<Location>();
        Beacon beacon = beaconDAO.find(id);
        if (null != beacon) {
            Location minor = beacon.getLocationBeacon();
            if (null != minor) {
                list.add(minor);
                while (0 != minor.getIdParent()) {
                    minor = locationDAO.find(minor.getIdParent());
                    if (null != minor) {
                        list.add(minor);
                    }
                }
            }
        }
        return list;
    }

    //get request by id
    @RequestMapping(value = "/{id}/request", method = RequestMethod.GET)
    public Set<Request> getRequest(@PathVariable int id) {
        Set<Request> requests = beaconDAO.find(id).getRequest();
        if (null != requests) {
            return requests;
        } else {
            return null;
        }
    }

    //get user updapted by id
    @RequestMapping(value = "/{id}/user", method = RequestMethod.GET)
    public User getUser(@PathVariable int id) {
        User user = beaconDAO.find(id).getUserBeacon();
        if (null != user) {
            return user;
        } else {
            return null;
        }
    }

    //get list artifact
    @RequestMapping(value = "/{id}/artifact", method = RequestMethod.GET)
    public Set<Artifact> getArtifact(@PathVariable int id) {
        Set<Artifact> artifacts = beaconDAO.find(id).getArtifacts();
        if (null != artifacts) {
            for (Artifact artifact : artifacts) {
                Hibernate.initialize(artifact.getMedias());
            }
            return (artifacts);
        } else {
            return null;
        }
    }

    //get list artifact on mobile
    @RequestMapping(value = "/{id}/artifact/m", method = RequestMethod.GET)
    public Set<Artifact> getArtifactMobile(@PathVariable int id) {
        Set<Artifact> artifacts = beaconDAO.find(id).getArtifacts();
        if (null != artifacts) {
            for (Artifact artifact : artifacts) {
                Hibernate.initialize(artifact.getMedias());
                artifact.setView(artifact.getView() + 1);
                artifactDAO.edit(artifact);
            }
            return (artifacts);
        } else {
            return null;
        }
    }

    //get most duration of Beacon
    @RequestMapping(value = "/getMostDuration", method = RequestMethod.GET)
    public List<Artifact> getHighestRateofArtifact(@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "num", defaultValue = "100") int num,
            @RequestParam(value = "sortType", defaultValue = "DESC") String sortType) {
        Map<String, Object> parametters = new HashMap<String, Object>();

        parametters.put("p", page);
        parametters.put("n", num);
        parametters.put("sortType", sortType);

        return artifactDAO.callSP("spGetMostDuration", parametters);
    }

}
