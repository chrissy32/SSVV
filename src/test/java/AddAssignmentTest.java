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
import validation.ValidationException;

import java.io.IOException;
import java.util.Collection;

import static org.junit.Assert.*;

public class AddAssignmentTest {
    private String ASSIGNMENT_ID = "1";
    private String ASSIGNMENT_DESCRIPTION = "Implement f1";
    private int ASSIGNMENT_PRIMIRE = 1;
    private int ASSIGNMENT_DEADLINE = 2;

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
        temporaryFolder.newFile("TemeTest.xml");
        String filenameTema = "TemeTest.xml";
        String filenameStudent = "fisiere/Studenti.xml";
        String filenameNota = "fisiere/Note.xml";
        studentXMLRepository = new StudentXMLRepo(filenameStudent);
        temaXMLRepository = new TemaXMLRepo(filenameTema);
        notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);
        notaXMLRepository = new NotaXMLRepo(filenameNota);
        service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);
    }

    // TC 1
    @Test
    public void addAssignmentTestSuccess() {
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

    // TC 2
    @Test
    public void addAssignmentTestFailure() {
        Tema assignment = new Tema(ASSIGNMENT_ID, ASSIGNMENT_DESCRIPTION, ASSIGNMENT_DEADLINE, ASSIGNMENT_PRIMIRE);
        Tema potentiallyAddedAssignment = service.addTema(assignment);
        assertNotNull(potentiallyAddedAssignment);
    }

    // TC 3
    @Test
    public void addAssignmentEmptyIdTestFailure() {
        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("Numar tema invalid!");
        Tema assignment = new Tema("", ASSIGNMENT_DESCRIPTION, ASSIGNMENT_DEADLINE, ASSIGNMENT_PRIMIRE);
        service.addTema(assignment);
    }

    // TC 4
    @Test
    public void addAssignmentEmptyDescriptionTestFailure() {
        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("Descriere invalida!");
        Tema assignment = new Tema(ASSIGNMENT_ID, "", ASSIGNMENT_DEADLINE, ASSIGNMENT_PRIMIRE);
        service.addTema(assignment);
    }

    // TC 5
    @Test
    public void addAssignmentInvalidDeadlineTestFailure() {
        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("Deadlineul trebuie sa fie intre 1-14.");
        Tema assignment = new Tema(ASSIGNMENT_ID, ASSIGNMENT_DESCRIPTION, -2, ASSIGNMENT_PRIMIRE);
        service.addTema(assignment);
    }

    // TC 6
    @Test
    public void addAssignmentInvalidPrimireTestFailure() {
        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("Saptamana primirii trebuie sa fie intre 1-14.");
        Tema assignment = new Tema(ASSIGNMENT_ID, ASSIGNMENT_DESCRIPTION, ASSIGNMENT_DEADLINE, -1);
        service.addTema(assignment);
    }
}