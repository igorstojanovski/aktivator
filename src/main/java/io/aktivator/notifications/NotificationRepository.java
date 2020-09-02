package io.aktivator.notifications;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface NotificationRepository extends PagingAndSortingRepository<Notification, Long> {
    List<Notification> findByUserId(Long id);
}
