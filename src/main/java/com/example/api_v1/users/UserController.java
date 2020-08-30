package com.example.api_v1.users;

/**Controladora da rota /users - Todas atividades relacionadas aos usuários. 
 * Atividades como: 
 * 					- Recuperar informações do usuário;
 * 					- Criar/Deletar/ Atualizar usuário
 * 					- Criar novo post ( depende de  PostController )
 * @author Gustavo Marques (@gutodisse)
 * @version 0.01
 * @since MVP 01
 */

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.example.api_v1.photos.PhotoModel;
import com.example.api_v1.photos.PhotosController;
import com.example.api_v1.posts.PostsController;
import com.example.api_v1.posts.PostsModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/v1/users")
public class UserController {
	/**
	 * REQUIRE POSTCONTROLLER TO GET POSTS BY USER
	 */
	@Autowired
	private PostsController postsController;

	/**
	 * REQUIRE PHOTOCONTROLLER TO GET PHOTOS BY USER
	 */
	@Autowired
	private PhotosController photosController;

	/**
	 * LETS CREATE SOME FAKE USERS
	 */
	private static List<UserModel> users = new ArrayList<UserModel>();

	public UserController() {
		UserModel user3 = new UserModel(0, "MeninoNey", "neyjr", "neyjr", "#oPaiTaON", "/images/avatar/user_4.png");
		user3.addToFriendList(1);
		user3.addToFriendList(2);
		user3.addToFriendList(3);
		users.add(user3);

		UserModel user0 = new UserModel(1, "Marta", "martavsilva10", "martavsilva10",
				"Orlando Pride and Brazilian Women's National Team player. United Nations Global Goodwill Ambassador.",
				"/images/avatar/user_2.png");
		user0.addToFriendList(0);
		users.add(user0);

		UserModel user1 = new UserModel(2, "Gustavo", "gutodisse", "gutodisse",
				"+ pra frente que para-choque de Toyota.", "/images/avatar/user_5.png");
		users.add(user1);
	}

	/**
	 * Método para verificar se o usuário existe dado o userId.
	 * 
	 * @param userId - Número de identificação do usuário.
	 * @return - Retorna true se o usuário está cadastrado; Retorna false caso
	 *         contrário.
	 */
	private boolean thisUserExist(Integer userId) {
		return userId >= 0 && userId < users.size();
	}

	/**
	 * Rota para recuperar as informações do usuário dado o username.
	 * 
	 * @param username - Recupera o username do usuário por meio do caminho(URL)
	 * @return - Retorna o userId com base no username do usuário. Caso o usuário
	 *         não exista, retorna -1.
	 */
	@GetMapping("/username/{username}")
	@ResponseBody
	public Integer getIdFromUser(@PathVariable String username) {
		for (UserModel userModel : users) {
			if (username.equalsIgnoreCase(userModel.getUsername())) {
				return userModel.getId();
			}
		}
		return -1;
	}

	/**
	 * Rota para recuperar a lista de amigos.
	 * 
	 * @param userId - Recupera o userId do usuário por meio do caminho(URL)
	 * @return - Retorna a lista de amigos. Caso o userId não exista, retorna null.
	 */
	@GetMapping("/id/{userId}/friends")
	@ResponseBody
	public List<Integer> getFriendList(@PathVariable Integer userId) {
		if (thisUserExist(userId)) {
			UserModel user = users.get(userId);
			return user.getFriendList();
		} else {
			return null;
		}
	}

	/**
	 * Rota para recuperar as informações do usuário dado o userId.
	 * 
	 * @param userId - Recupera o userId do usuário por meio do caminho(URL)
	 * @return - Retorna o usuário com base no userId do usuário. Caso o userId não
	 *         exista, retorna NULL.
	 */
	@GetMapping("/id/{userId}")
	@ResponseBody
	public UserModel user(@PathVariable Integer userId) {
		if (thisUserExist(userId)) {
			return users.get(userId);
		} else {
			return null;
		}
	}

