<%@ include file="common/header.jspf"%>
<%@ include file="common/navigation.jspf"%>


<div class="container">

	<h1>Build Data Query</h1>
	<br> <br>
	<form id="queryForm" method="get">
		<div class="form-group">
			<label class="col-xs-2 control-label">Search by: </label>
			<div class="col-xs-2">
				<select class="form-control" id="select1" name="select1" onchange="showOption()">
					<option value="">Please Select</option>
					<option value="Name">Repository Name</option>
					<option value="Status">Status</option>
					<option value="All">All</option>
				</select>
			</div>
			<div class="col-xs-2">
				<select class="form-control" id="select2" name="select2">
					<!--  onChange="search()"--> 
					<option value="">--</option>
				</select>
			</div>

			<div class="col-xs-1">
				<button class="btn btn-danger" type="submit">Query</button>
			</div>
		</div>
	</form>
	<br> <br>
	<form id="queryForm" method="get">
	<table id="myTable" class="table table-striped">
		<thead>
			<tr>
				<th>User Name/Repository Name</th>
				<th>Status</th>
				<th>Files</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${_githubInfoList}" var="githubInfo" varStatus="i">
				<tr>
					<td id="path">${githubInfo.username}/${githubInfo.reponame}</td>
					<td>${_Status[i.index]}</td>
					<c:if test="${_Status[i.index] == 'Built'}">
						<td><a class="btn btn-primary" type="submit" href="/ApplicationBuilder-0.0.1-SNAPSHOT/download/${i.index}">download ${githubInfo.reponame} apk file</a></td>
					</c:if>
					<c:if test="${_Status[i.index] == 'Failed'}">
						<td>fail to build ${githubInfo.reponame} apk file</td>
					</c:if>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	</form>
	<!--  
		<div>
			<a class="btn btn-primary" href="/appbuilder">Back</a>
		</div>
		-->
</div>

 
<script>

	function showOption() {
		console.log("changing the option");
		var sel1 = document.getElementById('select1');
		var sel2 = document.getElementById('select2');
			
		if(sel1.value == "Name") {
			<c:forEach items="${repoList}" var="repo" varStatus="i">
				sel2.options[${i.index}] = new Option("${repo}", "${repo}");
			</c:forEach>
			/*for(i = 0; i < ${repoList}.length; i++) {
				console.log("Repository Name" + reponame);
				sel2.options[i] = new Option(reponame[i], reponame[i]);
			}*/
			
		} else if(sel1.value == "Status") {
			sel2.options[0] = new Option("Built", "Built");
			sel2.options[1] = new Option("Failed", "Failed");
			//sel2.options[2] = new Option("Scheduled", "Scheduled");
			//sel2.options[3] = new Option("Not Scheduled", "Not Scheduled");
			sel2.length = 2;
		}
		else {
			sel2.options[0] = new Option("All", "All");
			sel2.length = 1;
		}
	}
</script>

<%@ include file="common/footer.jspf"%>