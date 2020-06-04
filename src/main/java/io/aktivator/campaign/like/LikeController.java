package io.aktivator.campaign.like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campaign/{campaignId}/like")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping
    ResponseEntity<Like> createLike(@PathVariable Long campaignId) {
        Like like = likeService.createLike(campaignId);
        return new ResponseEntity<>(like, HttpStatus.OK);
    }

    @DeleteMapping
    ResponseEntity<Like> removeLike(@PathVariable Long campaignId) {
        likeService.removeLike(campaignId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    ResponseEntity<List<Like>> getAllLikes(@PathVariable Long campaignId) {
        List<Like> likes = likeService.getLikes(campaignId);
        return new ResponseEntity<>(likes, HttpStatus.OK);
    }
}
