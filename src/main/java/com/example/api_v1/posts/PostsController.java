package com.example.api_v1.posts;

import java.util.ArrayList;
import java.util.List;

import com.example.api_v1.photos.PhotosController;
import com.example.api_v1.users.UserController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/v1/posts")
public class PostsController {
	@Autowired
	private PhotosController photosController;

	@Autowired
	private UserController userController;

	/**
	 * LETS CREATE SOME FAKE POSTS
	 */
	private static List<PostsModel> allPosts = new ArrayList<PostsModel>();

	public PostsController() {
		PostsModel post0 = new PostsModel(0, 0, 0, "Foco, Força e Fé", "Estamos na final!!!");
		PostsModel post1 = new PostsModel(1, 0, 1, "Aquecimento", "O gramado é quem tá me conhecendo.");
		
		PostsModel post2 = new PostsModel(2, 1, 2, "Uniforme novo", "Hoje foi apresentado o novo manto.");
		PostsModel post3 = new PostsModel(3, 1, 3, "TBT", "Vamos de #TBT da saudades.");
		
		PostsModel post4 = new PostsModel(4, 2, 4, "Olá!", "Cheguei e trouxe o refri.");
		PostsModel post5 = new PostsModel(5, 2, 5, "#TBT", "To aprendendo a usar essa rede, me ajudem!!");

		allPosts.add(post0);
		allPosts.add(post1);
		allPosts.add(post2);
		allPosts.add(post3);
		allPosts.add(post4);
		allPosts.add(post5);
	}

	@PostMapping("/new")
	public String newPost(@RequestParam("username") String username,
		@RequestParam("title") String title,
			@RequestParam("description") String description, @RequestParam("file") MultipartFile file, Model model) {

		if (file.isEmpty()) {
			model.addAttribute("message", "ERROR");
			return "redirect:/user/"+username+"/new";
		} else {
			Integer userId = userController.getIdFromUser(username);
			String filename = username + "_" + file.getOriginalFilename();
			Integer photoId = photosController.createNewPhoto(userId, filename, file);
			
			if (photoId != -1) {
				Integer postId = allPosts.size();
				PostsModel newPost = new PostsModel(postId, userId, photoId, title, description);
				allPosts.add(newPost);
				model.addAttribute("message", "SUCESSO");
				return "redirect:/user/"+username;
			} else {
				model.addAttribute("message", "ERROR");
				return "redirect:/user/"+username+"/new";
			}
		}
	}

	/** Rota para deletar posts. 
	 * @deprecated
	 * @param postId
	 * @return
	 */
	@DeleteMapping("/delete/{id}")
	public Boolean deletePost(Integer postId){
		PostsModel post;
		if(postId > 0 && postId < allPosts.size()){
			post = allPosts.get(postId);
			
			allPosts.remove((int)postId);
		}
		
		return true;
	}

	public List<PostsModel> searchByUserId(Integer UserId) {
		List<PostsModel> postsByUserId = new ArrayList<PostsModel>();
		for (PostsModel postsModel : allPosts) {
			if (postsModel.getUserId() == UserId) {
				postsByUserId.add(postsModel);
			}
		}
		return postsByUserId;
	}

}