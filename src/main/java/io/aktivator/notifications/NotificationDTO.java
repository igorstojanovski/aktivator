package io.aktivator.notifications;

import lombok.Getter;
import lombok.Setter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Getter
@Setter
public class NotificationDTO {
    private String id;
    private String title;
    private String text;
    private String date;
    private boolean seen;

    public NotificationDTO(Notification notification) {
        id = String.valueOf(notification.getId());
        title = notification.getTitle();
        text = notification.getText();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        date = df.format(notification.getDate());
        seen = notification.isSeen();
    }
}
