package io.aktivator.notifications;

import io.aktivator.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public Notification createNotification(String title, String text, User user) {
        Notification notification = new Notification();
        notification.setDate(new Date());
        notification.setText(text);
        notification.setTitle(title);
        notification.setUser(user);
        notification.setSeen(false);

        return notificationRepository.save(notification);
    }

}
