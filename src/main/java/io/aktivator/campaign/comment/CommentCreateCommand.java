package io.aktivator.campaign.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Command to create a {@link Comment}.
 */
@Getter
@Setter
@NoArgsConstructor
public class CommentCreateCommand {
    private String text;
    private Long campaignId;
    private Date date;
}
