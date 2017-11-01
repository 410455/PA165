package cz.muni.fi.pa165.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/pharmacy")
public class PharmacyServlet extends HttpServlet {

    final static Logger log = LoggerFactory.getLogger(HomeServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("pharmacy servlet called, forwarding to pharmacy.jsp");
        request.getRequestDispatcher("/WEB-INF/hidden-jsps/pharmacy.jsp").forward(request, response);
    }

}
