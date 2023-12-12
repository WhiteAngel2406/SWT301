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
import model.SubjectCategory;
import utils.IConstant;

/**
 *
 * @author minhf
 */
public class SubjectCategoryDAO extends BaseDAO {

    public List<SubjectCategory> getSubCategory() {
        List<SubjectCategory> list = new ArrayList<>();
        String sql = IConstant.SELECT_SUBJECTCATEGORY_QUERY;

        try ( Connection connection = getConnection();  PreparedStatement stm = connection.prepareStatement(sql);  ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                SubjectCategory subjectCategory = new SubjectCategory(
                        rs.getInt("category_id"),
                        rs.getString("category_code"),
                        rs.getString("category_name"),
                        rs.getBoolean("status"),
                        rs.getInt("created_by"),
                        rs.getDate("created_at"),
                        rs.getInt("updated_by"),
                        rs.getDate("updated_at")
                );
                list.add(subjectCategory);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SubjectCategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    public static void main(String[] args) {
        SubjectCategoryDAO sc = new SubjectCategoryDAO();
        List<SubjectCategory> list1 = sc.getSubCategory();
        for (SubjectCategory subjectcategory : list1) {
            System.out.println(subjectcategory);
        }
    }
}
