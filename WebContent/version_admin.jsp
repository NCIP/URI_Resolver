<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
                    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title></title>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.3/jquery.min.js"></script>

    <link rel="stylesheet" href="../style.css" type="text/css" />

    <script type="text/javascript">

        var serviceUrl = "";

        function clearForm() {
            $('input').each(function(){
                $(this).val("");
            });
            $('#identifiers').empty();
        	document.getElementById("identifierLoad").options.length = 0;
        	document.getElementById("versionIdentifierLoad").options.length = 0;
        	document.getElementById("versionIdentifierLoad").style.visibility='visible';
        }
        
        function setIds(data) {
            $.ajax({
                type: "GET",
                url: serviceUrl + "versions/" + data.resourceType + "/" + data.resourceName,
                dataType: 'json',
                contentType: "application/json",
                success: function(data) {
                    $('#resourceType').val(data.resourceType);
                    $('#resourceName').val(data.resourceName);
                    $('#resourceUri').val(data.resourceURI);
                    $('#versionOf').val(data.versionOf);

                    $('#identifiers').empty();
                    for(i in data.identifiers){
                        addIdentifier(data.identifiers[i]);
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
            });
        }
        
        function loadVersionOf() {
        	if(document.getElementById("resourceTypeLoad").selectedIndex == 0) {
        		clearForm();
        	}
        	else{
	            $.ajax({
	                type: "GET",
	                url: serviceUrl + "all/" + $('#resourceTypeLoad').val(),
	                dataType: 'json',
	                contentType: "application/json",
	                error: function(XMLHttpRequest, textStatus, errorThrown){
	                    if( XMLHttpRequest.status == '404'){
	                        alert("Error collecting data.");
	                    }
	                },
	                success: function(data) {
	                	clearForm();
	                	var select = document.getElementById("identifierLoad");
	                	
	                	select.options[0] = new Option("Select Identifier", "SELECT");
	                    for(i in data.resourceNames){
	                    	select.options[select.options.length] = new Option(data.resourceNames[i], data.resourceNames[i]);
	                    }
	                }
	            });
        	}
        }
        
        function loadVersionIds() {
        	if($('#resourceTypeLoad').val() == "CODE_SYSTEM") {
	            $.ajax({
	                type: "GET",
	                url: serviceUrl + "all/" + $('#resourceTypeLoad').val() + "/" + $('#identifierLoad').val(),
	                dataType: 'json',
	                contentType: "application/json",
	                error: function(XMLHttpRequest, textStatus, errorThrown){
	                    if( XMLHttpRequest.status == '404'){
	                        alert("Error collecting data.");
	                    }
	                },
	                success: function(data) {
	                	var select = document.getElementById("versionIdentifierLoad");
	                	select.options.length=0;
	                	select.options[0] = new Option("Select Version", "SELECT");
	                    for(i in data.versionIds){
	                    	select.options[select.options.length] = new Option(data.versionIds[i], data.versionIds[i]);
	                    }
	                }
	            });
        	}
        	else{
            	var select = document.getElementById("versionIdentifierLoad");
            	select.options.length=0;
            	document.getElementById("versionIdentifierLoad").style.visibility='hidden';
        		loadVersionIdentifiers();
        	}
        }
        
        function loadIdentifiers(){
        	var type = $('#resourceTypeLoad').val();
        	var id = escape($('#identifierLoad').val());
        	var vID = escape($('#versionIdentifierLoad').val());
        	var restURL = serviceUrl + "version/" + type + "/" + id + "/" + vID;
        	alert("restURL = " + restURL);
        	
            $.ajax({
                type: "GET",
                url: restURL,
                dataType: 'json',
                contentType: "application/json",
                error: function(XMLHttpRequest, textStatus, errorThrown){
                    if( XMLHttpRequest.status == '404'){
                        alert("No URI Map with identifier " + vID + " was found.");
                    }
                    else {
	                    alert("Unknown error");
                    }
                },
                success: function(data) {
                    setIds(data);
                }
            });
        }

        function loadVersionIdentifiers(){
            $.ajax({
                type: "GET",
                url: serviceUrl + "versions/" + $('#resourceTypeLoad').val() + "/" + 
                    $('#identifierLoad').val(),
                dataType: 'json',
                contentType: "application/json",
                error: function(XMLHttpRequest, textStatus, errorThrown){
                    if( XMLHttpRequest.status == '404'){
                        alert("No URI Map with identifier " + $('#versionIdentifierLoad').val() + " was found.");
                    }
                },
                success: function(data) {
                    setIds(data);
                }
            });
        }

        $(document).ready(function() {
	
            $('#btnAdd').click(function() {
                addIdentifier();

                return false;
            });
 
            $('#btnClearAll').click(function() {
            	clearForm();
                return false;
            });

            $('#btnSave').click(function() {
                var json = {
                    resourceType : $('#resourceType').val(),
                    resourceName : $('#resourceName').val(),
                    resourceURI : $('#resourceUri').val(),
                    versionOf : $('#versionOf').val(),
                    identifiers : []
                }

                $('.identifierInput').each(function(){
                    var val = $(this).val();
                    if(val && val != ''){
                        json.identifiers.push($(this).val())
                    }
                });

                $.ajax({
                    type: "PUT",
                    url: serviceUrl + "versions/" + json.resourceType + "/" + json.resourceName,
                    dataType: 'json',
                    contentType: "application/json",
                    data: JSON.stringify(json),
                    success: function(data) {
                        alert("Saved");
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        alert("ERROR: " + jqXHR.responseText);
                    }
                });

                return false;
            }); 
            
        });
    </script>
</head>
 
<body>
 
<form id="myForm" class="registration">
    <fieldset style="float:none !important">
        <legend>Load a Version Map</legend>
        <label>Resource Type: </label>
                        <select name="resourceTypeLoad" id="resourceTypeLoad" onchange="return loadVersionOf();" >
                        	<option value="SELECT_OPTION">Select Resource Type</option>
                            <option value="CODE_SYSTEM">CODE_SYSTEM</option>
                            <option value="CODE_SYSTEM_VERSION">CODE_SYSTEM_VERSION</option>
                            <option value="VALUE_SET">VALUE_SET</option>
                        </select>
        <br/>
        <label>Version Of: </label>
        				<select name="identifierLoad" id="identifierLoad"  onchange="return loadVersionIds();">
        				</select>
        <br/>
        <label>Version ID: </label>
        			 	<select name="versionIdentifierLoad" id="versionIdentifierLoad" onchange="return loadIdentifiers();">
        			 	</select>
        <br/>
        <br/>
    </fieldset>

    <fieldset>
        <legend>URI Map Details</legend>
    <label>Version Name: </label><input type="text" name="resourceName" id="resourceName" />
    <br/>
    <label>Version URI: </label><input type="text" name="resourceUri" id="resourceUri" />
    <br/>
    <label>Resource Type: </label>
                    <select name="resourceType" id="resourceType" >
                      <option value="CODE_SYSTEM_VERSION">CODE_SYSTEM_VERSION</option>
                      <option value="MAP_VERSION">MAP_VERSION</option>
                    </select>
    <br/>
    <label>Version Of: </label><input type="text" name="versionOf" id="versionOf" />

    </fieldset>

    <br/>
    <fieldset style="float:none !important">
        <div id="identifiers" ></div>
        <button id="btnAdd" class="button" value="Add Identifier">Add Identifier</button>
    </fieldset>
     
            
    <fieldset> 
        <button id="btnSave" class="button" value="save">Save</button>
        <button id="btnClearAll" class="button" value="clear">Clear All</button>
    </fieldset>
   
</form>
 
    <div id="divToClone" style='visibility:hidden'>
         <div class="clonedInput">
            <label>Identifier: </label>
            <input type="text" class="identifierInput"/>
            <button value="Remove Identifier" class="btnDel button">Remove Identifier</button>
        </div>
    </div>

</body>
</html>