	/**
	 * Rota para recuperar o(s) post(s) por usuário.
	 * 
	 * @param userId - Recupera o userId do usuário por meio do caminho(URL)
	 * @return - Retorna o(s) post(s) com base no userId do usuário. Caso não tenha
	 *         posts, retorna NULL; Caso o usuário não exista, retorna NULL.
	 */
	@GetMapping("/id/{userId}/posts")
	@ResponseBody
	public List<PostsModel> userPosts(@PathVariable Integer userId) {
		if (thisUserExist(userId)) {
			return postsController.searchByUserId(userId);
		} else {
			return null;
		}
	}

	/**
	 * Rota para recuperar as fotos por usuário.
	 * 
	 * @param userId - Recupera o userId do usuário por meio do caminho(URL)
	 * @return - Retorna a(s) foto(s) com base no userId do usuário. Caso não tenha
	 *         posts, retorna NULL; Caso o usuário não exista, retorna NULL.
	 */
	@GetMapping("/id/{userId}/photos")
	@ResponseBody
	public List<PhotoModel> userPhotos(@PathVariable Integer userId) {
		if (thisUserExist(userId)) {
			return photosController.searchByUserId(userId);
		} else {
			return null;
		}
	}

	/**
	 * Rota para criar um novo usuário.
	 * 
	 * @param newuser       - Recupera e valida o usuário por meio do corpo(body) da
	 *                      requisição.
	 * @param bindingResult - Armazena o resultado da validação do corpo(body) da
	 *                      requisição.
	 * @return - Existem diferentes retornos de acordo com a execução: Caso o
	 *         usuário seja criado: "OK" Caso o username já está em uso: "Usuário já
	 *         registrado" Caso ocorra erro de validação, retorna a mensagem de erro
	 *         definido no modelo. Alguns exemplos: - Id é obrigatório! - Nome é
	 *         obrigatório! - Usuário é obrigatório! - Senha é obrigatório!
	 */
	@PostMapping("/new")
	@ResponseBody
	public String userCreate(@Valid @RequestBody UserModel newuser, BindingResult bindingResult) {
		boolean userDataAvaible = (bindingResult.hasErrors() == false);
		boolean usernameAvaible = userDataAvaible && (getIdFromUser(newuser.getUsername()) == -1);
		if (newuser.getPhotoPath().equals("")) {
			newuser.setPhotoPath("/images/avatar/not-found.png");
		}
		if (userDataAvaible && usernameAvaible) {
			newuser.setId(users.size());
			users.add(newuser);
			return "OK";
		} else {
			if (userDataAvaible == false) {
				List<FieldError> errors = bindingResult.getFieldErrors();
				return errors.get(0).getDefaultMessage();
			} else {
				return "Usuário já registrado";
			}
		}
	}

	/**
	 * Rota para adicionar um novo amigo a lista de amigos.
	 * 
	 * @param newuser       - Recupera e valida o usuário por meio do corpo(body) da
	 *                      requisição. Com a seguinte regra: - Passar o novo amigo
	 *                      pelo "ID", o usuário pelo "username"
	 * @param bindingResult - Armazena o resultado da validação do corpo(body) da
	 *                      requisição.
	 * @return - Existem diferentes retornos de acordo com a execução: Caso o
	 *         usuário seja atualizado: "OK" Caso a senha seja incorreta: "Senha
	 *         incorreta" Caso o username não exista: "Usuário não encontrado" Caso
	 *         ocorra erro de validação, retorna a mensagem de erro definido no
	 *         modelo. Alguns exemplos: - Id é obrigatório! - Nome é obrigatório! -
	 *         Usuário é obrigatório! - Senha é obrigatório!
	 */
	@PostMapping("/newFriend")
	@ResponseBody
	public String userNewFriend(@Valid @RequestBody UserModel newuser, BindingResult bindingResult) {
		boolean userDataAvaible = (bindingResult.hasErrors() == false);
		Integer userId = getIdFromUser(newuser.getUsername());
		Integer friendId = newuser.getId();
		boolean friendFound = userDataAvaible && (friendId != -1);
		boolean passwordMatch = false;

		if (userDataAvaible && friendFound) {
			UserModel user = users.get(userId);
			passwordMatch = user.isPasswordCorrect(newuser.getPassword());

			if (passwordMatch) {
				user.addToFriendList(friendId);
				return "ok";
			} else {
				return "Senha incorreta";
			}
		} else {
			if (friendId == -1) {
				return "Amigo não encontrado";
			} else if (userId == -1) {
				return "Usuário não encontrado";
			} else {
				List<FieldError> errors = bindingResult.getFieldErrors();
				return errors.get(0).getDefaultMessage();
			}
		}
	}

