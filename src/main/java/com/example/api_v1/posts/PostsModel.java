package com.example.api_v1.posts;


public class PostsModel {
	private Integer id;
	private Integer userId;
	private Integer contentId;

	private String title;
	private String description;

	public PostsModel(Integer id, Integer userId, Integer contentId, String title, String description) {
		this.id 	= id;
		this.userId = userId;
		this.contentId = contentId;
		this.title 	= title;
		this.description = description;
	}

	public Integer getContentId(){
		return contentId;
	}

	public String getTitle(){
		return title;
	}

	public String getDescription(){
		return description;
	}

	public Integer getUserId(){
		return userId;
	}

}