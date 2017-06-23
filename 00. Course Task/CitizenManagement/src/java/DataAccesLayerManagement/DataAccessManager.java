package DataAccesLayerManagement;

import DALException.DALException;
import address.Address;
import education.Education;
import insurance.SocialInsuranceRecord;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import personaldetails.Citizen;

public interface DataAccessManager {

    public Citizen getCitizens(int id) throws DALException;

    public List<Address> getAddress(int id) throws DALException;

    public List<Education> getEducations(int id) throws DALException;

    public boolean isSocialInsuranced(int id) throws DALException;

    public double calculateSocialBenefit(int id) throws DALException;

    public void addEducation(int id, HttpServletRequest request) throws DALException;

    public List<SocialInsuranceRecord> getSocialInsuranceRecord(int id) throws DALException;

    public void addSocialInsuranceRecord(int id, HttpServletRequest record) throws DALException;

}
