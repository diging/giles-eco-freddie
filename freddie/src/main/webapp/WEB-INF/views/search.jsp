<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<c:url value="/search" var="actionUrl" />

<form:form action="${actionUrl}" method="POST" class="form-inline">
      <div class="form-group">
	     <label for="search">Search for</label>
	     <input style="min-width:400px" type="text" class="form-control" name="searchTerm" id="searchTerm" placeholder="Search term...">
	 </div>
	<button type="submit" class="btn btn-default">Search</button>  
</form:form>

<p>

<div class="panel panel-default">
  <div class="panel-heading">Search Results</div>

  <!-- Table -->
  <table class="table">
    <tr>
        <th>File Id</th>
        <th>Stored File Id</th>
        <th>Document Id</th>
        <th>Username</th>
        <th>Content</th>
    </tr>
    
    <c:forEach items="${results}" var="doc">
    <tr>
        <td>${doc.fileId}</td>
        <td>${doc.storedFileId}</td>
        <td>${doc.documentId}</td>
        <td>${doc.username}</td>
        <td>${fn:substring(doc.content, 0, 100)} ...</td>
    </tr>
	</c:forEach>
  </table>
</div>