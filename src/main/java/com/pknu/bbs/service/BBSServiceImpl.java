package com.pknu.bbs.service;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.pknu.bbs.common.Page;
import com.pknu.bbs.dao.BBSDao;
import com.pknu.bbs.dto.BBSDto;
import com.pknu.bbs.dto.CommentDto;
import com.pknu.bbs.dto.LoginStatus;


@Service
public class BBSServiceImpl implements BBSService {
	@Autowired
	BBSDao bbsDao;

	@Autowired
	Page page;
	@Resource(name="pageSize")
	Integer pageSize;
	@Resource(name="pageBlock")
	Integer pageBlock;
	
	private BBSDto article;

	@Override
	public void list(int pageNum,Model model) {
		int totalCount=0;	
		ArrayList<BBSDto> articleList = null;
		HashMap<String, String> pagingMap = null;
		
		try {	
			totalCount=bbsDao.getTotalCount();			
			pagingMap=page.paging(pageNum,totalCount,pageSize, pageBlock);				
			articleList=
					(ArrayList<BBSDto>)bbsDao.getArticleList(pagingMap.get("startRow"),pagingMap.get("endRow"));
	   	}catch (Exception e) {
			// TODO: handle exception
		}
		model.addAttribute("totalCount",totalCount);		
		model.addAttribute("articleList",articleList);
		model.addAttribute("pageCode",pagingMap.get("pageCode"));			
	}

	//-----------------------------------------------------------------------------------------------------------------------------------------------------	

	@Override
	public BBSDto content(String articleNum) {	
		try {
			article=bbsDao.content(articleNum);
		}catch (Exception e) {
			// TODO: handle exception
		}
		return article;
	}
//-----------------------------------------------------------------------------------------------------------------------------------------------------
//	public String write(HttpServletRequest req) throws ServletException, IOException {
//		req.setCharacterEncoding("UTF-8");
//		String saveDir=req.getServletContext().getInitParameter("saveDir");
//		
//		BBSDto article = new BBSDto();
//		
//		
//		
//
//	
//		article.setTitle(readParameterValue(req.getPart("title")));
//		article.setContent(readParameterValue(req.getPart("content")));
//		article.setId((String)req.getSession().getAttribute("id"));
//		if(req.getPart("fname").getSize()!=0) {
//			Part filePart = req.getPart("fname");
//			String orginFname = getFileName(filePart);
//			article.setFname(orginFname);
//			
//			File file = new File(saveDir+orginFname);
//			InputStream is = filePart.getInputStream();
//			FileOutputStream os = new FileOutputStream(file);
//			int temp = -1;
//			while ((temp = is.read())!=-1) {
//				os.write(temp);			
//			}
//			is.close();
//			os.close();
//		}
//
//		try {
//			bbsDao.write(article);
//		}catch(Exception e){
//			
//		}
//		return "redirect:list.bbs?pageNum=1";
//	}
	@Override
	public void write(BBSDto article) {
			bbsDao.write(article);
	}
	
	//----------------------------------------------------------------------------------

	@Override
	public String login(String id, String pass,HttpSession session) {
		String view=null;
		int loginStatus=0;
		try{
			loginStatus=bbsDao.loginCheck(id, pass);
		}catch (Exception e) {
			e.printStackTrace();
		}
		if(loginStatus==LoginStatus.LOGIN_SUCCESS){
			session.setAttribute("id", id);
			view="redirect:/list.bbs?pageNum=1";
		}else if(loginStatus==LoginStatus.PASS_FAIL){
			view="passFail";
		}else{
			view="joinMember";
		}
		return view;	
	}


	//-------------------------------------------------------------
	public String getFileName(Part filePart) {
		String  orifinFname=null;
		for(String cd:filePart.getHeader("Content-Disposition").split(";")) {
			if(cd.trim().startsWith("filename")) {
				orifinFname=cd.substring(cd.indexOf('=')+1).trim().replace("\"", "");
			}
		}

		return orifinFname;
	};
	
	private String readParameterValue(Part part) throws IOException {
		InputStreamReader reader =new InputStreamReader(part.getInputStream(),"utf-8");
		int temp = -1;
		StringBuilder builder = new StringBuilder();
		while ((temp = reader.read()) != -1) {
			//char�� ����ȯ�ؾ� ���ڷε�
			builder.append((char)temp);
		}
		return builder.toString();
	}
//------------------------------------------------------------------------------------------------------------



	
	public String delete(String articleNum,String pageNum ) {
				
			bbsDao.delete(articleNum);
		
		return "redirect:list.bbs?pageNum="+pageNum;
	}

