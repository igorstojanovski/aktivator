package io.aktivator.campaign.comment;

import io.aktivator.model.DataException;
import io.aktivator.user.model.UserDTO;
import io.aktivator.user.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    @Mock
    private CommentService commentService;
    @Mock
    private UserService userService;
    private CommentController commentController;
    private static final Date DATE = new Date();
    private CommentCreateCommand createCommand;
    private UserDTO user;
    @Captor
    private ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);

    @BeforeEach
    void beforeEach() {
        commentController = new CommentController(commentService, userService);
        user = new UserDTO();
        user.setId("223");

        createCommand = new CommentCreateCommand();
        createCommand.setDate(DATE);
        createCommand.setText("This is the comment.");
    }

    @Test
    void shouldAddNewComment() {

        Comment comment = new Comment();
        comment.setText("This is the comment.");
        comment.setCampaignId(123L);
        comment.setOwner("223");
        comment.setDate(DATE);

        Comment createdComment = new Comment();
        createdComment.setText("This is the comment.");
        createdComment.setOwner("223");
        createdComment.setId(3L);
        createdComment.setCampaignId(123L);
        createdComment.setDate(DATE);

        when(commentService.createComment(any(Comment.class))).thenReturn(createdComment);
        when(userService.getCurrentUser()).thenReturn(user);

        ResponseEntity<Comment> response = commentController.addComment(123L, createCommand);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Comment body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getId()).isEqualTo(3L);
        assertThat(body.getOwner()).isEqualTo("223");
        assertThat(body.getText()).isEqualTo("This is the comment.");
        assertThat(body.getCampaignId()).isEqualTo(123L);

        verify(commentService).createComment(commentArgumentCaptor.capture());
        Comment captorValue = commentArgumentCaptor.getValue();
        assertThat(captorValue.getId()).isNull();
        assertThat(captorValue.getOwner()).isEqualTo("223");
    }

    @Test
    void shouldRemoveCommentForOwner() throws DataException {
        when(commentService.isOwner(123L, "223")).thenReturn(true);
        when(userService.getCurrentUser()).thenReturn(user);
        ResponseEntity<String> response = commentController.deleteComment(1L, 123L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldBe403WhenCommentIsNotOwnedByUser() throws DataException {
        when(commentService.isOwner(123L, "223")).thenReturn(false);
        when(userService.getCurrentUser()).thenReturn(user);
        ResponseEntity<String> response = commentController.deleteComment(1L, 123L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldBe404WhenCommentIdDoesNotExist() throws DataException {
        when(userService.getCurrentUser()).thenReturn(user);
        when(commentService.isOwner(123L, "223")).thenThrow(DataException.class);
        ResponseEntity<String> response = commentController.deleteComment(1L, 123L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldGoToServiceToGetAllComments() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Order.desc("id")));

        commentController.getAllComments(123L, pageable);
        verify(commentService).getComments(123L, pageable);
    }

    @Test
    void shouldContainOwnerObject() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Order.desc("id")));

        Page<Comment> response = commentController.getAllComments(123L, pageable);
    }
}
