package io.aktivator.campaign.comment;

import io.aktivator.campaign.Campaign;
import io.aktivator.campaign.CampaignService;
import io.aktivator.model.DataException;
import io.aktivator.model.UserDTO;
import io.aktivator.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    @Mock
    private CommentService commentService;
    @Mock
    private CampaignService campaignService;
    @Mock
    private UserService userService;

    private CommentController commentController;
    public CommentCreateCommand createCommand;
    public static final Date DATE = new Date();

    @BeforeEach
    public void beforeEach() {
        commentController = new CommentController(campaignService, commentService, userService);
        UserDTO user = new UserDTO();
        user.setId("223");

        when(userService.getCurrentUser()).thenReturn(user);
        createCommand = new CommentCreateCommand();
        createCommand.setDate(DATE);
        createCommand.setText("This is the comment.");
    }

    @Test
    void shouldAddNewComment() throws DataException {

        Campaign initiative = new Campaign();
        initiative.setId(1L);

        Comment comment = new Comment();
        comment.setText("This is the comment.");
        comment.setCampaign(initiative);
        comment.setOwner("223");
        comment.setDate(DATE);

        Comment createdComment = new Comment();
        createdComment.setText("This is the comment.");
        createdComment.setCampaign(initiative);
        createdComment.setOwner("223");
        createdComment.setId(3L);
        createdComment.setDate(DATE);

        when(campaignService.getCampaign(1L)).thenReturn(initiative);
        when(commentService.createComment(comment)).thenReturn(createdComment);

        ResponseEntity<Comment> response = commentController.addComment(1L, createCommand);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(createdComment);
    }

    @Test
    void shouldReturn404WhenInitiativeNotFound() throws DataException {
        when(campaignService.getCampaign(1L)).thenThrow(new DataException("Missing campaign"));
        ResponseEntity<Comment> response = commentController.addComment(1L, createCommand);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldRemoveCommentForOwner() throws DataException {
        when(commentService.isOwner(123L, "223")).thenReturn(true);
        ResponseEntity<String> response = commentController.deleteComment(1L, 123L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldBe403WhenCommentIsNotOwnedByUser() throws DataException {
        when(commentService.isOwner(123L, "223")).thenReturn(false);
        ResponseEntity<String> response = commentController.deleteComment(1L, 123L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldBe404WhenCommentIdDoesNotExist() throws DataException {
        when(commentService.isOwner(123L, "223")).thenThrow(DataException.class);
        ResponseEntity<String> response = commentController.deleteComment(1L, 123L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
