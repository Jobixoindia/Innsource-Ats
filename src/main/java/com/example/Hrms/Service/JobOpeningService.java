package com.example.Hrms.Service;

import com.example.Hrms.Entity.JobOpening;
import com.example.Hrms.Entity.Users;
import com.example.Hrms.Repository.JobOpeningRepository;
import com.example.Hrms.Repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobOpeningService {

    private final JobOpeningRepository repository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public JobOpeningService(
            JobOpeningRepository repository,
            UserRepository userRepository,
            NotificationService notificationService
    ) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }
    // GET ALL
    public List<JobOpening> getAllOpenings() {
        return repository.findAll();
    }

    // GET BY ID
    public Optional<JobOpening> getOpeningById(Integer id) {
        return repository.findById(id);
    }
    public Optional<JobOpening> getOpeningByTitle( String title) 
    {
    	return repository.findByTitle(title); 
    }

 // CREATE
    public JobOpening createOpening(
            JobOpening opening,
            byte[] fileBytes,
            String fileName,
            String fileType,
            Integer createdById
    ) {

        if (opening.getStatus() == null ||
                opening.getStatus().isBlank()) {

            opening.setStatus("Open");
        }

        if (fileBytes != null) {
            opening.setJobDescriptionFile(fileBytes);
            opening.setJobDescriptionFileName(fileName);
            opening.setJobDescriptionFileType(fileType);
        }

        JobOpening savedOpening =
                repository.save(opening);


        /*
         * Notify ONLY assigned HR users
         */
        if (opening.getAssignedRecruiters() != null &&
                !opening.getAssignedRecruiters().isBlank()) {

            String[] recruiterNames =
                    opening.getAssignedRecruiters()
                            .split(",");

            for (String recruiterName : recruiterNames) {

                String name =
                        recruiterName.trim();

                if (name.isBlank()) {
                    continue;
                }

                List<Users> matchingUsers =
                        userRepository.findByRoleIgnoreCase("hr");

                for (Users user : matchingUsers) {

                    if (user.getName()
                            .equalsIgnoreCase(name)) {

                        String creatorName =
                                "Manager/CEO";

                        if (createdById != null) {

                            Optional<Users> creator =
                                    userRepository.findById(
                                            createdById
                                    );

                            if (creator.isPresent()) {
                                creatorName =
                                        creator.get().getName();
                            }
                        }

                        notificationService
                                .createNotification(
                                        user.getId(),
                                        null,
                                        "assignment",
                                        "New job opening assigned: "
                                                + opening.getTitle(),
                                        creatorName
                                                + " assigned you "
                                                + opening.getVacancies()
                                                + " vacancy/vacancies for "
                                                + opening.getTitle()
                                );
                    }
                }
            }
        }

        return savedOpening;
    }
    // UPDATE
    public JobOpening updateOpening(
            Integer id,
            JobOpening updatedOpening
    ) {

        JobOpening existing =
                repository.findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Job opening not found"
                                )
                        );

        existing.setTitle(
                updatedOpening.getTitle()
        );

        existing.setClientName(
                updatedOpening.getClientName()
        );

        existing.setLocation(
                updatedOpening.getLocation()
        );

        existing.setVacancies(
                updatedOpening.getVacancies()
        );

        existing.setStatus(
                updatedOpening.getStatus()
        );

        existing.setAssignedRecruiters(
                updatedOpening.getAssignedRecruiters()
        );

        if (updatedOpening.getJobDescriptionFile() != null) {
            existing.setJobDescriptionFile(
                    updatedOpening.getJobDescriptionFile()
            );

            existing.setJobDescriptionFileName(
                    updatedOpening.getJobDescriptionFileName()
            );

            existing.setJobDescriptionFileType(
                    updatedOpening.getJobDescriptionFileType()
            );
        }

        return repository.save(existing);
       
    }

    // DELETE
    public void deleteOpening(Integer id) {
        repository.deleteById(id);
    }
}