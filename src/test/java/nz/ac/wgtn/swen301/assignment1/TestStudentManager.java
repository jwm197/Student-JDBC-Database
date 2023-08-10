package nz.ac.wgtn.swen301.assignment1;

import nz.ac.wgtn.swen301.studentdb.Degree;
import nz.ac.wgtn.swen301.studentdb.NoSuchRecordException;
import nz.ac.wgtn.swen301.studentdb.Student;
import nz.ac.wgtn.swen301.studentdb.StudentDB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for StudentManager, to be extended.
 */
public class TestStudentManager {

    // DO NOT REMOVE THE FOLLOWING -- THIS WILL ENSURE THAT THE DATABASE IS AVAILABLE
    // AND IN ITS INITIAL STATE BEFORE EACH TEST RUNS
    @BeforeEach
    public void init() {
        StudentDB.init();
        StudentManager.reset();
    }

    // DO NOT REMOVE BLOCK ENDS HERE

    @Test
    public void dummyTest() throws Exception {
        Student student = StudentManager.fetchStudent("id42");
        // THIS WILL INITIALLY FAIL !!
        assertNotNull(student);
    }

    @Test
    public void testFetchStudent1() throws Exception {
        Student student = StudentManager.fetchStudent("id0");
        assertEquals(student, new Student("id0", "James", "Smith", new Degree("deg0", "BSc Computer Science")));

    }

    @Test
    public void testFetchStudent2() throws Exception {

        Student student = StudentManager.fetchStudent("id1");
        assertEquals(student, new Student("id1", "John", "Jones", new Degree("deg1", "BSc Computer Graphics")));
        Student student2 = StudentManager.fetchStudent("id1");
        assertSame(student, student2);
    }

    @Test
    public void testFetchStudent3() throws Exception {
        Student student = StudentManager.fetchStudent("id3");
        assertNotEquals(student, new Student("id3", "James", "Smith", new Degree("deg0", "BSc Computer Science")));

    }

    @Test
    public void testFetchStudent4() {
        try {
            StudentManager.fetchStudent("id-3");
            fail("Id shouldn't exist");
        } catch (NoSuchRecordException ignored) {
        }
    }

    @Test
    public void testFetchStudent5() {
        try {
            StudentManager.fetchStudent(null);
            fail("Id shouldn't be null");
        } catch (NoSuchRecordException ignored) {
        }
    }

    @Test
    public void testFetchDegree1() throws NoSuchRecordException {
        Degree degree = StudentManager.fetchDegree("deg2");
        assertEquals(degree, new Degree("deg2", "BE Cybersecurity"));
        Degree degree2 = StudentManager.fetchDegree("deg2");
        assertSame(degree, degree2, "objects aren't the same");
    }

    @Test
    public void testFetchDegree2() {
        try {
            StudentManager.fetchDegree(null);
            fail("Id shouldn't be null");
        } catch (NoSuchRecordException ignored) {
        }
    }

    @Test
    public void testFetchDegree3() {
        try {
            StudentManager.fetchDegree("deg-2");
            fail("Id shouldn't exist");
        } catch (NoSuchRecordException ignored) {
        }
    }

    @Test
    public void testFetchDegree5() throws NoSuchRecordException {
        Degree degree = StudentManager.fetchDegree("deg5");
        assertSame(degree, StudentManager.fetchDegree("deg5"));
    }

    @Test
    public void testRemove1() throws NoSuchRecordException {
        Student student = StudentManager.fetchStudent("id0");
        StudentManager.remove(student);
        try {
            StudentManager.fetchStudent("id0");
            fail("Student shouldn't exist");
        } catch (NoSuchRecordException ignored) {
        }
    }

    @Test
    public void testRemove2() {
        try {
            StudentManager.remove(null);
            fail("Student shouldn't be null");
        } catch (NoSuchRecordException ignored) {
        }
    }

    @Test
    public void testRemove3() {
        try {
            Student student = new Student();//assigns null id to object
            StudentManager.remove(student);
            fail("Student shouldn't have null id");
        } catch (NoSuchRecordException ignored) {
        }
    }

    @Test
    public void testRemove4() throws NoSuchRecordException {
        Student student = StudentManager.fetchStudent("id7");
        StudentManager.remove(student);
        try {
            StudentManager.remove(student);
            fail("Student shouldn't exist to remove");
        } catch (NoSuchRecordException ignored) {
        }
    }

    @Test
    public void testUpdate1() throws NoSuchRecordException {
        Student student = new Student("id3", "Clark", "Kent", StudentManager.fetchDegree("deg1"));
        StudentManager.update(student);
        assertEquals(StudentManager.fetchStudent("id3"), student);
    }

    @Test
    public void testUpdate2() {
        try {
            Student student = StudentManager.fetchStudent("id7");
            StudentManager.remove(student);
            StudentManager.update(student);
            fail("Student shouldn't be in database");
        } catch (NoSuchRecordException ignored) {
            try {
                StudentManager.update(null);
                fail("Student can't be null");
            } catch (NoSuchRecordException ignored1) {
            }
        }

    }

    @Test
    public void testFetchAllIds1() {
        assertEquals(Objects.requireNonNull(StudentManager.fetchAllStudentIds()).size(), 10000, "Number of all ids is wrong");
    }

    @Test
    public void testFetchAllIds2() throws NoSuchRecordException {

        assertEquals(Objects.requireNonNull(StudentManager.fetchAllStudentIds()).size(), 10000, "Number of all ids is wrong");
        StudentManager.remove(StudentManager.fetchStudent("id2"));
        assertEquals(Objects.requireNonNull(StudentManager.fetchAllStudentIds()).size(), 9999, "Number of all ids is wrong");

    }

    @Test
    public void testNewStudent1() throws NoSuchRecordException {
        Collection<String> ids = StudentManager.fetchAllStudentIds();
        Student student = StudentManager.newStudent("George", "Jack", StudentManager.fetchDegree("deg1"));
        assertNotNull(ids, "List of ids is null");
        assertNotNull(student, "Student is null");
        assertNotNull(student.getId(), "student id is null");
        assertFalse(ids.contains(student.getId()), "Id " + student.getId() + " is already in the list");
        Collection<String> ids2 = StudentManager.fetchAllStudentIds();
        assertNotNull(ids2, "ids2 list is null");
        assertTrue(ids2.contains(student.getId()), "list of ids doesn't contain the id " + student.getId());
        assertEquals(ids.size() + 1, ids2.size(), "new list of ids isn't one bigger than the last one");
    }

    @Test
    public void testNewStudent2() throws NoSuchRecordException {

        Student student = StudentManager.newStudent("George", "Jack", StudentManager.fetchDegree("deg1"));
        Student student2 = StudentManager.newStudent("George", "Jack", StudentManager.fetchDegree("deg1"));
        assertNotEquals(student.getId(), student2.getId(), "Students share same id");
    }

    @Test
    public void testPerformance() throws NoSuchRecordException, InterruptedException {
        StudentManager.fetchStudent("id0");
        Thread.sleep(2);
        Random rand = new Random();
        long time1 = System.nanoTime();
        IntStream.range(0, 500).parallel().forEach(num -> {
            try {
                StudentManager.fetchStudent("id" + ((rand.nextInt() * 9999)));
            } catch (NoSuchRecordException ignored) {

            }
        });
        long time2 = System.nanoTime();
        assertTrue(time2 - time1 <= 1000000000, "too slow");
    }


}
