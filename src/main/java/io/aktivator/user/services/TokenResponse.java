package io.aktivator.user.services;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenResponse {

    private String access_token;
    private Integer expires_in;
    private String scope;
//    @JsonProperty("token_type")
    private String token_type;

}
