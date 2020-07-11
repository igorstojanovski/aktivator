package io.aktivator.campaign.comment;

import io.aktivator.exceptions.DataException;
import io.aktivator.user.model.User;
import io.aktivator.user.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    public static final Long USER_ID = 223L;
    @Mock
    private CommentService commentService;
    @Mock
    private UserService userService;
    private CommentController commentController;
    private static final Date DATE = new Date();
    private CommentCreateCommand createCommand;
    private User user;
    @Captor
    private ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);

    @BeforeEach
    void beforeEach() {
        commentController = new CommentController(commentService, userService);
        user = new User();
        user.setId(USER_ID);

        createCommand = new CommentCreateCommand();
        createCommand.setText("This is the comment.");
    }

    @Test
    void shouldAddNewComment() {

        Comment comment = new Comment();
        comment.setText("This is the comment.");
        comment.setCampaignId(123L);
        comment.setUserId(USER_ID);
        comment.setDate(DATE);

        Comment createdComment = new Comment();
        createdComment.setText("This is the comment.");
        createdComment.setUserId(USER_ID);
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
        assertThat(body.getUserId()).isEqualTo(USER_ID);
        assertThat(body.getText()).isEqualTo("This is the comment.");
        assertThat(body.getCampaignId()).isEqualTo(123L);

        verify(commentService).createComment(commentArgumentCaptor.capture());
        Comment captorValue = commentArgumentCaptor.getValue();
        assertThat(captorValue.getId()).isNull();
        assertThat(captorValue.getUserId()).isEqualTo(USER_ID);
    }

    @Test
    void shouldRemoveCommentForOwner() throws DataException {
        when(commentService.isOwner(123L, USER_ID)).thenReturn(true);
        when(userService.getCurrentUser()).thenReturn(user);
        ResponseEntity<String> response = commentController.deleteComment(1L, 123L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldBe403WhenCommentIsNotOwnedByUser() throws DataException {
        when(commentService.isOwner(123L, USER_ID)).thenReturn(false);
        when(userService.getCurrentUser()).thenReturn(user);
        ResponseEntity<String> response = commentController.deleteComment(1L, 123L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldBe404WhenCommentIdDoesNotExist() throws DataException {
        when(userService.getCurrentUser()).thenReturn(user);
        when(commentService.isOwner(123L, USER_ID)).thenThrow(DataException.class);
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

        Comment comment = new Comment();
        comment.setCampaignId(123L);
        comment.setText("Yoohooo!");
        comment.setId(1L);
        comment.setUserId(123L);

        when(userService.getUser(comment.getUserId())).thenReturn(Optional.of(user));

        when(commentService.getComments(123L, pageable)).thenReturn(getPageOfList(Collections.singletonList(comment), pageable));
        Page<CommentDto> result = commentController.getAllComments(123L, pageable);
        assertThat(result.getContent()).hasSize(1);
    }

    private Page<Comment> getPageOfList(List<Comment> singletonList, Pageable pageable) {
        return new PageImpl<>(singletonList, pageable, singletonList.size());
    }
}
