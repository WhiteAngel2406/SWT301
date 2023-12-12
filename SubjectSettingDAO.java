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
import model.Chapter;
import utils.IConstant;

/**
 *
 * @author win
 */
public class SubjectSettingDAO extends BaseDAO {
    public Chapter getChapterById(int chapterId) {
    try (Connection connection = getConnection();
         PreparedStatement stm = connection.prepareStatement("SELECT * FROM chapter WHERE chapter_id = ?")) {

        stm.setInt(1, chapterId);
        try (ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                return new Chapter(
                        rs.getInt("chapter_id"),
                        rs.getString("chapter_name"),
                        new SubjectDAO().getSubjectById(rs.getInt("subject_id")),
                        rs.getBoolean("status"),
                        rs.getInt("created_by"),
                        rs.getDate("created_at"),
                        rs.getInt("updated_by"),
                        rs.getDate("updated_at"));
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(SubjectSettingDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
}

    
    public static void main(String[] args) {
        System.out.println(new SubjectSettingDAO().getChapterById(1));
    }
}
