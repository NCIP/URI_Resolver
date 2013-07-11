<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
                    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title></title>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.3/jquery.min.js"></script>

    <link rel="stylesheet" href="../style.css" type="text/css" />

    <script type="text/javascript">

        var oldResourceName = null;

        var serviceUrl = "";


        function clearURIMapDetails() {
            $('input').each(function(){
                $(this).val("");
            });        	
        }
        
        function clearIdentifiers(){
        	$('#identifiers').empty();
        }
        
        function clearURIMapIdentifiersList(){
            document.getElementById("listURIMapIdentifiers").options.length = 0;
        }

        function clearForm() {
        	clearURIMapIdentifiersList();
        	clearURIMapDetails();
        	clearIdentifiers();
        }
        
        function clearAll() {
        	clearForm();
            document.getElementById("listResourceType").options[0].selected = true;
        }

        function resetList(selectList, value){
        	for(var waiting=0; waiting < 2000; waiting++){
        		if(selectList.options.length > 0){
                	for(var i=0; i < selectList.options.length; i++){
                		if(selectList.options[i].value == value){
                			selectList.options[i].selected = true;
                			selectList.onchange();
                			break;
                		}
                	}
                	break;
        		}
        		else{
        			setTimeout(function(){}, 100);
        		}
        	}
        }
        
        function resetForm() {
        	var selectResourceTypes = document.getElementById("listResourceType");
        	var selectMapIdentifiers = document.getElementById("listURIMapIdentifiers");
        	
        	var resourceTypeVal = selectResourceTypes[selectResourceTypes.selectedIndex].value;        	
            var mapIdentifiersVal = selectMapIdentifiers[selectMapIdentifiers.selectedIndex].value;

            clearAll();
          	resetList(selectResourceTypes, resourceTypeVal);
          	setTimeout(function(){resetList(selectMapIdentifiers, mapIdentifiersVal);}, 500);
        }
        
        // Called when "Resource Type" list is changed
	    function loadURIMapIdentifiers(){
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
	                	var select = document.getElementById("listURIMapIdentifiers");
	                	
	                	select.options[0] = new Option("Select Identifier", "SELECT");
	                    for(i in data.resourceNames){
	                    	select.options[select.options.length] = new Option(data.resourceNames[i], data.resourceNames[i]);
	                    }
	                }
	            });
        	}
        }
	    
        // Called when "Identifiers" list is changed
	    function loadIdentifiers(){
            $.ajax({
                type: "GET",
                url: serviceUrl + "id/" + $('#listResourceType').val() + "?id=" + $('#listURIMapIdentifiers').val(),
                dataType: 'json',
                contentType: "application/json",
                error: function(XMLHttpRequest, textStatus, errorThrown){
                	alert("ERROR Using URL: " + serviceUrl + "id/" + $('#listResourceType').val() + "?id=" + $('#listURIMapIdentifiers').val());
                    if( XMLHttpRequest.status == '404'){
                        alert("No URI Map with identifier " + $('#listURIMapIdentifiers').val() + " was found.");
                    }
                },
                success: function(data) {
                    setIds(data);
                }
            });
            return false;
	    }

        function setIds(data) {
            $.ajax({
                type: "GET",
                url: serviceUrl + "ids/" + data.resourceType + "/" + data.resourceName,
                dataType: 'json',
                contentType: "application/json",
                error: function(XMLHttpRequest, textStatus, errorThrown){
                    if( XMLHttpRequest.status == '404'){
                        alert("No URI Map with identifier " + data.resourceName + " was found.");
                    }
                },
                success: function(data) {
                    oldResourceName = data.resourceName;

                    $('#inputUriMapResourceType').val(data.resourceType);
                    $('#inputUriMapResourceName').val(data.resourceName);
                    $('#inputUriMapResourceUri').val(data.resourceURI);
                    $('#inputUriMapBaseEntityUri').val(data.baseEntityURI);

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

		function createAndSendJSON() {
	        var json = {
	                resourceType : $('#inputUriMapResourceType').val(),
	                resourceName : $('#inputUriMapResourceName').val(),
	                resourceURI : $('#inputUriMapResourceUri').val(),
	                baseEntityURI : $('#inputUriMapBaseEntityUri').val(),
	                identifiers : []
	            };

//	            if(oldResourceName != null && json.resourceName != oldResourceName){
//	                json.oldResourceName = oldResourceName;
//	            }

	            $('.identifierInput').each(function(){
	                var val = $(this).val();
	                if(val && val != ''){
	                    json.identifiers.push($(this).val());
	                }
	            });

	            $.ajax({
	                type: "PUT",
	                url: serviceUrl + "ids/" + json.resourceType + "/" + json.resourceName,
	//                dataType: 'json',
	                contentType: "application/json",
	                data: JSON.stringify(json),
	                success: function(data) {
	  //                  oldResourceName = json.resourceName;
	                    alert("Saved");
	                    resetForm();
	                },
	                error: function(jqXHR, textStatus, errorThrown) {
	                    alert("ERROR: " + jqXHR.responseText);
	                }
	            });
		}        
        
        $(document).ready(function() {

            $('#btnAdd').click(function() {
                addIdentifier();
                return false;
            });
 
            $('#btnClearAll').click(function() {
            	clearAll();
                return false;
            });

            $('#btnSave').click(function() {
            	if($('#inputUriMapResourceName').val() == ""){
            		alert("Nothing to save. Please select a map using option lists above.");
            	}
            	else {
            		createAndSendJSON();
            	}

                return false;
            });
        });
    </script>
</head>
 
<body>
 
<form id="myForm" class="registration">
    <fieldset style="float:none !important">
        <legend>Load a URI Map</legend>
        <label>Resource Type: </label>
                        <select name="listResourceType" id="listResourceType" onchange="return loadURIMapIdentifiers();" >
                            <option value="SELECT_OPTION">Select Resource Type</option>
                            <option value="CODE_SYSTEM">CODE_SYSTEM</option>
                            <option value="CODE_SYSTEM_VERSION">CODE_SYSTEM_VERSION</option>
                            <option value="VALUE_SET">VALUE_SET</option>
                        </select>
        <br/>
        <label>Identifier: </label>
        	<select name="listURIMapIdentifiers" id="listURIMapIdentifiers" onchange="return loadIdentifiers();">
        	</select>
        <br/>
        <br/>
    </fieldset>

    <fieldset>
        <legend>URI Map Details</legend>
    <label>Resource Type: </label>
                    <select name="inputUriMapResourceType" id="inputUriMapResourceType" >
                      <option value="CODE_SYSTEM">CODE_SYSTEM</option>
                      <option value="VALUE_SET">VALUE_SET</option>
                    </select>
    <br/>
    <label>Resource Name: </label><input type="text" name="inputUriMapResourceName" id="inputUriMapResourceName" />
    <br/>
    <label>Resource URI: </label><input type="text" name="inputUriMapResourceUri" id="inputUriMapResourceUri" />
    <br/>
    <label>Base Entity URI: </label><input type="text" name="inputUriMapBaseEntityUri" id="inputUriMapBaseEntityUri" />
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
	<br/><br/>    
	<a href="<c:url value="j_spring_security_logout" />" > Logout</a>

</body>
</html>