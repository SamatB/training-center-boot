package com.training.trainingcenterboot.service;

import com.training.trainingcenterboot.dto.request.StudentRegisterRequest;
import com.training.trainingcenterboot.dto.response.PageResponse;
import com.training.trainingcenterboot.dto.response.StudentResponse;
import com.training.trainingcenterboot.exception.ResourceNotFoundException;
import com.training.trainingcenterboot.mapper.StudentMapper;
import com.training.trainingcenterboot.model.Enrollment;
import com.training.trainingcenterboot.model.Student;
import com.training.trainingcenterboot.repository.EnrollmentRepository;
import com.training.trainingcenterboot.repository.StudentRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("unit") // группа тестов
@DisplayName("StudentService unit tests") // красивое название класса
@TestMethodOrder(OrderAnnotation.class) // позволяет запускать тесты по @Order
@ExtendWith(MockitoExtension.class) // подключает Mockito
class StudentServiceTest {

    @Mock // фейковый repository
    private StudentRepository studentRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks // создает StudentService и вставляет туда mock-зависимости
    private StudentService studentService;

    @Captor // ловит объект, который передали в метод save/delete
    private ArgumentCaptor<Student> studentCaptor;

    private Student student;
    private StudentResponse response;

    @BeforeEach // подготовка перед каждым тестом
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setName("Ali");
        student.setAge(20);
        student.setEmail("ali@gmail.com");

