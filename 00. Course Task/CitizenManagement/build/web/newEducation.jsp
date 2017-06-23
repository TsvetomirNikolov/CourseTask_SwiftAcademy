<%@page import="DataAccesLayerManagement.MySQLDataAccessLayerManagement"%>
<%Class.forName("com.mysql.jdbc.Driver");%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title></title>
        <link rel = "stylesheet" href="css_newEducation.css">
    </head>
    <body>
        <%
            int idss = 0;
            try {
                String ids = session.getAttribute("searchID").toString();
                idss = Integer.parseInt(ids);

        %>
        <h2>Please enter new data for education</h2>
        <form action="newEducation.jsp" method="post">
            <table border="1">
                <tbody>
                    <tr>
                        <td>Type Education </td>
                        <td><select name="degree">
                                <option>None</option>
                                <option value="Primary">Primary</option>
                                <option value="Secondary">Secondary</option>
                                <option value="Bachelor">Bachelor</option>
                                <option value="Master">Master</option>
                                <option value="Doctorate">Doctorate</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>Institution name: </td>
                        <td><input type="text" name="institutionName"></td>
                    </tr>
                    <tr>
                        <td>Enrollment date: </td>
                        <td><input type="text" name="enrollmentDate"></td>
                    </tr>
                    <tr>
                        <td>Graduation date: </td>
                        <td><input type="text" name="graduationDate"></td>
                    </tr>
                    <tr>
                        <td>Final grade: </td>
                        <td><input type="text" name="grade"></td>
                    </tr>
                </tbody>
            </table>
            <h3></h3>
            <input type="submit" value="SUBMIT NewEducation">
        </form>
        <h1></h1>
        <%                MySQLDataAccessLayerManagement education = new MySQLDataAccessLayerManagement();
                if (request.getParameter("institutionName") != null) {
                    education.addEducation(idss, request);
                    session.setAttribute("searchID", session.getAttribute("searchID"));
                    response.sendRedirect("userInfo.jsp?searchID=" + idss);
                }
            } catch (NumberFormatException ex) {
                throw new NumberFormatException("Value is null!");
            } catch (NullPointerException ex) {
                throw new NullPointerException("You have to add value!");
            }
        %>
    </body>
</html>
