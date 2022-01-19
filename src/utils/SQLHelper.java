package utils;

import java.sql.*;
import java.util.Map;
import java.util.Scanner;

import model.Task;

public class SQLHelper {

    private final String url = "jdbc:oracle:thin:@sql.edu-netcracker.com:1251:xe";
    private final String user = "TLT_24";
    private final String password = "TLT_24";
    Scanner sc = new Scanner(System.in);

    public void addTask(Map<Integer, Task> taskMap){
        System.out.print("Enter name of exercise: ");
        String nameEx = sc.nextLine();
        System.out.print("Enter exercise description: ");
        String descriptionEx = sc.nextLine();
        Task task = new Task(nameEx, descriptionEx);
        int sizeMap = taskMap.size();
        taskMap.put(sizeMap + 1, task);
        try (Connection connection = DriverManager.getConnection(url, user, password)){
            String sqlRequest = "INSERT INTO Task (ID, NAME, DESCRIPTION) Values(task_list_seq.NEXTVAL, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlRequest);
            preparedStatement.setString(1, task.getName());
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.executeUpdate();
        }catch (Exception exception){
            System.out.println(exception);
        }
    }

    public void displayTaskList(){
        try (Connection connection = DriverManager.getConnection(url, user, password)){
            String sqlRequest = "SELECT * FROM Task";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlRequest);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                System.out.println("id: " + resultSet.getString(1) +
                        ".\ttask_name: " + resultSet.getString(2) +
                        ".\ttask_description: " + resultSet.getString(3));
            }
        }catch (Exception exception){
            System.out.println("Connection failed...");
        }
    }

    public void deleteTask(Map<Integer, Task> taskMap){
        boolean check = true;
        Integer numEx = null;
        System.out.println("Enter id of exercise to delete:");
        while (check){
            if(sc.hasNextInt()){
                numEx = sc.nextInt();
                check = false;
            }else{
                System.out.println("It's not a number! Try again:");
                sc.nextLine();
            }
        }
        taskMap.remove(numEx);
        try(Connection connection = DriverManager.getConnection(url, user, password)){
            String sqlRequest = "DELETE FROM Task WHERE ID = ";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlRequest + numEx);
            ResultSet resultSet = preparedStatement.executeQuery();
        }catch (Exception ex){
            System.out.println("Connection failed...");
        }
    }

    public void closeProgram(){
        System.exit(0);
    }

    public void fillMap(Map<Integer, Task> taskMap){
        try (Connection connection = DriverManager.getConnection(url, user, password)){
            String sqlRequest = "SELECT * FROM Task";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlRequest);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                taskMap.put(
                        resultSet.getInt("ID"),
                        new Task(resultSet.getString("NAME"),
                        resultSet.getString("DESCRIPTION"))
                );
            }
        }catch (Exception exception){
            System.out.println("Connection failed...");
        }
    }
}
