package com.pknu.bbs.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;

import com.pknu.bbs.dto.BBSDto;

public interface BBSService {
	public void list(int pageNum,Model model);
	//-----------------------------------------------------------------------------	
//	public void content(HttpServletRequest req, Model model) ;
	public BBSDto content(String articleNum); 
//-----------------------------------------------------------------------------	
//	public String write(HttpServletRequest req) throws ServletException, IOException;
	public void write(BBSDto article);
//-----------------------------------------------------------------------------	
//	public String login(HttpServletRequest req);
	public String login(String id,String pass,HttpSession session);
	//-----------------------------------------------------------------------------	
	

	public String delete(String articleNum,String pageNum );
	public void download(HttpServletRequest req,HttpServletResponse resp)throws ServletException, IOException;
	public String reply(BBSDto article,String pageNum);
	public String updateForm(String articleNum,String pageNum,Model model);
	public String update(HttpServletRequest req);
	public String commentWrite(HttpServletRequest req, HttpServletResponse resp);
	public void commentRead(HttpServletRequest req,HttpServletResponse resp);
}
