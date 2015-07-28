angular.module('app.sales-mgmt').factory('salesManagementRestService', function ($http, currentContextPath) {
    'use strict';

    var servicePath = currentContextPath.get() + 'services/rest/salesmanagement/v1';

    return {
        findOrders: function (params) {
            return $http.post(servicePath + '/order/search', params);
        },
        updateOrder: function (order, orderId) {
            return $http.put(servicePath + '/order/' + orderId, order);
        },
        createOrder: function (order) {
            return $http.post(servicePath + '/order', order);
        },
        updateOrderPosition: function (orderPosition) {
            return $http.post(servicePath + '/orderposition', orderPosition);
        },
        findOrderPositions: function (params) {
            return $http.get(servicePath + '/orderposition', {
                params: params
            });
        }
    };
});
