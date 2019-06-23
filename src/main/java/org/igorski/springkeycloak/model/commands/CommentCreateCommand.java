package org.igorski.springkeycloak.model.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Command to create a {@link org.igorski.springkeycloak.model.Comment}.
 */
@Getter
@Setter
@NoArgsConstructor
public class CommentCreateCommand {
    private String text;
    private Long initiativeId;
    private Date date;
}
