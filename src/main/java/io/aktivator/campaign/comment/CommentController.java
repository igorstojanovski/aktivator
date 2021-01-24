package io.aktivator.campaign.comment;

import io.aktivator.exceptions.DataException;
import io.aktivator.user.model.User;
import io.aktivator.user.services.AuthUserDTO;
import io.aktivator.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        User user = userService.getCurrentUser();
        Comment comment = new Comment();
        comment.setCampaignId(campaignId);
        comment.setText(commentCreateCommand.getText());
        comment.setDate(new Date());
        comment.setUserId(user.getId());
        comment = commentService.createComment(comment);

        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        User user = userService.getCurrentUser();
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
    public Page<CommentDto> getAllComments(@PathVariable Long campaignId,
                                           @SortDefault.SortDefaults({
                                            @SortDefault(sort = "id", direction = Sort.Direction.DESC)
                                        }) Pageable pageable) {
        Page<Comment> comments = commentService.getComments(campaignId, pageable);

        Page<CommentDto> pagedCommentDtos;
        if(comments != null) {
            pagedCommentDtos = getCommentDtos(comments, pageable);
        } else {
            pagedCommentDtos = new PageImpl<>(new ArrayList<>(), pageable, 0);
        }
        return pagedCommentDtos;
    }

    private Page<CommentDto> getCommentDtos(Page<Comment> comments, Pageable pageable) {
        List<CommentDto> dtos = comments
                .get()
                .map(this::getCommentDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, dtos.size());
    }

    private CommentDto getCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setCampaignId(comment.getCampaignId());
        commentDto.setDate(comment.getDate());
        commentDto.setText(comment.getText());
        User internalUser = userService.getUser(comment.getUserId())
                .orElseThrow(() -> new DataException("No such user found."));
        AuthUserDTO authUser = userService.getAuthUserInfo(internalUser.getExternalId());
        commentDto.setUser(authUser);

        return commentDto;
    }
}
