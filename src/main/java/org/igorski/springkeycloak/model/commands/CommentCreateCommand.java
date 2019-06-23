package org.igorski.springkeycloak.model.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class CommentCreateCommand {
    private String text;
    private Long initiativeId;
    private Date date;
}
