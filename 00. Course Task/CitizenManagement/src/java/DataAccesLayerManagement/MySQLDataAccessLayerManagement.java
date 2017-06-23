package DataAccesLayerManagement;

import DALException.DALException;
import address.Address;
import education.Education;
import education.EducationDegree;
import education.GradedEducation;
import education.HigherEducation;
import education.PrimaryEducation;
import education.SecondaryEducation;
import insurance.SocialInsuranceRecord;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
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
                set.next();
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
            throw new DALException(String.format("Citizen with id = %d can't be found!", id, ex));
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
                        for (Education education1 : educ) {

                        }
                        return educ;
                }
            }
        } catch (SQLException ex) {
            throw new DALException(String.format("You can't insert id with number %d", id, ex));
        }
    }

    @Override
    public boolean isSocialInsuranced(int id) throws DALException {
        String query = "SELECT * FROM citizen_db.social_insurances WHERE citizen_id = ?";
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        List<SocialInsuranceRecord> rec = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, username, password);
                PreparedStatement state = conn.prepareStatement(query);) {
            state.setInt(1, id);
            ResultSet set = state.executeQuery();
            while (set.next()) {
                set.getInt("citizen_id");
                int year = set.getInt("year");
                int month = set.getInt("month");
                double amount = set.getDouble("amount");
                SocialInsuranceRecord soc = new SocialInsuranceRecord(year, month, amount);
                rec.add(soc);
            }
            int citizenLastMonth = rec.get(0).getMonth();
            int citizenLastYear = rec.get(0).getYear();
            int differenceMonth = currentMonth - citizenLastMonth;
            if (currentYear != citizenLastYear) {
                return true;
            } else if (differenceMonth >= 3) {
                return true;
            }
            return false;
        } catch (SQLException ex) {
            throw new DALException("Please insert correct id!");
        }
    }

    @Override
    public double calculateSocialBenefit(int id) throws DALException {
        String query = "SELECT * FROM citizen_db.social_insurances where citizen_id = ?;";
        double sumOfInsurances = 0.0;
        int counter = 0;
        MySQLDataAccessLayerManagement socialInsurance = new MySQLDataAccessLayerManagement();
        try (Connection conn = DriverManager.getConnection(url, username, password);
                PreparedStatement state = conn.prepareStatement(query);) {
            state.setInt(1, id);
            ResultSet set = state.executeQuery();
            while (set.next()) {
                if (socialInsurance.isSocialInsuranced(id) == true && set.getInt("year") == 2017) {
                    sumOfInsurances += set.getDouble("amount");
                    counter++;
                }
            }
            return sumOfInsurances / counter;

        } catch (SQLException ex) {
            throw new DALException("Citizen can't take social insurances!");
        }
    }

    @Override
    public void addEducation(int id, HttpServletRequest request) throws DALException {
        String query = "INSERT INTO citizen_db.educations(institution_name,enrollment_date,graduation_date,"
                + "final_grade) VALUES(?,?,?,?);";
        if (request != null) {
            String enrollmentDate = request.getParameter("enrollmentDate");
            String graduationDate = request.getParameter("graduationDate");
            String nameInstitution = request.getParameter("institutionName");
            String grade = request.getParameter("grade");
            if (nameInstitution.equals("")) {
                throw new IllegalArgumentException("Please type Institution name!");
            }
            String degree = request.getParameter("degree");
            if (degree.equals("None")) {
                throw new IllegalArgumentException("Select degree!");
            }
            try (Connection conn = DriverManager.getConnection(url, username, password);
                    PreparedStatement state = conn.prepareStatement(query);) {
                state.setString(1, nameInstitution);
                state.setDate(2, Date.valueOf(enrollmentDate));
                state.setDate(3, Date.valueOf(graduationDate));
                if (!(grade.isEmpty())) {
                    state.setDouble(4, Double.parseDouble(grade));
                } else {
                    state.setNull(4, Types.DOUBLE);
                }
                state.execute();
            } catch (SQLException ex) {
                throw new DALException("You can't insert Education with such ID");
            }
        }
    }

    @Override
    public List<SocialInsuranceRecord> getSocialInsuranceRecord(int id) throws DALException {
        String query = "SELECT * FROM citizen_db.social_insurances WHERE citizen_id = (?);";
        List<SocialInsuranceRecord> rec = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, username, password);
                PreparedStatement state = conn.prepareStatement(query);) {
            state.setInt(1, id);
            ResultSet set = state.executeQuery();
            while (set.next()) {
                set.getInt("citizen_id");
                int year = set.getInt("year");
                int month = set.getInt("month");
                double amount = set.getDouble("amount");
                SocialInsuranceRecord soc = new SocialInsuranceRecord(year, month, amount);
                rec.add(soc);
            }
            return rec;
        } catch (SQLException ex) {
            throw new DALException("Can't find id with such number!");
        }
    }

    @Override
    public void addSocialInsuranceRecord(int id, HttpServletRequest request) throws DALException {
        String query = "INSERT INTO citizen_db.social_insurances(year,month,amount,citizen_id) "
                + "VALUES(?,?,?,?)";
        if (request != null) {
            String years = request.getParameter("year");
            String months = request.getParameter("month");
            String amounts = request.getParameter("amount");
            try (Connection conn = DriverManager.getConnection(url, username, password);
                    PreparedStatement state = conn.prepareStatement(query);) {
                state.setInt(1, Integer.parseInt(years));
                state.setInt(2, Integer.parseInt(months));
                state.setDouble(3, Double.parseDouble(amounts));
                state.setInt(4, id);
                state.execute();

            } catch (SQLException ex) {
                throw new DALException("You cant insert SocialInsuranceRecord with such id!");
            }
        }
    }

    public boolean isSecondary(int id) throws SQLException {
        String query = "SELECT type_id FROM citizen_db.educations";
        try (Connection conn = DriverManager.getConnection(url, username, password);
                PreparedStatement state = conn.prepareStatement(query);) {
            state.setInt(1, id);
            state.execute();
            ResultSet set = state.executeQuery();
            if (set.getInt("type_id") != 1) {
                return true;
            } else {
                return false;
            }
        }
    }
}
