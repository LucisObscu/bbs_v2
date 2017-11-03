package com.pknu.bbs.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.pknu.bbs.dto.BBSDto;
import com.pknu.bbs.dto.CommentDto;
import com.pknu.bbs.dto.LoginStatus;
import com.pknu.bbs.mapper.ContentRowMapper;
import com.pknu.bbs.mapper.ListRowMapper;

@Repository
public class BBSDaoImpl implements BBSDao {	
	private OracleDBConnector oracleDBConnector =OracleDBConnector.getInstance();	
	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	StringBuffer query;
	ArrayList<BBSDto> articleList;
	BBSDto article;
	@Autowired
	JdbcTemplate jdbcTemplate;	
	@Override
	public int getTotalCount(){		
		return jdbcTemplate.queryForObject("select count(*) from bbs",Integer.class);		
	}

	public List<BBSDto> getArticleList(String startRow, String endRow){			
		query= new StringBuffer();
		query.append("SELECT articleNum,id,title,depth,hit,");
//		query.append("		to_char(writeDate) wdate FROM ");
		query.append("		to_char(writeDate,'YYYY-MM-DD') wdate FROM ");
		query.append("		(SELECT ROWNUM RM, human.* FROM ");
		query.append("         		    (SELECT * FROM  BBS "); 
		query.append("       ORDER BY groupId desc, pos ) human) "); 
		query.append("WHERE RM BETWEEN ? AND ?");	
		return jdbcTemplate.query(query.toString(), 
				new Object[]{startRow, endRow}, new ListRowMapper());
	}
	
	public long getCommentCount(int articleNum) throws  SQLException, IOException{
		con=oracleDBConnector.getConnection();
		query=new StringBuffer();
		query.append("select count(*) from comments where articleNum=?");
		pstmt=con.prepareStatement(query.toString());
		pstmt.setInt(1, articleNum);
		ResultSet rs1=pstmt.executeQuery();
		int commentCount=0;
		
		if(rs1.next()){
			commentCount=rs1.getInt(1);
		}
		rs1.close();
		pstmt.close();
		con.close();
		return commentCount;
	}
	
	public ArrayList<CommentDto> getComments(String articleNum,String commentRow)throws SQLException, IOException{
		con=oracleDBConnector.getConnection();
		query=new StringBuffer();
//		rownum 1�� �׻� �����ϹǷ� �ٷ� ��밡��
		query.append("select * ");		
		query.append("from (select * ");
		query.append("       from comments ");
		query.append("     	where articleNum=? ");
		query.append("     	order by commentNum desc ) human ");
		query.append("where rownum between 1 and ?");
		
		pstmt=con.prepareStatement(query.toString());
		pstmt.setString(1, articleNum);
		pstmt.setString(2, commentRow);
		rs=pstmt.executeQuery();
		ArrayList<CommentDto> commentList=new ArrayList<>();
		CommentDto cd = null;
		while(rs.next()){
			cd = new CommentDto();
			cd.setCommentNum(rs.getInt("commentNum"));
			cd.setId(rs.getString("id"));
			cd.setCommentContent(rs.getString("commentContent"));
			cd.setCommentDate(rs.getString("commentDate"));			
			cd.setArticleNum(rs.getInt("articleNum"));			
			commentList.add(cd);			
		}
		streamClose();
		return commentList;		
	}
	
	public void write(BBSDto article){
		query= new StringBuffer();		
		query.append("INSERT INTO BBS ");	
		query.append("values(bbs_seq.nextval,?,?,?,0,0,");	
		query.append("bbs_seq.currval,0,sysdate,?)");
		jdbcTemplate.update(query.toString(), 
							new Object[]{article.getId(),article.getTitle(),
									article.getContent(),article.getFname()});		
	}	
	
	public int loginCheck(String id, String pass)throws SQLException, IOException{
		query= new StringBuffer();				
		query.append("SELECT PASS FROM LOGIN WHERE ID=? ");			
		int loginStatus =0;
		List<String> dbPass=jdbcTemplate.queryForList(query.toString(),new Object[]{id}, String.class);
				
		if(dbPass.size()==1){
			if(pass.equals(dbPass.get(0))){
				loginStatus =LoginStatus.LOGIN_SUCCESS;
			}else{
				loginStatus =LoginStatus.PASS_FAIL;
			}			
		}else{
			loginStatus =LoginStatus.NOT_MEMBER;
		}		
		
		return loginStatus;		
	}
	
