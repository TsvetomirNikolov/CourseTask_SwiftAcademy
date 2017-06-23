package DataAccessLayer.EducationStorage;

import DataAccessLayer.DALException;
import education.Education;
import education.EducationDegree;
import education.GradedEducation;
import education.HigherEducation;
import education.PrimaryEducation;
import education.SecondaryEducation;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import personaldetails.Citizen;
import personaldetails.Gender;

public class MySqlEducationStorage implements EducationStorage {

    private final String url;
    private final String username;
    private final String password;

    public MySqlEducationStorage(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public void removeEducationStorage(int id) throws DALException {
        String query = "DELETE FROM citizen_db.educations WHERE id = (?);";
        try (Connection conn = DriverManager.getConnection(url, username, password);
                PreparedStatement state = conn.prepareStatement(query);) {
            state.setInt(1, id);
            state.execute();

        } catch (SQLException ex) {
            throw new DALException(String.format("The inserted %d can't be added.", id, ex));
        }
    }

    @Override
    public int insertEducationStorage(Education educationStorage, int citizenId) throws DALException {
        String query = "INSERT INTO citizen_db.educations(type_id,institution_name,enrollment_date,graduation_date,"
                + "graduated,final_grade) VALUES(?,?,?,?,?,?);";
        try (Connection conn = DriverManager.getConnection(url, username, password);
                PreparedStatement state = conn.prepareStatement(query);) {
            state.setInt(1, educationStorage.getDegree().getValue());
            state.setString(2, educationStorage.getInstitutionName());
            state.setDate(3, Date.valueOf(educationStorage.getEnrollmentDate()));
            state.setDate(4, Date.valueOf(educationStorage.getGraduationDate()));
            if (educationStorage instanceof GradedEducation && (educationStorage.isGraduated())) {
                state.setBoolean(5, educationStorage.isGraduated());
                state.setDouble(6, ((GradedEducation) educationStorage).getFinalGrade());
            } else if (educationStorage.isGraduated()) {
                state.setBoolean(5, educationStorage.isGraduated());
                state.setNull(6, Types.DOUBLE);
            } else {
                state.setNull(5, Types.INTEGER);
                state.setNull(6, Types.DOUBLE);
            }
//            Gender gender = null;
//            Citizen citizen = new Citizen("d","s","s",gender.Female,123,LocalDate.now());
//            state.setInt(7,citizen.getID());
            state.execute();
            try (ResultSet set = state.executeQuery("SELECT LAST_INSERT_ID()");) {
                set.next();
                return set.getInt(1);
            }
        } catch (SQLException ex) {
            throw new DALException(String.format("Can't insert id with number %d",citizenId,ex));
        }
    }

    @Override
    public Education getEducationStorage(int id) throws DALException {
        Education education = null;
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
                        if (set.getBoolean("graduated")) {
                            ((GradedEducation) education).gotGraduated();
                        }
                        return education;
                    case 2:
                        education = new SecondaryEducation(set.getString("institution_name"),
                                set.getDate("enrollment_date").toLocalDate(),
                                set.getDate("graduation_date").toLocalDate());
                        grade = set.getFloat("final_grade");
                        if (!(grade < 2 || grade > 6)) {
                            ((GradedEducation) education).gotGraduated(grade);
                        }
                        return education;
                    default:
                        education = new HigherEducation(set.getString("institution_name"),
                                set.getDate("enrollment_date").toLocalDate(),
                                set.getDate("graduation_date").toLocalDate(),
                                EducationDegree.values()[set.getInt("type_id")]); //possible -1
                        if (set.getBoolean("graduated")) {
                            education.gotGraduated();
                        }
                        return education;
                }
            }
        } catch (SQLException ex) {
            throw new DALException("Can't get education with id", ex);
        }
    }
}
