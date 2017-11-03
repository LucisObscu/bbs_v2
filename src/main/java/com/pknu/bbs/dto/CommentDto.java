package com.pknu.bbs.dto;

/**
 * @author OBSCU
 *
 */
public class CommentDto {
	private int commentNum;
	private String id;
	private String commentContent;
	private String commentDate;
	private int articleNum;
	/**
	 * @return the commentNum
	 */
	public int getCommentNum() {
		return commentNum;
	}
	/**
	 * @param commentNum the commentNum to set
	 */
	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the commentContent
	 */
	public String getCommentContent() {
		return commentContent;
	}
	/**
	 * @param commentContent the commentContent to set
	 */
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	/**
	 * @return the commentDate
	 */
	public String getCommentDate() {
		return commentDate;
	}
	/**
	 * @param commentDate the commentDate to set
	 */
	public void setCommentDate(String commentDate) {
		this.commentDate = commentDate;
	}
	/**
	 * @return the articleNum
	 */
	public int getArticleNum() {
		return articleNum;
	}
	/**
	 * @param articleNum the articleNum to set
	 */
	public void setArticleNum(int articleNum) {
		this.articleNum = articleNum;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CommentDto [commentNum=" + commentNum + ", id=" + id + ", commentContent=" + commentContent
				+ ", commentDate=" + commentDate + ", articleNum=" + articleNum + "]";
	}
	
}
