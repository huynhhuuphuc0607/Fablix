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
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;


// Declaring a WebServlet called MoviesServlet, which maps to url "/api/movies"
@WebServlet(name = "MoviesServlet", urlPatterns = "/api/movies")
public class MovieServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Create a dataSource which registered in web.xml
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json"); // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        try {
            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();

            // Declare our statement
            Statement statement = dbcon.createStatement();

            String query  = 
            		"SELECT m.id, m.title, m.year, m.director, r.rating \n"+
            		"FROM movies as m, ratings as r \n" +
            		"Where r.movieID = m.id \n" +
            		"Order by r.rating desc \n" +
            		"Limit 20";
            				
            				
            // Perform the query
            JsonArray jsonArray = new JsonArray();
            
            ResultSet rs = statement.executeQuery(query);
            String[] movieIDs = new String[20];
            int i = 0;
            while(rs.next())
            {
            	movieIDs[i] = rs.getString("m.id");
            	i++;
            }
            
            String[] starsInMovies = new String[20];
            String[] genresInMovies = new String[20];
            
            for(int j = 0; j < 20; j++)
            {
            	query = "SELECT s.name \n" +
            			"FROM movies as m, stars as s, stars_in_movies as sim \n" +
            			"Where sim.movieID = m.id and sim.starId = s.id and m.id = '" + movieIDs[j] + "'" +
            			";";
            	Statement tempStatement = dbcon.createStatement();
            	ResultSet rs3 = tempStatement.executeQuery(query);
            	starsInMovies[j] ="";
            	while(rs3.next())
            		starsInMovies[j] += rs3.getString("s.name") +", ";
            	
            	starsInMovies[j] = starsInMovies[j].substring(0, starsInMovies[j].length()-2);
            	
            	query = "SELECT g.name\n" + 
            			"FROM genres as g, genres_in_movies as gim, movies as m\n" + 
            			"Where gim.movieId = m.id and gim.genreId = g.id and m.id = '" + movieIDs[j] + "'" + 
            			";";
            	ResultSet rs4 = tempStatement.executeQuery(query);
            	genresInMovies[j] = "";
            	while(rs4.next())
            		genresInMovies[j] += rs4.getString("g.name") +", "; 
            	
            	genresInMovies[j] = genresInMovies[j].substring(0, genresInMovies[j].length()-2);

                rs3.close();
                rs4.close();
                tempStatement.close();
            }
            
            int j = 0;
            rs.beforeFirst();
            // Iterate through each row of rs
            while (rs.next()) {
                String movies_id = rs.getString("m.id");
                String movies_title = rs.getString("m.title");
                Integer movies_year = rs.getInt("m.year");
                String movies_director = rs.getString("m.director");
                String genre_name = genresInMovies[j];
                String star_name = starsInMovies[j];
                Float movies_ratings = rs.getFloat("r.rating");
                
               
                // Create a JsonObject based on the data we retrieve from rs
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("movies_id", movies_id);
                jsonObject.addProperty("movies_title", movies_title);
                jsonObject.addProperty("movies_year", movies_year);
                jsonObject.addProperty("movies_director", movies_director);
                jsonObject.addProperty("genre_name", genre_name);
                jsonObject.addProperty("star_name", star_name);
                jsonObject.addProperty("movies_ratings", movies_ratings);
                jsonArray.add(jsonObject);
                
                j++;
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
