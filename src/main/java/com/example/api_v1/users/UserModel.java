package com.example.api_v1.users;

import java.util.ArrayList;
import java.util.List;

/**
 * Definição do modelo de usuário. (Contém validação)
 * @author Gustavo Marques (@gutodisse)
 * @version 0.01
 * @since MVP 01
 */

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


public class UserModel {

	@NotNull(message = "Id é obrigatório!")
	@Min(0)
	private Integer id;

	@NotEmpty(message = "Nome é obrigatório!")
	private String name;

	@NotEmpty(message = "Usuário é obrigatório!")
	private String username;

	@NotEmpty(message = "Senha é obrigatório!")
	private String password;

	private String description;
	private String photoPath;
	private List<Integer> friendList;
	
	public UserModel(Integer id, String name, String username, String password, String description, String photoPath) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.photoPath = photoPath;
		this.username = username;
		this.password = password;
		this.friendList = new ArrayList<>();
	}

	public void addToFriendList(Integer userId){
		this.friendList.add(userId);
	}

	public List<Integer> getFriendList(){
		return this.friendList;
	}

	public void setId(Integer newId) {
		this.id = newId;
	}

	public void setName(String newName){
		this.name = newName;
	}

	public void setDescription(String newDescription){
		this.description = newDescription;
	}

	public void setPhotoPath(String newPhotoPath){
		this.photoPath = newPhotoPath;
	}

	public void setUsername(String newUsername){
		this.username = newUsername;
	}

	public void setPassword(String newPassword){
		// this.password = cryptoPassword(newPassword);
		this.password = newPassword;
	}

	public String getPassword(){
		return this.password;
	}

	public boolean isPasswordCorrect(String cmpPassword){
		//String newPassHashed = cryptoPassword(cmpPassword);
		return this.password.equals(cmpPassword);
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getUsername() {
		return username;
	}

	public String getDescription() {
		return description;
	}

	public String getPhotoPath() {
		return photoPath;
	}
}