package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
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
      response.setContentType("text/html");
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
      out.write("<!DOCTYPE html>\n");
      out.write("<html>\n");
      out.write("    <head><link rel = \"stylesheet\" href=\"css_style.css\"></head>\n");
      out.write("    <body>\n");
      out.write("        <h1>Welcome to Citizen Management Data Base Engine</h1>\n");
      out.write("          <form action=\"\" method=\"get\">\n");
      out.write("                <p>Search citizen by ID:</p>\n");
      out.write("                <input type=\"text\" name=\"searchID\">\n");
      out.write("                <input type=\"submit\" class=\"btn_search\" value=\"Search\">\n");
      out.write("            </form>\n");
      out.write("    </table>\n");
      out.write("    <h4>Citizens details</h4>\n");
      out.write("    <table border =\"1\">\n");
      out.write("        <tbody>\n");
      out.write("            <tr>\n");
      out.write("                <td>First name: </td>\n");
      out.write("                <td>");
 out.println("5435");
      out.write("</td> \n");
      out.write("            </tr>\n");
      out.write("            <tr>\n");
      out.write("                <td>Second name: </td>\n");
      out.write("                <td></td>\n");
      out.write("            </tr>\n");
      out.write("            <tr>\n");
      out.write("                <td>Last name: </td>\n");
      out.write("                <td></td>\n");
      out.write("            </tr>\n");
      out.write("            <tr>\n");
      out.write("                <td>Date of birth: </td>\n");
      out.write("                <td></td>\n");
      out.write("            </tr>\n");
      out.write("            <tr>\n");
      out.write("                <td>Gender: </td>\n");
      out.write("                <td></td>\n");
      out.write("            </tr>\n");
      out.write("        <td>Address: </td>\n");
      out.write("        <td></td>\n");
      out.write("        <tr></tr>\n");
      out.write("        <tr>\n");
      out.write("            <td>Educations: <ol type=\"10\">\n");
      out.write("                    <li></li>\n");
      out.write("                </ol>\n");
      out.write("            </td>\n");
      out.write("            <td></td>\n");
      out.write("        </tr>\n");
      out.write("    </tbody>\n");
      out.write("</table>\n");
      out.write("</form>\n");
      out.write("<h1></h1>\n");
      out.write("<input type=\"button\" value=\"Go to newEducation\" />\n");
      out.write("<h1></h1>\n");
      out.write("<input type=\"button\" value=\"Go to newSocialInsuranceRecord\" />\n");
      out.write("<h1></h1>\n");
      out.write("<input type=\"reset\" value=\"Refresh\" />\n");
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
