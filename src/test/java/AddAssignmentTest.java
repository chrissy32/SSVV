import domain.Tema;
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

public class AddAssignmentTest {
    private String ASSIGNMENT_DESCRIPTION = "Tema";
    private int ASSIGNMENT_PRIMIRE = 5;
    private int ASSIGNMENT_DEADLINE = 12;

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

    @Test
    public void addAssignmentTestSuccess() {
        Iterable<Tema> assignments = temaXMLRepository.findAll();
        int size = ((Collection<?>) assignments).size();

        String uuid = String.valueOf(UUID.randomUUID());
        Tema assignment = new Tema(uuid, ASSIGNMENT_DESCRIPTION, ASSIGNMENT_DEADLINE, ASSIGNMENT_PRIMIRE);

        service.addTema(assignment);
        assertEquals(size + 1, ((Collection<?>) temaXMLRepository.findAll()).size());

        Tema addedAssignment = temaXMLRepository.findOne(uuid);
        assertEquals(uuid, addedAssignment.getID());
        assertEquals(ASSIGNMENT_DESCRIPTION, addedAssignment.getDescriere());
        assertEquals(ASSIGNMENT_DEADLINE, addedAssignment.getDeadline());
        assertEquals(ASSIGNMENT_PRIMIRE, addedAssignment.getPrimire());
    }

    @Test(expected = ValidationException.class)
    public void addAssignmentTestFailure() {
        Tema assignment = new Tema("32", ASSIGNMENT_DESCRIPTION, 32, ASSIGNMENT_PRIMIRE);
        service.addTema(assignment);
    }
}