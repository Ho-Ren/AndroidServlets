import java.io.IOException;
import java.util.HashMap;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class androidsearch extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getServletInfo() {
		return "Servlet connects to MySQL database and displays result of a SELECT";
	}

	/**
	 * @throws IOException
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		String search = request.getParameter("title");
		int perPage;
		String order = request.getParameter("order");
		if (order == null){
			order = "asc";
		}
		if (request.getParameter("perPage") == null)
			perPage = 10;
		else
			perPage = Integer.parseInt(request.getParameter("perPage"));
		PrintWriter out = response.getWriter();
        //response.setContentType("text/html"); // Response mime type
		out.println("Title = " + search);
		int pageNum;
		String value;

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection dbcon = DriverManager.getConnection("jdbc:mysql:///moviedb?autoReconnect=true&useSSL=false", Globals.un, Globals.pw);
			Statement statement = dbcon.createStatement();
			String query;
			//create query for browse by title to get number of pages for pagination
			query = "select * from movies where movies.title LIKE '"+search +"%'";
			value ="title";
			// get number of rows in the list
			ResultSet result = statement.executeQuery(query);
//			result.last();
//			int total = result.getRow();
//			System.out.println("total page: " + total);
//			pageNum = (total <= perPage ? 0 : (int)(Math.ceil((double)result.getRow() / perPage)));
//				// get current page and set limit and offset query is based on the parameter again
//			int offset;
//			System.out.println(request.getParameter("page"));
//			if (request.getParameter("page") == null) {
//					offset = 0;
//			} else {
//				offset = (Integer.parseInt(request.getParameter("page"))-1) * perPage;
//			}
			ArrayList <String> movieList = new ArrayList<String>();
			while (result.next()) {
				String title = result.getString("title");		
				out.println(title);
				movieList.add(title);
			}
		//	request.setAttribute("value", value);
			//request.setAttribute("pageNum", pageNum);
		//	request.setAttribute("perPage", perPage);
		//	request.setAttribute("order", order);
			request.setAttribute("search", search);

		} catch (SQLException ex) {
			while (ex != null) {
				System.out.println("SQL Exception:  " + ex.getMessage());
				ex = ex.getNextException();
			} // end while
		} // end catch SQLException

		catch (java.lang.Exception ex) {
			out.println("<HTML>" + "<HEAD><TITLE>" + "MovieDB: Error" + "</TITLE></HEAD>\n<BODY>"
					+ "<P>SQL error in doGet: " + ex.getMessage() + "</P></BODY></HTML>");
			return;
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

