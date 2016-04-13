<%@ page import="edu.cmu.lpsoca.model.Board" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %><%-- Created by IntelliJ IDEA. --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <% int auto = (int) request.getAttribute("numberBoards");%>
    <title>DashBoard</title>
</head>
<body>
<h1>Number of board connected <%out.print(auto);%></h1>
<h2>Devices</h2>
<table style="width:50%">
    <%
        Map<Board, List<String>> boardMessageMap = (Map<Board, List<String>>) request.getAttribute("boardsMessagesMap");
        Iterator it = boardMessageMap.entrySet().iterator();
        int counter = 1;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            out.println(String.format("<tr>"));
            out.println(String.format("<td> Device %d </td>", counter));
            out.println(String.format("<td> %s </td>", ((Board) pair.getKey()).getName()));
            out.println(String.format("</tr>"));
            counter++;
        }
    %>
</table>
<h2>Messages</h2>
<%
    boardMessageMap = (Map<Board, List<String>>) request.getAttribute("boardsMessagesMap");
    it = boardMessageMap.entrySet().iterator();
    counter = 1;
    while (it.hasNext()) {
        Map.Entry pair = (Map.Entry) it.next();
        out.println(String.format("%s </br>", ((Board) pair.getKey()).getName()));
        for (String item : ((List<String>) pair.getValue()))
            out.println(String.format("message %s \t %s </br>", counter, item));
        counter++;
        out.println("</br></br></br>");
    }
%>


</body>
</html>
