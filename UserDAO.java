/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;
import utils.IConstant;

/**
 *
 * @author win
 */
public class UserDAO extends BaseDAO {

    private int noOfRecords;

    /**
     * Perform update user's detailed information operation by admin
     *
     * @param fullName Property full name of user
     * @param password Property password of user
     * @param email Property email of user
     * @param mobile Property mobile of user
     * @param status Property status of user
     * @param roleId user's role ID
     * @param userId ID of user which is updated
     */
    public void updateUser(String fullName, String password, String email, String mobile, int status, int roleId, int userId, String description) {
        try ( Connection connection = getConnection();  PreparedStatement stm = connection.prepareStatement(IConstant.UPDATE_USER_QUERY)) {

            stm.setString(1, fullName);
            stm.setString(2, password);
            stm.setString(3, email);
            stm.setString(4, mobile);
            stm.setInt(5, status);
            stm.setInt(6, getAdminId());
            stm.setInt(7, roleId);
            stm.setString(8, description);
            stm.setInt(9, userId);
            stm.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<User> getUserList() {
        List<User> list = new ArrayList<>();
        SettingDAO sdao = new SettingDAO();

        try ( Connection connection = getConnection();  PreparedStatement stm = connection.prepareStatement(IConstant.SELECT_ALL_USER_QUERY);  ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                User user = new User(rs.getInt("user_id"),
                        rs.getString("full_name"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getBoolean("status"),
                        rs.getString("avatar_image"),
                        rs.getString("description"),
                        sdao.getSettingById(rs.getInt("role_id")),
                        rs.getInt("created_by"),
                        rs.getDate("created_at"),
                        rs.getInt("updated_by"),
                        rs.getDate("updated_at"));
                list.add(user);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    /**
     * Perform add a new user operation by admin
     *
     * @param fullName Property full name of user
     * @param password Property password of user
     * @param email Property email of user
     * @param mobile Property mobile of user
     */
    public void addUser(String fullName, String password, String email, String mobile, int status) {
        int id = getAdminId();
        try ( Connection connection = getConnection();  PreparedStatement stm = connection.prepareStatement(IConstant.ADD_USER_QUERY);) {
            stm.setString(1, fullName);
            stm.setString(2, password);
            stm.setString(3, email);
            stm.setString(4, mobile);
            stm.setInt(5, status);
            stm.setInt(6, id);
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Get list of users with pagination
     *
     * @param offset Number of users to skip
     * @param fetch Number of users to select
     * @param searchTerm Specified pattern for searching
     * @return List of users which is limited for pagination
     */
    public List<User> getUsers(int offset, int fetch, String searchTerm, int status, int roleId) {
        List<User> list = new ArrayList<>();
        SettingDAO sdao = new SettingDAO();
        searchTerm = "%" + searchTerm + "%";
        String s1 = "", s2 = "";
        if (status != -1) {
            s1 = " AND u.status=" + status;
        }
        if (roleId != -1) {
            s2 = " AND role_id=" + roleId;
        }
        String sql = "select SQL_CALC_FOUND_ROWS u.* "
                + "from user u  join setting s "
                + "on u.role_id = s.setting_id "
                + "where 1 = 1" + s1 + s2 + " AND (u.full_name like ? "
                + "or u.email like ? "
                + "or u.phone_number like ?) "
                + "order by s.display_order asc "
                + "limit ?,?";
        try ( Connection connection = getConnection();  PreparedStatement stm = connection.prepareStatement(sql)) {

            stm.setString(1, searchTerm);
            stm.setString(2, searchTerm);
            stm.setString(3, searchTerm);
            stm.setInt(4, offset);
            stm.setInt(5, fetch);

            try ( ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    User user = new User(rs.getInt("user_id"),
                            rs.getString("full_name"),
                            rs.getString("password"),
                            rs.getString("email"),
                            rs.getString("phone_number"),
                            rs.getBoolean("status"),
                            rs.getString("avatar_image"),
                            rs.getString("description"),
                            sdao.getSettingById(rs.getInt("role_id")),
                            rs.getInt("created_by"),
                            rs.getDate("created_at"),
                            rs.getInt("updated_by"),
                            rs.getDate("updated_at"));
                    list.add(user);
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

    /**
     * Get number of all records in database
     *
     * @return Number of records
     */
    public int getNoOfRecords() {
        return noOfRecords;
    }

    /**
     * get UserID of user who is Administrator
     *
     * @return ID of Administrator in User table, -1 if not exists
     */
    public int getAdminId() {
        try ( Connection connection = getConnection();  PreparedStatement stm = connection.prepareStatement(IConstant.SELECT_ADMIN_QUERY);  ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public User checkLogin(String email, String password) {
        SettingDAO sdao = new SettingDAO();
        try ( Connection connection = getConnection();  PreparedStatement stm = connection.prepareStatement(IConstant.CHECK_LOGIN_QUERY)) {

            stm.setString(1, email);
            stm.setString(2, password);
            try ( ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    User user = new User(rs.getInt("user_id"),
                            rs.getString("full_name"),
                            rs.getString("password"),
                            rs.getString("email"),
                            rs.getString("phone_number"),
                            rs.getBoolean("status"),
                            rs.getString("avatar_image"),
                            rs.getString("description"),
                            sdao.getSettingById(rs.getInt("role_id")),
                            rs.getInt("created_by"),
                            rs.getDate("created_at"),
                            rs.getInt("updated_by"),
                            rs.getDate("updated_at"));
                    return user;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void resetPassword(String password, int userId) {
        try ( Connection connection = getConnection();  PreparedStatement stm = connection.prepareStatement(IConstant.RESET_PASSWORD_QUERY)) {

            stm.setString(1, password);
            stm.setInt(2, userId);
            stm.setInt(3, userId);
            stm.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getUserIdByEmail(String email) {
        try ( Connection connection = getConnection();  PreparedStatement stm = connection.prepareStatement(IConstant.SELECT_USERID_BYEMAIL);  ResultSet rs = stm.executeQuery()) {

            stm.setString(1, email);
            while (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public static void main(String[] args) {
        List<User> user = new UserDAO().getUsers(0, 5, "", 1, 1);
        for (User user1 : user) {
            System.out.println(user1);
        }
    }
}
