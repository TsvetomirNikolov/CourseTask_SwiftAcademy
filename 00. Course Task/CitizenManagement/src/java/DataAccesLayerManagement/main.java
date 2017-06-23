package DataAccesLayerManagement;

import static DataAccesLayerManagement.MySQLDataAccessLayerManagement.password;
import static DataAccesLayerManagement.MySQLDataAccessLayerManagement.url;
import static DataAccesLayerManagement.MySQLDataAccessLayerManagement.username;
import DataAccessLayer.AddressStorage.MySQLAddressStorage;
import DataAccessLayer.DALException;
import DataAccessLayer.EducationStorage.MySqlEducationStorage;
import DataAccessLayer.SocialInsurance.MySQLSocialInsuranceStorage;
import address.Address;
import education.Education;
import education.EducationDegree;
import education.GradedEducation;
import education.HigherEducation;
import education.PrimaryEducation;
import education.SecondaryEducation;
import insurance.SocialInsuranceRecord;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import personaldetails.Citizen;
import personaldetails.Gender;

public class main {

    public static final String url = "jdbc:mysql://localhost:3306/citizen_db";
    public static final String username = "root";
    public static final String password = "1234567j";

    public static void main(String[] args) throws SQLException {
        Education education = null;
        String query = "SELECT * FROM citizen_db.educations WHERE id = (?);";
        Float grade;
        List<Education> list = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, username, password);
                PreparedStatement state = conn.prepareStatement(query);) {
            state.setInt(1, 5);
            state.execute();
            try (ResultSet set = state.executeQuery();) {
                set.next();
                switch (set.getInt("type_id")) {
                    case 1:
                        education = new PrimaryEducation(set.getString("institution_name"),
                                set.getDate("enrollment_date").toLocalDate(),
                                set.getDate("graduation_date").toLocalDate());
                        list.add(education);
                        for (Education education1 : list) {
                            System.out.println(education1.getDegree());
                            System.out.println(education1.getDegree());
                        }
                        break;
                    case 2:
                        education = new SecondaryEducation(set.getString("institution_name"),
                                set.getDate("enrollment_date").toLocalDate(),
                                set.getDate("graduation_date").toLocalDate());
                        grade = set.getFloat("final_grade");
                        System.out.println(education.getDegree());
                        break;
                    default:
                        education = new HigherEducation(set.getString("institution_name"),
                                set.getDate("enrollment_date").toLocalDate(),
                                set.getDate("graduation_date").toLocalDate(),
                                EducationDegree.values()[set.getInt("type_id")]); //possible -1
                        System.out.println(education.getDegree());
                        break;
                }
            }
        }
    }
}
