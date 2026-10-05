import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/Servlet1")
public class Servlet1 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Servlet1() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();

		String nombre = request.getParameter("userName");
		String edadTxt = request.getParameter("edad");
		String genero = request.getParameter("genero");
		String ciudad = request.getParameter("ciudad");
		String correo = request.getParameter("correo");
		boolean notificaciones = request.getParameter("notificaciones") != null;

		List<String> errores = new ArrayList<>();

		if (nombre == null || nombre.trim().isEmpty()) {
			errores.add("El nombre no puede estar vacío.");
		} else if (nombre.trim().length() < 2) {
			errores.add("El nombre debe tener al menos 2 caracteres.");
		}

		int edad = 0;
		if (edadTxt == null || edadTxt.trim().isEmpty()) {
			errores.add("La edad no puede estar vacía.");
		} else {
			try {
				edad = Integer.parseInt(edadTxt.trim());
				if (edad < 1 || edad > 120) {
					errores.add("La edad debe estar entre 1 y 120.");
				}
			} catch (NumberFormatException e) {
				errores.add("La edad debe ser un número entero.");
			}
		}

		if (genero == null || !(genero.equals("Femenino") || genero.equals("Masculino") || genero.equals("Otro"))) {
			errores.add("Debes seleccionar un género válido.");
		}

		if (ciudad == null || ciudad.trim().isEmpty()) {
			errores.add("La ciudad no puede estar vacía.");
		}

		if (correo == null || !correo.trim().matches("^[\\w.+-]+@[\\w-]+\\.[\\w.-]+$")) {
			errores.add("El correo no tiene un formato válido (ejemplo: ana@correo.com).");
		}

		if (!errores.isEmpty()) {
			out.print("<h3 style='color:red'>Hay errores en el formulario:</h3><ul>");
			for (String err : errores) {
				out.print("<li>" + err + "</li>");
			}
			out.print("</ul>");
			out.print("<a href='index.html'>Volver al formulario</a>");
			out.close();
			return;
		}

		agregarCookie(response, "nombre", nombre.trim());
		agregarCookie(response, "edad", String.valueOf(edad));
		agregarCookie(response, "genero", genero);
		agregarCookie(response, "ciudad", ciudad.trim());
		agregarCookie(response, "correo", correo.trim());
		agregarCookie(response, "notificaciones", String.valueOf(notificaciones));

		out.print("<h3>Welcome " + escapar(nombre.trim()) + "</h3>");
		out.print("<p>Tus datos fueron guardados en cookies.</p>");
		out.print("<form action='Servlet2' method='post'>");
		out.print("<input type='submit' value='go'>");
		out.print("</form>");
		out.close();
	}

	private void agregarCookie(HttpServletResponse response, String nombre, String valor)
			throws IOException {
		Cookie ck = new Cookie(nombre, URLEncoder.encode(valor, "UTF-8"));
		ck.setMaxAge(60 * 60); // dura 1 hora
		response.addCookie(ck);
	}

	private String escapar(String s) {
		return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
	}
}