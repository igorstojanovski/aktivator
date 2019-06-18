package org.igorski.springkeycloak.resources;

import org.igorski.springkeycloak.model.Initiative;
import org.igorski.springkeycloak.model.UserDTO;
import org.igorski.springkeycloak.services.InitiativeService;
import org.igorski.springkeycloak.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/initiative")
public class InitiativeController {

    private final InitiativeService initiativeService;
    private final UserService userService;

    @Autowired
    public InitiativeController(InitiativeService initiativeService, UserService userService) {
        this.initiativeService = initiativeService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Initiative> createIntiative(@RequestBody Initiative initiative) {
        UserDTO user = userService.getCurrentUser();
        initiative.setUserId(user.getId());
        initiative = initiativeService.createInitiative(initiative);
        return new ResponseEntity<>(initiative, HttpStatus.OK);
    }
}
