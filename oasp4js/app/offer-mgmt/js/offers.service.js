angular.module('app.offer-mgmt').factory('offers', function (offerManagementRestService) {
    'use strict';
    return {
        loadAllOffers: function () {
            return offerManagementRestService.getAllOffers().then(function (response) {
                return response.data;
            });
        },
        loadAllProducts: function () {
            return offerManagementRestService.getAllProducts().then(function (response) {
                return response.data;
            });
        }
    };
});
