package me.vladislav.homework01.app.service;

import me.vladislav.homework01.app.db.repository.university.UniversityRepository;
import me.vladislav.homework01.app.db.repository.user.UserRepository;
import me.vladislav.homework01.app.dto.api.request.UniversityCreateRequest;
import me.vladislav.homework01.app.dto.service.University;
import me.vladislav.homework01.app.dto.service.UniversityData;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UniversityService {
    private final UserRepository userRepository;
    private final UniversityRepository universityRepository;

    public UniversityService(UniversityRepository universityRepository, UserRepository userRepository) {
        this.universityRepository = universityRepository;
        this.userRepository = userRepository;
    }

    public void addNewUniversityForUser(Long userId, UniversityCreateRequest university) {
        Long universityId = universityRepository.create(new UniversityData(university.name(),
            university.city(), university.description(), university.rateKrutosty()));
        userRepository.addUniversityId(userId, universityId);
    }

    public List<University> getUniversitiesForUser(Long userId) {
        Set<Long> universityIds = userRepository.getUniversitiesIds(userId);
        return universityIds.parallelStream()
            .map(universityRepository::getById)
            .collect(Collectors.toList());
    }
}