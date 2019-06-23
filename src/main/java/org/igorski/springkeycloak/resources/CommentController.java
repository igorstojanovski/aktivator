package org.igorski.springkeycloak.resources;

import org.igorski.springkeycloak.model.Comment;
import org.igorski.springkeycloak.model.DataException;
import org.igorski.springkeycloak.model.UserDTO;
import org.igorski.springkeycloak.model.commands.CommentCreateCommand;
import org.igorski.springkeycloak.services.CommentService;
import org.igorski.springkeycloak.services.InitiativeService;
import org.igorski.springkeycloak.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final InitiativeService initiativeService;
    private final CommentService commentService;
    private final UserService userService;

    @Autowired
    public CommentController(InitiativeService initiativeService, CommentService commentService, UserService userService) {
        this.initiativeService = initiativeService;
        this.commentService = commentService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Comment> addComment(@RequestBody CommentCreateCommand commentCreateCommand) {
        UserDTO user = userService.getCurrentUser();
        Comment comment = new Comment();
        try {
            comment.setInitiative(initiativeService.getInitiative(commentCreateCommand.getInitiativeId()));
            comment.setText(commentCreateCommand.getText());
            comment.setDate(commentCreateCommand.getDate());
            comment.setOwnerId(user.getId());
            comment = commentService.createComment(comment);

            return new ResponseEntity<>(comment, HttpStatus.OK);
        } catch (DataException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
