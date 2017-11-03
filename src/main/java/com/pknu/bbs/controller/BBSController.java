package com.pknu.bbs.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.pknu.bbs.dto.BBSDto;
import com.pknu.bbs.service.BBSService;

@Controller
public class BBSController {
	//spring이 제공하는 애노태이션
	@Autowired 
//	Java가 지워하는 애노테이션 	@Inject
//	Java가 지원하는 애노테이션 	@Resource
//	@Autowired와 @Inject는 타입에 맞춰서 하는 반면, @Resource는 이름에 맞춰서 연결
	private BBSService bbsService;
	private BBSDto article;
	@RequestMapping("/list.bbs")
	public String list(int pageNum, Model model) { 
		bbsService.list(pageNum,model);
		model.addAttribute("pageNum",pageNum);
		return "list";
		
	}
	

//-----------------------------------------------------------------------------	

	
	@RequestMapping(value="/content.bbs")
	public String content(@RequestParam("articleNum") String articleNum,
						           @RequestParam String pageNum,
						          Model model){						
		article = bbsService.content(articleNum);			
		model.addAttribute("article", article);
		model.addAttribute("pageNum", pageNum);		
		return "content";
	}
	//-----------------------------------------------------------------------------	
	
	

	@RequestMapping(value="/write.bbs",method=RequestMethod.GET)
	public String write(HttpSession session) {
		if((String)session.getAttribute("id")==null){
			return "login";
		}
		return "writeForm";
	}
	
	@RequestMapping(value="/write.bbs",method=RequestMethod.POST)
	public String write(BBSDto article, HttpSession session) {
		article.setId((String)session.getAttribute("id"));
		bbsService.write(article);
		return "redirect:/list.bbs?pageNum=1";
	}
	
	//-----------------------------------------------------------------------------

	@RequestMapping(value="/login.bbs",method=RequestMethod.GET)
	public String loginForm(){
		return "login";
	}
	
	@RequestMapping(value="/login.bbs",method=RequestMethod.POST)
	public String login(@RequestParam("id")String id,@RequestParam("pass")String pass,HttpSession session) {
		return bbsService.login(id,pass,session);
	}
	
	@RequestMapping(value="/logout.bbs")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/list.bbs?pageNum=1";
	}
	//-----------------------------------------------------------------------------
	
	@RequestMapping(value="/delete.bbs")
	public String delete(String articleNum,String pageNum ) {
		String view;
		view=bbsService.delete(articleNum,pageNum);
		return view;
	}
	
	@RequestMapping(value="/download.bbs")
	public void download(HttpServletRequest req, HttpServletResponse resp) {
		try {
		bbsService.download(req,resp);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/replyForm.bbs")
	public String replyForm(BBSDto article ,Model model,@RequestParam("pageNum") String pageNum) {
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("depth", article.getDepth());
		model.addAttribute("groupId", article.getGroupId());
		model.addAttribute("pos", article.getPos());
		return "replyForm";
	}
	
	@RequestMapping(value="/reply.bbs")
	public String reply(BBSDto article,Model model, @RequestParam("title")String title, @RequestParam("content")String content,
			@RequestParam("fname")String fname ,@RequestParam("pageNum")String pageNum) {
		article.setTitle(title);
		article.setContent(content);
		article.setFname(fname);
		try {
		bbsService.reply(article,pageNum);
		}catch(Exception e) {
			e.printStackTrace();
		}
//		return view;
		return "redirect:/list.bbs?pageNum="+pageNum;
	}
	
	
	@RequestMapping(value="/updateForm.bbs")
	public String updateForm(String articleNum,String pageNum,Model model) {
		
		return bbsService.updateForm(articleNum,pageNum,model);
	}
	
	@RequestMapping(value="/update.bbs")
	public String update(HttpServletRequest req) {
		
		return bbsService.update(req);
	}
	
	@RequestMapping(value="/commentWrite.bbs")
	public void commentWrite(HttpServletRequest req,HttpServletResponse resp) {
		
		bbsService.commentWrite(req,resp);
	}
	
	
	@RequestMapping(value="/commentRead.bbs")
	public void commentRead(HttpServletRequest req,HttpServletResponse resp) {
		
		bbsService.commentRead(req,resp);
	}
}
