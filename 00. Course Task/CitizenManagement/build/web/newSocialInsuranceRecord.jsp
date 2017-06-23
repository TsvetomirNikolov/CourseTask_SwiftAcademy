<%@page import="DALException.DALException"%>
<%@page import="DataAccesLayerManagement.MySQLDataAccessLayerManagement"%>
<%@page import="insurance.SocialInsuranceRecord"%>
<%@page import="java.util.List"%>
<%Class.forName("com.mysql.jdbc.Driver");%>
<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html>
    <head><link rel = "stylesheet" href="css_newSocialInsurance.css"></head>
    <body>
        <div>
            <%
                int idss = 0;
                try {
                    String ids = session.getAttribute("searchID").toString();
                    idss = Integer.parseInt(ids);

            %>
        </div>
        <h2>Please enter new data for month, year and salary</h2>
        <form action="newSocialInsuranceRecord.jsp" method="post">
            <table border="1">
                <tbody>
                    <tr>
                        <td>Year: </td>
                        <td><input type="text" name="year"></td>
                    </tr>
                    <tr>
                        <td>Month: </td>
                        <td><input type="text" name="month"></td>
                    </tr>
                    <tr>
                        <td>Amount: </td>
                        <td><input type="text" name="amount"></td>
                    </tr>
                </tbody>
            </table>
        </form>
        <p></p>
        <input type="submit" value="SUBMIT SocialInsuranceRecord" onclick="location.href = 'userInfo.jsp?searchID=<%=idss%>'"/>
        <%
            MySQLDataAccessLayerManagement record = new MySQLDataAccessLayerManagement();
            if (request.getParameter("year") != null && request.getParameter("month") != null && request.getParameter("amount") != null) {
                record.addSocialInsuranceRecord(idss, request);
            }
        %>
        <h2>Social Insurance Record</h2>
        <div class="social_records">
            <table border="1">
                <thead>
                    <tr>
                        <td><h5>Year</h5></td><td><h5>Month</h5></td><td><h5>Amount</h5></td>
                    </tr>
                </thead>
                <tbody>
                    <%                        List<SocialInsuranceRecord> list = record.getSocialInsuranceRecord(idss);
                        for (SocialInsuranceRecord records : list) {
                            String month = null;
                            switch (records.getMonth()) {
                                case 1:
                                    month = "January";
                                    break;
                                case 2:
                                    month = "February";
                                    break;
                                case 3:
                                    month = "March";
                                    break;
                                case 4:
                                    month = "April";
                                    break;
                                case 5:
                                    month = "May";
                                    break;
                                case 6:
                                    month = "June";
                                    break;
                                case 7:
                                    month = "July";
                                    break;
                                case 8:
                                    month = "August";
                                    break;
                                case 9:
                                    month = "September";
                                    break;
                                case 10:
                                    month = "October";
                                    break;
                                case 11:
                                    month = "November";
                                    break;
                                case 12:
                                    month = "December";
                                    break;
                                default:
                                    throw new DALException("This month doesn't exist!");
                            }
                            out.print("<tr><td>" + records.getYear() + "</td><td>" + month + "</td><td>" + records.getAmount() + " лева </td>");
                        }
                    %>
                </tbody>
            </table>
        </div>    
    </ul>
</div>
<%
    } catch (NumberFormatException ex) {
        out.print("You have to enter digit!");
    } catch (DALException ex) {
        out.print(String.format("Citizen with ID = %d can't be found!", idss));
    } catch (NullPointerException ex) {
        out.print("Your value is null!");
    }
%>
</div>
</body>
</html>