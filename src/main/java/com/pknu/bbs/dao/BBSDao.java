package com.pknu.bbs.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pknu.bbs.dto.BBSDto;
import com.pknu.bbs.dto.CommentDto;

public interface BBSDao {
	public int getTotalCount() throws SQLException, IOException;
	public List<BBSDto> getArticleList(String startRow, String endRow);
	public int loginCheck(String id, String pass)throws SQLException, IOException;
	public void write(BBSDto article);
	public BBSDto content(String articleNum)throws SQLException, IOException;
	public void delete(String articleNum);
	public void replyArticle(BBSDto article) throws IOException, SQLException;
	public BBSDto getUpdateArticle(String articleNum)throws SQLException, IOException;
	public void updateArticle(String articleNum, String title, String content)throws IOException, SQLException;
	public void insertComment(String articleNum,String id,  String commentContent) throws  SQLException, IOException;
	public ArrayList<CommentDto> getComments(String articleNum,String commentRow)throws SQLException, IOException;
}
