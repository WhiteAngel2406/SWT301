package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Subject;
import utils.IConstant;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author minhf
 */
public class SubjectDAO extends BaseDAO {

    private int noOfRecords;

    public List<Subject> getSubjectList() {
        List<Subject> list = new ArrayList<>();
        String sql = IConstant.SELECT_ALL_SUBJECT_QUERY;

        try ( Connection connection = getConnection();  PreparedStatement stm = connection.prepareStatement(sql);  ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                Subject subject = new Subject(
                        rs.getInt("subject_id"),
                        rs.getString("subject_code"),
                        rs.getString("subject_name"),
                        rs.getInt("manager_id"),
                        rs.getBoolean("status"),
                        rs.getInt("category_id"),
                        rs.getInt("created_by"),
                        rs.getDate("created_at"),
                        rs.getInt("updated_by"),
                        rs.getDate("updated_at"),
                        rs.getString("description"));

                list.add(subject);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    public void addSubject(String subjectCode, String subjectName, int managerId, int categoryId, String description) {
        String sql = IConstant.ADD_SUBJECT_QUERY;

        try ( Connection connection = getConnection();  PreparedStatement stm = connection.prepareStatement(sql)) {

            int adminId = new UserDAO().getAdminId();
            stm.setString(1, subjectCode);
            stm.setString(2, subjectName);
            stm.setInt(3, managerId);
            stm.setInt(4, categoryId);
            stm.setInt(5, adminId);
            stm.setString(6, description);
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateSubject(int subjectId, String subjectCode, String subjectName, int managerId, int status, int categoryId, String description) {
        String sql = IConstant.UPDATE_SUBJECT_QUERY;

        try ( Connection connection = getConnection();  PreparedStatement stm = connection.prepareStatement(sql)) {

            int adminId = new UserDAO().getAdminId();
            stm.setString(1, subjectCode);
            stm.setString(2, subjectName);
            stm.setInt(3, managerId);
            stm.setInt(4, status);
            stm.setInt(5, categoryId);
            stm.setInt(6, adminId);
            stm.setInt(7, subjectId);
            stm.setString(8, description);
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Subject> getSubjects(int offset, int fetch, String searchTerm) {
        List<Subject> list = new ArrayList<>();
        String sql = IConstant.SELECT_SUBJECT_QUERY;
        searchTerm = "%" + searchTerm + "%";

        try ( Connection connection = getConnection();  PreparedStatement stm = connection.prepareStatement(sql)) {

            stm.setString(1, searchTerm);
            stm.setString(2, searchTerm);
            stm.setInt(3, offset);
            stm.setInt(4, fetch);

            try ( ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    Subject subject = new Subject(
                            rs.getInt("subject_id"),
                            rs.getString("subject_code"),
                            rs.getString("subject_name"),
                            rs.getInt("manager_id"),
                            rs.getBoolean("status"),
                            rs.getInt("category_id"),
                            rs.getInt("created_by"),
                            rs.getDate("created_at"),
                            rs.getInt("updated_by"),
                            rs.getDate("updated_at"),
                            rs.getString("description"));
                    list.add(subject);
                }
            }

            try ( PreparedStatement countStm = connection.prepareStatement("SELECT FOUND_ROWS()");  ResultSet countRs = countStm.executeQuery()) {
                if (countRs.next()) {
                    this.noOfRecords = countRs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    public int getNoOfRecords() {
        return noOfRecords;
    }

    public void deleteSubject(int subjectId) {
        try ( Connection connection = getConnection();  PreparedStatement stm = connection.prepareStatement(IConstant.DELETE_SUBJECT_QUERY)) {

            stm.setInt(1, subjectId);
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Subject getSubjectById(int subjectId) {
        String sql = "SELECT * FROM cmslvl10_db.subject WHERE subject_id=?";

        try ( Connection connection = getConnection();  PreparedStatement stm = connection.prepareStatement(sql)) {

            stm.setInt(1, subjectId);

            try ( ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    return new Subject(
                            rs.getInt("subject_id"),
                            rs.getString("subject_code"),
                            rs.getString("subject_name"),
                            rs.getInt("manager_id"),
                            rs.getBoolean("status"),
                            rs.getInt("category_id"),
                            rs.getInt("created_by"),
                            rs.getDate("created_at"),
                            rs.getInt("updated_by"),
                            rs.getDate("updated_at"),
                            rs.getString("description"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static void main(String[] args) {
        SubjectDAO s = new SubjectDAO();
        System.out.println(s.getSubjectById(3));
    }
}
