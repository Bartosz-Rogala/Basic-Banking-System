import org.sqlite.SQLiteDataSource;
import java.sql.*;


public class Database {
    SQLiteDataSource dataSource;
    String url;

    public Database() {
        url = "jdbc:sqlite:./card.s3db";

        dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        try (Connection con = dataSource.getConnection()) {
            // Statement creation
            try (Statement statement = con.createStatement()) {
                // Statement execution
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS card(" +
                        "id INTEGER PRIMARY KEY," +
                        "number TEXT NOT NULL," +
                        "pin TEXT NOT NULL," +
                        "balance INTEGER DEFAULT 0)");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void input(int id, String number, String pin, double balance) {
        String sql = "INSERT INTO card(id,number, pin, balance) VALUES(?,?,?,?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, number);
            pstmt.setString(3, pin);
            pstmt.setDouble(4, balance);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Card read(String number) {
        try (Connection conn = dataSource.getConnection()) {
            // Statement creation
            try (Statement statement = conn.createStatement()) {
                try (ResultSet correctCard = statement.executeQuery("SELECT * FROM card")) {
                    while (correctCard.next()) {
                        // Retrieve column values
                        if (correctCard.getString("number").equals(number)){
                            return new Card(correctCard.getInt("id"),
                                    correctCard.getString("number"),
                                    correctCard.getString("pin"),
                                    correctCard.getDouble("balance"));
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean ifExists(String number) {
        try (Connection conn = dataSource.getConnection()) {
            // Statement creation
            try (Statement statement = conn.createStatement()) {
                try (ResultSet correctCard = statement.executeQuery("SELECT * FROM card")) {
                    while (correctCard.next()) {
                        // Retrieve column values
                        if (correctCard.getString("number").equals(number)){
                            return true;
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean update(int id, double amount) {
        String sql = "UPDATE card SET balance = balance + ? "
                + "WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, id);
            // update
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean update(String cardNumber, double amount) {
        String sql = "UPDATE card SET balance = balance + ? "
                + "WHERE number = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setDouble(1, amount);
            pstmt.setString(2, cardNumber);
            // update
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void delete(String cardNumber) {
        String sql = "DELETE FROM card WHERE number = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, cardNumber);
            // update
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean logIntoAccount(String cardNumber, String pin) {
        if(this.ifExists(cardNumber)) {
            if (this.read(cardNumber).getPin().equals(pin)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void transfer(String minusAccount, String plusAccount, double amount){
        this.update(minusAccount, -amount);
        this.update(plusAccount, amount);
    }
}
