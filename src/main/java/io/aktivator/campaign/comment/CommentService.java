package io.aktivator.campaign.comment;

import io.aktivator.exceptions.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    boolean isOwner(Long commentId, Long userId) throws DataException {
        Comment comment = getComment(commentId);
        return comment.getUserId().equals(userId);
    }

    Comment hide(Long commentId) throws DataException {
        Comment comment = getComment(commentId);
        comment.setVisible(false);

        return commentRepository.save(comment);
    }

    private Comment getComment(Long commentId) throws DataException {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new DataException("No comment with id: " + commentId));
    }

    Page<Comment> getComments(Long campaignId, Pageable pageable) {
        return commentRepository.findByCampaignId(campaignId, pageable);
    }
}
