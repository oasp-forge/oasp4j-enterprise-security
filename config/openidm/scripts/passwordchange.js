/*global  objectID*/

(function () {
    var params = {
 "userId" : objectID,
 "emailEnabled" : "true",
 "_key": "passwordChangeReminder"
};
 
openidm.create('workflow/processinstance', null, params);
    
    return true; // return true to indicate successful completion
}());
