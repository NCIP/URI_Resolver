<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
                    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title></title>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.3/jquery.min.js"></script>

    <link rel="stylesheet" href="/WEB-INF/public/style.css" type="text/css" /> 

    <script type="text/javascript">

        var serviceUrl = "../";

        function clearURIMapDetails() {
            $('input').each(function(){
                $(this).val("");
            });        	
        }
        
        function clearIdentifiers(){
        	$('#identifiers').empty();
        }
        
        function clearVersionLists(){
            document.getElementById("listVersionOf").options.length = 0;
        	clearVersionIDList();
        }

        function clearVersionIDList(){
        	document.getElementById("listVersionID").options.length = 0;
        	document.getElementById("listVersionID").style.visibility='visible';
            document.getElementById("btnAddIdentifier").style.visibility = 'hidden';
        }
        
        function clearForm() {
            document.getElementById("btnAddIdentifier").style.visibility = 'hidden';
        	document.getElementById("btnSave").style.visibility = 'hidden';
        	clearVersionLists();
        	clearURIMapDetails();
        	clearIdentifiers();
        }
        
        function clearAll() {
        	clearForm();
            document.getElementById("listResourceType").options[0].selected = true;
        }

        function resetForm() {
        	var selectResourceTypes = document.getElementById("listResourceType");
        	var selectVersionOf = document.getElementById("listVersionOf");
        	var selectVersionID = document.getElementById("listVersionID");
        	
        	var resourceIndex = selectResourceTypes.selectedIndex;        	
        	
            var versionOfVal = selectVersionOf[selectVersionOf.selectedIndex].value;
        	var versionIDVal = selectVersionID[selectVersionID.selectedIndex].value;
  	
        	clearAll();

        	selectResourceTypes[resourceIndex].selected = true;
        	loadVersionOf(versionOfVal, versionIDVal);
        }
        
        // Called when "Resource Type" list is changed
        function loadVersionOf(versionOf, versionId) {
        	clearForm();
        	if(document.getElementById("listResourceType").selectedIndex == 0) {
        		clearForm();
        	}
        	else{
            	var type = $('#listResourceType').val();
            	var restURL = serviceUrl + "all/" + type;
	            $.ajax({
	                type: "GET",
	                url: restURL,
	                dataType: 'json',
	                contentType: "application/json",
	                error: function(XMLHttpRequest, textStatus, errorThrown){
	                	if( XMLHttpRequest.status == '404'){
	                        alert("404 Error collecting data.");
	                    }
	                	else{
	                		alert('There was an ' + errorThrown +
                                    ' error due to a ' + textStatus + 
                                    ' condition: ' + XMLHttpRequest.status);
	                	}
	                },
	                success: function(data) {
	                	var select = document.getElementById("listVersionOf");
	                	
	                	select.options[0] = new Option("Select Identifier", "SELECT");
	                    for(i in data.resourceNames){
	                    	select.options[select.options.length] = new Option(data.resourceNames[i], data.resourceNames[i]);
	                    	if(select.options[i].value == versionOf){
	                  			select.options[i].selected = true;
	                  			loadVersionIds(versionId);
	                    	}
	                    }
	                }
	            });
        	}
        }
        
        // Called when "Version Of" list is changed
        function loadVersionIds(versionId) {
        	document.getElementById("btnSave").style.visibility = 'hidden';
        	
        	var type = $('#listResourceType').val();
        	clearURIMapDetails();
        	clearIdentifiers();
        	
        	if(type == "CODE_SYSTEM") {
            	document.getElementById("listVersionID").style.visibility='visible';
	            $.ajax({
	                type: "GET",
	                url: serviceUrl + "all/" + $('#listResourceType').val() + "/" + $('#listVersionOf').val(),
	                dataType: 'json',
	                contentType: "application/json",
	                error: function(XMLHttpRequest, textStatus, errorThrown){
	                    if( XMLHttpRequest.status == '404'){
	                        alert("Error collecting data.");
	                    }
	                	else{
	                		alert('There was an ' + errorThrown +
                                    ' error due to a ' + textStatus + 
                                    ' condition: ' + XMLHttpRequest.status);
	                	}

	                },
	                success: function(data) {
	                	clearVersionIDList();
	                	var select = document.getElementById("listVersionID");
	                	select.options[0] = new Option("Select Version", "SELECT");
	                    for(i in data.versionIds){
	                    	select.options[select.options.length] = new Option(data.versionIds[i], data.versionIds[i]);
	                    	if(select.options[i].value == versionId){
	                  			select.options[i].selected = true;
	                  			loadIdentifiers();
	                    	}
	                    }
	                    if(select.options.length == 2){
	                    	select.options[1].selected = true;
                  			loadIdentifiers();	                    	
	                    }
	                }
	            });
        	}
        	else{
            	clearVersionIDList();
            	document.getElementById("listVersionID").style.visibility='hidden';
        		loadVersionIdentifiers();
                document.getElementById("btnAddIdentifier").style.visibility = 'visible';
        	}
        }
        
        function loadVersionIdentifiers(){
            $.ajax({
                type: "GET",
                url: serviceUrl + "versions/" + $('#listResourceType').val() + "/" + 
                    $('#listVersionOf').val(),
                dataType: 'json',
                contentType: "application/json",
                error: function(XMLHttpRequest, textStatus, errorThrown){
                    if( XMLHttpRequest.status == '404'){
                        alert("No URI Map with identifier " + $('#listVersionID').val() + " was found.");
                    }
                	else{
                		alert('There was an ' + errorThrown +
                                ' error due to a ' + textStatus + 
                                ' condition: ' + XMLHttpRequest.status);
                	}
                },
                success: function(data) {
                    setIds(data);
                }
            });
        }

        // Called when "Version ID" list is changed
        function loadIdentifiers(){
        	var type = $('#listResourceType').val();
        	var id = escape($('#listVersionOf').val());
        	var vID = escape($('#listVersionID').val());
        	var restURL = serviceUrl + "version/" + type + "/" + id + "/" + vID;
        	document.getElementById("btnSave").style.visibility = 'hidden';
        	
            $.ajax({
                type: "GET",
                url: restURL,
                dataType: 'json',
                contentType: "application/json",
                error: function(XMLHttpRequest, textStatus, errorThrown){
                    if( XMLHttpRequest.status == '404'){
                        alert("No URI Map with identifier " + vID + " was found.");
                    }
                	else{
                		alert('There was an ' + errorThrown +
                                ' error due to a ' + textStatus + 
                                ' condition: ' + XMLHttpRequest.status);
                	}
                },
                success: function(data) {
                    setIds(data);
                    document.getElementById("btnAddIdentifier").style.visibility = 'visible';
                }
            });
        }

        function setIds(data) {
            $.ajax({
                type: "GET",
                url: serviceUrl + "versions/" + data.resourceType + "/" + data.resourceName,
                dataType: 'json',
                contentType: "application/json",
                success: function(data) {
                    $('#inputUriMapResourceType').val(data.resourceType);
                    $('#inputUriMapVersionName').val(data.resourceName);
                    $('#inputUriMapVersionUri').val(data.resourceURI);
                    $('#inputUriMapVersionOf').val(data.versionOf);

					clearIdentifiers();                    
                    for(i in data.ids){
                        addIdentifier(data.ids[i]);
                    }
                }
            });
        }

        function addIdentifier(textValue) {

            // create the new element via clone(), and manipulate it's ID using newNum value
            var newElem = $('#divToClone').find('.clonedInput').clone();
            newElem.find(".identifierInput").val(textValue);


            // insert the new element after the last "duplicatable" input field
            $('#identifiers').append(newElem);

            newElem.find('.btnDel').click(function() {
                newElem.remove();
                document.getElementById("btnSave").style.visibility = 'visible';
            });
        }
        
        function identifierChanged() {
            document.getElementById("btnSave").style.visibility = 'visible';
        }
        
        function createAndSendJSON(){
            var json = {
                    resourceType : $('#inputUriMapResourceType').val(),
                    resourceName : $('#inputUriMapVersionName').val(),
                    resourceURI : $('#inputUriMapVersionUri').val(),
                    versionOf : $('#inputUriMapVersionOf').val(),
                    identifiers : []
                };

                $('.identifierInput').each(function(){
                    var val = $(this).val();
                    if(val && val != ''){
                        json.identifiers.push($(this).val());
                    }
                });

                $.ajax({
                    type: "PUT",
                    url: serviceUrl + "versions/" + json.resourceType + "/" + json.resourceName,
                   // dataType: 'json',
                    contentType: "application/json",
                    data: JSON.stringify(json),
                    success: function(data) {
                        alert("Saved");
                        resetForm();
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        alert("ERROR: " + jqXHR.responseText);
                    }
                });
        }
        
        $(document).ready(function() {
	
            $('#btnAddIdentifier').click(function() {
                addIdentifier();
                document.getElementById("btnSave").style.visibility = 'visible';

                return false;
            });
 
            $('#btnClearAll').click(function() {
            	clearAll();
                return false;
            });

            $('#btnSave').click(function() {
            	if($('#inputUriMapVersionName').val() == ""){
            		alert("Nothing to save. Please select a map using option lists above.");
            	}
            	else {
            		createAndSendJSON();
            	}
                
                return false;
            }); 
            
            document.getElementById("btnAddIdentifier").style.visibility = 'hidden';
            document.getElementById("btnSave").style.visibility = 'hidden';
            
        });
    </script>
