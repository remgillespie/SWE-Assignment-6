//Richard Gillespie, Nahom Ebssa, SWE 432 Assignment 6

import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.ArrayList;

//import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RootPage
 */
@WebServlet("/")
public class RootPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RootPage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	

        PrintWriter writer = response.getWriter();
        writer.append("<!DOCTYPE html>\r\n")
              .append("<html>\r\n")
              .append("        <head>\r\n")
              .append("            <title>SWE Assignment 5 Gillespie Ebssa</title>\r\n")
              .append("        </head>\r\n")
              .append("<style>")
              .append("body {background-color: white;} ")
              .append("body {font-family: Verdana;} ")
              .append("</style>")
              .append("        <body>\r\n")
              
              
              .append("            <form style=\"border:2px solid black;\" action=\"Assg5\" method=\"POST\">\r\n")
              .append("<h1>Welcome to the Gillespie-Ebssa Logical Super Collider With Data Persistence (SWE 432 Assignment 6).</h1>Use the options below to enter a logical predicate. Enter variable names, select the (NOT) checkbox if you wish to negate them, and choose an operator from the dropdown. You may also select a predicate which was previously used, and the supercollider will consider it once more.<br>")

              .append("                <input type=\"checkbox\" id=\"pnot\" name=\"pnot\" value=\"NOT\">")
              .append("<label for=\"pbutton\" >(NOT) </label>")
              .append("<input type=\"text\" id=\"ptext\" name=\"pname\" placeholder=\"Enter something for p\">")
              
              .append("<select name=\"operator\" size=\"1\" id=\"operator\">")
  			  .append("<option value=\"DEFAULT\" selected>[choose]</option>")
              .append("<option value=\"OR\">OR</option>")
              .append("<option value=\"AND\">AND</option>")
              .append("<option value=\"XOR\">XOR</option>")                    
              .append("</select>")
              
              .append("<input type=\"checkbox\" id=\"qnot\" name=\"qnot\" value=\"NOT\">")
              .append("<label for=\"qbutton\">(NOT) </label>")
              .append("<input type=\"text\" id=\"qtext\" name=\"qname\" placeholder=\"Enter something for q\">")
              
              .append("<select name=\"truthiness\" size=\"1\" id=\"truthStyle\">")
  			  .append("<option value=\"DEFAULT\" selected>[choose]</option>")
  			  .append("<option value=\"BINARY\">Binary</option>")
              .append("<option value=\"JAVA\">Java</option>")
              .append("<option value=\"STUDENT\">t/f</option>")
              .append("<option value=\"LISP\">LISP</option>")         
              .append("</select>")
              
              .append("                <input type=\"submit\" value=\"Submit\" />\r\n")
              .append("            <br>\n");
        
        
        ArrayList<String> pastPredicates = new ArrayList<String>(); //we will populate this with saved predicates from our file
        String temp;
        try { 
        	File s = new File("store.txt");
        	//s.delete();
        	//s = new File("store.txt");
            
			Scanner reader = new Scanner(s);
			String line = reader.nextLine();
			while (line != null) { // we will read lines from our file and put them in pastPredicates, to become part of the dropdown.
				//writer.append(".");
				temp = line.split(" ")[0] + " " + line.split(" ")[1] + " " + line.split(" ")[2] + " " + line.split(" ")[3];
				//writer.append("<! -- " + temp + " -->\n");
				pastPredicates.add(temp);
				line = reader.nextLine();
			}
			reader.close();
		} catch (Exception e) {
		}
        
        writer.append("<select name=\"Saved\" size=\"1\" id=\"store\">") // here we create the dropdown.
              .append("<option value=\"DEFAULT\" selected>[choose]</option>");
        
        int count = 0;
        for (String predicate : pastPredicates) { // here we construct the dropdown.
        	  writer.append("<option value=\"" + predicate + "\">" + predicate + "</option>");
        	  count++;
        }
        writer.append("</select></form>");
        
        
        writer.append("<p><br><br>This assignment was done by Richard \"Ed\" Gillespie and Nahom Ebssa. The back-end system validates the input predicate, includes the additional XOR operator, and has options to display truth values in different styles. <br>")
              .append("The two developers designed this page over Discord. Nahom worked mostly on the webpage, Ed helped with the webpage and implemented the validation. <br>")
              .append("<a href=\"http://mason.gmu.edu/~rgillesp/\">Ed's page</a> and <a href=\"http://mason.gmu.edu/~nebssa/\">Nahom's page</a>. </p>")
              .append("        </body>\r\n")
              .append("</html>\r\n");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pname;
		String pnot;
		String p;
		
		String qname;
		String qnot;
		String q;
		
		int operator;
		int truthStyle;
		String temp;
		
		if ((request.getParameter("Saved") == null) || (request.getParameter("Saved").contentEquals("DEFAULT"))) { //if they haven't selected an old predicate to run again
			temp = null;
			
			FileWriter s = new FileWriter("store.txt", true);
	
	        pname = request.getParameter("pname");
	        pnot = request.getParameter("pnot");
	        if (pnot == null) {
	        	pnot = "";
	        }
	        else {
	        	pnot = "!";
	        }
	        p = pnot + pname; // p is the first argument, possibly prefixed with a !
	        
	        qname = request.getParameter("qname");
	        qnot = request.getParameter("qnot");
	        if (qnot == null) {
	        	qnot = "";
	        }
	        else {
	        	qnot = "!";
	        }
	        q = qnot + qname; // q is the second argument, possibly prefixed with a ! and possibly not present at all
	        	        
	        operator = getOperator(request.getParameter("operator")); // the code is more convenient if this is an integer instead of a string
	        truthStyle = getTruthStyle(request.getParameter("truthiness")); // the code is more convenient if this is an integer
	        
	        s.write("" + p + " " + request.getParameter("operator") + " " + q + " " + request.getParameter("truthiness") + "\n"); // adding this to our file, for access later.
	        s.close();
		} else { // if they HAVE selected an old predicate
			temp = request.getParameter("Saved");
			String[] temps = temp.split(" ");
			if (temps.length == 3) {
				p = temps[0];
				operator = getOperator(temps[1]);
				q = "";
				truthStyle = getTruthStyle(temps[2]);
			} else {
				p = temps[0];
				operator = getOperator(temps[1]);
				q = temps[2];
				truthStyle = getTruthStyle(temps[3]);
			}
		}
        
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        
        // create HTML response
        PrintWriter writer = response.getWriter();
        writer.append("<!DOCTYPE html>\r\n")
              .append("<html>\r\n")
              .append("        <head>\r\n")
              .append("            <title>SWE Assignment 5 Gillespie Ebssa</title>\r\n")
              .append("<style>")
              .append("table, tr, th, td { border: 1px solid; }")
              .append("</style>")
              .append("        </head>\r\n")
              .append("        <body>\r\n");
        
        if (p != null) { // if they entered a first argument
        	if (operator == 0) { // if they didn't select an operator, just print the first argument
        		writer.append("<p>Your input: " + p + ".</p>");
        	} else { //otherwise print both arguments and the operator
        		if (temp == null) { // if this is a new predicate
        			writer.append("<p>Your input: " + p + " " + request.getParameter("operator") + " " + q + ".</p>");
        		} else { // if this is a saved predicate
        			writer.append("<p>Your input: " + p + " " + temp.split(" ")[1] + " " + q + ".</p>");
        		}
        	}
            writer.append(buildTable(p, operator, q, truthStyle));
            
        } else {
            writer.append("    Error: you did not enter enough arguments!\r\n");
        }
        writer.append("        </body>\r\n")
              .append("</html>\r\n");
    }

	private String buildTable(String p, int operator, String q, int truthStyle) {
		String ans = new String();
		
		//if (p.matches("^.*[^a-zA-Z0-9 ].*$") || q.matches("^.*[^a-zA-Z0-9 ].*$")) {
		//	ans = "Your predicate contains non-alphanumeric characters. Your session has been terminated and Interpol has been notified.";
		//	return ans;
		//}
		
		String t = "TRUE";
		String f = "FALSE";
		if (truthStyle == 0) {
			t = "1";
			f = "0";
		}
		if (truthStyle == 1) { 
			t = "true";
			f = "false";
		}
		if (truthStyle == 2) { 
			t = "t";
			f = "f";
		}
		if (truthStyle == 3) { 
			t = "T";
			f = "NIL";
		}
		
		if ((operator != 0) && (q.contentEquals("!") || q.isEmpty() || q == null || q.contentEquals(""))) { //chose an operator but did not specify a second argument
			ans = "You chose an operator but did not specify a second argument. Return to the previous page.";
			return ans;
		}
		
		if (operator == 0) { //just a single argument, true if true and false if false
			ans = "<table><tr><th>" + p + "</th><th>result</th></tr>";
			ans = ans + "<tr><td>" + t + "</td><td>" + t + "</td></tr>";
			ans = ans + "<tr><td>" + f + "</td><td>" + f + "</td></tr>";
			ans = ans + "</table>";
			return ans;
		}
		
		
		// if there is an operator, we need to build real truth tables
		ans = "<table><tr><th>" + p + "</th><th>" + q + "</th><th>result</th></tr>";
		if (operator == 1) { // AND
			ans = ans + "<tr><td>" + t + "</td><td>" + t + "</td><td>" + t + "</td></tr>";
			ans = ans + "<tr><td>" + t + "</td><td>" + f + "</td><td>" + f + "</td></tr>";
			ans = ans + "<tr><td>" + f + "</td><td>" + t + "</td><td>" + f + "</td></tr>";
			ans = ans + "<tr><td>" + f + "</td><td>" + f + "</td><td>" + f + "</td></tr>";
			ans = ans + "</table>";
		}
		if (operator == 2) { // OR
			ans = ans + "<tr><td>" + t + "</td><td>" + t + "</td><td>" + t + "</td></tr>";
			ans = ans + "<tr><td>" + t + "</td><td>" + f + "</td><td>" + t + "</td></tr>";
			ans = ans + "<tr><td>" + f + "</td><td>" + t + "</td><td>" + t + "</td></tr>";
			ans = ans + "<tr><td>" + f + "</td><td>" + f + "</td><td>" + f + "</td></tr>";
			ans = ans + "</table>";
		}
		if (operator == 3) { // XOR
			ans = ans + "<tr><td>" + t + "</td><td>" + t + "</td><td>" + f + "</td></tr>";
			ans = ans + "<tr><td>" + t + "</td><td>" + f + "</td><td>" + t + "</td></tr>";
			ans = ans + "<tr><td>" + f + "</td><td>" + t + "</td><td>" + t + "</td></tr>";
			ans = ans + "<tr><td>" + f + "</td><td>" + f + "</td><td>" + f + "</td></tr>";
			ans = ans + "</table>";
		}
		return ans;
	}
	
	private int getOperator(String op) {
		if (op.contentEquals("DEFAULT")) {
			return 0;
		}
		if (op.contentEquals("AND")) {
			return 1;
		}
		if (op.contentEquals("OR")) {
			return 2;
		}
		if (op.contentEquals("XOR")) {
			return 3;
		}
		return -1; //shouldn't ever reach this but may as well have an error value
	}
	private int getTruthStyle(String truthiness) {
		if (truthiness.contentEquals("BINARY")) {
			return 0;
		}
		if (truthiness.contentEquals("JAVA")) {
			return 1;
		}
		if (truthiness.contentEquals("STUDENT")) {
			return 2;
		}
		if (truthiness.contentEquals("LISP")) {
			return 3;
		}
		return -1; //shouldn't ever reach this but may as well have an error value
	}
}
