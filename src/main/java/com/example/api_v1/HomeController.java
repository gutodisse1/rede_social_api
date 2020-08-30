package com.example.api_v1;

import java.util.ArrayList;
import java.util.List;

import com.example.api_v1.feed.FeedController;
import com.example.api_v1.photos.PhotoModel;
import com.example.api_v1.posts.PostsModel;

/**Controladora da rota / - Todas atividades relacionadas ao Front (WEB).
 * Atividades como: 
 * 					- Enviar página index
 * 					- Enviar página de usuário, adicionando os dados do usuários.
 * @author Gustavo Marques (@gutodisse)
 * @version 0.01
 * @since MVP 01
 */

import com.example.api_v1.users.UserController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/")
public class HomeController {
	/**
	 * Depende do controller de usuário para recuperar o id do usuário
	 */
	@Autowired
	private UserController userController;

	/**
	 * Depende do controller de usuário para recuperar o id do usuário
	 */
	@Autowired
	private FeedController feedController;


	/**
	 * Rota para o formulário de envio de fotos
	 * @param username - Recebe o username do usário por meio do caminho(URL)
	 * @param model - Model(modelo) para adicionar os atrbiutos
	 * @return	- Retorna o template que será renderizado verificando se encontrou o usuário.
	 */
	@GetMapping("user/{username}/new")
	public String newPost(@PathVariable String username, Model model){
		Integer userId = userController.getIdFromUser(username);
		if(userId != -1){
			model.addAttribute("message", "");
			model.addAttribute("username", username);
			return "uploadForm";
		} else {
			return "index";
		}
	}

	@GetMapping("user/{username}")
	public String home(@PathVariable String username, Model model) 
	{
		Integer userId = userController.getIdFromUser(username);
		if(userId != -1){
			model.addAttribute("name", userController.user(userId).getName());
			model.addAttribute("desc", userController.user(userId).getDescription());
			model.addAttribute("photoPath", userController.user(userId).getPhotoPath());
			model.addAttribute("posts", userController.userPosts(userId));
			model.addAttribute("photos", userController.userPhotos(userId));
			model.addAttribute("username", username); // COMO POSSO RECUPERAR UMA PATH VARIAVEL DENTRO DO THYMELEAF?
			return "index"; 
		} else {
			model.addAttribute("name", "USUÁRIO NÃO ENCONTRADO");
			model.addAttribute("desc", "Talvez o usuário tenha desabilitado a conta.");
			model.addAttribute("photoPath", "/images/avatar/not-found.png");
			//model.addAttribute("posts", postsController.searchByUserId(0));
			return "index"; 
		}
	}

	@GetMapping("user/{username}/feed")
	public String feed(@PathVariable String username, Model model) 
	{
		Integer userId = userController.getIdFromUser(username);
		if(userId != -1){
			

			model.addAttribute("name", userController.user(userId).getName());
			model.addAttribute("desc", userController.user(userId).getDescription());
			model.addAttribute("photoPath", userController.user(userId).getPhotoPath());
			model.addAttribute("posts", feedController.getMainFeed(username));
			model.addAttribute("photos", feedController.getPhotosFeed(username));
			model.addAttribute("username", username); // COMO POSSO RECUPERAR UMA PATH VARIAVEL DENTRO DO THYMELEAF?
			return "index"; 
		} else {
			model.addAttribute("name", "USUÁRIO NÃO ENCONTRADO");
			model.addAttribute("desc", "Talvez o usuário tenha desabilitado a conta.");
			model.addAttribute("photoPath", "/images/avatar/not-found.png");
			//model.addAttribute("posts", postsController.searchByUserId(0));
			return "index"; 
		}
	}

}