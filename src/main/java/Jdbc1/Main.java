package Jdbc1;

import java.sql.*;
import java.util.Random;
import java.util.Scanner;

public class Main {
    // CREATE DATABASE mydb;
    static Connection conn;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            try {
                DbProperties db = new DbProperties();

                // create connection
                conn = DriverManager.getConnection(db.getUrl(), db.getUser(), db.getPassword());
                initDB();

                addClientInt("Tester", 15);
                addClientInt("Denis", 25);
                addClientInt("Mike", 33);
                addClientInt("James", 44);

                while (true) {
                    System.out.println("1: add client");
                    System.out.println("2: add random clients");
                    System.out.println("3: delete client");
                    System.out.println("4: change client");
                    System.out.println("5: view clients");
                    System.out.println("6: procedure");
                    System.out.print("-> ");

                    String s = sc.nextLine();
                    switch (s) {
                        case "1":
                            addClient(sc);
                            break;
                        case "2":
                            insertRandomClients(sc);
                            break;
                        case "3":
                            deleteClient(sc);
                            break;
                        case "4":
                            changeClient(sc);
                            break;
                        case "5":
                            viewClients();
                            break;
                        case "6":
                            checkProcedure();
                            break;
                        default:
                            return;
                    }
                }
            } finally {
                if (conn != null) conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void initDB() throws SQLException {
        Statement st = conn.createStatement();
        try {
            st.execute("DROP TABLE IF EXISTS Clients");
            st.execute("CREATE TABLE Clients (id INT NOT NULL " +
                    "AUTO_INCREMENT PRIMARY KEY, name VARCHAR(20) " +
                    "NOT NULL, age INT)");
        } finally {
            st.close();
        }

        /*try (Statement st1 = conn.createStatement()) {
            st1.execute("DROP TABLE IF EXISTS Clients");
            st1.execute("CREATE TABLE Clients (id INT NOT NULL " +
                    "AUTO_INCREMENT PRIMARY KEY, name VARCHAR(20) " +
                    "NOT NULL, age INT)");
        }*/
    }

    /*
   -> 1 2 3
      -----
      -----
      -----
      !----
     */

    private static void viewClients() throws SQLException {
        Statement st = conn.createStatement();
        try {
            ResultSet rs = st.executeQuery("SELECT * FROM Clients");
            // ps.setFetchSize(100);

            try {
                // can be used to get information about the types and properties of the columns in a ResultSet object
                ResultSetMetaData md = rs.getMetaData();

                for (int i = 1; i <= md.getColumnCount(); i++)
                    System.out.print(md.getColumnName(i) + "\t\t");
                System.out.println();

                while (rs.next()) {

                    /*for (int i = 1; i <= md.getColumnCount(); i++) {
                        System.out.print(rs.getString(i) + "\t\t");
                    }*/

                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        if (md.getColumnType(i) == Types.INTEGER)
                            System.out.print(rs.getInt(i) + "\t\t");
                        else if (md.getColumnType(i) == Types.VARCHAR)
                            System.out.print(rs.getString(i) + "\t\t");
                        else if (md.getColumnType(i) == Types.DATE)
                            System.out.print(rs.getDate(i) + "\t\t");
                    }

                    System.out.println();
                }

                System.out.println();
            } finally {
                rs.close(); // rs can't be null according to the docs
            }
        } finally {
            st.close();
        }
    }

    private static void addClientInt(String name, int age) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO Clients (name, age) VALUES(?, ?)");
        try {
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.executeUpdate(); // for INSERT, UPDATE & DELETE
        } finally {
            ps.close();
        }
    }

    private static void addClient(Scanner sc) throws SQLException {
        System.out.print("Enter client name: ");
        String name = sc.nextLine();
        System.out.print("Enter client age: ");
        String sAge = sc.nextLine();
        int age = Integer.parseInt(sAge);

        //String sql = "INSERT INTO Clients (name, age) VALUES(" + name + ", " + age + ")";

        PreparedStatement ps = conn.prepareStatement("INSERT INTO Clients (name, age) VALUES(?, ?)");
        try {
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.executeUpdate(); // for INSERT, UPDATE & DELETE

            //ps.setString(1, "Kate");
            //ps.setInt(2, age);
            //ps.executeUpdate();
        } finally {
            ps.close();
        }
    }

    private static void deleteClient(Scanner sc) throws SQLException {
        System.out.print("Enter client name: ");
        String name = sc.nextLine();

        PreparedStatement ps = conn.prepareStatement("DELETE FROM Clients WHERE name = ?");
        try {
            ps.setString(1, name);
            ps.executeUpdate(); // for INSERT, UPDATE & DELETE
        } finally {
            ps.close();
        }
    }

    private static void changeClient(Scanner sc) throws SQLException {
        System.out.print("Enter client name: ");
        String name = sc.nextLine();
        System.out.print("Enter new age: ");
        String sAge = sc.nextLine();
        int age = Integer.parseInt(sAge);

        PreparedStatement ps = conn.prepareStatement("UPDATE Clients SET age = ? WHERE name = ?");
        try {
            ps.setInt(1, age);
            ps.setString(2, name);
            ps.executeUpdate(); // for INSERT, UPDATE & DELETE
        } finally {
            ps.close();
        }
    }

    private static void insertRandomClients(Scanner sc) throws SQLException {
        System.out.print("Enter clients count: ");
        String sCount = sc.nextLine();
        int count = Integer.parseInt(sCount);
        Random rnd = new Random();

        conn.setAutoCommit(false); // enable transactions
        try {
            try {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO Clients (name, age) VALUES(?, ?)");
                try {
                    for (int i = 0; i < count; i++) {
                        ps.setString(1, "Name" + i);
                        ps.setInt(2, rnd.nextInt(100));
                        ps.executeUpdate();
                    }
                    conn.commit();
                } finally {
                    ps.close();
                }
            } catch (Exception ex) {
                conn.rollback();
            }
        } finally {
            conn.setAutoCommit(true); // return to default mode
        }
    }

    private static void checkProcedure() throws SQLException {
        try (CallableStatement cs = conn.prepareCall("{CALL Test(?, ?, ?)}")) {
            cs.setInt(1, 20); // in
            cs.setInt(2, 30); // in
            cs.registerOutParameter(3, Types.INTEGER); // out

            cs.execute();

            System.out.println(cs.getInt(3));
        }
    }
}
