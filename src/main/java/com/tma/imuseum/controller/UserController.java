/**
 *
 */
package com.tma.imuseum.controller;

/**
 * @author pnhathung
 */

import com.tma.imuseum.model.dao.UserDAO;
import com.tma.imuseum.model.pojo.Artifact;
import com.tma.imuseum.model.pojo.Beacon;
import com.tma.imuseum.model.pojo.User;

import com.tma.imuseum.utils.Ares;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.Date;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDAO userDAO;
    private static Ares ares = new Ares();

    @Autowired
    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    //get User by id
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public User findById(@PathVariable int id) {
        User user = userDAO.find(id);
        if (null != user) {
            return user;
        } else {
            return null;
        }
    }

    //Get list of media and paging, default page is 1st, exp: domain/api/user?page=1
    @RequestMapping(method = RequestMethod.GET)
    public List<User> listUser(@RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "num", defaultValue = "10") int num,
            @RequestParam(value = "a", defaultValue = "1") Integer active,
            @RequestParam(value = "s", defaultValue = "") String search) {
        return userDAO.getList(page, num, active, search);
    }

    //create User
    @RequestMapping(method = RequestMethod.POST)
    public String create(@RequestBody String request) {
        User user = new User();
        String message = "";

        try {
            JSONObject obj = new JSONObject(request);
            String username = obj.getString("username");
            String password = obj.getString("password");
            String email = obj.getString("email");
            String name = obj.getString("name");
            // Check null or more, if not pass throw the message type of ERROR
            if (null == username) {
                message += "User name has null value;/r/n";
            } else {
                user.setUsername(username);
            }
            if (null == password) {
                message += "Password has null value;/r/n";
            } else {
                user.setPassword(Ares.getMD5_Base64(password));
            }
            if (null == email) {
                message += "Email has null value;/r/n";
            } else {
                user.setEmail(email);
            }
            if (null == name) {
                message += "Name has null value;/r/n";
            } else {
                user.setName(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Throw ERROR message to user side if message != ""
        if ("".equals(message)) {
            user.setDateCreated(new Date());
            user.setIsActive(true);
            userDAO.create(user);
            return JSONObject.quote("Success");
        } else {
            return JSONObject.quote("Create failed: " + message);
        }
    }

    /*@RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@RequestParam(value = "username", defaultValue = "") String userName,
            @RequestParam(value = "password", defaultValue = "") String passWord) {
        int flag = 0;
        if ("".equals(userName) || "".equals(passWord)) {
            return JSONObject.quote("Login Fail!!!");
        } else {
            List<User> user = userDAO.getList(-1, 10, 1, "");
            for (User u : user) {
                if (userName.equals(u.getUsername()) && passWord.equals(u.getPassword())) {
                    flag = 1;
                }
            }
        }
        if (flag == 1) {
            return JSONObject.quote("Success!!!");
        } else {
            return JSONObject.quote("Login Fail!!!");
        }
    
    }*/

    // Login and return MD5 cookies
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestBody String request) {
        JSONObject result = new JSONObject();
        try {
            JSONObject obj = new JSONObject(request);
            String userName = obj.getString("username");
            String passWord = obj.getString("password");
            String passWordEnc = Ares.getMD5_Base64(passWord);
            if ("".equals(userName) || "".equals(passWord)) {
                return JSONObject.quote("Error");
            } else {
                List<User> user = userDAO.getList(1, -1, 1, "");
                for (User u : user) {
                    if (userName.equals(u.getUsername()) && passWordEnc.equals(u.getPassword())) {
                        Date loginTime = new Date();
                        long loginTimes = loginTime.getTime();
                        String cookies = Ares.getMD5_Base64(u.getName() + " " + Long.toString(loginTimes));
                        u.setLogin(loginTime);
                        u.setCookies(cookies);
                        userDAO.edit(u);
                        result.put("user", cookies);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    // Logout and save the logout time
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public void logout(@RequestBody String request) {
        try {
            JSONObject obj = new JSONObject(request);
            String cookies = obj.getString("user");
            List<User> user = userDAO.getList(1, -1, 1, "");
            for (User u : user) {
                if (u.getCookies().equals(cookies)) {
                    u.setLogout(new Date());
                    userDAO.edit(u);
                    logger.info("Has been loged out!! " + u.getLogout());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Check is the right user
    @RequestMapping(value = "/authorize", method = RequestMethod.POST)
    public String authorize(@RequestBody String request) {
        JSONObject result = new JSONObject();
        Boolean flag = false;
        try {
            JSONObject obj = new JSONObject(request);
            String cookies = obj.getString("user");
            List<User> user = userDAO.getList(1, -1, 1, "");
            for (User u : user) {
                if (u.getCookies().equals(cookies)) {
                    flag = true;
                }
            }
            if (flag) {
                result.put("authorize", true);
            } else {
                result.put("authorize", false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    //update User by ID
    @RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
    public String edit(@PathVariable int id, @RequestBody String request) {
        String message = "";
        //int idUser = userDAO.find(id).getIdUser();
        User user = userDAO.find(id);
        if (null != user) {
            try {
                JSONObject obj = new JSONObject(request);
                String username = obj.getString("username");
                String password = obj.getString("password");
                String email = obj.getString("email");
                String name = obj.getString("name");
                // Check null or more, if not pass throw the message type of ERROR
                if (null == username) {
                    message += "User name has null value;/r/n";
                } else {
                    user.setUsername(username);
                }
                if (null == password) {
                    message += "Password has null value;/r/n";
                } else {
                    user.setPassword(password);
                }
                if (null == email) {
                    message += "Email has null value;/r/n";
                } else {
                    user.setEmail(email);
                }
                if (null == name) {
                    message += "Name has null value;/r/n";
                } else {
                    user.setName(name);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Throw ERROR message to user side if message != ""
            if ("".equals(message)) {
                userDAO.edit(user);
                return JSONObject.quote("Success");
            } else {
                return JSONObject.quote("Update failed: " + message);
            }
        } else {
            return JSONObject.quote("Update failed");
        }
    }

    //delete User by ID
    public String delete(@PathVariable int id) {
        String message = "";
        User temp = userDAO.find(id);
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
                userDAO.edit(temp);
                return JSONObject.quote("Success");
            } else {
                return JSONObject.quote("Delete failed: ");
            }
        } else {
            return JSONObject.quote("Delete failed");
        }
    }
    // @RequestMapping(value = "/{id}/delete", method = RequestMethod.DELETE)
    // public String delete(@PathVariable int id) {
    //     if (userDAO.find(id) != null) {
    //         userDAO.remove(id);
    //         return JSONObject.quote("Success");
    //     } else {
    //         return JSONObject.quote("Not found user with id: " + id);
    //     }
    // }

    //get Beacon user updated by id
    @RequestMapping(value = "/{id}/beacon", method = RequestMethod.GET)
    public Set<Beacon> getBeaconsU(@PathVariable int id) {
        Set<Beacon> beacons = userDAO.find(id).getBeacons();
        if (null != beacons) {
            return beacons;
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/{id}/changeStatus", method = RequestMethod.POST)
    public String changeStatus(@PathVariable int id, @RequestBody String request) {
        String message = "";
        User user = userDAO.find(id);

        if (null == user) {
            message += "User not exist!";
        } else {
            try {
                JSONObject obj = new JSONObject(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Throw ERROR message to user side if message != ""
        if ("".equals(message)) {
            assert user != null;
            Boolean status = user.getIsActive();
            status = null == status || !status;
            user.setIsActive(status);
            userDAO.edit(user);
            return JSONObject.quote("Success");
        } else {
            return JSONObject.quote("Change status failed: " + message);
        }
    }

    //get Aritifact user updated by id
    @RequestMapping(value = "/{id}/artifact", method = RequestMethod.GET)
    public Set<Artifact> getArtifactU(@PathVariable int id) {
        Set<Artifact> artifacts = userDAO.find(id).getArtifacts();
        if (null != artifacts) {
            return artifacts;
        } else {
            return null;
        }
    }
}
