package com.example.Hrms.Controller;

import com.example.Hrms.Entity.Request;
import com.example.Hrms.Service.RequestService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requests")
@CrossOrigin(origins = "*")
public class RequestController {

    private final RequestService service;

    public RequestController(
            RequestService service
    ) {
        this.service = service;
    }


    /*
     * HR creates reject/delete request
     */
    @PostMapping
    public ResponseEntity<?> createRequest(

            @RequestParam String type,

            @RequestParam(required = false)
            Integer candidateId,

            @RequestParam Integer requestedById,

            @RequestParam(required = false)
            String note,

            @RequestParam(required = false)
            String requestedStatus,

            @RequestParam(required = false)
            String candidateData

    ) {

        try {

            Request request =
                    service.createRequest(
                            type,
                            candidateId,
                            requestedById,
                            note,
                            requestedStatus,
                            candidateData
                    );

            return ResponseEntity.ok(request);

        } catch (RuntimeException error) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(error.getMessage());
        }
    }

    /*
     * Manager / CEO resolves request
     */
    @PutMapping("/{id}/decision")
    public ResponseEntity<?> resolveRequest(
            @PathVariable Integer id,
            @RequestParam String decision,
            @RequestParam Integer resolvedById
    ) {

        try {

            Request request =
                    service.resolveRequest(
                            id,
                            decision,
                            resolvedById
                    );

            return ResponseEntity.ok(request);

        } catch (RuntimeException error) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(error.getMessage());
        }
    }


    /*
     * Requests created by a particular user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Request>> getRequests(
            @PathVariable Integer userId
    ) {

        return ResponseEntity.ok(
                service.getRequestsForUser(userId)
        );
    }
    /*
     * Get one request using its request ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getRequestById(
            @PathVariable Integer id
    ) {

        try {

            Request request =
                    service.getRequestById(id);

            return ResponseEntity.ok(request);

        } catch (RuntimeException error) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(error.getMessage());
        }
    }
}