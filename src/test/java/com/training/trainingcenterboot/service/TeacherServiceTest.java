package com.training.trainingcenterboot.service;

import com.training.trainingcenterboot.dto.response.PageResponse;
import com.training.trainingcenterboot.dto.response.TeacherResponse;
import com.training.trainingcenterboot.exception.ResourceNotFoundException;
import com.training.trainingcenterboot.mapper.TeacherMapper;
import com.training.trainingcenterboot.model.Teacher;
import com.training.trainingcenterboot.repository.TeacherRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("unit")
@DisplayName("TeacherService unit tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherService teacherService;

    @Captor
    private ArgumentCaptor<Long> idCaptor;

    private Teacher teacher;
    private TeacherResponse response;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setName("Aibek");
        teacher.setExperience(5);

        response = new TeacherResponse(
                1L,
                "Aibek",
                5
        );
    }

    @Test
    @Order(1)
    @DisplayName("getAll должен вернуть список преподавателей")
    void getAll_shouldReturnTeachers() {
        Teacher secondTeacher = new Teacher();
        secondTeacher.setId(2L);
        secondTeacher.setName("Nurbek");
        secondTeacher.setExperience(7);

        TeacherResponse secondResponse = new TeacherResponse(
                2L,
                "Nurbek",
                7
        );

        when(teacherRepository.findAll())
                .thenReturn(List.of(teacher, secondTeacher));

        when(teacherMapper.toResponse(teacher))
                .thenReturn(response);

        when(teacherMapper.toResponse(secondTeacher))
                .thenReturn(secondResponse);

        List<TeacherResponse> result = teacherService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals("Aibek", result.get(0).getName());
        assertEquals(5, result.get(0).getExperience());

        assertEquals("Nurbek", result.get(1).getName());
        assertEquals(7, result.get(1).getExperience());

        verify(teacherRepository, times(1)).findAll();
        verify(teacherMapper, times(2)).toResponse(any(Teacher.class));
    }

    @Test
    @Order(2)
    @DisplayName("getAll должен вернуть пустой список")
    void getAll_shouldReturnEmptyList() {
        when(teacherRepository.findAll())
                .thenReturn(List.of());

        List<TeacherResponse> result = teacherService.getAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(teacherRepository).findAll();
        verify(teacherMapper, never()).toResponse(any());
    }

    @Test
    @Order(3)
    @DisplayName("getExperiencedTeachers должен вернуть опытных преподавателей")
    void getExperiencedTeachers_shouldReturnTeachers() {
        when(teacherRepository.experiencedTeachers(3))
                .thenReturn(List.of(teacher));

        when(teacherMapper.toResponse(teacher))
                .thenReturn(response);

        List<TeacherResponse> result =
                teacherService.getExperiencedTeachers(3);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Aibek", result.get(0).getName());
        assertTrue(result.get(0).getExperience() >= 3);

        verify(teacherRepository).experiencedTeachers(3);
        verify(teacherMapper).toResponse(teacher);
    }

    @Test
    @Order(4)
    @DisplayName("getExperiencedTeachers должен вернуть пустой список")
    void getExperiencedTeachers_shouldReturnEmptyList() {
        when(teacherRepository.experiencedTeachers(10))
                .thenReturn(List.of());

        List<TeacherResponse> result =
                teacherService.getExperiencedTeachers(10);

        assertNotNull(result);
        assertEquals(0, result.size());

        verify(teacherRepository).experiencedTeachers(10);
        verifyNoInteractions(teacherMapper);
    }

    @Test
    @Order(5)
    @DisplayName("findTeacherById должен вернуть Teacher")
    void findTeacherById_shouldReturnTeacher_whenExists() {
        when(teacherRepository.findById(1L))
                .thenReturn(Optional.of(teacher));

        Teacher result = teacherService.findTeacherById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Aibek", result.getName());
        assertEquals(5, result.getExperience());

        verify(teacherRepository).findById(1L);
    }

    @Test
    @Order(6)
    @DisplayName("findTeacherById должен выбросить exception")
    void findTeacherById_shouldThrowException_whenNotFound() {
        when(teacherRepository.findById(100L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> teacherService.findTeacherById(100L)
        );

        assertEquals(
                "Преподаватель с id 100 не найден",
                exception.getMessage()
        );

        verify(teacherRepository).findById(100L);
    }

    @Test
    @Order(7)
    @DisplayName("ArgumentCaptor пример")
    void findTeacherById_shouldCallRepositoryWithCorrectId() {
        when(teacherRepository.findById(1L))
                .thenReturn(Optional.of(teacher));

        teacherService.findTeacherById(1L);

        verify(teacherRepository).findById(idCaptor.capture());

        Long capturedId = idCaptor.getValue();

        assertEquals(1L, capturedId);
    }

    @Test
    @Order(8)
    @DisplayName("getAll(Pageable) должен вернуть PageResponse")
    void getAllWithPageable_shouldReturnPageResponse() {
        Pageable pageable = PageRequest.of(
                0,
                5,
                Sort.by("name").ascending()
        );

        Page<Teacher> teacherPage = new PageImpl<>(
                List.of(teacher),
                pageable,
                1
        );

        when(teacherRepository.findAll(pageable))
                .thenReturn(teacherPage);

        when(teacherMapper.toResponse(teacher))
                .thenReturn(response);

        PageResponse<TeacherResponse> result =
                teacherService.getAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(0, result.getPage());
        assertEquals(5, result.getSize());
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertTrue(result.isLast());

        verify(teacherRepository).findAll(pageable);
        verify(teacherMapper).toResponse(teacher);
    }

    @Test
    @Order(9)
    @DisplayName("searchByName должен вернуть PageResponse")
    void searchByName_shouldReturnPageResponse() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Teacher> teacherPage = new PageImpl<>(
                List.of(teacher),
                pageable,
                1
        );

        when(teacherRepository.findByNameContainingIgnoreCase("aib", pageable))
                .thenReturn(teacherPage);

        when(teacherMapper.toResponse(teacher))
                .thenReturn(response);

        PageResponse<TeacherResponse> result =
                teacherService.searchByName("aib", pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Aibek", result.getContent().get(0).getName());

        verify(teacherRepository)
                .findByNameContainingIgnoreCase("aib", pageable);
    }

    @Test
    @Order(10)
    @DisplayName("searchByName должен вернуть пустую страницу")
    void searchByName_shouldReturnEmptyPage() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Teacher> emptyPage = Page.empty(pageable);

        when(teacherRepository.findByNameContainingIgnoreCase("unknown", pageable))
                .thenReturn(emptyPage);

        PageResponse<TeacherResponse> result =
                teacherService.searchByName("unknown", pageable);

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());

        verify(teacherMapper, never()).toResponse(any());
    }

    @Test
    @Order(11)
    @DisplayName("filterByExperience должен вернуть PageResponse")
    void filterByExperience_shouldReturnPageResponse() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Teacher> teacherPage = new PageImpl<>(
                List.of(teacher),
                pageable,
                1
        );

        when(teacherRepository.findByExperienceGreaterThanEqual(5, pageable))
                .thenReturn(teacherPage);

        when(teacherMapper.toResponse(teacher))
                .thenReturn(response);

        PageResponse<TeacherResponse> result =
                teacherService.filterByExperience(5, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(5, result.getContent().get(0).getExperience());

        verify(teacherRepository)
                .findByExperienceGreaterThanEqual(5, pageable);
    }

    @RepeatedTest(3)
    @DisplayName("RepeatedTest пример: findTeacherById стабильно работает")
    void findTeacherById_repeatedTestExample() {
        when(teacherRepository.findById(1L))
                .thenReturn(Optional.of(teacher));

        Teacher result = teacherService.findTeacherById(1L);

        assertEquals("Aibek", result.getName());

        verify(teacherRepository).findById(1L);
    }

    @Test
    @Disabled("Пример отключенного теста")
    void disabledTestExample() {
        fail("Этот тест не должен запускаться");
    }

    @AfterEach
    void tearDown() {
        clearInvocations(teacherRepository, teacherMapper);
    }
}