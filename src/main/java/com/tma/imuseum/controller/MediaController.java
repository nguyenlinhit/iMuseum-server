package com.tma.imuseum.controller;

import com.tma.imuseum.model.dao.ArtifactDAO;
import com.tma.imuseum.model.dao.MediaDAO;
import com.tma.imuseum.model.pojo.Artifact;
import com.tma.imuseum.model.pojo.Media;
import com.tma.imuseum.utils.Ares;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nvulinh on 19/06/2017.
 * Media Controller
 */
@RestController
@RequestMapping(value = "/api/media")
public class MediaController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MediaDAO mediaDAO;
    private final ArtifactDAO artifactDAO;

    //private String UPLOADED_FOLDER_MODEL3D = "resources/images/model3d/";
    private Ares ares = new Ares();

    @Autowired
    public MediaController(MediaDAO mediaDAO, ArtifactDAO artifactDAO) {
        this.mediaDAO = mediaDAO;
        this.artifactDAO = artifactDAO;
    }

    //get media by ID
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Media findById(@PathVariable int id) {
        Media media = mediaDAO.find(id);
        if (null != media) {
            return media;
        } else {
            return null;
        }
    }

    //Get list of media and paging, default page is 1st, exp: domain/api/media?page=1
    @RequestMapping(method = RequestMethod.GET)
    public List<Media> listBeacon(@RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "num", defaultValue = "-1") int num,
            @RequestParam(value = "a", defaultValue = "1") int active,
            @RequestParam(value = "s", defaultValue = "") String search) {
        //If you want get all list, set page < 0
        return mediaDAO.getList(page, num, active, search);
    }

    //get list media not map
    @RequestMapping(value = "/notmap", method = RequestMethod.GET)
    public List<Media> listMediaNotMap() {
        List<Media> medias = mediaDAO.getList(0, 10, 1, "");
        List<Media> mediasNotMap = new ArrayList<Media>();
        for (Media media : medias) {
            if (null == media.getArtifactMedia()) {
                mediasNotMap.add(media);
            }
        }
        return mediasNotMap;
    }

    //create Media
    @RequestMapping(method = RequestMethod.POST)
    public String create(@RequestBody String request) {
        String message = "";
        Media media = new Media();

        try {
            JSONObject obj = new JSONObject(request);
            int id = obj.getInt("artifactMedia");
            String mediaName = obj.getString("media");
            // Check null or more, if not pass throw the message type of ERROR
            Artifact artifact = artifactDAO.find(id);
            media.setArtifactMedia(artifact);
            if (null == mediaName) {
                message += "Name Media has null value;/r/n";
            } else {
                media.setMedia(mediaName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Throw ERROR message to user side if message != ""
        if ("".equals(message)) {
            mediaDAO.create(media);
            return JSONObject.quote("Success");
        } else {
            return JSONObject.quote("Create failed: " + message);
        }
    }

    //update Beacon by ID
    @RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
    public String edit(@PathVariable int id, @RequestBody String request) {
        String message = "";
        Media media = mediaDAO.find(id);
        // Check null or more, if not pass throw the message type of ERROR
        if (null != media) {
            try {
                JSONObject obj = new JSONObject(request);
                String mediaName = obj.getString("media");
                if (null == mediaName) {
                    message += "Name Media has null value;/r/n";
                } else {
                    media.setMedia(mediaName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Throw ERROR message to user side if message != ""
            if ("".equals(message)) {
                mediaDAO.edit(media);
                return JSONObject.quote("Success");
            } else {
                return JSONObject.quote("Update failed " + id);
            }
        } else {
            return JSONObject.quote("Update failed");
        }
    }

    //delete Media by ID
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    public String remove(@PathVariable int id) {
        if (mediaDAO.find(id) != null) {
            mediaDAO.remove(id);
            return JSONObject.quote("Success");
        } else {
            return JSONObject.quote("Not found Media with Id: " + id);
        }
    }

    //get artifact by id
    @RequestMapping(value = "/{id}/artifact", method = RequestMethod.GET)
    public Artifact getArtifact(@PathVariable int id) {
        Artifact artifact = mediaDAO.find(id).getArtifactMedia();
        if (null != artifact) {
            return artifact;
        } else {
            return null;
        }
    }

    //upload media to Medias
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    // public String singleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

    public String singleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("title") String title,
            @RequestParam("idArtifact") int idArtifact) {
        String extension = "";
        logger.info("File: " + file + " Title: " + title + " Artifact: " + idArtifact);
        // logger.info("Files: " + file + "- Redirect: " + redirectAttributes);
        Path path = null;
        if (file.isEmpty()) {
            // redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return JSONObject.quote("Fail");
        }
        try {
            //get the file and save it somewhere

            byte[] bytes = file.getBytes();
            String url = "";
            String root = "src/main/webapp/";
            String filename = file.getOriginalFilename();

            // Get file extension
            extension = ares.getLastStringSplitBy(filename, ".");
            if (extension.equals("jpeg") || extension.equals("png") || extension.equals("bmp")
                    || extension.equals("jpg") || extension.equals("gif") || extension.equals("svg")) {
                String UPLOADED_FOLDER_IMAGE = "resources/images/resize/";
                url = UPLOADED_FOLDER_IMAGE;
            } else if (extension.equals("mp4") || extension.equals("3gp") || extension.equals("wmv")
                    || extension.equals("avi") || extension.equals("mpeg")) {
                String UPLOADED_FOLDER_VIDEO = "resources/images/video/";
                url = UPLOADED_FOLDER_VIDEO;
            }
            path = Paths.get(root + url + filename);
            assert path != null;
            Files.write(path, bytes);

            // Create new media and store information to database
            Media media = new Media();
            Artifact artifact = artifactDAO.find(idArtifact);
            if (null != artifact) {
                media.setArtifactMedia(artifact);
            } else {
                media.setArtifactMedia(null);
            }
            media.setTitle(title);
            media.setMedia(url + filename);
            mediaDAO.create(media);
            // redirectAttributes.addFlashAttribute("message", "You successfuly uploaded '" + file.getOriginalFilename() + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSONObject.quote("Success");
    }
}
