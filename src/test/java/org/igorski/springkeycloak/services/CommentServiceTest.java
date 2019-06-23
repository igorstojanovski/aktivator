package org.igorski.springkeycloak.services;

import org.igorski.springkeycloak.model.Comment;
import org.igorski.springkeycloak.model.Initiative;
import org.igorski.springkeycloak.repositories.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    private CommentService commentService;

    @BeforeEach
    public void beforeEach() {
        commentService = new CommentService(commentRepository);
    }

    @Test
    public void shouldUseRepoToCreateComment() {
        Initiative initiative = new Initiative();
        initiative.setId(2L);

        Comment comment = new Comment();
        comment.setText("This is the comment.");
        comment.setInitiative(initiative);
        comment.setOwnerId("223");

        Comment createdComment = new Comment();
        createdComment.setText("This is the comment.");
        createdComment.setInitiative(initiative);
        createdComment.setOwnerId("223");
        createdComment.setId(3L);

        when(commentRepository.save(createdComment)).thenReturn(createdComment);
        assertThat(createdComment).isEqualTo(commentService.createComment(createdComment));
    }
}
