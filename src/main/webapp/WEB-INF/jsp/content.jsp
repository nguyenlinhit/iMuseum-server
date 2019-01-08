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

            <fieldset>

                <!-- Form Name -->
                <legend>
                    <h2><i class="glyphicon glyphicon-star-empty"><font color="#4da3f9">API INFORMATION</font></i></h2>
                </legend>
                <!-- Text input-->
                <table class="table">
                    <thead>
                        <tr class="bg-success">

                            <th>Field</th>
                            <th>Value</th>

                        </tr>
                    </thead>
                    <tbody>
                        <c:if test="${result.apiList.method=='POST'}">
                            <c:forEach items="${result.apiAttr}" var="attrList">
                                <tr class="div-table-row api-row">
                                    <td>${attrList}</td>
                                    <td><input type="text" class="attrValue" name="${attrList}" /></td>
                                </tr>
                            </c:forEach>
                        </c:if>
                        <tr>
                            <td>Description</td>
                            <td>${result.apiList.description}</td>
                        </tr>
                        <tr>
                            <td>Example</td>
                            <td><input type="hidden" id="apiMethod" value="${result.apiList.method}">
                                <input size="20" class="form-control" type="text" id="apiURL" value="http://192.168.69.207:8080${result.apiList.apiExample}">
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2" class="api-result"></td>
                        </tr>
                        <tr>
                            <td colspan="2" align="center"><button class="btn btn-info" id="submitbtn">Run API</button></td>
                        </tr>
                    </tbody>
                </table>



            </fieldset>
        </div>
        </div>


    </body>
    <script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
    <script src="resources/js/content.js"></script>

    </html>