package io.aktivator.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CommentITProfile.class)
@JsonTest
class CommentTest {

    @Autowired
    private JacksonTester<Comment> jacksonTester;

    @Test
    public void shouldSerializeOwnerInto() throws IOException {

        Initiative initiative = new Initiative();

        Comment comment = new Comment();
        comment.setOwner("223");
        comment.setDate(new Date());
        comment.setInitiative(initiative);

        JsonContent<Comment> jsonContent = jacksonTester.write(comment);
        String json = jsonContent.getJson();
        assertThat(json).contains("igorce@gmail.com", "223", "Igor", "Stojanovski");
    }
}
