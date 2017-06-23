<%@page import="DALException.DALException"%>
<%@page import="java.util.Scanner"%>
<%@page import="java.util.List"%>
<%@page import="education.Education"%>
<%@page import="education.Education"%>
<%@page import="address.Address"%>
<%@page import="DataAccesLayerManagement.MySQLDataAccessLayerManagement"%>
<%@page import="personaldetails.Citizen"%>
<%Class.forName("com.mysql.jdbc.Driver");%>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head><link rel = "stylesheet" href="css_style.css">
        <style>p.indent{ padding-left: 2.8em }</style>
    </head>
    <body>
        <h1>Welcome to Citizen Management Data Base Engine</h1>
        <form action="" method="get">
            <h4>Search citizen by ID:</h4>
            <input type="text" name="searchID">
            <input type="submit" class="btn_search" value="Search">
        </form>
        <%
            String ids = request.getParameter("searchID");
            session.setAttribute("searchID", ids);
            int id = 0;
            MySQLDataAccessLayerManagement dataStorage = new MySQLDataAccessLayerManagement();
            try {
                id = Integer.parseInt(ids);
                Citizen citizen = dataStorage.getCitizens(id);

        %>
        <div id="footer">
            <p>User ID = <%=ids%></p>
        </table>
        <h4>Citizens details</h4>
        <table border ="1">
            <tbody>
                <tr>
                    <td>First name: </td>
                    <td><%=citizen.getFirstName()%></td> 
                </tr>
                <tr>
                    <td>Second name: </td>
                    <td><%=citizen.getMiddleName()%></td>
                </tr>
                <tr>
                    <td>Last name: </td>
                    <td><%=citizen.getLastName()%></td>
                </tr>
                <tr>
                    <td>Date of birth: </td>
                    <td><%=citizen.getDateOfBirth()%></td>
                </tr>
                <tr>
                    <td>Gender: </td>
                    <td><%=citizen.getGender()%></td>
                </tr>
                <tr>
                    <td>Height: </td>
                    <td><%=citizen.getHeight()%></td>
                </tr>
            <td>Address: </td>
            <td><% for (Address address1 : dataStorage.getAddress(id)) {
                    out.print(String.format("%s, гр. %s, общ. %s, п.к.%s, ул. %s, N:%s",
                            address1.getCountry(), address1.getCity(), address1.getMunicipality(),
                            address1.getPostalCode(), address1.getStreet(), address1.getNumber()));
                    if (address1.getFloor() == null || address1.getApartmentNo() == null) {
                        continue;
                    } else {
                        out.print(String.format(", ет. %d, ап. %d", address1.getFloor(), address1.getApartmentNo()));
                    }
                }
                %></td>
            <tr></tr>
            <tr>
                <td>Educations: </td>
                <td><% for (Education education1 : dataStorage.getEducations(id)) {
                        out.println(education1.getInstitutionName());
                        out.println(education1.getEnrollmentDate());
                        out.println(education1.getGraduationDate());
                        out.println(education1.getDegree());

                    }%></td>
            </tr>
            </tbody>
        </table>
    </div>
</form>
<style>
    .button { 
        width: 100%;
        table-layout: fixed;
        border-collapse: collapse;
        background-color: red; 
    }
    .buttons button { 
        width: 100%;
    }
</style>
<ul>
    <span class="right"></span>
    <span class="right"></span>
</ul>​
<table class="button">
    <tr>
        <td><input type="button" onclick="showhide()"  value="Social Insurance Status"/>
        <td><input type="button" value="Go to New Education"  onclick="location.href = 'newEducation.jsp'"/>
        <td><input type="button" value="Go to New SocialInsuranceStorage" onclick="location.href = 'newSocialInsuranceRecord.jsp'"/>
    </tr>
</table>
<script type="text/javascript">
    function showhide()
    {
        var div = document.getElementById("newpost");
        if (div.style.display !== "none") {
            div.style.display = "none";
        } else {
            div.style.display = "block";
        }
    }
</script>
<div id="newpost">
    &#9679; <%
        double sumOfInsurances = dataStorage.calculateSocialBenefit(id);
        if (sumOfInsurances > 0) {
            out.print(String.format("The sum of money for social insurances for user with ID = %d is %.2f BGN", id, sumOfInsurances));
        } else {
            out.print("This user can't take social insurances!");
        }
    %>
</div>
</div>
<%
    } catch (NumberFormatException ex) {
        out.print("You have to enter digit!");
    } catch (DALException ex) {
        out.print(String.format("Citizen with ID = %d can't be found!", id));
    } catch (NullPointerException ex) {
        out.print("Your value is null!");
    }
%>
</div>
</body>
</html>
