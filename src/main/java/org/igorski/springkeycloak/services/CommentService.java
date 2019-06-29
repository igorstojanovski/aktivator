package org.igorski.springkeycloak.services;

import org.igorski.springkeycloak.model.Comment;
import org.igorski.springkeycloak.model.DataException;
import org.igorski.springkeycloak.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public boolean isOwner(Long commentId, String userId) throws DataException {
        Comment comment = getComment(commentId);
        return comment.getOwner().equals(userId);
    }

    public Comment hide(Long commentId) throws DataException {
        Comment comment = getComment(commentId);
        comment.setVisible(false);

        return commentRepository.save(comment);
    }

    private Comment getComment(Long commentId) throws DataException {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new DataException("No comment with id: " + commentId));
    }
}
