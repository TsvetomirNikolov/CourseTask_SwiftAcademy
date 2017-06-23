package DataAccesLayerManagement;

import DALException.DALException;
import address.Address;
import education.Education;
import java.util.List;
import personaldetails.Citizen;

public interface DataAccessManager {

    public Citizen getCitizens(int id) throws DALException;

    public List<Address> getAddress(int id) throws DALException;

    public List<Education> getEducations(int id) throws DALException;

    public boolean socialInsurances(int id) throws DALException;

}
