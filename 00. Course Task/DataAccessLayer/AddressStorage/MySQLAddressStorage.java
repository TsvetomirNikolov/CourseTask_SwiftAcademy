package DataAccessLayer.AddressStorage;

import DataAccessLayer.DALException;
import address.Address;
import java.sql.Connection;
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

public class MySQLAddressStorage implements AddressStorage {

    private final String url;
    private final String username;
    private final String password;

    public MySQLAddressStorage(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public void removeAddress(int id) throws DALException {
        String query = "DELETE FROM addreses WHERE id = (?);";
        try (Connection conn = DriverManager.getConnection(url, password, password);
                PreparedStatement state = conn.prepareStatement(query);) {
            state.setInt(1, id);
            state.execute();
            System.out.printf("The address with ID = %d was removed.\n", id);
        } catch (SQLException ex) {
            throw new DALException("Can't delete ID = " + id, ex);
        }
    }

    @Override
    public int insertAddress(Address address) throws DALException {
        String query = "INSERT INTO citizen_db.addresses(country,city,municipality,postalCode,street,number,floor,apartmentNo)"
                + "VALUES(?,?,?,?,?,?,?,?);";
        try (Connection conn = DriverManager.getConnection(url, username, password);
                PreparedStatement state = conn.prepareStatement(query);) {
            state.setString(1, address.getCountry());
            state.setString(2, address.getCity());
            state.setString(3, address.getMunicipality());
            state.setString(4, address.getPostalCode());
            state.setString(5, address.getStreet());
            state.setString(6, address.getNumber());
            if (!(address.getFloor() == null && address.getApartmentNo() == null)) {
                state.setInt(7, address.getFloor());
                state.setInt(8, address.getApartmentNo());
            } else {
                state.setNull(7, Types.INTEGER);
                state.setNull(8, Types.INTEGER);
            }
//            state.setInt(9, );
            state.execute();
            try (ResultSet set = state.executeQuery("SELECT LAST_INSERT_ID();")) {
                set.next();
                return set.getInt(1);
            }
        } catch (SQLException ex) {
            throw new DALException(ex.getSQLState() + "\n" + ex.getMessage());
        }
    }

    @Override
    public Address getAddress(int id) throws DALException {
        String query = "SELECT * FROM citizen_db.addresses WHERE id = (?);";
        try (Connection conn = DriverManager.getConnection(url, username, password);
                PreparedStatement state = conn.prepareStatement(query);) {
            state.setInt(1, id);
            ResultSet set = state.executeQuery();
            set.next();
            int floor = set.getInt("floor"); // ако от входа подадем 0 ще гръмне долу.
            int apartment = set.getInt("apartmentNo");
            if (!(floor == 0 && apartment == 0)) { //тук
                return new Address(set.getString("country"),
                        set.getString("city"), set.getString("municipality"), set.getString("postalCode"),
                        set.getString("street"), set.getString("number"), set.getInt("floor"), set.getInt("apartmentNo"));
            }
            return new Address(set.getString("country"),
                    set.getString("city"), set.getString("municipality"), set.getString("postalCode"),
                    set.getString("street"), set.getString("number"));

        } catch (SQLException ex) {
            throw new DALException(String.format("You can't add id with number %d", id, ex));
        }
    }
}
