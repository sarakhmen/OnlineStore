package controller;

import controller.command.Command;
import controller.command.Commands;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class FrontController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Command command;
        RequestDispatcher dispatcher;
        String pageUrl;
        try {
            command = Commands.valueOf(req.getParameter("command")).getCommand();
            pageUrl = command.process(req, resp);
        } catch (IllegalArgumentException | NullPointerException ex) {
            //should be logged
            pageUrl = Pages.ERROR_PAGE;
            ex.printStackTrace();
        }

        dispatcher = req.getRequestDispatcher(pageUrl);
        dispatcher.forward(req, resp);
    }
}
