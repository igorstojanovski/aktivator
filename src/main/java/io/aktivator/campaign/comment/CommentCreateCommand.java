package io.aktivator.campaign.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Command to create a {@link Comment}.
 */
@Getter
@Setter
@NoArgsConstructor
class CommentCreateCommand {
    private String text;
}