</head>
 
<body>
<h1>CTS2 URI Resolver - Version Data</h1>
 
<form id="myForm" class="registration">
    <fieldset style="float:none !important">
        <legend>Load a Version Map</legend>
        <label>Resource Type: </label>
                        <select name="listResourceType" id="listResourceType" onchange="return loadVersionOf();" >
                        	<option value="SELECT_OPTION">Select Resource Type</option>
                            <option value="CODE_SYSTEM">CODE_SYSTEM</option>
                            <option value="CODE_SYSTEM_VERSION">CODE_SYSTEM_VERSION</option>
                            <option value="VALUE_SET">VALUE_SET</option>
                        </select>
        <br/>
        <label>Version Of: </label>
        				<select name="listVersionOf" id="listVersionOf"  onchange="return loadVersionIds();">
        				</select>
        <br/>
        <label>Version ID: </label>
        			 	<select name="listVersionID" id="listVersionID" onchange="return loadIdentifiers();">
        			 	</select>
        <br/>
        <br/>
    </fieldset>

    <fieldset>
        <legend>URI Map Details</legend>
<!--     <label>Resource Type: </label> -->
<!--                     <select name="inputUriMapResourceType" id="inputUriMapResourceType" > -->
<!--                       <option value="SELECT"></option> -->
<!--                       <option value="CODE_SYSTEM">CODE_SYSTEM_VERSION</option> -->
<!--                       <option value="CODE_SYSTEM_VERSION">CODE_SYSTEM_VERSION</option> -->
<!--                       <option value="MAP_VERSION">MAP_VERSION</option> -->
<!--                     </select> -->
	<label>Resource Type: </label><input type="text" name="inputUriMapResourceType" id="inputUriMapResourceType" readonly />
    <br/>
    <label>Version Name: </label><input type="text" name="inputUriMapVersionName" id="inputUriMapVersionName" readonly />
    <br/>
    <label>Version URI: </label><input type="text" name="inputUriMapVersionUri" id="inputUriMapVersionUri" readonly />
    <br/>
    <label>Version Of: </label><input type="text" name="inputUriMapVersionOf" id="inputUriMapVersionOf" readonly />

		</fieldset>

    <br/>
    <fieldset style="float:none !important">
        <div id="identifiers" ></div>
        <button id="btnAddIdentifier" class="button" value="Add Identifier">Add Identifier</button>
    </fieldset>
     
            
    <fieldset> 
        <button id="btnClearAll" class="button" value="clear">Clear All</button>
        <button id="btnSave" class="button" value="save" style="background-color:#e5004f">Save</button>
    </fieldset>
   
</form>
 
    <div id="divToClone" style='visibility:hidden'>
         <div class="clonedInput">
            <label>Identifier: </label>
            <input type="text" class="identifierInput" onkeypress="identifierChanged()"/>
            <button value="Remove Identifier" class="btnDel button">Remove Identifier</button>
        </div>
    </div>
    
    
	<br/><br/>    
	<c:url value="/j_spring_security_logout" var="logoutUrl" />
    <h4 align="center">
    	<a href="identifierEdit">Edit Identifier Data</a> |
	    <a href="../public/examples">Example Public Queries</a> | 
		<a href="${logoutUrl}">Log Out</a>
    </h4>

</body>
</html>