        response = new StudentResponse(
                1L,
                "Ali",
                20,
                "ali@gmail.com"
        );
    }

    @Nested // группировка тестов по смыслу
    @DisplayName("getById")
    class GetByIdTests {

        @Test
        @Order(1)
        @DisplayName("должен вернуть студента, если он найден")
        void getById_shouldReturnStudent_whenExists() {
            when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
            when(studentMapper.toResponse(student)).thenReturn(response);

            StudentResponse result = studentService.getById(1L);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("Ali", result.getName());
            assertEquals(20, result.getAge());
            assertEquals("ali@gmail.com", result.getEmail());

            verify(studentRepository, times(1)).findById(1L);
            verify(studentMapper, times(1)).toResponse(student);
        }

        @Test
        @Order(2)
        @DisplayName("должен выбросить ResourceNotFoundException, если студент не найден")
        void getById_shouldThrowException_whenNotFound() {
            when(studentRepository.findById(100L)).thenReturn(Optional.empty());

            ResourceNotFoundException exception = assertThrows(
                    ResourceNotFoundException.class,
                    () -> studentService.getById(100L)
            );

            assertEquals("Студент с id 100 не найден", exception.getMessage());

            verify(studentRepository).findById(100L);
            verify(studentMapper, never()).toResponse(any());
        }
    }

    @Nested
    @DisplayName("update")
    class UpdateTests {

        @Test
        @Order(3)
        @DisplayName("должен обновить студента")
        void update_shouldUpdateStudent_whenExists() {
            StudentRegisterRequest request = new StudentRegisterRequest();
            request.setName("Aibek");
            request.setAge(25);
            request.setEmail("aibek@gmail.com");

            Student savedStudent = new Student();
            savedStudent.setId(1L);
            savedStudent.setName("Aibek");
            savedStudent.setAge(25);
            savedStudent.setEmail("aibek@gmail.com");

            StudentResponse updatedResponse = new StudentResponse(
                    1L,
                    "Aibek",
                    25,
                    "aibek@gmail.com"
            );

            when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
            when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);
            when(studentMapper.toResponse(savedStudent)).thenReturn(updatedResponse);

            StudentResponse result = studentService.update(1L, request);

            assertEquals("Aibek", result.getName());
            assertEquals(25, result.getAge());
            assertEquals("aibek@gmail.com", result.getEmail());

            verify(studentRepository).save(studentCaptor.capture());

            Student capturedStudent = studentCaptor.getValue();
            System.out.println(capturedStudent.toString());

            assertEquals("Aibek", capturedStudent.getName());
            assertEquals(25, capturedStudent.getAge());
            assertEquals("aibek@gmail.com", capturedStudent.getEmail());
        }

        @Test
        @Order(4)
        @DisplayName("должен выбросить exception при обновлении несуществующего студента")
        void update_shouldThrowException_whenStudentNotFound() {
            StudentRegisterRequest request = new StudentRegisterRequest();
            request.setName("Aibek");
            request.setAge(25);
            request.setEmail("aibek@gmail.com");

            when(studentRepository.findById(404L)).thenReturn(Optional.empty());

            assertThrows(
                    ResourceNotFoundException.class,
                    () -> studentService.update(404L, request)
            );

            verify(studentRepository, never()).save(any());
            verify(studentMapper, never()).toResponse(any());
        }
    }

    @Nested
    @DisplayName("delete")
    class DeleteTests {

        @Test
        @Order(5)
        @DisplayName("должен удалить студента и отвязать enrollment")
        void delete_shouldDetachEnrollmentAndDeleteStudent() {
            Enrollment enrollment = new Enrollment();
            enrollment.setId(10L);
            enrollment.setStudent(student);

            when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
            when(enrollmentRepository.findEnrollmentByStudent_Id(1L)).thenReturn(enrollment);

            studentService.delete(1L);

            assertNull(enrollment.getStudent());

            verify(enrollmentRepository).save(enrollment);
            verify(studentRepository).delete(student);
        }

        @Test
        @Order(6)
        @DisplayName("должен удалить студента без enrollment")
        void delete_shouldDeleteStudent_whenEnrollmentIsNull() {
            when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
            when(enrollmentRepository.findEnrollmentByStudent_Id(1L)).thenReturn(null);

            studentService.delete(1L);

            verify(enrollmentRepository, never()).save(any());
            verify(studentRepository).delete(student);
        }

        @Test
        @Order(7)
        @DisplayName("должен выбросить exception при удалении несуществующего студента")
        void delete_shouldThrowException_whenStudentNotFound() {
            when(studentRepository.findById(100L)).thenReturn(Optional.empty());

            assertThrows(
                    ResourceNotFoundException.class,
                    () -> studentService.delete(100L)
            );

            verify(enrollmentRepository, never()).findEnrollmentByStudent_Id(anyLong());
            verify(studentRepository, never()).delete(any());
        }
    }

    @Nested
    @DisplayName("list methods")
    class ListMethodsTests {

        @Test
        @Order(8)
        @DisplayName("getAll должен вернуть список студентов")
        void getAll_shouldReturnStudents() {
            Student secondStudent = new Student();
            secondStudent.setId(2L);
            secondStudent.setName("Bek");
            secondStudent.setAge(22);
            secondStudent.setEmail("bek@gmail.com");

            StudentResponse secondResponse = new StudentResponse(
                    2L,
                    "Bek",
                    22,
                    "bek@gmail.com"
            );

            when(studentRepository.findAll()).thenReturn(List.of(student, secondStudent));
            when(studentMapper.toResponse(student)).thenReturn(response);
            when(studentMapper.toResponse(secondStudent)).thenReturn(secondResponse);

            List<StudentResponse> result = studentService.getAll();

            assertEquals(2, result.size());
            assertEquals("Ali", result.get(0).getName());
            assertEquals("Bek", result.get(1).getName());

            verify(studentRepository).findAll();
            verify(studentMapper, times(2)).toResponse(any(Student.class));
        }

        @ParameterizedTest // один тест с разными входными данными
        @CsvSource({
                "18, 2",
                "21, 1",
                "30, 0"
        })
        @DisplayName("getStudentsOlderThan должен работать с разными возрастами")
        void getStudentsOlderThan_shouldReturnStudents(int age, int expectedSize) {
            List<Student> students = expectedSize == 0
                    ? List.of()
                    : List.of(student);

            when(studentRepository.findByAgeGreaterThan(age)).thenReturn(students);

            if (expectedSize > 0) {
                when(studentMapper.toResponse(any(Student.class))).thenReturn(response);
            }

            List<StudentResponse> result = studentService.getStudentsOlderThan(age);

            assertEquals(expectedSize == 0 ? 0 : 1, result.size());

            verify(studentRepository).findByAgeGreaterThan(age);
        }

        @Test
        @Order(9)
        @DisplayName("getStudentsBetweenAges должен вернуть студентов в диапазоне")
        void getStudentsBetweenAges_shouldReturnStudents() {
            when(studentRepository.findStudentsBetweenAges(18, 30))
                    .thenReturn(List.of(student));

            when(studentMapper.toResponse(student)).thenReturn(response);

            List<StudentResponse> result =
                    studentService.getStudentsBetweenAges(18, 30);

            assertEquals(1, result.size());
            assertEquals("Ali", result.get(0).getName());

            verify(studentRepository).findStudentsBetweenAges(18, 30);
        }
    }

    @Nested
    @DisplayName("pagination")
    class PaginationTests {

        @Test
        @Order(10)
        @DisplayName("getAll(Pageable) должен вернуть PageResponse")
        void getAllWithPageable_shouldReturnPageResponse() {
            Pageable pageable = PageRequest.of(0, 5, Sort.by("name").ascending());

            Page<Student> studentPage = new PageImpl<>(
                    List.of(student),
                    pageable,
                    1
            );

            when(studentRepository.findAll(pageable)).thenReturn(studentPage);
            when(studentMapper.toResponse(student)).thenReturn(response);

            PageResponse<StudentResponse> result = studentService.getAll(pageable);

            assertNotNull(result);
            assertEquals(1, result.getContent().size());
            assertEquals(0, result.getPage());
            assertEquals(5, result.getSize());
            assertEquals(1, result.getTotalElements());
            assertEquals(1, result.getTotalPages());
            assertTrue(result.isLast());

            verify(studentRepository).findAll(pageable);
        }

        @Test
        @Order(11)
        @DisplayName("searchByName должен вернуть страницу найденных студентов")
        void searchByName_shouldReturnPageResponse() {
            Pageable pageable = PageRequest.of(0, 10);

            Page<Student> page = new PageImpl<>(
                    List.of(student),
                    pageable,
                    1
            );

            when(studentRepository.findByNameContainingIgnoreCase("ali", pageable))
                    .thenReturn(page);

            when(studentMapper.toResponse(student)).thenReturn(response);

            PageResponse<StudentResponse> result =
                    studentService.searchByName("ali", pageable);

            assertEquals(1, result.getContent().size());
            assertEquals("Ali", result.getContent().get(0).getName());

            verify(studentRepository)
                    .findByNameContainingIgnoreCase("ali", pageable);
        }

        @Test
        @Order(12)
        @DisplayName("filterByAge должен вернуть страницу студентов")
        void filterByAge_shouldReturnPageResponse() {
            Pageable pageable = PageRequest.of(0, 10);

            Page<Student> page = new PageImpl<>(
                    List.of(student),
                    pageable,
                    1
            );

            when(studentRepository.findByAgeBetween(18, 30, pageable))
                    .thenReturn(page);

            when(studentMapper.toResponse(student)).thenReturn(response);

            PageResponse<StudentResponse> result =
                    studentService.filterByAge(18, 30, pageable);

            assertEquals(1, result.getContent().size());
            assertEquals(20, result.getContent().get(0).getAge());

            verify(studentRepository)
                    .findByAgeBetween(18, 30, pageable);
        }

        @Test
        @Order(13)
        @DisplayName("searchByNameOrEmail должен искать по keyword")
        void searchByNameOrEmail_shouldReturnPageResponse() {
            Pageable pageable = PageRequest.of(0, 10);

            Page<Student> page = new PageImpl<>(
                    List.of(student),
                    pageable,
                    1
            );

            when(studentRepository.searchByNameOrEmail("gmail", pageable))
                    .thenReturn(page);

            when(studentMapper.toResponse(student)).thenReturn(response);

            PageResponse<StudentResponse> result =
                    studentService.searchByNameOrEmail("gmail", pageable);

            assertEquals(1, result.getContent().size());
            assertEquals("ali@gmail.com", result.getContent().get(0).getEmail());

            verify(studentRepository)
                    .searchByNameOrEmail("gmail", pageable);
        }
    }

    @AfterEach // выполняется после каждого теста
    void tearDown() {
        clearInvocations(studentRepository, enrollmentRepository, studentMapper);
    }
}