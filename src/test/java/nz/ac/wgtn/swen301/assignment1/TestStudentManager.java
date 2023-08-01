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
    }
    // DO NOT REMOVE BLOCK ENDS HERE

    @Test
    public void dummyTest() throws Exception {
        Student student = new StudentManager().fetchStudent("id42");
        // THIS WILL INITIALLY FAIL !!
        assertNotNull(student);
    }
    @Test
    public void testFetchStudent1()throws Exception{
        Student student = StudentManager.fetchStudent("id0");
        assertEquals(new Student("id0","James","Smith",new Degree("deg0","BSc Computer Science")),student);

    }
    @Test
    public void testFetchStudent2()throws Exception{
        //test is the same as above to test cached version rather than getting from the db directly as in testFetchStudent1
        Student student = StudentManager.fetchStudent("id0");
        assertEquals(new Student("id0","James","Smith",new Degree("deg0","BSc Computer Science")),student);

    }
    @Test
    public void testFetchStudent3()throws Exception{
        Student student = StudentManager.fetchStudent("id1");
        assertEquals(new Student("id1","John","Jones",new Degree("deg1","BSc Computer Graphics")),student);
        Student student2 =StudentManager.fetchStudent("id1");
        assertSame(student,student2);
    }
    @Test
    public void testFetchStudent4()throws Exception{
        Student student = StudentManager.fetchStudent("id3");
        assertFalse(new Student("id3","James","Smith",new Degree("deg0","BSc Computer Science")).equals(student));

    }
    @Test
    public void testFetchStudent5()throws Exception{
        try{
            Student student = StudentManager.fetchStudent("id-3");
            fail("Id shouldn't exist");
        }
        catch (NoSuchRecordException e){}
    }
    @Test
    public void testFetchStudent6()throws Exception{
        try{
            Student student = StudentManager.fetchStudent(null);
            fail("Id shouldn't be null");
        }
        catch (NoSuchRecordException e){}
    }

}
