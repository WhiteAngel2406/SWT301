package dal;

import static dal.BaseDAO.getConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Setting;
import utils.IConstant;

public class SettingDAO extends BaseDAO {

    private int noOfSemesters;

    public Setting getSettingById(int id) {
        try ( Connection connection = getConnection();  PreparedStatement stm = connection.prepareStatement(IConstant.SELECT_SETTING_BYID_QUERY)) {
            stm.setInt(1, id);
            try ( ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    return new Setting(
                            rs.getInt("setting_id"),
                            rs.getString("setting_group"),
                            rs.getString("setting_name"),
                            rs.getString("setting_value"),
                            rs.getString("description"),
                            rs.getBoolean("status"),
                            rs.getInt("created_by"),
                            rs.getDate("created_at"),
                            rs.getInt("updated_by"),
                            rs.getDate("updated_at")
                    );
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SettingDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public int getSettingIdByName(String name) {
        try ( Connection connection = getConnection();  PreparedStatement stm = connection.prepareStatement(IConstant.SELECT_SETTINGID_BYNAME_QUERY)) {
            stm.setString(1, name);
            try ( ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    return rs.getInt("setting_id");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SettingDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public List<Setting> getSettingByRole() {
        List<Setting> list = new ArrayList<>();
        try ( Connection connection = getConnection();  PreparedStatement stm = connection.prepareStatement(IConstant.GET_SETTINGBYROLE_QUERY);  ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                Setting setting = new Setting(
                        rs.getInt("setting_id"),
                        rs.getString("setting_group"),
                        rs.getString("setting_name"),
                        rs.getString("setting_value"),
                        rs.getString("description"),
                        rs.getBoolean("status"),
                        rs.getInt("created_by"),
                        rs.getDate("created_at"),
                        rs.getInt("updated_by"),
                        rs.getDate("updated_at")
                );
                list.add(setting);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SettingDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public List<Setting> getSettingBySemester() {
        List<Setting> list = new ArrayList<>();
        try ( Connection connection = getConnection();  PreparedStatement stm = connection.prepareStatement(IConstant.GET_SEMESTER_QUERY);  ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                Setting setting = new Setting(
                        rs.getInt("setting_id"),
                        rs.getString("setting_group"),
                        rs.getString("setting_name"),
                        rs.getString("setting_value"),
                        rs.getString("description"),
                        rs.getBoolean("status"),
                        rs.getInt("created_by"),
                        rs.getDate("created_at"),
                        rs.getInt("updated_by"),
                        rs.getDate("updated_at")
                );
                list.add(setting);
            }
            try ( ResultSet countRs = stm.executeQuery("SELECT FOUND_ROWS()")) {
                if (countRs.next()) {
                    this.noOfSemesters = countRs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SettingDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public int getNoOfSemesters() {
        return noOfSemesters;
    }

    public void addSemester(String setting_name, String setting_value, int display_order, String description) {
        int admin_id = new UserDAO().getAdminId();
        try ( Connection connection = getConnection();  PreparedStatement stm = connection.prepareStatement(IConstant.ADD_SEMESTER_QUERY)) {
            stm.setString(1, setting_name);
            stm.setString(2, setting_value);
            stm.setInt(3, display_order);
            stm.setString(4, description);
            stm.setInt(5, admin_id);
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SettingDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getNoOfSemester() {
        try ( Connection connection = getConnection();  PreparedStatement stm = connection.prepareStatement(IConstant.GET_NOOFSEMESTER_QUERY);  ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                return rs.getInt("no");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SettingDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public void updateSemester(String setting_name, String setting_value, String description, int status, int setting_id) {
        try ( Connection connection = getConnection();  PreparedStatement stm = connection.prepareStatement(IConstant.UPDATE_SEMESTER_QUERY)) {
            stm.setString(1, setting_name);
            stm.setString(2, setting_value);
            stm.setString(3, description);
            stm.setInt(4, status);
            stm.setInt(5, setting_id);
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SettingDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Get list of setting by Domain
     *
     * @return List of Domain
     */
    public List<Setting> getSettingByDomain() {
        SettingDAO sdao = new SettingDAO();
        List<Setting> list = new ArrayList<>();
        try ( Connection connection = getConnection();  PreparedStatement stm = connection.prepareStatement(IConstant.GET_DOMAIN_QUERY);  ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                Setting setting = new Setting(rs.getInt("setting_id"),
                        rs.getString("setting_group"),
                        rs.getString("setting_name"),
                        rs.getString("setting_value"),
                        rs.getString("description"),
                        rs.getBoolean("status"),
                        rs.getInt("created_by"),
                        rs.getDate("created_at"),
                        rs.getInt("updated_by"),
                        rs.getDate("updated_at"));
                list.add(setting);

            }
            try ( ResultSet countRs = stm.executeQuery("SELECT FOUND_ROWS()")) {
                if (countRs.next()) {
                    this.noOfSemesters = countRs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SettingDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public void updateDomain(String setting_name, String setting_value, String description, int status, int setting_id) {
        try ( Connection connection = getConnection();  PreparedStatement stm = connection.prepareStatement(IConstant.UPDATE_DOMAIN_QUERY)) {
            stm.setString(1, setting_name);
            stm.setString(2, setting_value);
            stm.setString(3, description);
            stm.setInt(4, status);
            stm.setInt(5, setting_id);
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SettingDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    public static void main(String[] args) {
        List<Setting> list = new SettingDAO().getSettingByRole();
        for (Setting setting : list) {
            System.out.println(setting);
        }
    }
}
