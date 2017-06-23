package DataAccessLayer.AddressStorage;

import DataAccessLayer.DALException;
import address.Address;
import personaldetails.Citizen;

public interface AddressStorage {

    public void removeAddress(int id) throws DALException;

    public int insertAddress(Address address) throws DALException;

    public Address getAddress(int id) throws DALException;
}
