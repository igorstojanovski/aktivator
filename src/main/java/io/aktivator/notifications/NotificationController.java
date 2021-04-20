package io.aktivator.notifications;

import io.aktivator.exceptions.DataException;
import io.aktivator.exceptions.UserUnauthorizedException;
import io.aktivator.user.model.User;
import io.aktivator.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    private UserService userService;
    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getAllNotifications() {
        User user = userService.getCurrentUser();
        List<Notification> notifications = notificationRepository.findByUserIdAndSeen(user.getId(), false);

        return new ResponseEntity<>(notifications.stream()
                .map(NotificationDTO::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @PatchMapping("/{notificationId}")
    public ResponseEntity<Notification> markNotificationAsSeen(@PathVariable Long notificationId) {
        User user = userService.getCurrentUser();
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new DataException("No such notification exists."));
        if(!user.getId().equals(notification.getUser().getId())) {
            throw new UserUnauthorizedException("The user is not authorized to change this notification");
        }
        notification.setSeen(true);
        return new ResponseEntity<>(notificationRepository.save(notification), HttpStatus.OK);
    }

}
