package nz.ac.wgtn.swen301.assignment1.cli;

import nz.ac.wgtn.swen301.assignment1.StudentManager;
import nz.ac.wgtn.swen301.studentdb.NoSuchRecordException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import java.util.Objects;
import java.util.stream.Stream;

public class StudentManagerUI {

    // THE FOLLOWING METHOD MUST BE IMPLEMENTED

    /**
     * Executable: the user will provide argument(s) and print details to the console as described in the assignment brief,
     * E.g. a user could invoke this by running "java -cp <someclasspath> <arguments></arguments>"
     *
     * @param arg list of the parameters required to run the command
     */
    public static void main(String[] arg) {
        try {
            switch (arg[0]) {
                case "-fetchone":
                    fetchOne("id" + arg[1]);
                    break;
                case "-fetchall":
                    fetchAll();
                    break;
                case "-export":
                    writeToCSV(arg[1], arg[2]);
                    break;
                default:
                    System.out.println("Unknown command");
            }


        } catch (IndexOutOfBoundsException e) {
            System.err.println("too few arguments specified for command");
        }

    }

    private static void writeToCSV(String flag, String filename) {
        try {
            if (!flag.equals("-f")) {
                System.err.println("-f argument not found");
                return;
            }
            File file = new File(filename + ".csv");
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println("id,first_name,name,degree");
                Objects.requireNonNull(StudentManager.fetchAllStudentIds()).stream().map(id -> {
                            try {
                                return StudentManager.fetchStudent(id);
                            } catch (NoSuchRecordException ignored) {
                                return null;
                            }
                        }).filter(Objects::nonNull).map(student -> student.getId() + "," + student.getFirstName() + "," + student.getName() + "," + student.getDegree().getId())
                        .forEach(writer::println);
            }

        } catch (FileNotFoundException e) {
            System.err.println("file " + filename + " not found " + e);
        }
    }

    /**
     * fetches and prints all students' details
     */
    private static void fetchAll() {
        StudentManager.fetchAllStudentIds().forEach(StudentManagerUI::fetchOne);
    }

    /**
     * fetches and prints out a student's details with a specified id
     *
     * @param id the id of the student being fetched
     */
    private static void fetchOne(String id) {
        try {
            Stream.of(Objects.requireNonNull(StudentManager.fetchStudent(id))).forEach(student -> System.out.println(student.getId() + " " + student.getFirstName() + " " + student.getName()));
        } catch (NoSuchRecordException e) {
            System.err.println("Student with id" + id + " not found");
        }
    }
}
