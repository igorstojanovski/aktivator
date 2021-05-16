package io.aktivator.campaign.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.aktivator.user.services.AuthUserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class CommentDto {
    private final Long id;
    private final AuthUserDTO user;
    private final String text;
    private final Long campaignId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final Date date;
}
