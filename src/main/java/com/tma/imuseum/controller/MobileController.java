package com.tma.imuseum.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Hibernate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tma.imuseum.model.dao.ArtifactDAO;
import com.tma.imuseum.model.dao.BeaconDAO;
import com.tma.imuseum.model.dao.CategoryDAO;
import com.tma.imuseum.model.dao.FeedbackDAO;
import com.tma.imuseum.model.dao.LocationDAO;
import com.tma.imuseum.model.dao.MediaDAO;
import com.tma.imuseum.model.dao.RequestDAO;
import com.tma.imuseum.model.dao.UserDAO;
import com.tma.imuseum.model.pojo.Artifact;
import com.tma.imuseum.model.pojo.Beacon;
import com.tma.imuseum.model.pojo.Category;
import com.tma.imuseum.model.pojo.Feedback;
import com.tma.imuseum.model.pojo.Location;
import com.tma.imuseum.model.pojo.Media;
import com.tma.imuseum.model.pojo.Request;
import com.tma.imuseum.model.pojo.Survey;
import com.tma.imuseum.model.pojo.User;

@RestController
@RequestMapping(value = "api/mobile")
public class MobileController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final LocationDAO locationDAO;
    private final ArtifactDAO artifactDAO;
    private final MediaDAO mediaDAO;
    private final UserDAO userDAO;
    private final FeedbackDAO feedbackDAO;
    private final RequestDAO requestDAO;
    private final BeaconDAO beaconDAO;
    private final CategoryDAO categoryDAO;

    @Autowired
    public MobileController(LocationDAO locationDAO, ArtifactDAO artifactDAO, MediaDAO mediaDAO, UserDAO userDAO,
            FeedbackDAO feedbackDAO, RequestDAO requestDAO, BeaconDAO beaconDAO, CategoryDAO categoryDAO) {
        this.locationDAO = locationDAO;
        this.artifactDAO = artifactDAO;
        this.mediaDAO = mediaDAO;
        this.userDAO = userDAO;
        this.feedbackDAO = feedbackDAO;
        this.requestDAO = requestDAO;
        this.beaconDAO = beaconDAO;
        this.categoryDAO = categoryDAO;
    }

    /**
     * Get locations with the same parent
     */
    @RequestMapping(value = "/location/{id}/family")
    public List<Location> getLocationFamily(@PathVariable int id) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("locationId", id);
        return locationDAO.callSP("spLocationFamily", parameters);
    }

    /**
     * Get locations that is 1st tier children
     */
    @RequestMapping(value = "/location/{id}/child")
    public List<Location> getLocationChild(@PathVariable int id) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("locationId", id);
        return locationDAO.callSP("spLocationChild", parameters);
    }

    /**
     * Scan idBeacon, and return list root location. If idBeacon not exist, insert this idBeacon to database
     */
    @RequestMapping(value = "location/{id}/scanbeacon", method = RequestMethod.POST)
    public Map<String, Object> scanBeacon(@PathVariable int id, @RequestBody String request) {
        // JSONObject result = new JSONObject();
        Map<String, Object> result = new HashMap<String, Object>();

        Beacon beacon = beaconDAO.find(id);
        try {
            JSONObject obj = new JSONObject(request);
            String cookies = obj.getString("idUser");

            List<User> users = userDAO.getList(0, -1, 1, "");
            User user = new User();
            for (User user2 : users) {
                if (user2.getCookies().equals(cookies)) {
                    user = user2;
                }
            }

            if (null != beacon) {
                result.put("status", "exist");
                result.put("info", beacon);
            } else {
                Beacon temp = new Beacon();
                temp.setUserBeacon(user);
                temp.setDateCreated(new Date());
                temp.setActive(true);
                temp.setId(id);
                beaconDAO.create(temp);
                result.put("status", "created");
                result.put("info", temp);
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        return result;
    }

    // Get number of page for each table
    @RequestMapping(value = "page/{table}/{num}", method = RequestMethod.GET)
    public String pageMax(@PathVariable String table, @PathVariable int num,
            @RequestParam(value = "a", defaultValue = "1") Integer active,
            @RequestParam(value = "s", defaultValue = "") String search) {
        logger.info("Max page: for " + table + " with " + num + " where active = " + active + " search: " + search);
        int numberOfPage = 0;

        if ("artifact".equals(table)) {
            List<Artifact> temp = artifactDAO.getList(0, num, active, search);
            numberOfPage = (int) Math.ceil(temp.size() * 1.0f / num);

        } else if ("beacon".equals(table)) {
            List<Beacon> temp = beaconDAO.getList(0, num, active, search);
            numberOfPage = (int) Math.ceil(temp.size() * 1.0f / num * 1.0f);

        } else if ("media".equals(table)) {
            List<Media> temp = mediaDAO.getList(0, num, active, search);
            numberOfPage = (int) Math.ceil(temp.size() * 1.0f / num);

        } else if ("feedback".equals(table)) {
            List<Feedback> temp = feedbackDAO.getList(0, num, active, search);
            numberOfPage = (int) Math.ceil(temp.size() * 1.0f / num);

        } else if ("request".equals(table)) {
            List<Request> temp = requestDAO.getList(0, num, active, search);
            numberOfPage = (int) Math.ceil(temp.size() * 1.0f / num);
        } else if ("category".equals(table)) {
            List<Category> temp = categoryDAO.getList(0, num, active, search);
            numberOfPage = (int) Math.ceil(temp.size() * 1.0f / num);
        } else if ("user".equals(table)) {
            List<User> temp = userDAO.getList(0, -1, active, search);
            numberOfPage = (int) Math.ceil(temp.size() * 1.0f / num);
        }

        if (num < 0) {
            numberOfPage = 1;
        }
        return JSONObject.quote(Integer.toString(numberOfPage));
    }

    // Get list artifact with phone mac address and timestamp: /api/mobile/beacon/{id}/artifact
    @RequestMapping(value = "beacon/{id}/artifact", method = RequestMethod.POST)
    public Set<Artifact> startBeaconTransaction(@PathVariable int id, @RequestBody String request) {
        logger.info("Mobile start: " + request + " - id: " + id);
        // Store request information
        try {
            // Get request information
            JSONObject obj = new JSONObject(request);
            String macAddress = obj.getString("macAddress");
            String timeStamp = obj.getString("timeStamp");

            Beacon beacon = beaconDAO.find(id);

            if (null != beacon) {
                Request r = new Request();
                r.setIdDevice(macAddress);
                r.setBeanconRequest(beacon);
                r.setTimestamp(new Date(Long.parseLong(timeStamp)));

                requestDAO.create(r);
                logger.info("Mobile start: created");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("Mobile start: exception");
        }

        // get list artifact and reponse to mobile's request
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

    // End transaction with beacon after get out of range with this beacon
    @RequestMapping(value = "beacon/{id}", method = RequestMethod.POST)
    public String endBeaconTransaction(@PathVariable int id, @RequestBody String request) {
        logger.info("Mobile end: " + request + " - id: " + id);
        String message = "";
        // Store request information
        try {
            // Get request information
            JSONObject obj = new JSONObject(request);
            String macAddress = obj.getString("macAddress");
            String timeStamp = obj.getString("timeStamp");
            String durationString = obj.getString("duration");
            Long duration = Long.parseLong(durationString);
            Request r = requestDAO.find(new Date(Long.parseLong(timeStamp)));
            if (null != r) {
                if (r.getIdDevice().equals(macAddress) && r.getDuration() == null) {
                    r.setDuration(duration);
                }
                requestDAO.edit(r);
                logger.info("Mobile end: created");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("Mobile end: exception");
            message += "Json parse error";
        }

        if ("".equals(message)) {
            return JSONObject.quote("Success");
        } else {
            return JSONObject.quote("Create failed: " + message);
        }
    }

    @RequestMapping(value = "overview", method = RequestMethod.GET)
    public String overviewInfo() {
        JSONObject obj = new JSONObject();
        try {
            /**
             * Information for artifact
             */
            List<Artifact> artifacts = artifactDAO.getList(0, 10, -1, "");
            List<Category> categories = categoryDAO.getList(0, 10, 1, "");

            int artifactIsActive = 0;
            int artifactIsInactive = 0;
            int[] totalArtifactCategory = new int[categories.size()];
            int artifactMapped = 0;

            // Counting
            for (Artifact artifact : artifacts) {
                // Count for is active or not
                if (artifact.getActive()) {
                    artifactIsActive++;
                } else {
                    artifactIsInactive++;
                }

                // Count for has been mapped with beacon or not
                if (null != artifact.getBeaconArtifact()) {
                    artifactMapped++;
                }

                // Count for category
                int index = 0;
                for (Category category : categories) {
                    if (artifact.getCategoryArtifact().getIdCategory() == category.getIdCategory()) {
                        totalArtifactCategory[index]++;
                    }
                    index++;
                }
            }

            // Artifact by category
            JSONArray categoryArray = new JSONArray();
            int index = 0;
            for (Category category : categories) {
                JSONObject categoryObject = new JSONObject();
                categoryObject.put(category.getNameCategory(), totalArtifactCategory[index]);
                categoryArray.put(categoryObject);
                index++;
            }

            // Artifact overview information
            obj.put("totalArtifact", artifacts.size());
            obj.put("totalActiveArtifact", artifactIsActive);
            obj.put("totalInactiveArtifact", artifactIsInactive);
            obj.put("totalMappedArtifact", artifactMapped);
            obj.put("totalArtifactCategory", categoryArray);

            /**
             * Information for beacon
             */
            List<Beacon> beacons = beaconDAO.getList(0, 10, -1, "");
            int beaconIsActive = 0;
            int beaconIsInactive = 0;
            int beaconMapped = 0;
            for (Beacon beacon : beacons) {
                if (null != beacon.getActive() && beacon.getActive()) {
                    beaconIsActive++;
                } else {
                    beaconIsInactive++;
                }

                if (null != beacon.getArtifacts() && 0 != beacon.getArtifacts().size()) {
                    beaconMapped++;
                }
            }

            // Beacon overview information
            obj.put("totalBeacon", beacons.size());
            obj.put("totalActiveBeacon", beaconIsActive);
            obj.put("totalInactiveBeacon", beaconIsInactive);
            obj.put("totalMappedBeacon", beaconMapped);

            /**
             * Information for user
             */
            obj.put("totalActiveUser", userDAO.getList(0, -1, 1, "").size());

            /**
             * Information for location
             */
            obj.put("totalIdea", feedbackDAO.getList(0, -1, 1, "idea").size());
            obj.put("totalError", feedbackDAO.getList(0, -1, 1, "error").size());

        } catch (Exception e) {
            logger.warn("error");
            e.printStackTrace();
            //TODO: handle exception
        }
        return obj.toString();
    }

    //data chart view, {split} is number of smallest split
    @RequestMapping(value = "/dataView/{split}", method = RequestMethod.GET)
    public String dataView(@PathVariable int split) {
        // Get data from database
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("p", -1);
        parameters.put("n", 10);
        parameters.put("sortType", "DESC");
        parameters.put("sortColumn", "view");
        List<Artifact> artifacts = artifactDAO.callSP("spGetMostView", parameters);

        JSONObject result = new JSONObject();
        if (null != artifacts) {
            // Get number of column for graph
            int column = artifacts.get(0).getView() / split + 1;
            int[] values = new int[column];

            // Count number artifacts
            for (int i = 0; i < artifacts.size(); i++) {
                int index = artifacts.get(i).getView() / split;
                values[index]++;
            }

            // Add to json object
            try {
                for (int i = 0; i < column; i++) {
                    result.put(Integer.toString(i * split), values[i]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result.toString();
    }

    //data chart rate
    @RequestMapping(value = "/dataRate", method = RequestMethod.GET)
    public String dataRate() {
        // Get data from database
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("p", 0);
        parameters.put("n", 10);
        parameters.put("sortType", "DESC");
        parameters.put("sortColumn", "rate");

        // Get list artifact with highest rank
        List<Artifact> artifacts = artifactDAO.callSP("spGetHighestRate", parameters);

        JSONObject result = new JSONObject();
        if (null != artifacts) {
            int[] values = new int[6];

            // Count number artifacts
            for (int i = 0; i < artifacts.size(); i++) {

                Set<Survey> surveys = artifacts.get(i).getsurveys();
                int rank = 0;
                if (null != surveys) {
                    // Get list survey
                    for (Survey survey : surveys) {
                        rank += survey.getRank();
                    }

                    // Calculate rank
                    int index = rank / surveys.size();
                    values[index]++;
                }
            }

            // Add to json object
            try {
                for (int i = 0; i < 6; i++) {
                    result.put(Integer.toString(i), values[i]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result.toString();
    }

    @RequestMapping(value = "/pageMaxidea", method = RequestMethod.GET)
    public String pageMaxIdea(@RequestParam(value = "page", defaultValue = "-1") int page,
            @RequestParam(value = "num", defaultValue = "10") int num,
            @RequestParam(value = "a", defaultValue = "1") int active) {
        int mumOfPage = 0;
        Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put("p", page - 1);
        parameters.put("n", num);
        parameters.put("isActive", active);

        List<Feedback> temp = feedbackDAO.callSP("spPagingIdea", parameters);

        mumOfPage = (int) Math.ceil(temp.size() * 1.0f / num * 1.0f);

        return JSONObject.quote(Integer.toString(mumOfPage));
    }

    @RequestMapping(value = "/pageMaxerror", method = RequestMethod.GET)
    public String pageMaxError(@RequestParam(value = "page", defaultValue = "-1") int page,
            @RequestParam(value = "num", defaultValue = "10") int num,
            @RequestParam(value = "a", defaultValue = "1") int active) {
        int mumOfPage = 0;
        Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put("p", page - 1);
        parameters.put("n", num);
        parameters.put("isActive", active);

        List<Feedback> temp = feedbackDAO.callSP("spPagingError", parameters);

        mumOfPage = (int) Math.ceil(temp.size() * 1.0f / num * 1.0f);

        return JSONObject.quote(Integer.toString(mumOfPage));
    }

    @RequestMapping(value = "/beacon/{id}/location", method = RequestMethod.GET)
    public String beaconAddress(@PathVariable int id) {
        JSONObject result = new JSONObject();
        Beacon beacon = beaconDAO.find(id);
        try {
            if (null != beacon) {
                Location minor = beacon.getLocationBeacon();
                if (null != minor) {
                    result.put(minor.getType(), minor.getNameLoc());
                    while (0 != minor.getIdParent()) {
                        minor = locationDAO.find(minor.getIdParent());
                        if (null != minor) {
                            result.put(minor.getType(), minor.getNameLoc());
                        }
                    }
                }
            }
        } catch (Exception e) {
            //TODO: handle exception
        }
        return result.toString();
    }
}