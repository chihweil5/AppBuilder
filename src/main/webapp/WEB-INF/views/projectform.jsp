<%@ include file="common/header.jspf"%>
<%@ include file="common/navigation.jspf"%>

<div class="container">
	<h1>Android Application Builder</h1>
	<br>
	<form:form id="githubForm" method="post" class="form-horizontal" modelAttribute="githubInfoWrapper">
		<c:forEach items="${githubInfoWrapper.githubInfoList}"
			var="githubInfoList" varStatus="i">

			<div class="form-group">
				<label class="col-xs-1 control-label">Project</label>
				<div class="col-xs-2">
					<form:input path="githubInfoList[${i.index}].username" type="text"
						class="form-control" id="github[${i.index}].username"
						name="github[${i.index}].username" placeholder="GitHub User Name" />
				</div>
				<div class="col-xs-2">
					<form:input path="githubInfoList[${i.index}].reponame" type="text"
						class="form-control" id="github[${i.index}].reponame"
						name="github[${i.index}].reponame" placeholder="Repository Name" />
				</div>
				<div class="col-xs-3">
					<form:input path="githubInfoList[${i.index}].tags" type="text"
						class="form-control" id="github[${i.index}].tags" name="github[${i.index}].tags"
						placeholder="SHA Tag" />
				</div>
			</div>
		</c:forEach>

		<div class="col-xs-2">
			<button id="CloneBtn" type="submit" class="btn btn-success btn-md">Clone and Build</button>
		</div>
	
	</form:form>
</div>

<%@ include file="common/footer.jspf"%>



