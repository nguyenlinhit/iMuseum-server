<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

    <html>

    <head>
        <title>Index</title>
        <link href="resources/css/style.css" rel="stylesheet" type="text/css">
        <link href="resources/css/bootstrap-theme.css" rel="stylesheet" type="text/css">
        <link href="resources/css/bootstrap-theme.min.css" rel="stylesheet" type="text/css">
        <link href="resources/css/bootstrap.css" rel="stylesheet" type="text/css">
        <link href="resources/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    </head>

    <body>

        <div class="container">
            <table class="table table-striped" border="1" align="center">
                <thead>
                    <tr class="bg-info">
                        <th>GROUP</th>
                        <th>API NAME</th>
                        <th>API LINK</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${result}" var="apiList">
                        <tr class="div-table-row row-num-${apiList.idApi} api-row">
                            <td>${apiList.group}</td>
                            <td>${apiList.apiName}</td>
                            <td>${apiList.apiLink}</td>
                            <c:if test="${apiList.method=='GET'}">
                                <td>
                                    <font color="red">${apiList.method}</font>
                                </td>
                            </c:if>
                            <c:if test="${apiList.method=='POST'}">
                                <td>
                                    <font color="green">${apiList.method}</font>
                                </td>
                            </c:if>
                        </tr>
                        <tr>
                            <td colspan="4" class="result-${apiList.idApi} result">
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </body>
    <script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
    <script src="resources/js/index.js"></script>

    </html>