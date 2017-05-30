import java.io.*; 
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class androidlogin extends HttpServlet
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getServletInfo()
    {
       return "Servlet connects to MySQL database and displays result of a SELECT";
    }
    // Use http GET

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {

        response.setContentType("text/html");    // Response mime type
        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
        try
           {
              //Class.forName("org.gjt.mm.mysql.Driver");
              Class.forName("com.mysql.jdbc.Driver").newInstance();

              Connection dbcon = DriverManager.getConnection("jdbc:mysql:///moviedb?autoReconnect=true&useSSL=false", Globals.un, Globals.pw );
           
              Statement statement = dbcon.createStatement();    
              String pw = request.getParameter("pw");
              String email = request.getParameter("email");
//              out.println(email);
//              out.println(pw);
              String query = "SELECT * from customers where password = '" + pw + "'" + "and email ='" + email +"'";

              ResultSet rm = statement.executeQuery(query);
              rm.last();
              int count = rm.getRow();
              if (count !=1){
            	  out.print("Sorry username or password error");  
                  //RequestDispatcher rd=request.getRequestDispatcher("../index.html");  
                 // rd.include(request,response); 
              }
              // Perform the query
              else{
              ResultSet rs = statement.executeQuery(query);
              String name = null;
              int customerID = 961;
              while (rs.next()){
            	  name = rs.getString("first_name");
            	  customerID = rs.getInt("id");
              }
        	  out.print("successful");
             // HttpSession session = request.getSession(true);
              rs.close();
           }
              statement.close();
              dbcon.close();
              
            }
        catch (SQLException ex) {
              while (ex != null) {
                    System.out.println ("SQL Exception:  " + ex.getMessage ());
                    ex = ex.getNextException ();
                }  // end while
              //response.sendRedirect("../index.html");
            }  // end catch SQLException

        catch(java.lang.Exception ex)
            {	
                out.println("<HTML>" +
                            "<HEAD><TITLE>" +
                            "MovieDB: Error" +
                            "</TITLE></HEAD>\n<BODY>" +
                            "<P>SQL error in doGet: " +
                            ex.getMessage() + "</P></BODY></HTML>");
                return;
            }
         out.close();
    }
    
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
	doGet(request, response);
    }
}