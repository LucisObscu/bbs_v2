package com.pknu.bbs.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.pknu.bbs.dto.BBSDto;

public class ListRowMapper implements RowMapper<BBSDto> {
	BBSDto article=null;
	@Override
	public BBSDto mapRow(ResultSet rs, int arg1) throws SQLException {		
		article = new BBSDto();
		article.setArticleNum(rs.getInt("articleNum"));
		article.setId(rs.getString("id"));
		article.setTitle(rs.getString("title"));			
		article.setDepth(rs.getInt("depth"));
		article.setHit(rs.getInt("hit"));			
		article.setWriteDate(rs.getString("wDate"));		
	
		return article;		
	}

	

}
