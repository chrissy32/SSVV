package validation;

import domain.Student;

public class StudentValidator implements Validator<Student> {

    /**
     * Valideaza un student
     *
     * @param entity - studentul pe care il valideaza
     * @throws ValidationException - daca studentul nu e valid
     */
    @Override
    public void validate(Student entity) throws ValidationException {
        if (entity.getID().equals("")) {
            throw new ValidationException("Id incorect!");
        }
        if (entity.getID() == null) {
            throw new ValidationException("Id incorect!");
        }
        if (entity.getNume() == "") {
            throw new ValidationException("Nume incorect!");
        }
        if (entity.getGrupa() < 0) {
            throw new ValidationException("Grupa incorecta!");
        }
        if (entity.getEmail() == null) {
            throw new ValidationException("Email incorect!");
        }
        if (entity.getNume() == null) {
            throw new ValidationException("Nume incorect!");
        }
        if (entity.getEmail().equals("")) {
            throw new ValidationException("Email incorect!");
        }
        // new validations
        if (!entity.getNume().matches("[a-zA-Z ]+")) {
            throw new ValidationException("Nume incorect!");
        }
        if (!entity.getEmail().matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            throw new ValidationException("Email incorect!");
        }
        if (!entity.getProfessor().matches("[a-zA-Z ]+")) {
            throw new ValidationException("Nume profesor incorect!");
        }
        if (entity.getProfessor().equals("")) {
            throw new ValidationException("Nume profesor incorect!");
        }
        if (entity.getProfessor() == null) {
            throw new ValidationException("Nume profesor incorect!");
        }
    }
}
