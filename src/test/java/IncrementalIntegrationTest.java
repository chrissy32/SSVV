import domain.Nota;
import domain.Student;
import domain.Tema;
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

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;

import static org.junit.Assert.*;

public class IncrementalIntegrationTest {
    private String STUDENT_ID = "1";
    private String STUDENT_NAME = "Ana";
    private int STUDENT_GROUP = 934;
    private String STUDENT_EMAIL = "ana@gmail.com";
    private String STUDENT_PROFESSOR = "John";
    private String ASSIGNMENT_ID = "1";
    private String ASSIGNMENT_DESCRIPTION = "Implement f1";
    private int ASSIGNMENT_PRIMIRE = 1;
    private int ASSIGNMENT_DEADLINE = 2;
    private String GRADE_ID = "1";
    private double GRADE = 10;
    private LocalDate GRADE_DATE = LocalDate.parse("2018-10-22");

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
        temporaryFolder.newFile("TemeTest.xml");
        temporaryFolder.newFile("NoteTest.xml");
        String filenameStudent = "StudentiTest.xml";
        String filenameTema = "TemeTest.xml";
        String filenameNota = "NoteTest.xml";
        studentXMLRepository = new StudentXMLRepo(filenameStudent);
        temaXMLRepository = new TemaXMLRepo(filenameTema);
        notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);
        notaXMLRepository = new NotaXMLRepo(filenameNota);
        service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);
    }

    @Test
    public void addStudentTestSuccess() {
        studentXMLRepository.delete(STUDENT_ID);
        Iterable<Student> students = studentXMLRepository.findAll();
        int size = ((Collection<?>) students).size();

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

    @Test
    public void addAssignmentTestSuccess() {
        addStudentTestSuccess();
        temaXMLRepository.delete(ASSIGNMENT_ID);
        Iterable<Tema> assignments = temaXMLRepository.findAll();
        int size = ((Collection<?>) assignments).size();

        Tema assignment = new Tema(ASSIGNMENT_ID, ASSIGNMENT_DESCRIPTION, ASSIGNMENT_DEADLINE, ASSIGNMENT_PRIMIRE);
        Tema potentiallyAddedAssignment = service.addTema(assignment);
        assertNull(potentiallyAddedAssignment);
        assertEquals(size + 1, ((Collection<?>) temaXMLRepository.findAll()).size());

        Tema addedAssignment = temaXMLRepository.findOne(ASSIGNMENT_ID);
        assertEquals(ASSIGNMENT_ID, addedAssignment.getID());
        assertEquals(ASSIGNMENT_DESCRIPTION, addedAssignment.getDescriere());
        assertEquals(ASSIGNMENT_DEADLINE, addedAssignment.getDeadline());
        assertEquals(ASSIGNMENT_PRIMIRE, addedAssignment.getPrimire());
    }

    @Test
    public void addGradeTestSuccess() {
        addAssignmentTestSuccess();
        notaXMLRepository.delete(GRADE_ID);
        Iterable<Nota> grades = notaXMLRepository.findAll();
        int size = ((Collection<?>) grades).size();

        Nota grade = new Nota(GRADE_ID, STUDENT_ID, ASSIGNMENT_ID, GRADE, GRADE_DATE);
        double potentiallyAddedGrade = service.addNota(grade, "Excellent");
        assertEquals(size + 1, ((Collection<?>) notaXMLRepository.findAll()).size());

        Nota addedGrade = notaXMLRepository.findOne(GRADE_ID);
        assertEquals(GRADE_ID, addedGrade.getID());
        assertEquals(STUDENT_ID, addedGrade.getIdStudent());
        assertEquals(ASSIGNMENT_ID, addedGrade.getIdTema());
        assertEquals(BigDecimal.valueOf(potentiallyAddedGrade), BigDecimal.valueOf(addedGrade.getNota()));
        assertEquals(GRADE_DATE, addedGrade.getData());
    }

}