	@Override
	public void download(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
		String fname=req.getParameter("fname");
		String saveDir=req.getServletContext().getInitParameter("saveDir");
		
		File file = new File(saveDir+fname);
		resp.setContentType("application/download");
		resp.setContentLength((int)file.length());
	
		fname = new String(fname.getBytes("utf-8"),	"iso-8859-1");
		
		resp.setHeader("Content-Disposition", "attachment; filename=\""+fname+ "\";");		
		OutputStream out = resp.getOutputStream();
		FileInputStream fis = null;
		try {
			int temp;
			fis = new FileInputStream(file);
			while((temp=fis.read())!=-1) {
				out.write(temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(fis!=null) {
				try {
					fis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
//---------------------------------------------------------------------------------------------------------
	@Override
	public String reply(BBSDto article,String pageNum){
//		req.setCharacterEncoding("utf-8");
//		String saveDir=req.getServletContext().getInitParameter("saveDir");
//		if(req.getPart("fname").getSize()!=0) {
//			Part filePart = req.getPart("fname");
//			String orginFname = getFileName(filePart);
//			article.setFname(orginFname);
//			
//			File file = new File(saveDir+orginFname);
//			InputStream is = filePart.getInputStream();
//			FileOutputStream os = new FileOutputStream(file);
//			int temp = -1;
//			while ((temp = is.read())!=-1) {
//				os.write(temp);			
//			}
//			is.close();
//			os.close();
//		}		
		try{
			bbsDao.replyArticle(article); 
		}catch(Exception e){
			e.printStackTrace();
		}

		return "/bbs/list.bbs?pageNum="+pageNum;
	}
	//---------------------------------------------------------------------------------------------------------

	@Override
	public String updateForm(String articleNum,String pageNum,Model model) {
		BBSDto article = null;
		try {
			article=bbsDao.getUpdateArticle(articleNum);
		}catch(Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("article", article);
		model.addAttribute("pageNum", pageNum);
		return "updateForm";
	}

	@Override
	public String update(HttpServletRequest req) {
		try {
		req.setCharacterEncoding("utf-8");
		}catch(Exception e) {
			e.printStackTrace();
		}
		String articleNum=req.getParameter("articleNum");		
		String pageNum=req.getParameter("pageNum");
		String title=req.getParameter("title");
		String content=req.getParameter("content");
		
		try{
			bbsDao.updateArticle(articleNum,title,content);
		}catch(Exception e){
			e.printStackTrace();
		}
		 
		return "redirect:content.bbs?pageNum="+pageNum+"&articleNum="+articleNum; 
	}

	@Override
	public String commentWrite(HttpServletRequest req,HttpServletResponse resp) {
		String commentContent =req.getParameter("commentContent");
		String articleNum =req.getParameter("articleNum");
		String id = (String)req.getSession().getAttribute("id");
		ArrayList<CommentDto> commentList=null;
		try {		
			bbsDao.insertComment(articleNum,id,commentContent);
			commentList=bbsDao.getComments(articleNum,"10");
			
		}catch (Exception e) {			
		}
		resp.setCharacterEncoding("utf-8");
		JSONArray jb= new JSONArray(commentList);
		PrintWriter pw=null;;
		try {
		pw = resp.getWriter();
		}catch(Exception e) {
			e.getStackTrace();
		}
		pw.println(jb.toString());
		return null;
	}

	@Override
	public void commentRead(HttpServletRequest req, HttpServletResponse resp) {
		String articleNum=req.getParameter("articleNum");
		String commentRow=req.getParameter("commentRow");
		ArrayList<CommentDto> commentList=null;
		try{
			commentList=bbsDao.getComments(articleNum, commentRow);
		}catch(Exception e){
			e.printStackTrace();
		}
		resp.setCharacterEncoding("utf-8");
		JSONArray jb= new JSONArray(commentList);
		PrintWriter pw=null;
		try{
		pw= resp.getWriter();
		}catch(Exception e) {
			e.printStackTrace();
		}
		pw.println(jb.toString());
	}
	
	
	
	
}
