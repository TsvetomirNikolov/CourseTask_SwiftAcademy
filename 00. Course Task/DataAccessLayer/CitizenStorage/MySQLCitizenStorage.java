package DataAccessLayer.CitizenStorage;

import DataAccessLayer.AddressStorage.MySQLAddressStorage;
import DataAccessLayer.DALException;
import DataAccessLayer.SocialInsurance.MySQLSocialInsuranceStorage;
import education.Education;
import insurance.SocialInsuranceRecord;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import personaldetails.Citizen;
import personaldetails.Gender;
import DataAccessLayer.AddressStorage.AddressStorage;
import DataAccessLayer.EducationStorage.MySqlEducationStorage;
import address.Address;

public class MySQLCitizenStorage implements CitizenStorage {

    private final String url;
    private final String username;
    private final String password;
    private final MySQLAddressStorage addressStorage;
    private final MySqlEducationStorage educationStorage;
    private final MySQLSocialInsuranceStorage insuranceStorage;

    public MySQLCitizenStorage(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        addressStorage = new MySQLAddressStorage(url, username, password);
        educationStorage = new MySqlEducationStorage(url, username, password);
        insuranceStorage = new MySQLSocialInsuranceStorage(url, username, password);
    }

    @Override
    public Citizen getCitizen(int id) throws DALException {
        String query
                = "SELECT * FROM citizen_db.citizens WHERE id = (?)";
        List<SocialInsuranceRecord> insurance = new ArrayList<>();
        insurance = insuranceStorage.getSocialInsuranceRecord(id);
        List<Education> educations = new ArrayList<>();
//        education = educationStorage.getEducationStorage(id);
        Citizen citizen = null;
        try (Connection conn = DriverManager.getConnection(url, username, password);
                PreparedStatement state = conn.prepareStatement(query);) {
            state.setInt(1, id);
            try (ResultSet set = state.executeQuery()) {
                while (set.next()) {
                    citizen = new Citizen(set.getString("first_name"), set.getString("middle_name"),
                            set.getString("last_name"), Gender.values()[set.getInt("gender")],
                            set.getInt("height"), set.getDate("date_of_birth").toLocalDate());
                    citizen.setAddress(addressStorage.getAddress(set.getInt("id")));
                    for (Education education : educations) {
                        citizen.addEducation(education);
                    }
                    for (SocialInsuranceRecord record : insurance) {
                        citizen.addSocialInsuranceRecord(record);
                    }
                }
                return citizen;
            }
        } catch (SQLException ex) {
            throw new DALException(String.format("Citizen with such %d can't find", id, ex));
        }
    }

    @Override
    public int insertCitizen(Citizen citizens) throws DALException { // за insert може да се направи stored procedure
        String query = "INSERT INTO citizen_db.citizens(first_name,middle_name,last_name,gender,height,date_of_birth)\n"
                + "VALUES(?,?,?,?,?,?)";
        String insertEducationMatches = "INSERT INTO citizen_db.people_educations(person_id,education_id)"
                + "VALUES(?,?);";
        String query2 = "INSERT INTO citizen_db.addresses(country,city,municipality,postalCode,street,number,floor,apartmentNo,citizen_id)"
                + "VALUES(?,?,?,?,?,?,?,?,?);";
        int id = 0;
        try (Connection conn = DriverManager.getConnection(url, username, password);
                PreparedStatement state = conn.prepareStatement(query);) {
            state.setString(1, citizens.getFirstName());
            state.setString(2, citizens.getMiddleName());
            state.setString(3, citizens.getLastName());
            Gender gender = citizens.getGender();
            if (gender.equals(gender.Male)) {
                state.setString(4, "M");
            } else {
                state.setString(4, "F");
            }
            state.setInt(5, citizens.getHeight());
            state.setDate(6, Date.valueOf(citizens.getDateOfBirth()));
            try (PreparedStatement states = conn.prepareStatement(query2)) {
                states.setInt(7, addressStorage.insertAddress(citizens.getAddress()));
                states.setInt(8, citizens.getID());
            }
            state.execute();
            try (ResultSet rs = state.executeQuery("SELECT LAST_INSERT_ID()")) {
                rs.next();
                citizens.setID(rs.getInt(1));
                if (!(citizens.getEducations().isEmpty())) {
                    try (PreparedStatement stmt = conn.prepareStatement(insertEducationMatches)) {
                        for (Education education : citizens.getEducations()) {
                            stmt.setInt(1, rs.getInt(1));
                            stmt.setInt(2, educationStorage.insertEducationStorage(education, id));
                            stmt.execute();
                        }
                    }
                }
                if (!(citizens.getSocialInsuranceRecords().isEmpty())) {
                    insuranceStorage.insertInsurances(citizens);
                }
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            throw new DALException("SQL failed \n" + ex.getSQLState() + "\n" + ex.getMessage() + "\n" + ex.getErrorCode(), ex);
        }
    }

    @Override
    public void deleteCitizen(int id) throws DALException {
        String query = "DELETE FROM citizen_db.citizens WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, username, password);
                PreparedStatement state = conn.prepareStatement(query);) {
            state.setInt(1, id);
            state.execute();
        } catch (SQLException ex) {
            Logger.getLogger(MySQLCitizenStorage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
