package citizenstoragemanagercli;

import DataAccessLayer.CitizenStorage.MySQLCitizenStorage;
import DataAccessLayer.DALException;
import address.Address;
import education.Education;
import education.EducationDegree;
import education.GradedEducation;
import education.HigherEducation;
import java.time.LocalDate;
import personaldetails.Citizen;
import personaldetails.Gender;
import education.PrimaryEducation;
import education.SecondaryEducation;
import insurance.SocialInsuranceRecord;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class CitizenStorageManagerCLI {

    static final String url = "jdbc:mysql://localhost:3306/citizen_db";
    static final String username = "root";
    static final String password = "1234567j";

    public static MySQLCitizenStorage citizenStorage = new MySQLCitizenStorage(url, username, password);

    static final String path = "C:\\JAVA\\in_10";
    static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("d.M.yyyy");
    static final String disableForeignKey = "SET FOREIGN_KEY_CHECKS = 0;";
    static String truncateTable = "TRUNCATE TABLE ?";
    static final String enableForeignKey = "SET FOREIGN_KEY_CHECKS = 1;";
    static final String reset = "ALTER TABLE ? AUTO_INCREMENT =1";

    private static final String addresses = "addresses";
    private static final String educations = "educations";
    private static final String insurances = "social_insurances";
    private static final String peopleEducations = "people_educations";
    private static final String citizens = "citizens";

    public static void main(String[] args) throws DALException, IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to Citizen Store Managment");
        System.out.println();
        System.out.println("Choose action: INSERT or DELETE");
        String command = sc.next();
        switch (command) {
            case "INSERT":
                insertCitizens(path);
                break;
            case "DELETE":
                System.out.println("Which table do you want to delete");
                String table = sc.next();
                switch (table) {
                    case "addresses":
                        deleteTable(addresses);
                        reset_AI(addresses);
                        deleteTable(peopleEducations);
                        break;
                    case "educations":
                        deleteTable(educations);
                        reset_AI(educations);
                        deleteTable(peopleEducations);
                        break;
                    case "social_insurances":
                        System.out.println();
                        deleteTable(insurances);
                        reset_AI(insurances);
                        break;
                    case citizens:
                        deleteTable(citizens);
                        break;
                    case "all":
                        deleteAllTables();
                        break;
                    case "people_educations":
                        deleteTable(peopleEducations);
                        break;
                    default:
                        throw new IllegalArgumentException("You have to choose between: addresses, educations, citizens, social_insurances, people_educations or all!\n");

                }
                break;
            default:
                throw new IllegalArgumentException("You have to choose DELETE or INSERT");
        }
    }

    private static String truncateTable(String tableName) {
        return "TRUNCATE TABLE " + tableName;
    }

    private static void deleteTable(String tableName) throws DALException {
        try (Connection conn = DriverManager.getConnection(url, username, password);
                PreparedStatement stmt = conn.prepareStatement(disableForeignKey);) {
            stmt.execute();
            System.out.println("Foreign_keys for [" + tableName + "] were disabled ");

            try (PreparedStatement pstmt = conn.prepareStatement(truncateTable(tableName))) {
                pstmt.execute();
                System.out.println("Table [" + tableName + "] was truncated");
            }

            try (ResultSet rs = stmt.executeQuery(enableForeignKey)) {
                System.out.println("Foreign_keys for [" + tableName + "] were enabled");
            }
        } catch (SQLException ex) {
            throw new DALException(ex.getSQLState() + "\n" + ex.getMessage() + "\n" + ex.getErrorCode(), ex);
        }
    }

    private static String resetAI(String tableName) {
        return "ALTER TABLE " + tableName + " AUTO_INCREMENT =1";
    }

    private static void reset_AI(String tableName) throws DALException {
        try (Connection conn = DriverManager.getConnection(url, username, password);
                PreparedStatement pstmt = conn.prepareStatement(resetAI(tableName));) {
            pstmt.execute();
            System.out.println("Foreign_Keys were reset  for table " + tableName);
        } catch (SQLException ex) {
            throw new DALException(ex.getSQLState() + "\n" + ex.getMessage() + "\n" + ex.getErrorCode(), ex);
        }
    }

    private static void deleteAllTables() throws DALException {
        deleteTable(citizens);
        reset_AI(citizens);
        deleteTable(addresses);
        reset_AI(addresses);
        deleteTable(educations);
        reset_AI(educations);
        deleteTable(insurances);
        reset_AI(insurances);
        deleteTable(peopleEducations);
    }

    private static void insertCitizens(String path) throws DALException {
        try {
            int counter = 0;
            String line = null;
            File project = new File(path);
            FileReader fr = new FileReader(project);
            try (BufferedReader buffer = new BufferedReader(fr)) { // here
                int N = Integer.parseInt(buffer.readLine());
                Citizen person = null;
                while ((line = buffer.readLine()) != null) {
                    ++counter;
                    if (counter % 2 == 0) {
                        for (SocialInsuranceRecord socialInsurance : convertLineToInsuranceList(line)) {
                            person.addSocialInsuranceRecord(socialInsurance);
                        }
                        citizenStorage.insertCitizen(person);
                        System.out.println("Citizen has been inserted");
                    } else {
                        person = convertLineToCitizen(line);
                    }
                }
            }
            System.out.println();

        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file");
        } catch (IOException ex) {
            System.out.println("Error reading file");
        }
    }

    private static Citizen convertLineToCitizen(String line) throws IllegalArgumentException {
        Citizen citizen;
        Education education;
        String firstName = null;
        String middleName = null;
        String lastName = null;
        Gender gender = null;
        Address address = null;
        LocalDate date = null;
        int height = 0;
        String[] split = line.split(";");
        for (int i = 0; i < split.length - 1; i++) {
            firstName = split[0];
            middleName = split[1];
            lastName = split[2];
            if (split[3].equals("M")) {
                gender = Gender.Male;
            } else {
                gender = Gender.Female;
            }
            date = LocalDate.parse(split[4], dateFormat);
            height = Integer.parseInt(split[5]);
            if ((split.length == 12) || split[12].isEmpty()) {
                address = new Address(
                        split[6],
                        split[7],
                        split[8],
                        split[9],
                        split[10],
                        split[11]);
            } else {
                address = new Address(
                        split[6],
                        split[7],
                        split[8],
                        split[9],
                        split[10],
                        split[11],
                        Integer.parseInt(split[12]),
                        Integer.parseInt(split[13]));
            }
        }
        citizen = new Citizen(firstName, middleName, lastName, gender, height, date);
        citizen.setAddress(address);
        citizen.getAddress();
        for (int i = 13; i < split.length - 1; i++) {//here
            switch (split[i]) {
                case "P":
                    education = new PrimaryEducation(split[i + 1],
                            LocalDate.parse(split[i + 2], dateFormat),
                            LocalDate.parse(split[i + 3], dateFormat));
                    if (!(education.getGraduationDate().isAfter(LocalDate.now()))) {
                        education.gotGraduated();
                    }
                    citizen.addEducation(education);
                    break;
                case "S":
                    education = new SecondaryEducation(split[i + 1],
                            LocalDate.parse(split[i + 2], dateFormat),
                            LocalDate.parse(split[i + 3], dateFormat));
                    if (!(education.getGraduationDate().isAfter(LocalDate.now()))) {
                        ((GradedEducation) education).gotGraduated(Float.parseFloat(split[i + 4]));
                    }
                    citizen.addEducation(((GradedEducation) education));
                    break;
                case "M":
                    education = new HigherEducation(split[i + 1],
                            LocalDate.parse(split[i + 2], dateFormat),
                            LocalDate.parse(split[i + 3], dateFormat),
                            EducationDegree.Master);
                    if (!(LocalDate.parse(split[i + 3], dateFormat).isAfter(LocalDate.now()))) {
                        ((GradedEducation) education).gotGraduated(Float.parseFloat(split[i + 4]));
                    }
                    citizen.addEducation(((GradedEducation) education));
                    break;
                case "B":
                    education = new HigherEducation(split[i + 1],
                            LocalDate.parse(split[i + 2], dateFormat),
                            LocalDate.parse(split[i + 3], dateFormat),
                            EducationDegree.Bachelor);
                    if (!(LocalDate.parse(split[i + 3], dateFormat).isAfter(LocalDate.now()))) {
//                        ((GradedEducation) education).gotGraduated(Float.parseFloat(split[i + 4]));
                    }
                    citizen.addEducation(((GradedEducation) education));
                    break;
                case "D":
                    education = new HigherEducation(split[i + 1],
                            LocalDate.parse(split[i + 2], dateFormat),
                            LocalDate.parse(split[i + 3], dateFormat),
                            EducationDegree.Doctorate);
                    if (!(LocalDate.parse(split[i + 3], dateFormat).isAfter(LocalDate.now()))) {
                        ((GradedEducation) education).gotGraduated(Float.parseFloat(split[i + 4]));
                    }
                    citizen.addEducation(((GradedEducation) education));
                    break;
            }
        }
        return citizen;
    }

    private static List<SocialInsuranceRecord> convertLineToInsuranceList(String line) throws IllegalArgumentException {
        List<SocialInsuranceRecord> insurances = new LinkedList<>();
        String[] split = line.split(";");
        for (int i = 0; i < split.length - 1; i++) {
            insurances.add(new SocialInsuranceRecord(
                    Integer.parseInt(split[i]),
                    Integer.parseInt(split[++i]),
                    Double.parseDouble(split[++i])));
        }
        return insurances;
    }
}
