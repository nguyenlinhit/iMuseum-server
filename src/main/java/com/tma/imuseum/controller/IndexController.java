package com.tma.imuseum.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.*;
import java.lang.reflect.Field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.tma.imuseum.configuration.IgnoreTest;
import com.tma.imuseum.model.dao.ApiDAO;
import com.tma.imuseum.model.pojo.Api;

@Controller
public class IndexController {
	private final ApiDAO dao;

	@Autowired
	public IndexController(ApiDAO dao) {
		this.dao = dao;
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView firstPage() {
		List<Api> result = dao.getList(-1, 10, 1, "");
		return new ModelAndView("index", "result", result);
	}

	@RequestMapping("/login")
	public ModelAndView loginPage() {
		return new ModelAndView("login");
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ModelAndView homePage() {
		List<Api> result = dao.getList(-1, 10, 1, "");
		return new ModelAndView("home", "result", result);
	}

	@RequestMapping("/content/{id}")
	public ModelAndView welcomePage(@PathVariable int id) {
		Map<String, Object> rs = new HashMap<String, Object>();
		Api result = dao.find(id);
		if (null != result) {
			List<String> attribute = new ArrayList<String>();
			if (!"GET".equals(result.getMethod())) {
				try {
					Class<?> act = Class.forName("com.tma.imuseum.model.pojo." + result.getGroup());
					Field[] fields = act.getDeclaredFields();
					for (Field field : fields) {
						if (null == field.getAnnotation(IgnoreTest.class)) {
							attribute.add(field.getName());
						}
					}
				} catch (Exception e) {
					//TODO: handle exception
				}
			}
			rs.put("apiList", result);
			rs.put("apiAttr", attribute);
		}
		return new ModelAndView("content", "result", rs);
	}
}
