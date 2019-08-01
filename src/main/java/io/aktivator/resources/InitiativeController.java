package io.aktivator.resources;

import io.aktivator.model.Initiative;
import io.aktivator.model.UserDTO;
import io.aktivator.services.InitiativeService;
import io.aktivator.services.UserService;
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
    public ResponseEntity<Initiative> createInitiative(@RequestBody Initiative initiative) {
        UserDTO user = userService.getCurrentUser();
        initiative.setUserId(user.getId());
        initiative = initiativeService.createInitiative(initiative);
        return new ResponseEntity<>(initiative, HttpStatus.OK);
    }
}
