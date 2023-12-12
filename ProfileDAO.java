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
public class ProfileDAO extends BaseDAO {

    public User getOneUser(int userId) {
        User user = null;
        String sql = IConstant.SELECT_ONE_USER_QUERY;

        try ( Connection connection = getConnection();  PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setObject(1, userId);
            try ( ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    SettingDAO sdao = new SettingDAO();
                    user = new User(
                            rs.getInt("user_id"),
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
                            rs.getDate("updated_at")
                    );
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProfileDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return user;
    }

    public boolean changePassword(int userId, String newPassword) {
        String sql = IConstant.RESET_PASSWORD_QUERY;
        int check = 0;

        try ( Connection connection = getConnection();  PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setObject(1, newPassword);
            stm.setObject(2, userId);
            stm.setObject(3, userId);
            check = stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }

        return check > 0;
    }

    public boolean changePhoto(int userId, String imgUrl) {
        String sql = IConstant.CHANGE_PHOTO_QUERY;
        int check = 0;

        try ( Connection connection = getConnection();  PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setObject(1, imgUrl);
            stm.setObject(2, userId);
            stm.setObject(3, userId);
            check = stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }

        return check > 0;
    }

    public boolean updateInformation(int userId, String fullName, String mobile) {
        String sql = IConstant.UPDATE_USER_INFORMATION_QUERY;
        int check = 0;

        try ( Connection connection = getConnection();  PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setObject(1, fullName);
            stm.setObject(2, mobile);
            stm.setObject(3, userId);
            stm.setObject(4, userId);
            check = stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }

        return check > 0;
    }

    public static void main(String[] args) {
        ProfileDAO d = new ProfileDAO();
        System.out.println(d.updateInformation(6, "Toan", "0348512186"));

    }

}
