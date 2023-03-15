package DAO;

import Model.Account;
import Util.BCrypt;
import Util.ConnectionUtil;

import java.sql.*;

public class AccountDAO {

    /**
     * Get an account from the database by ID
     *
     * @param id an account ID.
     */
    public Account getAccountById(int id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM account WHERE account_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) {
                return new Account(rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password") );
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Get an account from the database by username
     *
     * @param username an account username.
     */
    public Account getAccountByUsername(String username) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM account WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) {
                return new Account(rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password") );
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Insert provided account argument into the database.
     *
     * @param account an object modelling an Account.
     */
    public Account insertAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String username = account.getUsername();
            String passwordHash = BCrypt.hashpw(account.getPassword(), BCrypt.gensalt(12));

            String sql = "INSERT into account (username, password) VALUES (?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, passwordHash);

            preparedStatement.executeUpdate();
            ResultSet pkrs = preparedStatement.getGeneratedKeys();

            if(pkrs.next()){
                int generated_id = pkrs.getInt(1);
                return new Account(generated_id,
                        pkrs.getString("username"),
                        pkrs.getString("password") );
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}


