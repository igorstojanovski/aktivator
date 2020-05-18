package io.aktivator.campaign.comment;

import io.aktivator.exceptions.DataException;
import io.aktivator.user.model.UserDTO;
import io.aktivator.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/campaign/{campaignId}/comment")
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;

    @Autowired
    public CommentController(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Comment> addComment(@PathVariable Long campaignId,
                                              @RequestBody CommentCreateCommand commentCreateCommand) {
        UserDTO user = userService.getCurrentUser();
        Comment comment = new Comment();
        comment.setCampaignId(campaignId);
        comment.setText(commentCreateCommand.getText());
        comment.setDate(commentCreateCommand.getDate());
        comment.setOwner(user.getId());
        comment = commentService.createComment(comment);

        return new ResponseEntity<>(comment, HttpStatus.OK);
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
