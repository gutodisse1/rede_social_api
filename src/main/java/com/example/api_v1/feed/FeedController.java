package com.example.api_v1.feed;

import java.util.ArrayList;
import java.util.List;

import com.example.api_v1.photos.PhotoModel;
import com.example.api_v1.photos.PhotosController;
import com.example.api_v1.posts.PostsController;
import com.example.api_v1.posts.PostsModel;
import com.example.api_v1.users.UserController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/v1/feed")
public class FeedController {
    /**
	 * REQUIRE POSTCONTROLLER TO GET POSTS BY USER
	 */
	@Autowired
    private PostsController postsController;
    
    /**
	 * REQUIRE USERCONTROLLER TO GET FRIENDLIST BY USER
	 */
	@Autowired
    private UserController userController;
    
    /**
	 * REQUIRE PHOTOCONTROLLER TO GET PHOTOS BY CONTENTID
	 */
	@Autowired
    private PhotosController photosController;

    @GetMapping("/{username}/main")
    @ResponseBody
    public List<PostsModel> getMainFeed(@PathVariable String username){
        Integer userId = userController.getIdFromUser(username);
        List<Integer> friendList = userController.getFriendList(userId);

        List<PostsModel> feedPosts = new ArrayList<PostsModel>();
        for (Integer friendId : friendList) {
            List<PostsModel> friendPosts = postsController.searchByUserId(friendId);
            Integer lastPostId = friendPosts.size() - 1;
            if(lastPostId > -1){
                PostsModel lastFriendPost = friendPosts.get(lastPostId);
                feedPosts.add(lastFriendPost);   
            }
        }

        return feedPosts;
    }

    @GetMapping("/{username}/photos")
    @ResponseBody
    public List<PhotoModel> getPhotosFeed(@PathVariable String username){
        List<PostsModel> feedPosts = getMainFeed(username);
        List<PhotoModel> feedPhotos = new ArrayList<PhotoModel>();

        for (PostsModel post : feedPosts) {
            Integer contentId = post.getContentId();
            feedPhotos.add( photosController.searchByContentId(contentId));
        };

        return feedPhotos;
    }

    @GetMapping("/{username}/ads")
    @ResponseBody
    public List<PostsModel> getAdsFeed(@PathVariable String username){
        return null;
    }

}