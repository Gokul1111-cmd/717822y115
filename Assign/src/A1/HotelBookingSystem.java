package A1;

import java.sql.*;
import java.util.Scanner;

public class HotelBookingSystem {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/HBS";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "gokul";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            System.out.println("Connected to the database");
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\nHotel Booking System Menu:");
                System.out.println("1. View all rooms");
                System.out.println("2. Add a room");
                System.out.println("3. Update room rate");
                System.out.println("4. Delete a room");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        retrieveRooms(connection);
                        break;
                    case 2:
                        addRoom(connection);
                        break;
                    case 3:
                        updateRoomRate(connection);
                        break;
                    case 4:
                        deleteRoom(connection);
                        break;
                    case 5:
                        System.out.println("Exiting program...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void retrieveRooms(Connection connection) throws SQLException {
        String query = "SELECT * FROM rooms";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int roomId = resultSet.getInt("room_id");
                String roomNumber = resultSet.getString("room_number");
                String roomType = resultSet.getString("room_type");
                double ratePerNight = resultSet.getDouble("rate_per_night");
                System.out.println("Room ID: " + roomId + ", Room Number: " + roomNumber +
                        ", Type: " + roomType + ", Rate/Night: " + ratePerNight);
            }
        }
    }

    private static void addRoom(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter room number: ");
        String roomNumber = scanner.nextLine();
        System.out.print("Enter room type: ");
        String roomType = scanner.nextLine();
        System.out.print("Enter rate per night: ");
        double ratePerNight = scanner.nextDouble();

        String query = "INSERT INTO rooms (room_number, room_type, rate_per_night) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, roomNumber);
            preparedStatement.setString(2, roomType);
            preparedStatement.setDouble(3, ratePerNight);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Room added successfully");
            } else {
                System.out.println("Failed to add room");
            }
        }
    }

    private static void updateRoomRate(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter room ID to update rate: ");
        int roomId = scanner.nextInt();
        System.out.print("Enter new rate per night: ");
        double newRate = scanner.nextDouble();

        String query = "UPDATE rooms SET rate_per_night = ? WHERE room_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDouble(1, newRate);
            preparedStatement.setInt(2, roomId);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Room rate updated successfully");
            } else {
                System.out.println("Failed to update room rate. Room ID not found");
            }
        }
    }

    private static void deleteRoom(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter room ID to delete: ");
        int roomId = scanner.nextInt();

        String query = "DELETE FROM rooms WHERE room_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, roomId);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Room deleted successfully");
            } else {
                System.out.println("Failed to delete room. Room ID not found");
            }
        }
    }
}
