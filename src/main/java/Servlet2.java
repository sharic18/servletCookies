import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/Servlet2")
public class Servlet2 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Servlet2() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();

		Cookie[] cookies = request.getCookies();

		String nombre = buscar(cookies, "nombre");
		String edad = buscar(cookies, "edad");
		String genero = buscar(cookies, "genero");
		String ciudad = buscar(cookies, "ciudad");
		String correo = buscar(cookies, "correo");
		String notificaciones = buscar(cookies, "notificaciones");

		if (nombre == null) {
			out.print("<h3 style='color:red'>No se encontraron cookies.</h3>");
			out.print("<p>Primero debes llenar el formulario.</p>");
			out.print("<a href='index.html'>Ir al formulario</a>");
			out.close();
			return;
		}

		out.print("<h3>Hello " + escapar(nombre) + "</h3>");
		out.print("<p>Estos datos se leyeron desde las cookies:</p>");
		out.print("<table border='1' cellpadding='6'>");
		out.print("<tr><th>Campo</th><th>Valor</th></tr>");
		out.print("<tr><td>Nombre (texto)</td><td>" + escapar(nombre) + "</td></tr>");
		out.print("<tr><td>Edad (número)</td><td>" + escapar(edad) + "</td></tr>");
		out.print("<tr><td>Género (select)</td><td>" + escapar(genero) + "</td></tr>");
		out.print("<tr><td>Ciudad (texto)</td><td>" + escapar(ciudad) + "</td></tr>");
		out.print("<tr><td>Correo (texto)</td><td>" + escapar(correo) + "</td></tr>");
		out.print("<tr><td>Notificaciones (booleano)</td><td>" + escapar(notificaciones) + "</td></tr>");
		out.print("</table>");
		out.print("<br/><a href='index.html'>Volver al formulario</a>");
		out.close();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	private String buscar(Cookie[] cookies, String nombre) throws IOException {
		if (cookies == null) {
			return null;
		}
		for (Cookie c : cookies) {
			if (c.getName().equals(nombre)) {
				return URLDecoder.decode(c.getValue(), "UTF-8");
			}
		}
		return null;
	}

	private String escapar(String s) {
		if (s == null) {
			return "";
		}
		return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
	}
}