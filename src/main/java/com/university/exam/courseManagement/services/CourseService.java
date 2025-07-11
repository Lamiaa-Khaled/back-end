package com.university.exam.courseManagement.services;

import com.university.exam.courseManagement.dtos.requestDTO.CourseRequestDTO;
import com.university.exam.courseManagement.dtos.responseDTO.CourseResponseDTO;
import com.university.exam.courseManagement.entities.*;
import com.university.exam.courseManagement.repos.*;
import com.university.exam.exceptions.ValidationException;
import com.university.exam.resourceManagement.dtos.responseDTO.DirectoryWithResourcesDTO;
import com.university.exam.resourceManagement.dtos.responseDTO.ResourceResponseDTO;
import com.university.exam.resourceManagement.entities.Resource;
import com.university.exam.resourceManagement.entities.ResourceDirectory;
import com.university.exam.resourceManagement.entities.SuperResource;
import com.university.exam.resourceManagement.repos.ResourceDirectoryRepository;
import com.university.exam.resourceManagement.repos.ResourceRepository;
import com.university.exam.resourceManagement.repos.SuperResourceRepository;
import com.university.exam.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ResourceDirectoryRepository resourceDirectoryRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private SuperResourceRepository superResourceRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Transactional
    public CourseResponseDTO createCourse(CourseRequestDTO courseRequestDTO, MultipartFile avatar) throws IOException {
        Group group = fetchGroup(courseRequestDTO.getGroupId());
        validateCourse(courseRequestDTO.getCode());

        ResourceDirectory baseDirectory = createBaseDirectory(courseRequestDTO.getName());
        Resource avatarResource = null;
        if(avatar != null && !avatar.isEmpty() && avatar.getContentType() != null && !avatar.getContentType().isEmpty()) {
            avatarResource = createAvatarResource(courseRequestDTO, avatar, baseDirectory);
            createAvatarSuperResource(courseRequestDTO, avatarResource, avatar);
        }

        Course newCourse = buildCourse(courseRequestDTO, group, baseDirectory, avatarResource);
        courseRepository.save(newCourse);

        byte[] avatarData = avatar != null && !avatar.isEmpty() ? avatar.getBytes() : null;
        String avatarType = avatar != null && !avatar.isEmpty() ? avatar.getContentType() : null;
        return CourseResponseDTO.fromEntity(newCourse, avatarData, avatarType);
    }

    @Transactional
    public CourseResponseDTO updateCourse(String code, CourseRequestDTO courseRequestDTO, MultipartFile avatar) throws IOException {
        validateCourseDTO(courseRequestDTO);

        Group group = fetchGroup(courseRequestDTO.getGroupId());
        Course course = fetchCourse(code);

        updateCourseDetails(course, courseRequestDTO, group);

        updateBaseDirectoryName(course.getBaseDirectory(), courseRequestDTO.getName());
        Resource avatarResource = updateOrCreateAvatarResource(avatar, course, courseRequestDTO);

        course.setAvatarId(avatarResource != null ? avatarResource.getId() : null);
        course = courseRepository.save(course);

        return buildCourseResponseDTO(course, avatar);
    }

    private void updateBaseDirectoryName(ResourceDirectory baseDirectory, String courseName) {
        baseDirectory.setName("Course_" + courseName + "_BaseDir");
        resourceDirectoryRepository.save(baseDirectory);
    }

    private Resource updateOrCreateAvatarResource(MultipartFile avatar, Course course, CourseRequestDTO courseRequestDTO) throws IOException {
        if (avatar == null) {
            if (course.getAvatarId() != null) {
                superResourceRepository.deleteByResourceIdIn(Collections.singletonList(course.getAvatarId()));
                resourceRepository.deleteById(course.getAvatarId());
            }
            return null;
        }

        Resource avatarResource;
        if (course.getAvatarId() == null) {
            avatarResource = createAvatarResource(courseRequestDTO, avatar, course.getBaseDirectory());
            createAvatarSuperResource(courseRequestDTO, avatarResource, avatar);
        } else {
            avatarResource = resourceRepository.findById(course.getAvatarId())
                    .orElseThrow(() -> new NoSuchObjectException("Avatar resource not found"));
            avatarResource.setName("Course_" + courseRequestDTO.getName() + "_Avatar");
            resourceRepository.save(avatarResource);
            updateAvatarIfProvided(course, avatar);
        }
        return avatarResource;
    }

    private CourseResponseDTO buildCourseResponseDTO(Course course, MultipartFile avatar) throws IOException {
        byte[] avatarData = avatar != null && !avatar.isEmpty() ? avatar.getBytes() : null;
        String avatarType = avatar != null && !avatar.isEmpty() ? avatar.getContentType() : null;
        return CourseResponseDTO.fromEntity(course, avatarData, avatarType);
    }

    @Transactional
    public void deleteCourse(String code) throws NoSuchObjectException {
        Course course = fetchCourse(code);
        ResourceDirectory baseDirectory = course.getBaseDirectory();

        List<ResourceDirectory> subDirectories = resourceDirectoryRepository.findByBaseDirId(baseDirectory.getId());
        List<UUID> directoryIds = getDirectoryIds(baseDirectory, subDirectories);
        List<Resource> allResources = resourceRepository.findByResourceDirectoryIdIn(directoryIds);
        List<UUID> resourceIds = getResourceIds(allResources);

        deleteSuperResources(resourceIds);
        deleteResources(resourceIds);
        deleteSubDirectories(subDirectories);
        deleteCourse(course);
    }

    @Transactional(readOnly = true)
    public List<DirectoryWithResourcesDTO> getDirectoriesByCourseCode(String courseCode) throws NoSuchObjectException {
        Course course = fetchCourse(courseCode);
        ResourceDirectory baseDirectory = course.getBaseDirectory();

        List<ResourceDirectory> directories = fetchAllDirectories(baseDirectory);
        List<Resource> resources = fetchResourcesForDirectories(directories);
        Map<UUID, List<Resource>> resourcesByDirectoryId = groupResourcesByDirectoryId(resources);

        return mapDirectoriesToDTOs(directories, resourcesByDirectoryId);
    }

    @Transactional(readOnly = true)
    public List<CourseResponseDTO> getCoursesByGroupId(UUID groupId) throws NoSuchObjectException {
        validateGroupExists(groupId);
        List<Course> courses = fetchCourse(groupId);
        return assignAvatars(courses);
    }

    @Transactional(readOnly = true)
    public CourseResponseDTO getCourseByCode(String code) throws NoSuchObjectException {
        Course course = fetchCourse(code);
        return assignAvatars(List.of(course)).get(0);
    }

    private void validateCourseDTO(CourseRequestDTO courseRequestDTO) throws ValidationException {
        if (Utils.isEmpty(courseRequestDTO.getName())) {
            throw new ValidationException("Course name cannot be empty");
        }
    }

    private Group fetchGroup(UUID groupId) throws NoSuchObjectException {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new NoSuchObjectException("Group not found with ID: " + groupId));
    }

    private ResourceDirectory createBaseDirectory(String courseName) {
        ResourceDirectory baseDirectory = new ResourceDirectory();
        baseDirectory.setName("Course_" + courseName + "_BaseDir");
        baseDirectory.setCreator("Admin"); // Replace with actual creator logic
        return resourceDirectoryRepository.save(baseDirectory);
    }

    private Resource createAvatarResource(CourseRequestDTO courseRequestDTO, MultipartFile avatar, ResourceDirectory baseDirectory) {
        Resource avatarResource = new Resource();
        avatarResource.setName("Course_" + courseRequestDTO.getName() + "_Avatar");
        avatarResource.setType(avatar.getContentType());
        avatarResource.setSize(avatar.getSize());
        avatarResource.setResourceDirectory(baseDirectory);
        return resourceRepository.save(avatarResource);
    }

    private void createAvatarSuperResource(CourseRequestDTO courseRequestDTO, Resource avatarResource, MultipartFile avatar) throws IOException {
        SuperResource avatarSuperResource = new SuperResource();
        avatarSuperResource.setData(avatar != null && !avatar.isEmpty() ? avatar.getBytes() : new byte[0]);
        avatarSuperResource.setResource(avatarResource);
        superResourceRepository.save(avatarSuperResource);
    }

    private Course buildCourse(CourseRequestDTO courseRequestDTO, Group group, ResourceDirectory baseDirectory, Resource avatarResource) {
        return Course.builder()
                .code(courseRequestDTO.getCode())
                .name(courseRequestDTO.getName())
                .avatarId(avatarResource != null ? avatarResource.getId() : null)
                .active(courseRequestDTO.isActive())
                .group(group)
                .baseDirectory(baseDirectory)
                .build();
    }

    private void validateCourse(String code) throws NoSuchObjectException {
        if(courseRepository.existsById(code))
            throw new ValidationException("Course with code " + code + " already exists");
    }

    private Course fetchCourse(String code) throws NoSuchObjectException {
        return courseRepository.findById(code)
                .orElseThrow(() -> new NoSuchObjectException("Course not found with code: " + code));
    }

    private void updateCourseDetails(Course course, CourseRequestDTO courseRequestDTO, Group group) throws NoSuchObjectException {
        course.setName(courseRequestDTO.getName());
        course.setActive(courseRequestDTO.isActive());
        course.setGroup(group);
    }

    private void updateAvatarIfProvided(Course course, MultipartFile avatar) throws IOException {
        if (avatar != null) {
            Resource avatarResource = resourceRepository.findById(course.getAvatarId())
                    .orElseThrow(() -> new NoSuchObjectException("Avatar resource not found"));

            SuperResource avatarSuperResource = superResourceRepository.findByResourceId(avatarResource.getId())
                    .orElseThrow(() -> new NoSuchObjectException("Avatar super resource not found"));
            avatarSuperResource.setData(avatar.getBytes());
            superResourceRepository.save(avatarSuperResource);

            avatarResource.setSize(avatar.getSize());
            resourceRepository.save(avatarResource);
        }
    }

    private List<UUID> getDirectoryIds(ResourceDirectory baseDirectory, List<ResourceDirectory> subDirectories) {
        List<UUID> directoryIds = subDirectories.stream()
                .map(ResourceDirectory::getId)
                .collect(Collectors.toList());
        directoryIds.add(baseDirectory.getId());
        return directoryIds;
    }

    private List<UUID> getResourceIds(List<Resource> resources) {
        return resources.stream()
                .map(Resource::getId)
                .collect(Collectors.toList());
    }

    private void deleteSuperResources(List<UUID> resourceIds) {
        superResourceRepository.deleteByResourceIdIn(resourceIds);
    }

    private void deleteResources(List<UUID> resourceIds) {
        resourceRepository.deleteAllByIdInBatch(resourceIds);
    }

    private void deleteSubDirectories(List<ResourceDirectory> subDirectories) {
        resourceDirectoryRepository.deleteAllByIdInBatch(
                subDirectories.stream()
                        .map(ResourceDirectory::getId)
                        .collect(Collectors.toList())
        );
    }

    private void deleteCourse(Course course) {
        courseRepository.delete(course);
    }

    private List<ResourceDirectory> fetchAllDirectories(ResourceDirectory baseDirectory) {
        List<ResourceDirectory> subDirectories = resourceDirectoryRepository.findByBaseDirId(baseDirectory.getId());
        subDirectories.add(baseDirectory);
        return subDirectories;
    }

    private List<Resource> fetchResourcesForDirectories(List<ResourceDirectory> directories) {
        List<UUID> directoryIds = directories.stream()
                .map(ResourceDirectory::getId)
                .collect(Collectors.toList());

        return resourceRepository.findByResourceDirectoryIdIn(directoryIds);
    }

    private Map<UUID, List<Resource>> groupResourcesByDirectoryId(List<Resource> resources) {
        return resources.stream()
                .collect(Collectors.groupingBy(resource -> resource.getResourceDirectory().getId()));
    }

    private List<DirectoryWithResourcesDTO> mapDirectoriesToDTOs(
            List<ResourceDirectory> directories,
            Map<UUID, List<Resource>> resourcesByDirectoryId) {
        return directories.stream()
                .map(directory -> {
                    List<ResourceResponseDTO> resourceDTOs = resourcesByDirectoryId.getOrDefault(directory.getId(), List.of())
                            .stream()
                            .map(ResourceResponseDTO::fromEntity)
                            .collect(Collectors.toList());
                    return DirectoryWithResourcesDTO.fromEntity(directory, resourceDTOs);
                })
                .collect(Collectors.toList());
    }

    private void validateGroupExists(UUID groupId) throws NoSuchObjectException {
        groupRepository.findById(groupId)
                .orElseThrow(() -> new NoSuchObjectException("Group not found with ID: " + groupId));
    }

    private List<Course> fetchCourse(UUID groupId) throws NoSuchObjectException {
        List<Course> courses = courseRepository.findByGroupId(groupId);
        if(Utils.isEmpty(courses))
            throw new NoSuchObjectException("No Found Courses For Group ["+ groupId +"]");
        return courses;
    }

    private List<CourseResponseDTO> assignAvatars(List<Course> courses){
        Map<UUID, byte[]> avatarDataMap = fetchAvatarDataForCourses(courses);
        Map<UUID, String> avatarTypeMap = fetchAvatarTypeForCourses(courses);

        return courses.stream()
                .map(course -> {
                    byte[] avatar = avatarDataMap.get(course.getAvatarId());
                    String avatarType = avatarTypeMap.get(course.getAvatarId());

                    return CourseResponseDTO.fromEntity(course, avatar, avatarType);
                })
                .collect(Collectors.toList());
    }

    private Map<UUID, byte[]> fetchAvatarDataForCourses(List<Course> courses) {
        List<UUID> avatarIds = courses.stream()
                .map(Course::getAvatarId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return superResourceRepository.findByResourceIdIn(avatarIds)
                .stream()
                .collect(Collectors.toMap(
                        superResource -> superResource.getResource().getId(),
                        SuperResource::getData
                ));
    }

    private Map<UUID, String> fetchAvatarTypeForCourses(List<Course> courses) {
        List<UUID> avatarIds = courses.stream()
                .map(Course::getAvatarId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return resourceRepository.findAllById(avatarIds)
                .stream()
                .collect(Collectors.toMap(
                        Resource::getId,
                        Resource::getType
                ));
    }
}