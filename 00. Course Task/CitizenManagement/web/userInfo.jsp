<%@page import="personaldetails.Citizen"%>
<!DOCTYPE html>
<html>
    <head><link rel = "stylesheet" href="css_style.css"></head>
    <body>
        <h1>Welcome to Citizen Management Data Base Engine</h1>
          <form action="" method="get">
                <p>Search citizen by ID:</p>
                <input type="text" name="searchID">
                <input type="submit" class="btn_search" value="Search">
            </form>
    </table>
    <h4>Citizens details</h4>
    <table border ="1">
        <tbody>
            <tr>
                <td>First name: </td>
                <td><% out.println("5435");%></td> 
            </tr>
            <tr>
                <td>Second name: </td>
                <td></td>
            </tr>
            <tr>
                <td>Last name: </td>
                <td></td>
            </tr>
            <tr>
                <td>Date of birth: </td>
                <td></td>
            </tr>
            <tr>
                <td>Gender: </td>
                <td></td>
            </tr>
        <td>Address: </td>
        <td></td>
        <tr></tr>
        <tr>
            <td>Educations: <ol type="10">
                    <li></li>
                </ol>
            </td>
            <td></td>
        </tr>
    </tbody>
</table>
</form>
<h1></h1>
<input type="button" value="Go to newEducation" />
<h1></h1>
<input type="button" value="Go to newSocialInsuranceRecord" />
<h1></h1>
<input type="reset" value="Refresh" />
</body>
</html>
