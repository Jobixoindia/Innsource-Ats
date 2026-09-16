package com.example.Hrms.Controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.Hrms.Entity.JobOpening;
import com.example.Hrms.Service.JobOpeningService;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/openings")
@CrossOrigin(origins = "*")
public class JobOpeningController {

    private final JobOpeningService service;

    public JobOpeningController(
            JobOpeningService service
    ) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<JobOpening>> getAllOpenings() {

        return ResponseEntity.ok(
                service.getAllOpenings()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobOpening> getOpening(
            @PathVariable Integer id
    ) {

        return service.getOpeningById(id)
                .map(ResponseEntity::ok)
                .orElse(
                        ResponseEntity.notFound().build()
                );
    }

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<JobOpening> createOpening(
            @RequestParam String title,
            @RequestParam String clientName,
            @RequestParam String location,
            @RequestParam Integer vacancies,
            @RequestParam(required = false) String status,
            @RequestParam String assignedRecruiters,
            @RequestParam Integer createdById,
            @RequestPart(
                    value = "jobDescriptionFile",
                    required = false
            ) MultipartFile file
    ) throws IOException {

        JobOpening opening = new JobOpening();

        opening.setTitle(title);
        opening.setClientName(clientName);
        opening.setLocation(location);
        opening.setVacancies(vacancies);
        opening.setStatus(status);
        opening.setAssignedRecruiters(assignedRecruiters);

        byte[] fileBytes = null;
        String fileName = null;
        String fileType = null;

        if (file != null && !file.isEmpty()) {

            fileBytes = file.getBytes();
            fileName = file.getOriginalFilename();
            fileType = file.getContentType();
        }

        return ResponseEntity.ok(
                service.createOpening(
                        opening,
                        fileBytes,
                        fileName,
                        fileType,
                        createdById
                )
        );
    }

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<JobOpening> updateOpening(
            @PathVariable Integer id,

            @RequestParam String title,
            @RequestParam String clientName,
            @RequestParam String location,
            @RequestParam Integer vacancies,
            @RequestParam(required = false) String status,
            @RequestParam String assignedRecruiters,

            @RequestPart(
                    value = "jobDescriptionFile",
                    required = false
            ) MultipartFile file
    ) throws IOException {

        JobOpening opening = new JobOpening();

        opening.setTitle(title);
        opening.setClientName(clientName);
        opening.setLocation(location);
        opening.setVacancies(vacancies);
        opening.setStatus(status);
        opening.setAssignedRecruiters(assignedRecruiters);

        if (file != null && !file.isEmpty()) {
            opening.setJobDescriptionFile(file.getBytes());
            opening.setJobDescriptionFileName(
                    file.getOriginalFilename()
            );
            opening.setJobDescriptionFileType(
                    file.getContentType()
            );
        }

        return ResponseEntity.ok(
                service.updateOpening(
                        id,
                        opening
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOpening(
            @PathVariable Integer id
    ) {

        service.deleteOpening(id);

        return ResponseEntity.noContent().build();
    }
}