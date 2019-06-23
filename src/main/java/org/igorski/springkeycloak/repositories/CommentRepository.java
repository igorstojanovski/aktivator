package org.igorski.springkeycloak.repositories;

import org.igorski.springkeycloak.model.Comment;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CommentRepository extends PagingAndSortingRepository<Comment, Long> {
}
