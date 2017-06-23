package DataAccessLayer.SocialInsurance;

import DataAccessLayer.DALException;
import insurance.SocialInsuranceRecord;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import personaldetails.Citizen;

public class MySQLSocialInsuranceStorage implements SocialInsuranceStorage {

    private final String url;
    private final String username;
    private final String password;

    public MySQLSocialInsuranceStorage(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }
//
//    @Override
//    public void insertSocialInsuranceRecord(SocialInsuranceRecord record, int citizenId) throws DALException {
//        String query = "INSERT INTO social_insurances(year,month,amount,citizen_id) VALUES (?,?,?,?);";
//        try (Connection conn = DriverManager.getConnection(url, username, password);
//                PreparedStatement state = conn.prepareStatement(query);) {
//            state.setInt(1, record.getYear());
//            state.setInt(2, record.getMonth());
//            state.setDouble(3, record.getAmount());
//            state.setInt(3, citizenId);
//            state.setInt(4,citizenId);
//            state.execute();
//        } catch (SQLException ex) {
//            throw new DALException("Inserting in this table failed!", ex);
//        }
//    }

    @Override
    public void insertInsurances(Citizen person) throws DALException {
        try (Connection conn = DriverManager.getConnection(url, username, password);
                PreparedStatement pstmt = conn.prepareStatement("INSERT INTO social_insurances(year,month,amount,citizen_id) VALUES (?,?,?,?);")) {
            for (SocialInsuranceRecord insurance : person.getSocialInsuranceRecords()) {
                pstmt.setInt(1, insurance.getYear());
                pstmt.setInt(2, insurance.getMonth());
                pstmt.setDouble(3, insurance.getAmount());
                pstmt.setInt(4, person.getID());
                pstmt.execute();
            }
        } catch (SQLException ex) {
            throw new DALException("SQL failed \n" + ex.getSQLState() + "\n" + ex.getMessage() + "\n" + ex.getErrorCode(), ex);
        }
    }

    @Override
    public List<SocialInsuranceRecord> getSocialInsuranceRecord(int citizenId) throws DALException {
        String query = "SELECT * FROM social_insurances "
                + "WHERE citizen_id = ?;";
        List<SocialInsuranceRecord> record = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, username, password);
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, citizenId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int year = rs.getInt("year");
                    int month = rs.getInt("month");
                    double amount = rs.getDouble("amount");
                    record.add(new SocialInsuranceRecord(year, month, amount));
                }
                return record;
            }
        } catch (SQLException ex) {
            throw new DALException("Unable to get social insurance records for citizen with Id = "
                    + citizenId, ex);
        }
    }

    @Override
    public void removeSocialInsuranceRecord(int id) throws DALException {
        String query = "DELETE FROM citizen_db.citizens WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, username, password);
                PreparedStatement state = conn.prepareStatement(query);) {
            state.setInt(1, id);
            state.execute();
        } catch (SQLException ex) {
            throw new DALException(String.format("Delete from id = %d failed", id, ex));
        }
    }
}
