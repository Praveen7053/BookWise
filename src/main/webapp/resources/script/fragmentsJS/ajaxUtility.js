
function getData(url,dataType,callbackFunction){
   $.ajax({
       type: "GET",
       url: url,
       dataType: dataType,
       crossDomain:true,

       //if received a response from the server
       success: (data, textStatus, jqXHR) => {
           if (jqXHR.status >= 200 && jqXHR.status < 300) {
               callbackFunction(data);
           }else{
               //error
               alert("Something went wrong,","Error IN ELSE!");
           }
       },

       //If there was no response from the server
       error: (data, jqXHR, textStatus, errorThrown) => {

           closeProgressBar("progressBarDiv", "bodyDiv");

           alert(errorThrown != '' ? errorThrown : "Something went wrong,","Error3!");
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

function deleteData(url, data, dataType, successCallback, errorCallback) {
    $.ajax({
        type: "DELETE",
        url: url,
        data: data,
        dataType: dataType,
        contentType: "application/json",
        crossDomain: true,
        success: successCallback,
        error: function(jqXHR, textStatus, errorThrown) {
            closeProgressBar("progressBarDiv", "bodyDiv");
            // If a custom error handler is provided, use it. Otherwise, use the default alert.
            if (typeof errorCallback === 'function') {
                errorCallback(jqXHR, textStatus, errorThrown);
            } else {
                alert(errorThrown || "Something went wrong", "Error!");
            }
        }
    });
}

function postFormData(url, data, callbackFunction) {
    $.ajax({
        type: "POST",
        url: url,
        data: data, // form-urlencoded
        success: function (response, textStatus, jqXHR) {
            if (jqXHR.status >= 200 && jqXHR.status < 300) {
                callbackFunction(response);
            } else {
                alert("Something went wrong", "Error!");
            }
        },
        error: function () {
            closeProgressBar("progressBarDiv", "bodyDiv");
            alert("Something went wrong", "Error!");
        }
    });
}

/**
 * POST data to url.
 * @param {string} url - Request URL
 * @param {string|FormData} data - JSON string or FormData (for file uploads)
 * @param {string} dataType - Response type: 'json' or 'text'
 * @param {function} callbackFunction - Success callback(response)
 * @param {function} [errorCallback] - Optional error callback(jqXHR, textStatus, errorThrown)
 */
function postData(url, data, dataType, callbackFunction, errorCallback) {

    let ajaxOptions = {
        type: "POST",
        url: url,
        crossDomain: true,
        dataType: dataType,
        cache: false,

        success: (response, textStatus, jqXHR) => {
            if (jqXHR.status >= 200 && jqXHR.status < 300) {
                callbackFunction(response);
            } else {
                if (typeof errorCallback === 'function') {
                    errorCallback(jqXHR, textStatus, 'Something went wrong');
                } else {
                    closeProgressBar("progressBarDiv", "bodyDiv");
                    alert("Something went wrong", "Error!");
                }
            }
        },

        error: (jqXHR, textStatus, errorThrown) => {
            closeProgressBar("progressBarDiv", "bodyDiv");
            if (typeof errorCallback === 'function') {
                errorCallback(jqXHR, textStatus, errorThrown || "Something went wrong");
            } else {
                alert(errorThrown ? errorThrown : "Something went wrong", "Error!");
            }
        },

        beforeSend: (jqXHR, settings) => {},
        complete: (jqXHR, textStatus) => {}
    };

    if (data instanceof FormData) {
        ajaxOptions.data = data;
        ajaxOptions.processData = false;
        ajaxOptions.contentType = false;
        ajaxOptions.timeout = 300000; // 5 minutes for file uploads
    } else {
        ajaxOptions.data = data;
        ajaxOptions.contentType = "application/json";
    }

    $.ajax(ajaxOptions);
}

function postPdfBlob(url, data, successCallback, errorCallback) {
    $.ajax({
        type: "POST",
        url: url,
        data: data,
        processData: false,
        contentType: false,
        crossDomain: true,
        xhr: function() {
            var xhr = new window.XMLHttpRequest();
            xhr.responseType = 'blob';
            return xhr;
        },
        success: function(data, textStatus, jqXHR) {
            successCallback(data);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            if (typeof errorCallback === 'function') {
                errorCallback(jqXHR, textStatus, errorThrown);
            } else {
                alert(errorThrown || "Something went wrong", "Error!");
            }
        }
    });
}

function getContextPath(url){
    var urlsub = url.split('://')[1];
    var slashIndex1 = urlsub.indexOf("/");
    var slashIndex2 = urlsub.indexOf("/",slashIndex1 + 1);
    return urlsub.substring(slashIndex1,slashIndex2);
}