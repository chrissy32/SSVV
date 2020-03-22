import domain.Student;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import repository.NotaXMLRepo;
import repository.StudentXMLRepo;
import repository.TemaXMLRepo;
import service.Service;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;
import validation.ValidationException;

import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class AddStudentTest {
    private String STUDENT_NAME = "Student";
    private int STUDENT_GROUP = 934;
    private int INVALID_STUDENT_GROUP = -32;
    private String STUDENT_EMAIL = "email@gmail.com";
    private String STUDENT_PROFESSOR = "John";

    StudentValidator studentValidator = new StudentValidator();
    TemaValidator temaValidator = new TemaValidator();
    StudentXMLRepo studentXMLRepository = null;
    TemaXMLRepo temaXMLRepository = null;
    NotaValidator notaValidator = null;
    NotaXMLRepo notaXMLRepository = null;
    Service service = null;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setup() throws IOException {
        temporaryFolder.newFile("StudentiTest.xml");
        String filenameStudent = "StudentiTest.xml";
        String filenameTema = "fisiere/Teme.xml";
        String filenameNota = "fisiere/Note.xml";
        studentXMLRepository = new StudentXMLRepo(filenameStudent);
        temaXMLRepository = new TemaXMLRepo(filenameTema);
        notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);
        notaXMLRepository = new NotaXMLRepo(filenameNota);
        service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);
    }

    @Test
    public void addStudentTestSuccess() {
        Iterable<Student> students = studentXMLRepository.findAll();
        int size = ((Collection<?>) students).size();

        String uuid = String.valueOf(UUID.randomUUID());
        Student student = new Student(uuid, STUDENT_NAME, STUDENT_GROUP, STUDENT_EMAIL, STUDENT_PROFESSOR);

        service.addStudent(student);
        assertEquals(size + 1, ((Collection<?>) studentXMLRepository.findAll()).size());

        Student addedStudent = studentXMLRepository.findOne(uuid);
        assertEquals(uuid, addedStudent.getID());
        assertEquals(STUDENT_NAME, addedStudent.getNume());
        assertEquals(STUDENT_GROUP, addedStudent.getGrupa());
        assertEquals(STUDENT_EMAIL, addedStudent.getEmail());
    }

    @Test(expected = ValidationException.class)
    public void addStudentTestFailure() {
        Student student = new Student("32", STUDENT_NAME, INVALID_STUDENT_GROUP, STUDENT_EMAIL, STUDENT_PROFESSOR);
        service.addStudent(student);
    }

}