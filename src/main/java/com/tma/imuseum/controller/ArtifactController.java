package com.tma.imuseum.controller;

import com.tma.imuseum.model.dao.*;
import com.tma.imuseum.model.pojo.Artifact;
import com.tma.imuseum.model.pojo.Beacon;
import com.tma.imuseum.model.pojo.Category;
import com.tma.imuseum.model.pojo.Location;
import com.tma.imuseum.model.pojo.Media;
import com.tma.imuseum.model.pojo.Survey;
import com.tma.imuseum.model.pojo.User;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.Date;

@Transactional
@RestController
@RequestMapping(value = "/api/artifact")
public class ArtifactController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ArtifactDAO artifactDAO;
    private final CategoryDAO categoryDAO;
    private final LocationDAO locationDAO;
    private final UserDAO userDAO;
    private final MediaDAO mediaDAO;
    private final BeaconDAO beaconDAO;

    @Autowired
    public ArtifactController(ArtifactDAO artifactDAO, CategoryDAO categoryDAO, LocationDAO locationDAO,
            UserDAO userDAO, MediaDAO mediaDAO, BeaconDAO beaconDAO) {
        this.artifactDAO = artifactDAO;
        this.categoryDAO = categoryDAO;
        this.locationDAO = locationDAO;
        this.userDAO = userDAO;
        this.mediaDAO = mediaDAO;
        this.beaconDAO = beaconDAO;
    }

    //get Artifact by id
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Artifact findById(@PathVariable int id, @RequestParam(value = "view", defaultValue = "false") Boolean view) {
        Artifact artifact = artifactDAO.find(id);
        if (null != artifact) {

            if (view) {
                artifact.setView(artifact.getView() + 1);
                artifactDAO.edit(artifact);
            }
            artifact.getLocationArtifact();
            return artifact;
        }

        return null;
    }

    //Get list of category and paging, default page is 1st, exp: domain/api/artifact?page=1
    @RequestMapping(method = RequestMethod.GET)
    public List<Artifact> lstArtifact(@RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "num", defaultValue = "10") int num,
            @RequestParam(value = "a", defaultValue = "1") Integer active,
            @RequestParam(value = "s", defaultValue = "") String search) {

        List<Artifact> artifacts = artifactDAO.getList(page, num, active, search);
        for (Artifact artifact : artifacts) {
            artifact.getLocationArtifact();
        }
        return artifacts;
    }

    @RequestMapping(value = "/notmap", method = RequestMethod.GET)
    public List<Artifact> lstArtifactNotMap() {
        List<Artifact> artifacts = artifactDAO.getList(0, 1, -1, "");
        List<Artifact> artifactsNotMap = new ArrayList<Artifact>();
        for (Artifact artifact : artifacts) {
            if (null == artifact.getBeaconArtifact()) {
                artifactsNotMap.add(artifact);
            }
        }
        return artifactsNotMap;
    }

    //create Artifact
    @RequestMapping(method = RequestMethod.POST)
    public String create(@RequestBody String request) {
        logger.info(request);

        String message = "";
        Artifact artifact = new Artifact();

        try {
            JSONObject obj = new JSONObject(request);
            String name = obj.getString("nameArtifact");
            String description = obj.getString("description");
            String author = obj.getString("author");
            int idCategory = obj.getInt("categoryArtifact");
            String cookies = obj.getString("userArtifact");
            Integer idBeacon = obj.getInt("beaconArtifact");
            Integer idLocation = obj.getInt("idLocation");
            String title = obj.getString("title");
            Boolean status = 1 == obj.getInt("status");
            Category category = categoryDAO.find(idCategory);
            Beacon beacon = beaconDAO.find(idBeacon);
            Location location = locationDAO.find(idLocation);
            // Check null or more, if not pass throw the message type of ERROR
            List<User> users = userDAO.getList(0, -1, 1, "");
            User user = new User();
            for (User user2 : users) {
                if (user2.getCookies().equals(cookies)) {
                    user = user2;
                }
            }

            if (null == location) {
                message += "Id Location has null value;/r/n";
            } else {
                artifact.setLocationArtifact(location);
            }
            if (null == name) {
                message += "Name has null value;/r/n";
            } else {
                artifact.setNameArtifact(name);
            }
            if (null == description) {
                message += "Description has null value;/r/n";
            } else {
                artifact.setDescription(description);
            }
            if (null == author) {
                message += "Author has null value;/r/n";
            } else {
                artifact.setAuthor(author);
            }
            if (null == category) {
                message += "Category has null value;/r/n";
            } else {
                artifact.setCategoryArtifact(category);
            }
            if (null != beacon) {
                artifact.setBeaconArtifact(beacon);
            }
            if (null == user) {
                message += "Please login to use this method;/r/n";
            } else {
                artifact.setUserArtifact(user);
            }
            if (null == title) {
                message += "Title has null value;/r/n";
            } else {
                artifact.setTitleArtifact(title);
            }
            artifact.setIsActive(status);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Throw ERROR message to user side if message != ""
        if ("".equals(message)) {
            artifact.setDateCreated(new Date());
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("idBeacon",
                    artifact.getBeaconArtifact() != null ? artifact.getBeaconArtifact().getId() : null);
            parameters.put("idCategory",
                    artifact.getCategoryArtifact() != null ? artifact.getCategoryArtifact().getIdCategory() : null);
            parameters.put("name", artifact.getNameArtifact());
            parameters.put("title", artifact.getTitleArtifact());
            parameters.put("description", artifact.getDescription());
            parameters.put("idLocation",
                    artifact.getLocationArtifact() != null ? artifact.getLocationArtifact().getIdLocation() : null);
            parameters.put("author", artifact.getAuthor());
            parameters.put("dateCreated", artifact.getDateCreated());
            parameters.put("idUser",
                    artifact.getUserArtifact() != null ? artifact.getUserArtifact().getIdUser() : null);
            parameters.put("status", artifact.getActive());

            return JSONObject
                    .quote(Integer.toString(artifactDAO.callSP("spAddArtifact", parameters).get(0).getIdArtifact()));
        } else {
            return JSONObject.quote("Create failed: " + message);
        }
    }

    //update Artifact by ID
    @RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
    public String edit(@PathVariable int id, @RequestBody String request) {
        logger.info(request);

        String message = "";
        Artifact artifact = artifactDAO.find(id);

        if (null == artifact) {
            message += "Artifact not exist!";
        } else {
            try {
                JSONObject obj = new JSONObject(request);
                int idArtifact = obj.getInt("idArtifact");
                int idLocation = obj.getInt("locationArtifact");
                Boolean status = 1 == obj.getInt("statusArtifact");
                String name = obj.getString("nameArtifact");
                String description = obj.getString("description");
                String author = obj.getString("author");
                int idCategory = obj.getInt("categoryArtifact");
                int idUser = obj.getInt("userArtifact");
                String title = obj.getString("titleArtifact");
                int idBeacon = obj.getInt("beaconArtifact");

                User user = userDAO.find(idUser);
                Category category = categoryDAO.find(idCategory);
                Beacon beacon = beaconDAO.find(idBeacon);
                Location location = locationDAO.find(idLocation);
                // Check null or more, if not pass throw the message type of ERROR
                if (null == name) {
                    message += "Name has null value;/r/n";
                } else {
                    artifact.setNameArtifact(name);
                }
                if (null == description) {
                    message += "Description has null value;/r/n";
                } else {
                    artifact.setDescription(description);
                }
                if (null == author) {
                    message += "Author has null value;/r/n";
                } else {
                    artifact.setAuthor(author);
                }
                if (null == category) {
                    message += "Id category has null value;/r/n";
                } else {
                    artifact.setCategoryArtifact(category);
                }
                if (null == user) {
                    message += "Please login to use this method;/r/n";
                } else {
                    artifact.setUserArtifact(user);
                }
                if (null == title) {
                    message += "Title has null value;/r/n";
                } else {
                    artifact.setTitleArtifact(title);
                }
                if (null == beacon) {
                    message += "Id Beacon has null value;/r/n";
                } else {
                    artifact.setBeaconArtifact(beacon);
                }
                if (null == location) {
                    message += "Location has null value;/r/n";
                } else {
                    artifact.setLocationArtifact(location);
                }
                artifact.setIsActive(status);
                artifact.setIdArtifact(idArtifact);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Throw ERROR message to user side if message != ""
        if ("".equals(message)) {

            assert artifact != null;
            artifact.setDateEdit(new Date());
            logger.info("Artifact edit: des: " + artifact.getDescription() + " - date edit: " + artifact.getDateEdit());
            Map<String, Object> parameters = new HashMap<String, Object>();

            parameters.put("idArtifact", artifact.getIdArtifact());
            parameters.put("idBeacon",
                    artifact.getBeaconArtifact() != null ? artifact.getBeaconArtifact().getId() : null);
            parameters.put("idCategory", artifact.getCategoryArtifact());
            parameters.put("name", artifact.getNameArtifact());
            parameters.put("title", artifact.getTitleArtifact());
            parameters.put("description", artifact.getDescription());
            parameters.put("idLocation", artifact.getLocationArtifact());
            parameters.put("author", artifact.getAuthor());
            parameters.put("editor", artifact.getUserArtifact());
            parameters.put("dateEdit", artifact.getDateEdit());
            parameters.put("status", artifact.getActive());

            artifactDAO.callSP("spEditArtifact", parameters);

            return JSONObject.quote("Success");
        } else {
            return JSONObject.quote("Update failed: " + message);
        }
    }

    //add media to artifacts
    @RequestMapping(value = "/{id}/addMedia", method = RequestMethod.POST)
    public Media addMediaToArtifact(@PathVariable int id, @RequestBody String request) {
        int idMedia = 0;

        try {
            JSONObject obj = new JSONObject(request);
            idMedia = obj.getInt("idMedia");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Artifact artifact = artifactDAO.find(id);
        Media media = mediaDAO.find(idMedia);
        if (null == artifact || null == media) {
            return null;
        } else {
            Map<String, Object> parameters = new HashMap<String, Object>();

            parameters.put("idMedia", media.getIdMedia());
            parameters.put("idArtifact", artifact.getIdArtifact());
            mediaDAO.callSP("spMapMedia", parameters);

            return mediaDAO.callSP("spMapMedia", parameters).get(0);
        }
    }

    //remove media out of artifacts
    @RequestMapping(value = "/{id}/removeMedia", method = RequestMethod.POST)
    public Media addMediaOutOfArtifact(@PathVariable int id, @RequestBody String request) {
        int idMedia = 0;

        try {
            JSONObject obj = new JSONObject(request);
            idMedia = obj.getInt("idMedia");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Media media = mediaDAO.find(idMedia);
        Artifact artifact = artifactDAO.find(id);
        if (null != artifact && null != media) {
            Map<String, Object> parameters = new HashMap<String, Object>();

            parameters.put("idMedia", media.getIdMedia());
            parameters.put("idArtifact", null);
            mediaDAO.callSP("spMapMedia", parameters);

            return mediaDAO.callSP("spMapMedia", parameters).get(0);
        } else {
            return null;
        }
    }

    //get most view of Artifact
    @RequestMapping(value = "/getMostView", method = RequestMethod.GET)
    public List<Artifact> getMostViewofArtifact(@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "num", defaultValue = "10") int num,
            @RequestParam(value = "sortType", defaultValue = "DESC") String sortType,
            @RequestParam(value = "sortColumn", defaultValue = "view") String sortColumn) {
        logger.info("Get most view: page: " + page + " - num: " + num + " - sort: " + sortType + " - column: "
                + sortColumn);
        Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put("p", page);
        parameters.put("n", num);
        parameters.put("sortType", sortType);
        parameters.put("sortColumn", sortColumn);

        return artifactDAO.callSP("spGetMostView", parameters);
    }

    //get most view of Artifact
    @RequestMapping(value = "/getRate", method = RequestMethod.GET)
    public String getHighestRateArtifact(@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "num", defaultValue = "10") int num,
            @RequestParam(value = "sortType", defaultValue = "DESC") String sortType,
            @RequestParam(value = "sortColumn", defaultValue = "rate") String sortColumn) {
        logger.info("Get most view: page: " + page + " - num: " + num + " - sort: " + sortType + " - column: "
                + sortColumn);

        Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put("p", page);
        parameters.put("n", num);
        parameters.put("sortType", sortType);
        parameters.put("sortColumn", sortColumn);
        // Get list artifact with highest rank
        List<JSONObject> results = new ArrayList<JSONObject>();
        List<Artifact> artifacts = artifactDAO.callSP("spGetHighestRate", parameters);

        // Get rank for each artifact
        for (Artifact artifact : artifacts) {
            Set<Survey> surveys = artifact.getsurveys();
            double rank = 0;
            for (Survey survey : surveys) {
                rank += survey.getRank();
            }
            JSONObject result = new JSONObject();
            try {
                result.put("idArtifact", artifact.getIdArtifact());
                result.put("nameArtifact", artifact.getNameArtifact());
                result.put("idBeacon",
                        artifact.getBeaconArtifact() != null ? artifact.getBeaconArtifact().getId() : null);
                result.put("category",
                        artifact.getCategoryArtifact() != null ? artifact.getCategoryArtifact().getNameCategory()
                                : null);
                result.put("location",
                        artifact.getLocationArtifact() != null ? artifact.getLocationArtifact().getPath() : null);
                result.put("total", (double) surveys.size());
                result.put("rank", (double) Math.round(rank * 10f / surveys.size()) / 10f);
                results.add(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return results.toString();
    }

    //change status Artifact by ID
    @RequestMapping(value = "/{id}/changeStatus", method = RequestMethod.POST)
    public String changeStatus(@PathVariable int id, @RequestBody String request) {
        String message = "";
        Artifact artifact = artifactDAO.find(id);

        if (null == artifact) {
            message += "Artifact not exist!";
        } else {
            try {
                JSONObject obj = new JSONObject(request);
                int idUser = obj.getInt("userArtifact");
                // Check null or more, if not pass throw the message type of ERROR
                User user = userDAO.find(idUser);
                if (null == user) {
                    message += "Please login to use this method;/r/n";
                } else {
                    artifact.setUserArtifact(user);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Throw ERROR message to user side if message != ""
        if ("".equals(message)) {
            assert artifact != null;
            Boolean status = artifact.getActive();
            status = null == status || !status;
            artifact.setIsActive(status);
            artifactDAO.edit(artifact);
            return JSONObject.quote("Success");
        } else {
            return JSONObject.quote("Change status failed: " + message);
        }
    }

    // get all media of artifact by id
    @RequestMapping(value = "/{id}/media", method = RequestMethod.GET)
    public Set<Media> getMedia(@PathVariable int id) {
        Set<Media> medias = new HashSet<Media>();
        Artifact artifact = artifactDAO.find(id);
        if (null != artifact) {
            medias = artifact.getMedias();
        }
        return medias;
    }

    //get location by Id 
    @RequestMapping(value = "/{id}/location", method = RequestMethod.GET)
    public List<Location> getLocation(@PathVariable int id) {
        List<Location> list = new ArrayList<Location>();
        Artifact artifact = artifactDAO.find(id);
        if (null != artifact) {
            Location minor = artifact.getLocationArtifact();
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

    // get beacon by Id
    @RequestMapping(value = "/{id}/beacon", method = RequestMethod.GET)
    public Beacon getBeacon(@PathVariable int id) {
        Beacon beacon = new Beacon();
        Artifact artifact = artifactDAO.find(id);
        if (null != artifact) {
            beacon = artifact.getBeaconArtifact();
        }
        return beacon;
    }

    //get category by id
    @RequestMapping(value = "/{id}/category", method = RequestMethod.GET)
    public Category getCategory(@PathVariable int id) {
        Category category = artifactDAO.find(id).getCategoryArtifact();
        if (null != category) {
            return category;
        } else {
            return null;
        }
    }

    //get survey by id
    @RequestMapping(value = "/{id}/survey", method = RequestMethod.GET)
    public Set<Survey> getSurveys(@PathVariable int id) {
        Set<Survey> surveys = new HashSet<Survey>();
        Artifact artifact = artifactDAO.find(id);
        if (null != artifact) {
            surveys = artifact.getsurveys();
        }
        return surveys;
    }

    private double roundToHalf(double d) {
        return Math.round(d * 2) / 2.0;
    }

    // get average rank of survey
    @RequestMapping(value = "/{id}/survey/rank", method = RequestMethod.GET)
    public Map<String, Double> getSurveyRank(@PathVariable int id) {
        Map<String, Double> result = new HashMap<String, Double>();
        Artifact artifact = artifactDAO.find(id);

        if (null != artifact) {
            Set<Survey> surveys = artifact.getsurveys();
            double rank = 0;
            for (Survey survey : surveys) {
                rank += survey.getRank();
            }
            result.put("total", (double) surveys.size());
            result.put("rank", roundToHalf(rank / surveys.size()));
        }
        return result;
    }

    //get user update by id
    @RequestMapping(value = "/{id}/user", method = RequestMethod.GET)
    public User getUser(@PathVariable int id) {
        User user = artifactDAO.find(id).getUserArtifact();
        if (null != user) {
            return user;
        } else {
            return null;
        }
    }
}
