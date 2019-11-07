package hilfling.backend.hilfing.controllers;

import hilfling.backend.hilfing.model.PhotographyRequest;
import hilfling.backend.hilfing.service.PhotographyRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/photography_requests")
public class PhotographyRequestController extends GenericBaseControllerImplementation<PhotographyRequest> {
    @Autowired
    private PhotographyRequestService service;
    public PhotographyRequestService getService() {
        return service;
    }
}