	/**
	 * Rota para atualizar as informações usuário.
	 * 
	 * @param newuser       - Recupera e valida o usuário por meio do corpo(body) da
	 *                      requisição.
	 * @param bindingResult - Armazena o resultado da validação do corpo(body) da
	 *                      requisição.
	 * @return - Existem diferentes retornos de acordo com a execução: Caso o
	 *         usuário seja atualizado: "OK" Caso a senha seja incorreta: "Senha
	 *         incorreta" Caso o username não exista: "Usuário não encontrado" Caso
	 *         ocorra erro de validação, retorna a mensagem de erro definido no
	 *         modelo. Alguns exemplos: - Id é obrigatório! - Nome é obrigatório! -
	 *         Usuário é obrigatório! - Senha é obrigatório!
	 */
	@PatchMapping("/update")
	@ResponseBody
	public String userUpdate(@Valid @RequestBody UserModel newuser, BindingResult bindingResult) {
		boolean userDataAvaible = (bindingResult.hasErrors() == false);
		Integer userId = getIdFromUser(newuser.getUsername());
		boolean usernameFound = userDataAvaible && (userId != -1);
		boolean passwordMatch = false;

		if (userDataAvaible && usernameFound) {
			UserModel user = users.get(userId);
			passwordMatch = user.isPasswordCorrect(newuser.getPassword());

			if (passwordMatch) {
				newuser.setId(userId);
				users.set(newuser.getId(), newuser);
				return "ok";
			} else {
				return "Senha incorreta";
			}
		} else {
			if (usernameFound == false) {
				return "Usuário não encontrado";
			} else {
				List<FieldError> errors = bindingResult.getFieldErrors();
				return errors.get(0).getDefaultMessage();
			}
		}
	}

	/**
	 * Rota para deletar um usuário.
	 * 
	 * @param newuser       - Recupera e valida o usuário por meio do corpo(body) da
	 *                      requisição.
	 * @param bindingResult - Armazena o resultado da validação do corpo(body) da
	 *                      requisição.
	 * @return - Existem diferentes retornos de acordo com a execução: Caso o
	 *         usuário seja atualizado: "OK" Caso a senha seja incorreta: "Senha
	 *         incorreta" Caso o username não exista: "Usuário não encontrado" Caso
	 *         ocorra erro de validação, retorna a mensagem de erro definido no
	 *         modelo. Alguns exemplos: - Id é obrigatório! - Nome é obrigatório! -
	 *         Usuário é obrigatório! - Senha é obrigatório!
	 */
	@DeleteMapping("/delete")
	@ResponseBody
	public String userDelete(@Valid @RequestBody UserModel newuser, BindingResult bindingResult) {
		boolean userDataAvaible = (bindingResult.hasErrors() == false);
		boolean usernameFound = userDataAvaible && (getIdFromUser(newuser.getUsername()) != -1);
		boolean passwordMatch = false;

		if (userDataAvaible && usernameFound) {
			UserModel user = users.get(newuser.getId());
			passwordMatch = user.isPasswordCorrect(newuser.getPassword());

			if (passwordMatch) {
				users.remove(user);
				return "ok";
			} else {
				return "Senha incorreta";
			}
		} else {
			if (usernameFound == false) {
				return "Usuário não encontrado";
			} else {
				List<FieldError> errors = bindingResult.getFieldErrors();
				return errors.get(0).getDefaultMessage();
			}
		}
	}

}