package org.igorski.springkeycloak.services;

import org.igorski.springkeycloak.model.Comment;
import org.igorski.springkeycloak.model.DataException;
import org.igorski.springkeycloak.model.Initiative;
import org.igorski.springkeycloak.repositories.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    private CommentService commentService;
    private Initiative initiative;
    private Comment comment;

    @BeforeEach
    public void beforeEach() {
        commentService = new CommentService(commentRepository);

        initiative = new Initiative();
        initiative.setId(2L);

        comment = new Comment();
        comment.setText("This is the comment.");
        comment.setInitiative(initiative);
        comment.setOwner("223");
    }

    @Test
    public void shouldUseRepoToCreateComment() {

        Comment createdComment = new Comment();
        createdComment.setText("This is the comment.");
        createdComment.setInitiative(initiative);
        createdComment.setOwner("223");
        createdComment.setId(3L);

        when(commentRepository.save(createdComment)).thenReturn(createdComment);
        assertThat(createdComment).isEqualTo(commentService.createComment(createdComment));
    }

    @Test
    public void shouldReturnTrueIfUserIdIsOwner() throws DataException {
        long commentId = 123L;
        comment.setId(commentId);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        assertThat(commentService.isOwner(commentId, "223")).isTrue();
    }

    @Test
    public void shouldReturnFalseIfUserIdIsNotOwner() throws DataException {
        long commentId = 123L;
        comment.setId(commentId);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        assertThat(commentService.isOwner(commentId, "225")).isFalse();
    }

    @Test
    public void shouldThrowWhenCommentDoesNotExit() throws DataException {
        long commentId = 123L;
        comment.setId(commentId);
        assertThrows(DataException.class, () -> commentService.isOwner(commentId, "223"));
    }
}
