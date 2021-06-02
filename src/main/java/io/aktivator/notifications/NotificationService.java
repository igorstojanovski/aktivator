package io.aktivator.notifications;

import io.aktivator.exceptions.DataException;
import io.aktivator.user.model.User;
import io.aktivator.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class NotificationService {

  @Autowired private NotificationRepository notificationRepository;
  @Autowired private UserService userService;

  public Notification createSystemNotification(String title, String text, User user) {
    Notification notification = new Notification();
    notification.setDate(new Date());
    notification.setText(text);
    notification.setTitle(title);
    notification.setUser(user);
    notification.setSeen(false);
    notification.setType(NotificationType.SYSTEM);
    return notificationRepository.save(notification);
  }

  public void createLikeNotification(User fromUser, Long toUser) {
    Notification notification = new Notification();
    notification.setDate(new Date());
    notification.setTitle("New like!");
    notification.setText(
        "You have a new like from: " + fromUser.getUserInformation().getUsername());
    notification.setUser(
        userService.getUser(toUser).orElseThrow(() -> new DataException("No such user exists.")));
    notification.setSeen(false);
    notification.setType(NotificationType.LIKE);
  }
}
