package nz.ac.wgtn.swen301.assignment1.cli;

import nz.ac.wgtn.swen301.assignment1.StudentManager;
import nz.ac.wgtn.swen301.studentdb.NoSuchRecordException;
import nz.ac.wgtn.swen301.studentdb.Student;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class StudentManagerUI {

    // THE FOLLOWING METHOD MUST BE IMPLEMENTED
    /**
     * Executable: the user will provide argument(s) and print details to the console as described in the assignment brief,
     * E.g. a user could invoke this by running "java -cp <someclasspath> <arguments></arguments>"
     * @param arg
     */
    public static void main (String[] arg) {
        switch (arg[0]){
            case "-fetchone":
                try{
                    System.out.println(StudentManager.fetchStudent("id"+arg[1]).toString());

                }
                catch (NoSuchRecordException e){
                    System.err.println("Student with id"+arg[1]+" not found");
                }
                break;
                case "-fetchAll":
                    Objects.requireNonNull(StudentManager.fetchAllStudentIds()).stream().map(id-> {
                        try {
                            return StudentManager.fetchStudent(id);
                        } catch (NoSuchRecordException ignored) {
                            return null;
                        }
                    }).filter(Objects::nonNull).forEach(student->System.out.println(student.getId()+" "+student.getFirstName()+" "+student.getName()));
                break;
            case "-export":

        }
    }
}
