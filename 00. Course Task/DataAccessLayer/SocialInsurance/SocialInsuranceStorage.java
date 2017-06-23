package DataAccessLayer.SocialInsurance;

import DataAccessLayer.DALException;
import insurance.SocialInsuranceRecord;
import java.util.List;
import personaldetails.Citizen;

public interface SocialInsuranceStorage {

//    public void insertSocialInsuranceRecord(SocialInsuranceRecord record, int citizenId) throws DALException;

    public List<SocialInsuranceRecord> getSocialInsuranceRecord(int citizenId) throws DALException;

    public void removeSocialInsuranceRecord(int id) throws DALException;
    public void insertInsurances(Citizen person) throws DALException;
}
