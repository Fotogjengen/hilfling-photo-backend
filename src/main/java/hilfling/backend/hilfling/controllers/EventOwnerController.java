package hilfling.backend.hilfling.controllers;

import hilfling.backend.hilfling.model.EventOwner;
import hilfling.backend.hilfling.service.EventOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/event_owners")
public class EventOwnerController extends GenericBaseControllerImplementation<EventOwner> {
    @Autowired
    private EventOwnerService service;
    public EventOwnerService getService() {
        return service;
    }
}
