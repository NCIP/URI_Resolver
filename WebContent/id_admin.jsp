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

        $(document).ready(function() {

            $('#btnAdd').click(function() {
                addIdentifier();

                return false;
            });
 
            $('#btnClearAll').click(function() {
                $('input').each(function(){
                    $(this).val("");
                });
                $('#identifiers').empty();

                return false;
            });

            $('#btnLoad').click(function() {
                $.ajax({
                    type: "GET",
                    url: serviceUrl + "id/" + $('#resourceTypeLoad').val() + "?id=" + $('#identifierLoad').val(),
                    dataType: 'json',
                    contentType: "application/json",
                    error: function(XMLHttpRequest, textStatus, errorThrown){
                    	alert("ERROR Using URL: " + serviceUrl + "id/" + $('#resourceTypeLoad').val() + "?id=" + $('#identifierLoad').val());
                        if( XMLHttpRequest.status == '404'){
                            alert("No URI Map with identifier " + $('#identifierLoad').val() + " was found.");
                        }
                    },
                    success: function(data) {
                        setIds(data);
                    }
                });
                return false;
            });

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

                        $('#resourceType').val(data.resourceType);
                        $('#resourceName').val(data.resourceName);
                        $('#resourceUri').val(data.resourceURI);
                        $('#baseEntityUri').val(data.baseEntityURI);

                        $('#identifiers').empty();
                        for(i in data.identifiers){
                            addIdentifier(data.identifiers[i]);
                        }
                    }
                });
            }

            $('#btnSave').click(function() {
                var json = {
                    resourceType : $('#resourceType').val(),
                    resourceName : $('#resourceName').val(),
                    resourceURI : $('#resourceUri').val(),
                    baseEntityURI : $('#baseEntityUri').val(),
                    identifiers : []
                };

                if(oldResourceName != null && json.resourceName != oldResourceName){
                    json.oldResourceName = oldResourceName;
                }

                $('.identifierInput').each(function(){
                    var val = $(this).val();
                    if(val && val != ''){
                        json.identifiers.push($(this).val());
                    }
                });

                $.ajax({
                    type: "PUT",
                    url: serviceUrl + "ids/" + json.resourceType + "/" + json.resourceName,
                    dataType: 'json',
                    contentType: "application/json",
                    data: JSON.stringify(json),
                    success: function(data) {
                        oldResourceName = json.resourceName;
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
        <legend>Load a URI Map</legend>
        <label>Identifier: </label><input type="text" name="identifierLoad" id="identifierLoad" />
        <br/>
        <label>Resource Type: </label>
                        <select name="resourceTypeLoad" id="resourceTypeLoad" >
                            <option value="CODE_SYSTEM">CODE_SYSTEM</option>
                            <option value="CODE_SYSTEM_VERSION">CODE_SYSTEM_VERSION</option>
                            <option value="VALUE_SET">VALUE_SET</option>
                        </select>
        <br/>
        <button id="btnLoad" class="button" value="Load" >Load</button>
    </fieldset>

    <fieldset>
        <legend>URI Map Details</legend>
    <label>Resource Name: </label><input type="text" name="resourceName" id="resourceName" />
    <br/>
    <label>Resource URI: </label><input type="text" name="resourceUri" id="resourceUri" />
    <br/>
    <label>Resource Type: </label>
                    <select name="resourceType" id="resourceType" >
                      <option value="CODE_SYSTEM">CODE_SYSTEM</option>
                      <option value="VALUE_SET">VALUE_SET</option>
                    </select>
    <br/>
    <label>Base Entity URI: </label><input type="text" name="baseEntityUri" id="baseEntityUri" />
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