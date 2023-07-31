package nz.ac.wgtn.swen301.assignment1;

import nz.ac.wgtn.swen301.studentdb.*;
import java.sql.*;
import java.util.Collection;
import java.util.HashMap;

/**
 * A student manager providing basic CRUD operations for instances of Student, and a read operation for instances of Degree.
 * @author jens dietrich
 */
public class StudentManager {

    // DO NOT REMOVE THE FOLLOWING -- THIS WILL ENSURE THAT THE DATABASE IS AVAILABLE
    // AND THE APPLICATION CAN CONNECT TO IT WITH JDBC
    static {
        StudentDB.init();
    }
    // DO NOT REMOVE BLOCK ENDS HERE
    private static HashMap<String,Student>students=new HashMap<>();
    private static HashMap<String,Degree>degrees=new HashMap<>();
    private static final String url = "jdbc:derby:memory:studentdb";
    // THE FOLLOWING METHODS MUST BE IMPLEMENTED :

    /**
     * Return a student instance with values from the row with the respective id in the database.
     * If an instance with this id already exists, return the existing instance and do not create a second one.
     * @param id The id of the student
     * @return the student with the id
     * @throws NoSuchRecordException if no record with such an id exists in the database
     * This functionality is to be tested in nz.ac.wgtn.swen301.assignment1.TestStudentManager::testFetchStudent (followed by optional numbers if multiple tests are used)
     */
    public static Student fetchStudent(String id) throws NoSuchRecordException {
        if(students.containsKey(id)){
            return students.get(id);
        }
        Connection conn;
        Statement stmt;
        String url = "jdbc:derby:memory:studentdb";
        try {

            conn = DriverManager.getConnection(url);
            stmt = conn.createStatement();
            String sql = "SELECT * FROM STUDENTS WHERE id='" + id + "' ";

            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                Student student= new Student(rs.getString("id"),rs.getString("first_name"), rs.getString("name"),fetchDegree(rs.getString("degree")));
                students.put(id,student);
                rs.close();
                stmt.close();
                conn.close();
                return student;
            }
            throw new NoSuchRecordException("Record for id "+id+" Not found");
        }
        catch (SQLException e){
            throw new NoSuchRecordException("Record for id "+id+" Not found "+e);
        }
    }

    /**
     * Return a degree instance with values from the row with the respective id in the database.
     * If an instance with this id already exists, return the existing instance and do not create a second one.
     * @param id
     * @return
     * @throws NoSuchRecordException if no record with such an id exists in the database
     * This functionality is to be tested in nz.ac.wgtn.swen301.assignment1.TestStudentManager::testFetchDegree (followed by optional numbers if multiple tests are used)
     */
    public static Degree fetchDegree(String id) throws NoSuchRecordException {
        if(degrees.containsKey(id)){
            return degrees.get(id);
        }
        Connection conn;
        Statement stmt;

        try {

            conn = DriverManager.getConnection(url);
            stmt = conn.createStatement();
            String sql = "SELECT * FROM DEGREES WHERE id='" + id + "' ";

            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                Degree degree=new Degree(rs.getString("id"),rs.getString("name"));
                degrees.put(id,degree);
                rs.close();
                stmt.close();
                conn.close();
                return degree;
            }
            throw new NoSuchRecordException("Record for id "+id+" Not found");
        }
        catch (SQLException e){
            throw new NoSuchRecordException("Record for id "+id+" Not found "+e);
        }
    }

    /**
     * Delete a student instance from the database.
     * I.e., after this, trying to read a student with this id will result in a NoSuchRecordException.
     * @param student
     * @throws NoSuchRecordException if no record corresponding to this student instance exists in the database
     * This functionality is to be tested in nz.ac.wgtn.swen301.assignment1.TestStudentManager::testRemove
     */
    public static void remove(Student student) throws NoSuchRecordException {
        if (student.equals(null)){
            throw new NoSuchRecordException("Student is null");
        }
        if(students.containsValue(student)){
            students.remove(student.getId());
        }
        Connection conn;
        Statement stmt;

        try {
            conn = DriverManager.getConnection(url);
            stmt = conn.createStatement();
            String sql = "DELETE * FROM DEGREES WHERE id='" + student.getId() + "' ";
            ResultSet rs = stmt.executeQuery(sql);
            rs.close();
            stmt.close();
            conn.close();
        }
        catch (SQLException e){
            throw new NoSuchRecordException("Record for student "+student.toString()+" Not found "+e);
        }


    }

    /**
     * Update (synchronize) a student instance with the database.
     * The id will not be changed, but the values for first names or degree in the database might be changed by this operation.
     * After executing this command, the attribute values of the object and the respective database value are consistent.
     * Note that names and first names can only be max 1o characters long.
     * There is no special handling required to enforce this, just ensure that tests only use values with < 10 characters.
     * @param student
     * @throws NoSuchRecordException if no record corresponding to this student instance exists in the database
     * This functionality is to be tested in nz.ac.wgtn.swen301.assignment1.TestStudentManager::testUpdate (followed by optional numbers if multiple tests are used)
     */
    public static void update(Student student) throws NoSuchRecordException {}


    /**
     * Create a new student with the values provided, and save it to the database.
     * The student must have a new id that is not being used by any other Student instance or STUDENTS record (row).
     * Note that names and first names can only be max 1o characters long.
     * There is no special handling required to enforce this, just ensure that tests only use values with < 10 characters.
     * @param name
     * @param firstName
     * @param degree
     * @return a freshly created student instance
     * This functionality is to be tested in nz.ac.wgtn.swen301.assignment1.TestStudentManager::testNewStudent (followed by optional numbers if multiple tests are used)
     */
    public static Student newStudent(String name,String firstName,Degree degree) {
        return null;
    }

    /**
     * Get all student ids currently being used in the database.
     * @return
     * This functionality is to be tested in nz.ac.wgtn.swen301.assignment1.TestStudentManager::testFetchAllStudentIds (followed by optional numbers if multiple tests are used)
     */
    public static Collection<String> fetchAllStudentIds() {
        return null;
    }



}
