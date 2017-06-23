package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import DALException.DALException;
import java.util.Scanner;
import java.util.List;
import education.Education;
import education.Education;
import address.Address;
import DataAccesLayerManagement.MySQLDataAccessLayerManagement;
import personaldetails.Citizen;

public final class userInfo_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  private org.glassfish.jsp.api.ResourceInjector _jspx_resourceInjector;

  public java.util.List<String> getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;

    try {
      response.setContentType("text/html; charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.glassfish.jsp.api.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
Class.forName("com.mysql.jdbc.Driver");
      out.write("\n");
      out.write("\n");
      out.write("<!DOCTYPE html>\n");
      out.write("<html>\n");
      out.write("    <head><link rel = \"stylesheet\" href=\"css_style.css\">\n");
      out.write("        <style>p.indent{ padding-left: 2.8em }</style>\n");
      out.write("    </head>\n");
      out.write("    <body>\n");
      out.write("        <h1>Welcome to Citizen Management Data Base Engine</h1>\n");
      out.write("        <form action=\"\" method=\"get\">\n");
      out.write("            <h4>Search citizen by ID:</h4>\n");
      out.write("            <input type=\"text\" name=\"searchID\">\n");
      out.write("            <input type=\"submit\" class=\"btn_search\" value=\"Search\">\n");
      out.write("        </form>\n");
      out.write("        ");

            String ids = request.getParameter("searchID");
            session.setAttribute("searchID", ids);
            int id = 0;
            MySQLDataAccessLayerManagement citizen = new MySQLDataAccessLayerManagement();
            try {
                id = Integer.parseInt(ids);


        
      out.write("\n");
      out.write("        <div id=\"footer\">\n");
      out.write("            <p>User ID = ");
      out.print(ids);
      out.write("</p>\n");
      out.write("        </table>\n");
      out.write("        <h4>Citizens details</h4>\n");
      out.write("        <table border =\"1\">\n");
      out.write("            <tbody>\n");
      out.write("                <tr>\n");
      out.write("                    <td>First name: </td>\n");
      out.write("                    <td>");
      out.print(citizen.getCitizens(id).getFirstName());
      out.write("</td> \n");
      out.write("                </tr>\n");
      out.write("                <tr>\n");
      out.write("                    <td>Second name: </td>\n");
      out.write("                    <td>");
      out.print(citizen.getCitizens(id).getMiddleName());
      out.write("</td>\n");
      out.write("                </tr>\n");
      out.write("                <tr>\n");
      out.write("                    <td>Last name: </td>\n");
      out.write("                    <td>");
      out.print(citizen.getCitizens(id).getLastName());
      out.write("</td>\n");
      out.write("                </tr>\n");
      out.write("                <tr>\n");
      out.write("                    <td>Date of birth: </td>\n");
      out.write("                    <td>");
      out.print(citizen.getCitizens(id).getDateOfBirth());
      out.write("</td>\n");
      out.write("                </tr>\n");
      out.write("                <tr>\n");
      out.write("                    <td>Gender: </td>\n");
      out.write("                    <td>");
      out.print(citizen.getCitizens(id).getGender());
      out.write("</td>\n");
      out.write("                </tr>\n");
      out.write("                <tr>\n");
      out.write("                    <td>Height: </td>\n");
      out.write("                    <td>");
      out.print(citizen.getCitizens(id).getHeight());
      out.write("</td>\n");
      out.write("                </tr>\n");
      out.write("            <td>Address: </td>\n");
      out.write("            <td>");
 for (Address address1 : citizen.getAddress(id)) {
                    out.print(String.format("%s, гр. %s, общ. %s, п.к.%s, ул. %s, N:%s",
                            address1.getCountry(), address1.getCity(), address1.getMunicipality(),
                            address1.getPostalCode(), address1.getStreet(), address1.getNumber()));
                    if (address1.getFloor() == null || address1.getApartmentNo() == null) {
                        continue;
                    } else {
                        out.print(String.format(", ет. %d, ап. %d", address1.getFloor(), address1.getApartmentNo()));
                    }
                }
                
      out.write("</td>\n");
      out.write("            <tr></tr>\n");
      out.write("            <tr>\n");
      out.write("                <td>Educations: </td>\n");
      out.write("                <td>");
 for (Education education1 : citizen.getEducations(id)) {
                        out.println(education1.getInstitutionName());
                        out.println(education1.getEnrollmentDate());
                        out.println(education1.getGraduationDate());
                        out.println(education1.getDegree());

                    }
      out.write("</td>\n");
      out.write("            </tr>\n");
      out.write("            </tbody>\n");
      out.write("        </table>\n");
      out.write("    </div>\n");
      out.write("</form>\n");
      out.write("<style>\n");
      out.write("    .button { \n");
      out.write("        width: 100%;\n");
      out.write("        table-layout: fixed;\n");
      out.write("        border-collapse: collapse;\n");
      out.write("        background-color: red; \n");
      out.write("    }\n");
      out.write("    .buttons button { \n");
      out.write("        width: 100%;\n");
      out.write("    }\n");
      out.write("</style>\n");
      out.write("<ul>\n");
      out.write("    <span class=\"right\"></span>\n");
      out.write("    <span class=\"right\"></span>\n");
      out.write("</ul>​\n");
      out.write("<table class=\"button\">\n");
      out.write("    <tr>\n");
      out.write("        <td><input type=\"button\" onclick=\"showhide()\"  value=\"Social Insurance Status\"/>\n");
      out.write("        <td><input type=\"button\" value=\"Go to New Education\"  onclick=\"location.href = 'newEducation.jsp'\"/>\n");
      out.write("        <td><input type=\"button\" value=\"Go to New SocialInsuranceStorage\" onclick=\"location.href = 'newSocialInsuranceRecord.jsp'\"/>\n");
      out.write("    </tr>\n");
      out.write("</table>\n");
      out.write("<script type=\"text/javascript\">\n");
      out.write("    function showhide()\n");
      out.write("    {\n");
      out.write("        var div = document.getElementById(\"newpost\");\n");
      out.write("        if (div.style.display !== \"none\") {\n");
      out.write("            div.style.display = \"none\";\n");
      out.write("        } else {\n");
      out.write("            div.style.display = \"block\";\n");
      out.write("        }\n");
      out.write("    }\n");
      out.write("</script>\n");
      out.write("<div id=\"newpost\">\n");
      out.write("    &#9679; ");

        double sumOfInsurances = citizen.calculateSocialBenefit(id);
        if (sumOfInsurances > 0) {
            out.print(String.format("The sum of money for social insurances for user with ID = %d is %.2f BGN", id, sumOfInsurances));
        } else {
            out.print("This user can't take social insurances!");
        }
    
      out.write("\n");
      out.write("</div>\n");
      out.write("</div>\n");

    } catch (NumberFormatException ex) {
        out.print("You have to enter digit!");
    } catch (DALException ex) {
        out.print(String.format("Citizen with ID = %d can't be found!", id));
    } catch (NullPointerException ex) {
        out.print("Your value is null!");
    }

      out.write("\n");
      out.write("</div>\n");
      out.write("</body>\n");
      out.write("</html>\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
