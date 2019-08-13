import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// Declaring a WebServlet called SingleStarServlet, which maps to url "/api/single-movie"
@WebServlet(name = "SingleMovieServlet", urlPatterns = "/api/single-movie")
public class SingleMovieServlet extends HttpServlet {
	private static final long serialVersionUID = 2L;

	// Create a dataSource which registered in web.xml
	@Resource(name = "jdbc/moviedb")
	private DataSource dataSource;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json"); // Response mime type

		// Retrieve parameter id from url request.
		String id = request.getParameter("id");

		// Output stream to STDOUT
		PrintWriter out = response.getWriter();

		try {
			// Get a connection from dataSource
			Connection dbcon = dataSource.getConnection();

			
			// Construct a query with parameter represented by "?"
			String query = "SELECT m.id, m.title, m.year, m.director, g.id, g.name,  r.rating, s.id, s.name \n" + 
					"FROM movies as m, ratings as r, genres as g, genres_in_movies as gim, stars as s, stars_in_movies as sim\n" + 
					"WHERE  r.movieId = m.id and g.id = gim.genreId and m.id = gim.movieId\n" + 
					"	and sim.starId = s.id and sim.movieId = m.id\n" + 
					"	and m.id =  ?\n" + 
					"ORDER BY g.name ASC";
						
			// Declare our statement
			PreparedStatement statement = dbcon.prepareStatement(query);

			// Set the parameter represented by "?" in the query to the id we get from url,
			// num 1 indicates the first "?" in the query
			statement.setString(1, id);

			// Perform the query
			ResultSet rs = statement.executeQuery();

			
			///RUN QUERY MOVIE STARS
			JsonArray jsonArray = new JsonArray();

			// Iterate through each row of rs
			while (rs.next()) {

				String movieId = rs.getString("m.id");
				String movieTitle = rs.getString("m.title");
				Integer	movieYear = rs.getInt("m.year");
				String movieDirector = rs.getString("m.director");
				Float movieRating = rs.getFloat("r.rating");
				String starId = rs.getString("s.id");
				String starName = rs.getString("s.name");
				String genreId = rs.getString("g.id");
				String genreName = rs.getString("g.name");
				
				// Create a JsonObject based on the data we retrieve from rs

				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("movie_id", movieId);
				jsonObject.addProperty("movie_title", movieTitle);
				jsonObject.addProperty("movie_year", movieYear);
				jsonObject.addProperty("movie_director", movieDirector);
				jsonObject.addProperty("movie_rating", movieRating);
				jsonObject.addProperty("star_id", starId);
				jsonObject.addProperty("star_name", starName);
				jsonObject.addProperty("genre_id", genreId);
				jsonObject.addProperty("genre_name", genreName);

				jsonArray.add(jsonObject);
			}
			
			
            // write JSON string to output
            out.write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);

			rs.close();
			statement.close();
			dbcon.close();
		} catch (Exception e) {
			// write error message JSON object to output
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("errorMessage", e.getMessage());
			out.write(jsonObject.toString());

			// set reponse status to 500 (Internal Server Error)
			response.setStatus(500);
		}
		out.close();

	}

}
