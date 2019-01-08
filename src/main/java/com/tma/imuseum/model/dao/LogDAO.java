package com.tma.imuseum.model.dao;

import java.util.List;

import com.tma.imuseum.model.pojo.Log;


public interface LogDAO {
	
	Log find(int id);

	void create(Log log);
	
	void remove(int id);
	
	void edit(Log log);
	
	// Get pagination
	// page: page number, page < 0 function will get all element
	// num: maximum element per page
	List<Log> getList(int page, int num, int active, String search);

	// need to add filter 
}