	public BBSDto content(String articleNum)throws SQLException, IOException{
		con=oracleDBConnector.getConnection();
		query= new StringBuffer();				
		query.append("SELECT * FROM BBS WHERE ARTICLENUM=? ");			
		
		return jdbcTemplate.queryForObject(query.toString(),
				new Object[] {articleNum}, new ContentRowMapper());		
	}
	
	public void replyArticle(BBSDto article) throws IOException, SQLException {
		con=oracleDBConnector.getConnection();		
		String sql="update bbs set pos=pos+1 where groupId=? and pos>?";				
	    pstmt=con.prepareStatement(sql);
		pstmt.setInt(1,article.getGroupId());
		pstmt.setInt(2,article.getPos());
		pstmt.executeUpdate();
		
		query= new StringBuffer();
		query.append("insert into bbs values(bbs_seq.nextval,");
		query.append("?,?,?,?,0,?,?,sysdate,?)");				
	    pstmt=con.prepareStatement(query.toString());
		pstmt.setString(1,article.getId());
		pstmt.setString(2,article.getTitle());
		pstmt.setString(3,article.getContent());
		pstmt.setInt(4,article.getDepth()+1);
		pstmt.setInt(5,article.getGroupId());
		pstmt.setInt(6,article.getPos()+1);			
		pstmt.setString(7,article.getFname());			
		pstmt.executeUpdate();
		
		streamClose();			
	}	
	
	public void delete(String articleNum){
		
		con=oracleDBConnector.getConnection();		
		
		query= new StringBuffer();
		query.append("delete from bbs where articleNum=?");					
		jdbcTemplate.update(query.toString(), 
							new Object[]{articleNum});		
	}	
	
	public BBSDto getUpdateArticle(String articleNum)throws SQLException, IOException{
		con=oracleDBConnector.getConnection();
		query= new StringBuffer();				
		query.append("SELECT TITLE,CONTENT FROM BBS WHERE ARTICLENUM=? ");				
		jdbcTemplate.update(query.toString(),new Object[]{articleNum}, new ListRowMapper());
			
		
		
		return article;		
	}
	
	public void updateArticle(String articleNum, String title, String content)throws IOException, SQLException {
		con=oracleDBConnector.getConnection();
		query = new StringBuffer();
		query.append("update bbs ");
		query.append("set title=?, content=? ");
		query.append("where articleNum=?" );							
	    pstmt=con.prepareStatement(query.toString());
	    pstmt.setString(1,title);
	    pstmt.setString(2,content);
		pstmt.setString(3,articleNum);
		pstmt.executeUpdate();
		streamClose();
	}
	
	public void insertComment(String articleNum,String id,  String commentContent) throws  SQLException, IOException {
		con=oracleDBConnector.getConnection();
		query=new StringBuffer();
		query.append("insert into comments values(comment_seq.nextval,?,?,sysdate,?)");
		pstmt=con.prepareStatement(query.toString());
		pstmt.setString(1, id);
		pstmt.setString(2, commentContent);
		pstmt.setString(3, articleNum);		
		pstmt.executeUpdate();
		
		streamClose();
	}
	
//	public ArrayList<LoginDto> getAllLoign() throws IOException, SQLException {
//		con=oracleDBConnector.getConnection();
//		query= new StringBuffer();				
//		query.append("SELECT * FROM Login ");		
//		pstmt=con.prepareStatement(query.toString());	
//		rs=pstmt.executeQuery();
//		LoginDto login = null;		
//		ArrayList<LoginDto> loginList =new ArrayList<>();		
//		while(rs.next()){
//			login= new LoginDto();		
//			login.setId(rs.getString("id"));
//			login.setPass(rs.getString("pass"));			
//			loginList.add(login);			
//		}
//		streamClose();		
//		return loginList;
//	}
	
//	public LoginDto getOneLogin(String id) throws IOException, SQLException{
//		con=oracleDBConnector.getConnection();
//		query= new StringBuffer();				
//		query.append("SELECT * FROM Login where id=? ");		
//		pstmt=con.prepareStatement(query.toString());	
//		pstmt.setString(1, id);
//		rs=pstmt.executeQuery();
//		LoginDto login = null;		
//		
//		if(rs.next()){
//			login= new LoginDto();		
//			login.setId(rs.getString("id"));
//			login.setPass(rs.getString("pass"));							
//		}
//		streamClose();		
//		return login;
//	}
	
	public void streamClose(){
		try{
			if(rs!=null){
				rs.close();
			}
			pstmt.close();
			con.close();
		}catch(Exception e){
			e.printStackTrace();
		}			
	}

}
