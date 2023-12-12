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
import model.Trainee;

/**
 *
 * @author win
 */
public class TraineeDAO {
    private Connection connection;
    public void addTrainee(Trainee trainee) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO trainees (id, name, age, class) VALUES (?, ?, ?, ?)");
            preparedStatement.setInt(1, trainee.getId());
            preparedStatement.setString(2, trainee.getName());
            preparedStatement.setInt(3, trainee.getAge());
            preparedStatement.setString(4, trainee.getClassName());

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
     public List<Trainee> getAllTrainees(String className) {
        List<Trainee> trainees = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM trainees WHERE class = ?");
            preparedStatement.setString(1, className);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");

                Trainee trainee = new Trainee(id, name, age, className);
                trainees.add(trainee);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return trainees;
    }
      public void deleteTrainee(int traineeId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM trainees WHERE id = ?");
            preparedStatement.setInt(1, traineeId);

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
       public void updateTrainee(Trainee trainee) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE trainees SET name = ?, age = ? WHERE id = ?");
            preparedStatement.setString(1, trainee.getName());
            preparedStatement.setInt(2, trainee.getAge());
            preparedStatement.setInt(3, trainee.getId());

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
