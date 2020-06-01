package io.aktivator.campaign.like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
