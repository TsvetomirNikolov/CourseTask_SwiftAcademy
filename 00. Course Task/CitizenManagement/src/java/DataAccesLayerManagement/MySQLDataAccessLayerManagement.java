package DataAccesLayerManagement;

import DALException.DALException;
import address.Address;
import education.Education;
import education.EducationDegree;
import education.GradedEducation;
import education.HigherEducation;
import education.PrimaryEducation;
import education.SecondaryEducation;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import personaldetails.Citizen;
import personaldetails.Gender;

public class MySQLDataAccessLayerManagement implements DataAccessManager {

    public static final String url = "jdbc:mysql://localhost:3306/citizen_db";
    public static final String username = "root";
    public static final String password = "1234567j";

    public MySQLDataAccessLayerManagement() {
    }

    @Override
    public Citizen getCitizens(int id) throws DALException {
        String query = "SELECT * FROM citizen_db.citizens WHERE id = (?);";
        Gender gender = null;
        try (Connection conn = DriverManager.getConnection(url, username, password);
                PreparedStatement state = conn.prepareStatement(query);) {
            state.setInt(1, id);
            try (ResultSet set = state.executeQuery()) {
                String name = set.getString("first_name");
                String middleName = set.getString("middle_name");
                String lastName = set.getString("last_name");
                switch (set.getString("gender")) {
                    case "M":
                        gender = Gender.Male;
                        break;
                    case "F":
                        gender = Gender.Female;
                        break;
                }
                int height = set.getInt("height");
                LocalDate date = set.getDate("date_of_birth").toLocalDate();

                return new Citizen(name, middleName, lastName, gender, height, date);
            }
        } catch (SQLException ex) {
            throw new DALException(String.format("Citizen with id = %d can't be founr!", id, ex));
        }
    }

    @Override
    public List<Address> getAddress(int id) throws DALException {
        String query = "SELECT * FROM citizen_db.addresses WHERE id = (?);";
        List<Address> address = new ArrayList<>();
        Address addresses = null;
        try (Connection conn = DriverManager.getConnection(url, username, password);
                PreparedStatement state = conn.prepareStatement(query);) {
            state.setInt(1, id);
            ResultSet set = state.executeQuery();
            while (set.next()) {
                if (set.getString("street") != null) {
                    if (set.getInt("floor") > 0) {
                        addresses = new Address(set.getString("country"),
                                set.getString("city"), set.getString("municipality"), set.getString("postalCode"),
                                set.getString("street"), set.getString("number"), set.getInt("floor"), set.getInt("apartmentNo"));
                    } else {
                        addresses = new Address(set.getString("country"),
                                set.getString("city"), set.getString("municipality"), set.getString("postalCode"),
                                set.getString("street"), set.getString("number"), null, null);
                    }
                }
            }
            address.add(addresses);
            return address;
        } catch (SQLException ex) {
            throw new DALException(String.format("Address with id = ? can't be found!", id, ex));
        }
    }

    @Override
    public List<Education> getEducations(int id) throws DALException {
        Education education = null;
        List<Education> educ = new ArrayList<>();
        String query = "SELECT * FROM citizen_db.educations WHERE id = (?);";
        Float grade;
        try (Connection conn = DriverManager.getConnection(url, username, password);
                PreparedStatement state = conn.prepareStatement(query);) {
            state.setInt(1, id);
            state.execute();
            try (ResultSet set = state.executeQuery();) {
                set.next();
                switch (set.getInt("type_id")) {
                    case 1:
                        education = new PrimaryEducation(set.getString("institution_name"),
                                set.getDate("enrollment_date").toLocalDate(),
                                set.getDate("graduation_date").toLocalDate());
                        educ.add(education);
                        return educ;
                    case 2:
                        education = new SecondaryEducation(set.getString("institution_name"),
                                set.getDate("enrollment_date").toLocalDate(),
                                set.getDate("graduation_date").toLocalDate());
                        grade = set.getFloat("final_grade");
                        educ.add(education);
                        return educ;
                    default:
                        education = new HigherEducation(set.getString("institution_name"),
                                set.getDate("enrollment_date").toLocalDate(),
                                set.getDate("graduation_date").toLocalDate(),
                                EducationDegree.values()[set.getInt("type_id")]); //possible -1
                        educ.add(education);
                        return educ;
                }
            }
        } catch (SQLException ex) {
            throw new DALException(String.format("You can't insert id with number %d", id, ex));
        }
    }

    @Override
    public boolean socialInsurances(int id) throws DALException {

    }
