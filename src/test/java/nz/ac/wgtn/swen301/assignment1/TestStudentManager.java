package nz.ac.wgtn.swen301.assignment1;

import nz.ac.wgtn.swen301.studentdb.Degree;
import nz.ac.wgtn.swen301.studentdb.NoSuchRecordException;
import nz.ac.wgtn.swen301.studentdb.Student;
import nz.ac.wgtn.swen301.studentdb.StudentDB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for StudentManager, to be extended.
 */
public class TestStudentManager {

    // DO NOT REMOVE THE FOLLOWING -- THIS WILL ENSURE THAT THE DATABASE IS AVAILABLE
    // AND IN ITS INITIAL STATE BEFORE EACH TEST RUNS
    @BeforeEach
    public  void init () {
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
    public void testFetchStudent1()throws Exception{
        Student student = StudentManager.fetchStudent("id0");
        assertEquals(student,new Student("id0","James","Smith",new Degree("deg0","BSc Computer Science")));

    }

    @Test
    public void testFetchStudent2()throws Exception{

        Student student = StudentManager.fetchStudent("id1");
        assertEquals(student,new Student("id1","John","Jones",new Degree("deg1","BSc Computer Graphics")));
        Student student2 =StudentManager.fetchStudent("id1");
        assertSame(student,student2);
    }
    @Test
    public void testFetchStudent3()throws Exception{
        Student student = StudentManager.fetchStudent("id3");
        assertNotEquals(student,new Student("id3","James","Smith",new Degree("deg0","BSc Computer Science")));

    }
    @Test
    public void testFetchStudent4() {
        try{
            StudentManager.fetchStudent("id-3");
            fail("Id shouldn't exist");
        }
        catch (NoSuchRecordException ignored){}
    }
    @Test
    public void testFetchStudent5() {
        try{
            StudentManager.fetchStudent(null);
            fail("Id shouldn't be null");
        }
        catch (NoSuchRecordException ignored){}
    }
    @Test
    public void testFetchDegree1() throws NoSuchRecordException {
        Degree degree = StudentManager.fetchDegree("deg2");
        assertEquals(degree,new Degree("deg2","BE Cybersecurity"));
    }
    @Test
    public void testFetchDegree2() throws NoSuchRecordException {
        Degree degree = StudentManager.fetchDegree("deg2");
        assertEquals(degree,new Degree("deg2","BE Cybersecurity"));
    }
    @Test
    public void testFetchDegree3() {
        try{
            StudentManager.fetchDegree(null);
            fail("Id shouldn't be null");
        }
        catch (NoSuchRecordException ignored){}
    }
    @Test
    public void testFetchDegree4() {
        try{
            StudentManager.fetchDegree("deg-2");
            fail("Id shouldn't exist");
        }
        catch (NoSuchRecordException ignored){}
    }
    @Test
    public void testFetchDegree5() throws NoSuchRecordException {
        Degree degree = StudentManager.fetchDegree("deg5");
        assertSame(degree,StudentManager.fetchDegree("deg5"));
    }
    @Test
    public void testRemove1() throws NoSuchRecordException {
        Student student = StudentManager.fetchStudent("id0");
        StudentManager.remove(student);
        try{
            StudentManager.fetchStudent("id0");
            fail("Student shouldn't exist");
        }
        catch (NoSuchRecordException ignored){}
    }
    @Test
    public void testRemove2() throws NoSuchRecordException {
        try{
            StudentManager.remove(null);
            fail("Student shouldn't be null");
        }
        catch (NoSuchRecordException ignored){}
    }
    @Test
    public void testRemove3() throws NoSuchRecordException {
        try{
            Student student=new Student();//assigns null id to object
            StudentManager.remove(student);
            fail("Student shouldn't have null id");
        }
        catch (NoSuchRecordException ignored){}
    }
    @Test
    public void testRemove4() throws NoSuchRecordException {
        Student student = StudentManager.fetchStudent("id7");
        StudentManager.remove(student);
        try{
            StudentManager.remove(student);
            fail("Student shouldn't exist to remove");
        }
        catch (NoSuchRecordException ignored){}
    }
    @Test
    public void testUpdate() throws NoSuchRecordException {
            Student student=new Student("id3","Clark","Kent",StudentManager.fetchDegree("deg1"));
            StudentManager.update(student);
            assertEquals(StudentManager.fetchStudent("id3"),student);
    }
    @Test
    public void testUpdate2() throws NoSuchRecordException {
        try {
            Student student = StudentManager.fetchStudent("id7");
            StudentManager.remove(student);
            StudentManager.update(student);
            fail("Student shouldn't be in database");
        }
        catch (NoSuchRecordException ignored){}

    }






}
