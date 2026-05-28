package az.fatia.servlet;

import az.fatia.enums.Status;
import az.fatia.model.Apartment;
import az.fatia.repository.JdbcApartmentRepository;
import az.fatia.service.ApartmentService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/apartments/*")
public class ApartmentServlet extends HttpServlet {

    private ApartmentService apartmentService;
    private JdbcApartmentRepository repository;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        this.repository = new JdbcApartmentRepository();
        this.apartmentService = new ApartmentService(repository);
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            List<Apartment> apartments = repository.findAll();
            String jsonResult = objectMapper.writeValueAsString(apartments);
            resp.getWriter().write(jsonResult);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            Apartment newApartment = objectMapper.readValue(req.getReader(), Apartment.class);

            newApartment.setStatus(Status.AVAILABLE);
            newApartment.setClientName(null);

            repository.save(newApartment);

            resp.setStatus(HttpServletResponse.SC_CREATED); // 201 Created
            resp.getWriter().write("{\"message\": \"Apartment registered successfully\"}");
        } catch (Exception e) {
            System.err.println("=== Error Occurred in POST Request ===");
            e.printStackTrace();

            // Bubble up the real database error directly to Postman for visibility
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            String msg = e.getMessage() != null ? e.getMessage() : "Unknown error";
            resp.getWriter().write("{\"error\": \"Data could not be written to the database!\", \"details\": \"" + msg.replace("\"", "'") + "\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Missing parameters in URL\"}");
            return;
        }

        String[] splits = pathInfo.split("/");
        if (splits.length < 3) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Invalid URL format\"}");
            return;
        }

        try {
            int apartmentId = Integer.parseInt(splits[1]);
            String action = splits[2].toLowerCase();

            Apartment apartment = repository.findById(apartmentId);
            if (apartment == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
                resp.getWriter().write("{\"error\": \"Apartment not found\"}");
                return;
            }

            if ("reserve".equals(action)) {
                Apartment bodyData = objectMapper.readValue(req.getReader(), Apartment.class);
                String clientName = bodyData.getClientName();

                if (clientName == null || clientName.trim().isEmpty()) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("{\"error\": \"Client name is required for reservation\"}");
                    return;
                }

                apartmentService.reserve(apartmentId, clientName);

                apartment = repository.findById(apartmentId);

                if (apartment.getStatus() == Status.RESERVED) {
                    resp.getWriter().write("{\"message\": \"Apartment reserved successfully\"}");
                } else {
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    resp.getWriter().write("{\"error\": \"Action blocked by configuration or invalid state\"}");
                }

            } else if ("release".equals(action)) {
                apartmentService.release(apartmentId);
                apartment = repository.findById(apartmentId);

                if (apartment.getStatus() == Status.AVAILABLE) {
                    resp.getWriter().write("{\"message\": \"Apartment released successfully\"}");
                } else {
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    resp.getWriter().write("{\"error\": \"Action blocked by configuration or invalid state\"}");
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\": \"Unknown action. Use 'reserve' or 'release'\"}");
            }

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"ID must be an integer\"}");
        } catch (Exception e) {
            System.err.println("=== Error Occurred in PUT Request ===");
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"An error occurred: " + e.getMessage() + "\"}");
        }
    }
}