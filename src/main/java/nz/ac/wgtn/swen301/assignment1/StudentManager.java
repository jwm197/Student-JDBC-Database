package nz.ac.wgtn.swen301.assignment1;

import nz.ac.wgtn.swen301.studentdb.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    //cashes so you don't have to look up in the db every time:
    private static final HashMap<String,Student>students=new HashMap<>();
    private static final HashMap<String,Degree>degrees=new HashMap<>();
    private static final String url = "jdbc:derby:memory:studentdb";//db url

    /**Clears the students and degrees caches*/
    static void reset(){
        students.clear();
        degrees.clear();
    }
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
            Connection conn=null;
            Statement stmt=null;
            ResultSet rs=null;
            try {

                conn = DriverManager.getConnection(url);
                stmt = conn.createStatement();
                String sql = "SELECT * FROM STUDENTS WHERE id='" + id + "' ";
                rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    Student student = new Student(rs.getString("id"), rs.getString("first_name"), rs.getString("name"), fetchDegree(rs.getString("degree")));
                    students.put(id, student);
                    return student;
                }
                throw new NoSuchRecordException("Record for id " + id + " Not found");
            } catch (SQLException e) {
                throw new NoSuchRecordException("Record for id " + id + " Not found " + e);
            } finally {
                try{
                    if(rs!=null){
                        rs.close();
                    }
                    if (stmt!=null){
                        stmt.close();
                    }
                    if (conn!=null){
                        conn.close();
                    }
                }
                 catch (SQLException ignored) {}
            }

    }

    /**
     * Return a degree instance with values from the row with the respective id in the database.
     * If an instance with this id already exists, return the existing instance and do not create a second one.
     * @param id the id of the degree being fetched
     * @return a degree object containing the degree being fetched
     * @throws NoSuchRecordException if no record with such an id exists in the database
     * This functionality is to be tested in nz.ac.wgtn.swen301.assignment1.TestStudentManager::testFetchDegree (followed by optional numbers if multiple tests are used)
     */
    public static Degree fetchDegree(String id) throws NoSuchRecordException {

        Connection conn=null;
        Statement stmt=null;
        ResultSet rs=null;
        try {
            if(degrees.containsKey(id)){
                return degrees.get(id);
            }

            conn = DriverManager.getConnection(url);
            stmt = conn.createStatement();
            String sql = "SELECT * FROM DEGREES WHERE id='" + id + "' ";

            rs = stmt.executeQuery(sql);
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
        catch (NullPointerException|SQLException e){
            throw new NoSuchRecordException("Record for id "+id+" Not found "+e);
        }
        finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new NoSuchRecordException("Record for id " + id + " Not found " + e);
            }
        }
    }

    /**
     * Delete a student instance from the database.
     * I.e., after this, trying to read a student with this id will result in a NoSuchRecordException.
     * @param student the student being removed
     * @throws NoSuchRecordException if no record corresponding to this student instance exists in the database
     * This functionality is to be tested in nz.ac.wgtn.swen301.assignment1.TestStudentManager::testRemove
     */
    public static void remove(Student student) throws NoSuchRecordException {
        if (student==null){
            throw new NoSuchRecordException("Student is null");
        }
        else if(student.getId()==null){
            throw new NoSuchRecordException("Student ID is null");
        }
        if(students.containsValue(student)){
            students.remove(student.getId());
        }
        Connection conn=null;
        Statement stmt=null;

        try {
            conn = DriverManager.getConnection(url);
            stmt = conn.createStatement();
            String sql = "DELETE FROM STUDENTS WHERE id='" + student.getId() + "' ";
            if(stmt.executeUpdate(sql)==0){
                throw new NoSuchRecordException("Student not in db");
            }
        }
        catch (SQLException e){
            throw new NoSuchRecordException("Record for student "+student+" Not found "+e);
        }
        finally {
            try {

                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }
            catch (SQLException ignored) {}
        }


    }

    /**
     * Update (synchronize) a student instance with the database.
     * The id will not be changed, but the values for first names or degree in the database might be changed by this operation.
     * After executing this command, the attribute values of the object and the respective database value are consistent.
     * Note that names and first names can only be max 1o characters long.
     * There is no special handling required to enforce this, just ensure that tests only use values with < 10 characters.
     * @param student the student being updated
     * @throws NoSuchRecordException if no record corresponding to this student instance exists in the database
     * This functionality is to be tested in nz.ac.wgtn.swen301.assignment1.TestStudentManager::testUpdate (followed by optional numbers if multiple tests are used)
     */
    public static void update(Student student) throws NoSuchRecordException {
        if(student==null){
            throw new NoSuchRecordException("Student is null");
        }
        else if(student.getId()==null){
            throw new NoSuchRecordException("Student ID is null");
        }
        Connection conn=null;
        Statement stmt=null;

        try {
            conn = DriverManager.getConnection(url);
            stmt = conn.createStatement();
            String sql = "UPDATE STUDENTS SET first_name ='"+ student.getFirstName() +"', name = '"+student.getName()+"', degree ='"+student.getDegree().getId()+"'WHERE id='" + student.getId() + "'";
            if(stmt.executeUpdate(sql)==0){
                throw new NoSuchRecordException("Record for student "+student+" Not found ");
            }
            students.put(student.getId(), student);
        }
        catch (SQLException e){
            throw new NoSuchRecordException("Record for student "+student+" Not found "+e);
        }
        finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }
            catch (SQLException ignored) {}
        }


    }


    /**
     * Create a new student with the values provided, and save it to the database.
     * The student must have a new id that is not being used by any other Student instance or STUDENTS record (row).
     * Note that names and first names can only be max 1o characters long.
     * There is no special handling required to enforce this, just ensure that tests only use values with < 10 characters.
     * @param name The last name of the student
     * @param firstName the first name of the student
     * @param degree The degree the student is doing
     * @return a freshly created student instance
     * This functionality is to be tested in nz.ac.wgtn.swen301.assignment1.TestStudentManager::testNewStudent (followed by optional numbers if multiple tests are used)
     */
    public static Student newStudent(String name,String firstName,Degree degree) {
        Collection<String>ids=fetchAllStudentIds();
        String id="id0";
        if (ids!=null&&!ids.isEmpty()){
            id="id"+(1+ids.stream().map(i->Integer.parseInt(i.substring(2))).max(Integer::compare).orElse(0));
        }
        Student student= new Student(id,name,firstName,degree);
        students.put(id,student);

        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()) {
            String sql = "INSERT INTO STUDENTS VALUES ('" + id + "', '" + name + "', '" + firstName + "', '" + degree.getId() + "')";
            stmt.executeUpdate(sql);

        } catch (SQLException ignored) {
        }
        return student;
    }

    /**
     * Get all student ids currently being used in the database.
     * @return collection of ids
     * This functionality is to be tested in nz.ac.wgtn.swen301.assignment1.TestStudentManager::testFetchAllStudentIds (followed by optional numbers if multiple tests are used)
     */
    public static Collection<String> fetchAllStudentIds() {
        Connection conn;
        Statement stmt;
        List<String>ids=new ArrayList<>();
        try {

            conn = DriverManager.getConnection(url);
            stmt = conn.createStatement();
            String sql = "SELECT id FROM STUDENTS";

            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
               ids.add(rs.getString("id"));
            }
            rs.close();
            stmt.close();
            conn.close();

            return ids;
        }
        catch (SQLException e){
            return null;
        }

    }




}
