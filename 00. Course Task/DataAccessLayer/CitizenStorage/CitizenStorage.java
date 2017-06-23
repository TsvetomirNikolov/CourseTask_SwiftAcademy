package DataAccessLayer.CitizenStorage;

import DataAccessLayer.DALException;
import java.util.List;
import personaldetails.Citizen;

public interface CitizenStorage {

    public Citizen getCitizen(int id) throws DALException;

    public int insertCitizen(Citizen citizens) throws DALException;

    public void deleteCitizen(int id) throws DALException;
}
