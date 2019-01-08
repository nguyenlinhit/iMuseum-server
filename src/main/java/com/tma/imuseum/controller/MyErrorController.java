// package com.tma.imuseum.controller;

// import org.springframework.boot.autoconfigure.web.ErrorController;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestMethod;
// import org.springframework.web.bind.annotation.RestController;

// @RestController
// // @RequestMapping("/error")
// public class MyErrorController implements ErrorController{

//     private static final String ERROR_MAPPING = "/error";

//     @RequestMapping(ERROR_MAPPING)
//     public String getDataError(){
//         return "No data exist!";
//     }

//     @Override
//     public String getErrorPath() {
//         return ERROR_MAPPING;
//     }
// }