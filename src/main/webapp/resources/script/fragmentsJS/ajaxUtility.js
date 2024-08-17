
function getData(url,dataType,callbackFunction){
   $.ajax({
       type: "GET",
       url: url,
       dataType: dataType,
       crossDomain:true,

       //if received a response from the server
       success: (data, textStatus, jqXHR) => {
           if(jqXHR.status == 200){
               callbackFunction(data);
           }else{
               //error
               alert("Something went wrong with "+getContextPath(url)+",","Error IN ELSE!");
           }
       },

       //If there was no response from the server
       error: (data, jqXHR, textStatus, errorThrown) => {

           closeProgressBar("progressBarDIV");

           alert(errorThrown != '' ? errorThrown : "Something went wrong with "+getContextPath(url)+" ,","Error3!");
           //error message
       },

       //capture the request before it was sent to server
       beforeSend: (jqXHR, settings) => {
           //disable the button until we get the response
       },

       //this is called after the response or error functions are finished
       //so that we can take some action
       complete: (jqXHR, textStatus) => {
           //enable the button
       }
   });
}

function deleteData(url, data, dataType, callbackFunction){
    $.ajax({
        type: "DELETE",
        url: url,
        data:data,
        crossDomain:true,
        dataType: dataType,
        contentType: "application/json",

        //if received a response from the server
        success: (data, textStatus, jqXHR) => {
            if(jqXHR.status == 200){
                callbackFunction(data);
            }else{
                alert("Something went wrong with"+getContextPath(url)+" , ","Error!");
            }
        },

        //If there was no response from the server
        error: (jqXHR, textStatus, errorThrown) => {
            closeProgressBar("progressBarDIV");
            alert(errorThrown != '' ? errorThrown : "Something went wrong with"+getContextPath(url)+" , ","Error!");
        },

        //capture the request before it was sent to server
        beforeSend: (jqXHR, settings) => {
            //disable the button until we get the response
        },

        //this is called after the response or error functions are finished
        //so that we can take some action
        complete: (jqXHR, textStatus) => {
            //enable the button
        }

    });
}

function postData(url,data,dataType,callbackFunction){
    $.ajax({
        type: "POST",
        url: url,
        crossDomain:true,
        data: data,
        dataType: dataType,
        contentType: "application/json",

        //if received a response from the server
        success: (data, textStatus, jqXHR) => {
            if(jqXHR.status == 200){
                callbackFunction(data);
            }else{
                alert("Something went wrong with "+getContextPath(url)+" , ","Error!");

            }
        },

        //If there was no response from the server
        error: (jqXHR, textStatus, errorThrown) => {
            closeProgressBar("progressBarDIV");
            //error message
            alert(errorThrown != '' ? errorThrown : "Something went wrong with "+getContextPath(url)+" , ","Error!");
        },

        //capture the request before it was sent to server
        beforeSend: (jqXHR, settings) => {
            //disable the button until we get the response
        },

        //this is called after the response or error functions are finished
        //so that we can take some action
        complete: (jqXHR, textStatus) => {
            //enable the button
        }
    });
}

function getContextPath(url){
    var urlsub = url.split('://')[1];
    var slashIndex1 = urlsub.indexOf("/");
    var slashIndex2 = urlsub.indexOf("/",slashIndex1 + 1);
    return urlsub.substring(slashIndex1,slashIndex2);
}