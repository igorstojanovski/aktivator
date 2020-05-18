package io.aktivator.campaign.comment;

import io.aktivator.exceptions.DataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @Captor
    private ArgumentCaptor<Comment> commentCaptor = ArgumentCaptor.forClass(Comment.class);
    private CommentService commentService;
    private Comment comment;

    @BeforeEach
    void beforeEach() {
        commentService = new CommentService(commentRepository);

        comment = new Comment();
        comment.setText("This is the comment.");
        comment.setOwner("223");
    }

    @Test
    void shouldUseRepoToCreateComment() {

        Comment createdComment = new Comment();
        createdComment.setText("This is the comment.");
        createdComment.setOwner("223");
        createdComment.setId(3L);

        when(commentRepository.save(createdComment)).thenReturn(createdComment);
        assertThat(createdComment).isEqualTo(commentService.createComment(createdComment));
    }

    @Test
    void shouldReturnTrueIfUserIdIsOwner() throws DataException {
        long commentId = 123L;
        comment.setId(commentId);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        assertThat(commentService.isOwner(commentId, "223")).isTrue();
    }

    @Test
    void shouldReturnFalseIfUserIdIsNotOwner() throws DataException {
        long commentId = 123L;
        comment.setId(commentId);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        assertThat(commentService.isOwner(commentId, "225")).isFalse();
    }

    @Test
    void shouldThrowWhenCommentDoesNotExit() {
        long commentId = 123L;
        comment.setId(commentId);
        assertThrows(DataException.class, () -> commentService.isOwner(commentId, "223"));
    }

    @Test
    void shouldHideComments() throws DataException {
        Comment hiddenComment = new Comment();
        hiddenComment.setVisible(false);

        when(commentRepository.findById(123L)).thenReturn(Optional.of(comment));
        comment.setVisible(false);
        when(commentRepository.save(comment)).thenReturn(hiddenComment);
        Comment returnedComment = commentService.hide(123L);
        assertThat(returnedComment.isVisible()).isFalse();

        verify(commentRepository).save(commentCaptor.capture());
        assertThat(commentCaptor.getValue().isVisible()).isFalse();
    }

    @Test
    void shouldThrowWhenCommentDesNotExist() {
        when(commentRepository.findById(123L)).thenReturn(Optional.empty());
        assertThrows(DataException.class, () -> commentService.hide(123L));
    }

    @Test
    void shouldGetAllComments() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Order.desc("id")));
        commentService.getComments(123L, pageable);
        verify(commentRepository).findByCampaignId(123L, pageable);
    }

    @Test
    void shouldReturnCommentResponseObject() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Order.desc("id")));
        commentService.getComments(123L, pageable);
        verify(commentRepository).findByCampaignId(123L, pageable);
    }
}
