package DataAccessLayer.EducationStorage;

import DataAccessLayer.DALException;
import address.Address;
import education.Education;
import personaldetails.Citizen;

public interface EducationStorage {

    public void removeEducationStorage(int id) throws DALException;

    public int insertEducationStorage(Education educationStorae, int citizenId) throws DALException;

    public Education getEducationStorage(int id) throws DALException;
}
