<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <html>

    <head>
        <title>Index</title>
        <link href="resources/css/style.css" rel="stylesheet" type="text/css">
        <link href="resources/css/menu.css" rel="stylesheet" type="text/css">
        <link href="resources/css/bootstrap-theme.css" rel="stylesheet" type="text/css">
        <link href="resources/css/bootstrap-theme.min.css" rel="stylesheet" type="text/css">
        <link href="resources/css/bootstrap.css" rel="stylesheet" type="text/css">
        <link href="resources/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    </head>

    <body>
        <div id="main">
            <div id="header"><p>Welcome to Smart Museum</p><p>TMA Solution</p></div>
            <div id="content">
                <div class="left">
                <div class="arrowsidemenu">
                    <div><a href="#" title="Home">HOME</a></div>
                    <div class="menuheaders"><a href="#">ARTIFACT</a></div>
                        <ul class="menucontents">
                            <li><a href="#">Get List Artifact</a></li>
                            <li><a href="#">Find Artifact</a></li>
                            <li><a href="#">Create New Artifact</a></li>
                            <li><a href="#">Edit Artifact</a></li>
                            <li><a href="#">Delete Artifact</a></li>
                        </ul>
                    <div class="menuheaders"><a href="#">BEACON</a></div>
                        <ul class="menucontents">
                            <li><a href="#">Get List Beacon</a></li>
                            <li><a href="#">Find Beacon</a></li>
                            <li><a href="#">Create New Beacon</a></li>
                            <li><a href="#">Edit Beacon</a></li>
                            <li><a href="#">Delete Beacon</a></li>
                            <li><a href="#">Get Location of Beacon</a></li>
                            <li><a href="#">Get Request of Beacon</a></li>
                            <li><a href="#">Get User Who Create Beacon</a></li>
                            <li><a href="#">Get List Artifact of Beacon</a></li>
                        </ul>
                </div>
                </div>
                <!--<table class="table table-striped" border="1" align="center">
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
                    </c:forEach>
                </tbody>
            </table>-->
                </div>
                <div class="right">
                    <div class="result-${apiList.idApi} result"></div>
                </div>
            </div>
        </div>
        
    </body>
    <script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
    <script src="resources/js/content.js"></script>
    <script src="resources/js/menu.js"></script>
    </html>