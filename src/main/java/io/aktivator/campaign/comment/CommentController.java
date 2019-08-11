package io.aktivator.campaign.comment;

import io.aktivator.campaign.CampaignService;
import io.aktivator.model.DataException;
import io.aktivator.model.UserDTO;
import io.aktivator.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/campaign/{campaignId}/comment")
public class CommentController {

    private final CampaignService campaignService;
    private final CommentService commentService;
    private final UserService userService;

    @Autowired
    public CommentController(CampaignService campaignService, CommentService commentService, UserService userService) {
        this.campaignService = campaignService;
        this.commentService = commentService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Comment> addComment(@PathVariable Long campaignId,
                                              @RequestBody CommentCreateCommand commentCreateCommand) {
        UserDTO user = userService.getCurrentUser();
        Comment comment = new Comment();
        try {
            comment.setCampaign(campaignService.getCampaign(campaignId));
            comment.setText(commentCreateCommand.getText());
            comment.setDate(commentCreateCommand.getDate());
            comment.setOwner(user.getId());
            comment = commentService.createComment(comment);

            return new ResponseEntity<>(comment, HttpStatus.OK);
        } catch (DataException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long campaignId, @PathVariable Long commentId) {
        UserDTO user = userService.getCurrentUser();
        try {
            if (commentService.isOwner(commentId, user.getId())) {
                commentService.hide(commentId);
                return new ResponseEntity<>("Comment removed.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Not owner of comment.", HttpStatus.UNAUTHORIZED);
            }
        } catch (DataException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public Page<Comment> getAllComments(@PathVariable Long campaignId,
                                        @SortDefault.SortDefaults({
                                            @SortDefault(sort = "id", direction = Sort.Direction.DESC)
                                        }) Pageable pageable) {
        return commentService.getComments(campaignId, pageable);
    }
}
