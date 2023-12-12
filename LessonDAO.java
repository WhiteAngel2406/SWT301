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
import model.Lesson;
import utils.IConstant;

/**
 *
 * @author win
 */
public class LessonDAO extends BaseDAO {

    private int noOfRecords;

//    private int lessonId;
//    private String lessonName;
//    private String youtubeLink;
//    private String attachedFile;
//    private String description;
//    private boolean status;
//    private Chapter chapter;
//    private int createdBy;
//    private Date createdAt;
//    private int updatedBy;
//    private Date updatedAt;
    /**
     * Get list of lessons with pagination
     *
     * @param offset Number of records to skip
     * @param fetch Number of records to select
     * @param searchTerm Specified pattern for searching
     * @return List of lessons which is limited for pagination
     */
    public List<Lesson> getLessons(int offset, int fetch, String searchTerm) {
        List<Lesson> list = new ArrayList<>();
        searchTerm = "%" + searchTerm + "%";
        String sql = "SELECT SQL_CALC_FOUND_ROWS * "
                + "FROM lesson "
                + "WHERE lesson_name LIKE ? "
                + "LIMIT ?, ?";

        try ( Connection connection = getConnection();  PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, searchTerm);
            stm.setInt(2, offset);
            stm.setInt(3, fetch);

            try ( ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    Lesson lesson = new Lesson(
                            rs.getInt("lesson_id"),
                            rs.getString("lesson_name"),
                            rs.getString("youtube_link"),
                            rs.getString("attached_files"),
                            rs.getString("description"),
                            rs.getBoolean("status"),
                            new SubjectSettingDAO().getChapterById(rs.getInt("chapter_id")),
                            rs.getInt("created_by"),
                            rs.getDate("created_at"),
                            rs.getInt("updated_by"),
                            rs.getDate("updated_at"));
                    list.add(lesson);
                }
            }

            try ( PreparedStatement countStm = connection.prepareStatement("SELECT FOUND_ROWS()");  ResultSet countRs = countStm.executeQuery()) {
                if (countRs.next()) {
                    this.noOfRecords = countRs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
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

    public static void main(String[] args) {
        LessonDAO l = new LessonDAO();
        List<Lesson> lessons = l.getLessons(0, 5, "Lesson");
        for (Lesson lesson : lessons) {
            System.out.println(lesson);
        }
    }
}
