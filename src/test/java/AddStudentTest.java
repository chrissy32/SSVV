import domain.Student;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AddStudentTest {
    private String STUDENT_ID = "1";
    private String STUDENT_NAME = "Ana";
    private String INVALID_STUDENT_NAME = "Ana12";
    private int STUDENT_GROUP = 934;
    private String STUDENT_EMAIL = "ana@gmail.com";
    private String STUDENT_PROFESSOR = "John";
    private int MAX_INT = Integer.MAX_VALUE;

    StudentValidator studentValidator = new StudentValidator();
    TemaValidator temaValidator = new TemaValidator();
    StudentXMLRepo studentXMLRepository = null;
    TemaXMLRepo temaXMLRepository = null;
    NotaValidator notaValidator = null;
    NotaXMLRepo notaXMLRepository = null;
    Service service = null;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

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

    // EQUIVALENCE CLASS TEST CASES
    // TC 1
    @Test
    public void addStudentTestSuccess() {
        int size = getStudentRepositorySize();

        Student student = new Student(STUDENT_ID, STUDENT_NAME, STUDENT_GROUP, STUDENT_EMAIL, STUDENT_PROFESSOR);
        Student potentiallyAddedStudent = service.addStudent(student);
        assertNull(potentiallyAddedStudent);
        assertEquals(size + 1, ((Collection<?>) studentXMLRepository.findAll()).size());

        Student addedStudent = studentXMLRepository.findOne(STUDENT_ID);
        assertEquals(STUDENT_ID, addedStudent.getID());
        assertEquals(STUDENT_NAME, addedStudent.getNume());
        assertEquals(STUDENT_GROUP, addedStudent.getGrupa());
        assertEquals(STUDENT_EMAIL, addedStudent.getEmail());
        assertEquals(STUDENT_PROFESSOR, addedStudent.getProfessor());
    }

    // TC 2
    @Test
    public void addStudentTestFailure() {
        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("Grupa incorecta!");
        int INVALID_STUDENT_GROUP = -32;
        Student student = new Student(STUDENT_ID, STUDENT_NAME, INVALID_STUDENT_GROUP, STUDENT_EMAIL, STUDENT_PROFESSOR);
        service.addStudent(student);
    }

    // TC 3
    @Test
    public void addStudentEmptyIdTestFailure() {
        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("Id incorect!");
        String INVALID_STUDENT_ID = "";
        Student student = new Student(INVALID_STUDENT_ID, STUDENT_NAME, STUDENT_GROUP, STUDENT_EMAIL, STUDENT_PROFESSOR);
        service.addStudent(student);
    }

    // TC 4
    @Test
    public void addStudentNullIdTestFailure() {
        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("Id incorect!");
        Student student = new Student(null, STUDENT_NAME, STUDENT_GROUP, STUDENT_EMAIL, STUDENT_PROFESSOR);
        service.addStudent(student);
    }

    // TC 5
    @Test
    public void addStudentEmptyNameTestFailure() {
        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("Nume incorect!");
        Student student = new Student(STUDENT_ID, INVALID_STUDENT_NAME, STUDENT_GROUP, STUDENT_EMAIL, STUDENT_PROFESSOR);
        service.addStudent(student);
    }

    // TC 6
    @Test
    public void addStudentNullNameTestFailure() {
        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("Nume incorect!");
        Student student = new Student(STUDENT_ID, null, STUDENT_GROUP, STUDENT_EMAIL, STUDENT_PROFESSOR);
        service.addStudent(student);
    }

    // TC 7
    @Test
    public void addStudentInvalidNameTestFailure() {
        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("Nume incorect!");
        Student student = new Student(STUDENT_ID, INVALID_STUDENT_NAME, STUDENT_GROUP, STUDENT_EMAIL, STUDENT_PROFESSOR);
        service.addStudent(student);
    }

    // TC 8
    @Test
    public void addStudentEmptyEmailTestFailure() {
        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("Email incorect!");
        Student student = new Student(STUDENT_ID, STUDENT_NAME, STUDENT_GROUP, "", STUDENT_PROFESSOR);
        service.addStudent(student);
    }

    // TC 9
    @Test
    public void addStudentNullEmailTestFailure() {
        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("Email incorect!");
        Student student = new Student(STUDENT_ID, STUDENT_NAME, STUDENT_GROUP, null, STUDENT_PROFESSOR);
        service.addStudent(student);
    }

    // TC 10
    @Test
    public void addStudentInvalidEmailTestFailure() {
        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("Email incorect!");
        String INVALID_STUDENT_EMAIL = "ana";
        Student student = new Student(STUDENT_ID, STUDENT_NAME, STUDENT_GROUP, INVALID_STUDENT_EMAIL, STUDENT_PROFESSOR);
        service.addStudent(student);
    }

    // TC 11
    @Test
    public void addStudentEmptyProfessorNameTestFailure() {
        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("Nume profesor incorect!");
        Student student = new Student(STUDENT_ID, STUDENT_NAME, STUDENT_GROUP, STUDENT_EMAIL, "");
        service.addStudent(student);
    }

    // TC 12
    @Test
    public void addStudentNullProfessorNameTestFailure() {
        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("Nume profesor incorect!");
        Student student = new Student(STUDENT_ID, STUDENT_NAME, STUDENT_GROUP, STUDENT_EMAIL, null);
        service.addStudent(student);
    }

    // TC 13
    @Test
    public void addStudentInvalidProfessorNameTestFailure() {
        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("Nume profesor incorect!");
        String INVALID_STUDENT_PROFESSOR = "John12";
        Student student = new Student(STUDENT_ID, STUDENT_NAME, STUDENT_GROUP, STUDENT_EMAIL, INVALID_STUDENT_PROFESSOR);
        service.addStudent(student);
    }

    // BOUNDARY VALUE ANALYSIS TEST CASES
    // TC 1
    @Test
    public void addStudentGroup0TestSuccess() {
        int size = getStudentRepositorySize();
        Student student = new Student(STUDENT_ID, STUDENT_NAME, 0, STUDENT_EMAIL, STUDENT_PROFESSOR);
        Student potentiallyAddedStudent = service.addStudent(student);
        assertNull(potentiallyAddedStudent);
        assertEquals(size + 1, ((Collection<?>) studentXMLRepository.findAll()).size());

        Student addedStudent = studentXMLRepository.findOne(STUDENT_ID);
        assertEquals(STUDENT_ID, addedStudent.getID());
        assertEquals(STUDENT_NAME, addedStudent.getNume());
        assertEquals(0, addedStudent.getGrupa());
        assertEquals(STUDENT_EMAIL, addedStudent.getEmail());
        assertEquals(STUDENT_PROFESSOR, addedStudent.getProfessor());
    }

    // TC 2
    @Test
    public void addStudentGroupMAXINTTestSuccess() {
        int size = getStudentRepositorySize();
        Student student = new Student(STUDENT_ID, STUDENT_NAME, MAX_INT, STUDENT_EMAIL, STUDENT_PROFESSOR);
        Student potentiallyAddedStudent = service.addStudent(student);
        assertNull(potentiallyAddedStudent);
        assertEquals(size + 1, ((Collection<?>) studentXMLRepository.findAll()).size());

        Student addedStudent = studentXMLRepository.findOne(STUDENT_ID);
        assertEquals(STUDENT_ID, addedStudent.getID());
        assertEquals(STUDENT_NAME, addedStudent.getNume());
        assertEquals(MAX_INT, addedStudent.getGrupa());
        assertEquals(STUDENT_EMAIL, addedStudent.getEmail());
        assertEquals(STUDENT_PROFESSOR, addedStudent.getProfessor());
    }

    // TC 3
    @Test
    public void addStudentGroupNegativeTestFailure() {
        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("Grupa incorecta!");
        Student student = new Student(STUDENT_ID, STUDENT_NAME, -1, STUDENT_EMAIL, STUDENT_PROFESSOR);
        service.addStudent(student);
    }

    // TC 4
    @Test
    public void addStudentGroup1TestSuccess() {
        int size = getStudentRepositorySize();
        Student student = new Student(STUDENT_ID, STUDENT_NAME, 1, STUDENT_EMAIL, STUDENT_PROFESSOR);
        Student potentiallyAddedStudent = service.addStudent(student);
        assertNull(potentiallyAddedStudent);
        assertEquals(size + 1, ((Collection<?>) studentXMLRepository.findAll()).size());

        Student addedStudent = studentXMLRepository.findOne(STUDENT_ID);
        assertEquals(STUDENT_ID, addedStudent.getID());
        assertEquals(STUDENT_NAME, addedStudent.getNume());
        assertEquals(1, addedStudent.getGrupa());
        assertEquals(STUDENT_EMAIL, addedStudent.getEmail());
        assertEquals(STUDENT_PROFESSOR, addedStudent.getProfessor());
    }

    // TC 5
    @Test
    public void addStudentGroupMAXINTMinus1TestSuccess() {
        int size = getStudentRepositorySize();
        Student student = new Student(STUDENT_ID, STUDENT_NAME, MAX_INT - 1, STUDENT_EMAIL, STUDENT_PROFESSOR);
        Student potentiallyAddedStudent = service.addStudent(student);
        assertNull(potentiallyAddedStudent);
        assertEquals(size + 1, ((Collection<?>) studentXMLRepository.findAll()).size());

        Student addedStudent = studentXMLRepository.findOne(STUDENT_ID);
        assertEquals(STUDENT_ID, addedStudent.getID());
        assertEquals(STUDENT_NAME, addedStudent.getNume());
        assertEquals(MAX_INT - 1, addedStudent.getGrupa());
        assertEquals(STUDENT_EMAIL, addedStudent.getEmail());
        assertEquals(STUDENT_PROFESSOR, addedStudent.getProfessor());
    }

    // TC 6
    @Test
    public void addStudentGroupMAXINTPlus1TestFailure() {
        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("Grupa incorecta!");
        Student student = new Student(STUDENT_ID, STUDENT_NAME, MAX_INT + 1, STUDENT_EMAIL, STUDENT_PROFESSOR);
        service.addStudent(student);
    }

    int getStudentRepositorySize() {
        studentXMLRepository.delete(STUDENT_ID);
        Iterable<Student> students = studentXMLRepository.findAll();
        return ((Collection<?>) students).size();
    }
}