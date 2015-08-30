angular.module("app",["ui.select","ngRoute","app.main","app.table-mgmt","app.offer-mgmt","app.sales-mgmt"]).config(["$locationProvider","uiSelectConfig",function(e,t){"use strict";e.html5Mode(!1),t.theme="bootstrap"}]).run(["globalSpinner",function(e){"use strict";e.showOnRouteChangeStartAndHideWhenComplete()}]),angular.module("app.main",["ngRoute","oasp.oaspUi","oasp.oaspSecurity","app.main.templates","oasp.oaspI18n","ui.bootstrap"]).constant("SIGN_IN_DLG_PATH","/main/sign-in").config(["SIGN_IN_DLG_PATH","$routeProvider","oaspTranslationProvider",function(e,t,n){"use strict";t.when("/",{templateUrl:"main/html/blank.html",controller:"RedirectorCntl"}).when(e,{templateUrl:"main/html/sign-in.html",controller:"SignInCntl",resolve:{check:["homePageRedirector",function(e){return e.rejectAndRedirectToHomePageIfUserLoggedIn()}]}}).otherwise({templateUrl:"main/html/page-not-found.html"}),n.enableTranslationForModule("main",!0),n.setSupportedLanguages([{key:"en",label:"English","default":!0},{key:"de",label:"German"}])}]),angular.module("app.main").factory("appContext",["oaspSecurityService","$q",function(e,t){"use strict";var n={isLoggedIn:!1},a=function(e){return{isLoggedIn:function(){return e.isLoggedIn},getUserName:function(){var t="";return e.profile&&e.profile.firstName&&e.profile.lastName&&(t=e.profile.firstName+" "+e.profile.lastName),t},getUserId:function(){return e.profile&&e.profile.id},getHomeDialogPath:function(){return e.profile&&e.profile.homeDialogPath||""}}},o=function(e){n.isLoggedIn=!0,n.profile=e,angular.isUndefined(e.homeDialogPath)&&(n.profile.homeDialogPath="WAITER"===e.role?"/table-mgmt/table-search":"COOK"===e.role?"/sales-mgmt/cook-positions":"/table-mgmt/table-search")},r=function(){n.isLoggedIn=!1,n.profile=void 0};return{getCurrentUser:function(){var i=t.defer();return e.getCurrentUserProfile().then(function(e){e?o(e):r(),i.resolve(a(n))}),i.promise},onLoggingIn:function(e){o(e)},onLoggingOff:function(){r()}}}]),angular.module("app.main").controller("AppCntl",["SIGN_IN_DLG_PATH","$scope","$location","$window","appContext","oaspSecurityService","globalSpinner",function(e,t,n,a,o,r,i){"use strict";o.getCurrentUser().then(function(e){t.currentUser=e}),t.logOff=function(){var t=function(){n.path(e),a.location.href=n.absUrl(),a.location.reload()};i.decorateCallOfFunctionReturningPromise(function(){return r.logOff()}).then(function(){t()})}}]),angular.module("app.main").factory("authenticator",["$modal",function(e){"use strict";return{execute:function(){return e.open({templateUrl:"main/html/sign-in-modal.html",backdrop:"static",keyboard:!1,controller:"SignInModalCntl",size:"sm"}).result}}}]),angular.module("app.main").factory("currentContextPath",["$window",function(e){"use strict";var t="";return{get:function(){var n,a,o,r=t?!1:!0;return r&&(t="/",n=e.location.pathname,n&&(a=n.split("/"),a.length>1&&(o=a[1],o&&(t+=o+"/")))),t}}}]),angular.module("app.main").factory("homePageRedirector",["$location","appContext","$q",function(e,t,n){"use strict";return{rejectAndRedirectToHomePageIfUserLoggedIn:function(){var a=n.defer();return t.getCurrentUser().then(function(t){var n;t.isLoggedIn()?(a.reject(),n=t.getHomeDialogPath(),e.url(n)):a.resolve()}),a.promise}}}]),angular.module("app.main").controller("RedirectorCntl",["SIGN_IN_DLG_PATH","$location","appContext",function(e,t,n){"use strict";n.getCurrentUser().then(function(n){var a;a=n.isLoggedIn()?n.getHomeDialogPath():e,t.url(a)})}]),angular.module("app.main").factory("securityRestService",["$http","currentContextPath",function(e,t){"use strict";var n=t.get()+"services/rest/";return{getCurrentUser:function(){return e.get(n+"security/v1/currentuser/")},getCsrfToken:function(){return e.get(n+"security/v1/csrftoken/")},login:function(t,a){return e.post(n+"login",{j_username:t,j_password:a})},logout:function(){return e.get(n+"logout")}}}]),angular.module("app.main").controller("SignInModalCntl",["$scope","signIn",function(e,t){"use strict";t(e,function(){e.$close()})}]),angular.module("app.main").controller("SignInCntl",["$scope","$location","appContext","signIn",function(e,t,n,a){"use strict";a(e,function(){n.getCurrentUser().then(function(e){t.url(e.getHomeDialogPath())})})}]),angular.module("app.main").factory("signIn",["oaspSecurityService","globalSpinner",function(e,t){"use strict";return function(n,a){n.errorMessage={text:"",hasOne:function(){return this.text?!0:!1},clear:function(){this.text=""}},n.credentials={},n.validation={userNameNotProvided:function(){return(n.loginForm.userName.$dirty||this.forceShowingValidationErrors)&&n.loginForm.userName.$error.required},passwordNotProvided:function(){return(n.loginForm.password.$dirty||this.forceShowingValidationErrors)&&n.loginForm.password.$error.required},forceShowingValidationErrors:!1},n.signIn=function(){var o=function(e){n.errorMessage.text=e,n.credentials={},n.validation.forceShowingValidationErrors=!1,n.loginForm.$setPristine()};n.loginForm.$invalid?n.validation.forceShowingValidationErrors=!0:t.decorateCallOfFunctionReturningPromise(function(){return e.logIn(n.credentials.username,n.credentials.password)}).then(function(){a()},function(){o("Authentication failed. Please try again!")})}}}]),angular.module("app.main.templates",[]).run(["$templateCache",function(e){e.put("main/html/blank.html","<!DOCTYPE html><html><body><div></div></body></html>"),e.put("main/html/page-not-found.html",'<!DOCTYPE html><html><body><div class="row"><div class="col-md-12"><h2>Page Not Found!</h2><p>The page you trying to reach could not be found.</p></div></div></body></html>'),e.put("main/html/sign-in-modal.html",'<div><form name="loginForm" novalidate=""><div class="modal-header"><h3 class="modal-title">Sign In</h3></div><div class="modal-body"><div class="row"><div class="col-md-12"><alert data-ng-show="errorMessage.hasOne()" data-close="errorMessage.clear()" data-type="danger"><span data-ng-bind="errorMessage.text"></span></alert><p><span class="label label-danger" data-ng-show="validation.userNameNotProvided()">Please enter your user name!</span> <input type="text" placeholder="User Name" name="userName" class="form-control" data-ng-model="credentials.username" data-ng-required="true"></p><p><span class="label label-danger" data-ng-show="validation.passwordNotProvided()">Please enter your password!</span> <input type="password" placeholder="Password" name="password" class="form-control" data-ng-model="credentials.password" data-ng-required="true"></p></div></div></div><div class="modal-footer"><button class="btn btn-success" data-ng-click="signIn()">Sign In</button> <button class="btn btn-warning" ng-click="$dismiss()">Cancel</button></div></form></div>'),e.put("main/html/sign-in.html",'<!DOCTYPE html><html><body><div class="row"><div class="row"><div class="col-md-8"><h1>This is the login to the devonfw restaurant example application as technical showcase for an AngularJS client.</h1><p><img src="main/img/login_logo.png"></p></div></div><div class="row"><div class="col-md-4"><h2>Sign In</h2><alert data-ng-show="errorMessage.hasOne()" data-close="errorMessage.clear()" data-type="danger"><span data-ng-bind="errorMessage.text"></span></alert><form name="loginForm" novalidate=""><p><span class="label label-danger" data-ng-show="validation.userNameNotProvided()">Please enter your user name!</span> <input type="text" placeholder="User Name" name="userName" class="form-control" data-ng-model="credentials.username" data-ng-required="true"></p><p><span class="label label-danger" data-ng-show="validation.passwordNotProvided()">Please enter your password!</span> <input type="password" placeholder="Password" name="password" class="form-control" data-ng-model="credentials.password" data-ng-required="true"></p><p><button type="submit" class="btn btn-success" data-ng-click="signIn()">Sign In</button></p></form></div></div></div></body></html>')}]),angular.module("oasp",["oasp.oaspUi","oasp.oaspSecurity","oasp.oaspI18n"]),angular.module("oasp.oaspSecurity",[]).config(["$httpProvider",function(e){"use strict";e.interceptors.push("oaspSecurityInterceptor")}]).run(["oaspSecurityService",function(e){"use strict";e.checkIfUserIsLoggedInAndIfSoReinitializeAppContext()}]),angular.module("oasp.oaspSecurity").factory("oaspSecurityInterceptor",["$q","oaspUnauthenticatedRequestResender",function(e,t){"use strict";return{responseError:function(n){var a;return 403===n.status?(a=n.config,t.addRequest(a)):e.reject(n)}}}]),angular.module("oasp.oaspSecurity").provider("oaspSecurityService",function(){"use strict";var e={securityRestServiceName:"securityRestService",appContextServiceName:"appContext"};return{setSecurityRestServiceName:function(t){e.securityRestServiceName=t||e.securityRestServiceName},setAppContextServiceName:function(t){e.appContextServiceName=t||e.appContextServiceName},$get:["$injector","$http","$q",function(t,n,a){var o={set:function(e,t){this.headerName=e,this.token=t},invalidate:function(){this.headerName=void 0,this.token=void 0}},r=function(){return{hasToken:function(){return o.headerName&&o.token?!0:!1},getHeaderName:function(){return o.headerName},getToken:function(){return o.token}}}(),i=function(){var e,t,n=!1;return{initializationStarts:function(){n=!0,t=a.defer()},initializationSucceeded:function(a){e=a,n=!1,t.resolve(e),t=void 0},initializationFailed:function(){e=void 0,n=!1,t.resolve(e),t=void 0},userLoggedOff:function(){e=void 0},getProfile:function(){return n?t.promise:a.when(e)}}}(),s=function(){return t.get(e.securityRestServiceName)},l=function(){return t.get(e.appContextServiceName)},u=function(){return s().getCsrfToken().then(function(e){var t=e.data;return n.defaults.headers.common[t.headerName]=t.token,o.set(t.headerName,t.token),t},function(){return a.reject("Requesting a CSRF token failed")})};return{logIn:function(e,t){var n=a.defer();return i.initializationStarts(),s().login(e,t).then(function(){a.all([s().getCurrentUser(),u()]).then(function(e){var t=e[0].data;i.initializationSucceeded(t),l().onLoggingIn(t),n.resolve()},function(e){i.initializationFailed(),n.reject(e)})},function(){i.initializationFailed(),n.reject("Authentication failed")}),n.promise},logOff:function(){return s().logout().then(function(){o.invalidate(),i.userLoggedOff(),l().onLoggingOff()})},checkIfUserIsLoggedInAndIfSoReinitializeAppContext:function(){i.initializationStarts(),s().getCurrentUser().then(function(e){var t=e.data;u().then(function(){i.initializationSucceeded(t),l().onLoggingIn(t)},function(){i.initializationFailed()})},function(){i.initializationFailed()})},getCurrentCsrfToken:function(){return r},getCurrentUserProfile:function(){return i.getProfile()}}}]}}),angular.module("oasp.oaspSecurity").provider("oaspUnauthenticatedRequestResender",function(){"use strict";var e={authenticatorServiceName:"authenticator"};return{setAuthenticatorServiceName:function(t){e.authenticatorServiceName=t||e.authenticatorServiceName},$get:["$q","$injector",function(t,n){var a={queue:[],push:function(e){this.queue.push(e)},resendAll:function(e){for(;this.queue.length;)this.queue.shift().resend(e)},cancelAll:function(){for(;this.queue.length;)this.queue.shift().cancel()}},o=function(){return n.get("oaspSecurityService")},r=function(){return n.get(e.authenticatorServiceName)},i=function(){var e=!0;return function(t,n){e&&(r().execute().then(function(){t(),e=!0},function(){n(),e=!0}),e=!1)}}();return{addRequest:function(e){var r=t.defer(),s={resend:function(t){var a=function(e,t){var a=n.get("$http");return e.headers[t.headerName]=t.token,a(e)};a(e,t).then(function(e){r.resolve(e)},function(e){r.reject(e)})},cancel:function(){r.reject()}},l=function(){var e=o().getCurrentCsrfToken();a.resendAll({headerName:e.getHeaderName(),token:e.getToken()})},u=function(){a.cancelAll()};return a.push(s),i(l,u),r.promise}}}]}}),angular.module("oasp.oaspUi",["oasp.oaspUi.oaspGrid","oasp.oaspUi.spinner","oasp.oaspUi.modal","oasp.oaspUi.buttonBar"]),angular.module("oasp.oaspUi.buttonBar",["oasp.oaspUi.templates"]),angular.module("oasp.oaspUi.modal",["oasp.oaspUi.spinner","ui.bootstrap.modal","oasp.oaspUi.templates"]),angular.module("oasp.oaspUi.oaspGrid",["oasp.oaspUi.templates","trNgGrid"]).run(function(){"use strict";TrNgGrid.tableCssClass="tr-ng-grid table table-striped"}),angular.module("oasp.oaspUi.spinner",["angularSpinner","oasp.oaspUi.templates"]),angular.module("oasp.oaspUi.buttonBar").directive("buttonBar",function(){"use strict";return{restrict:"EA",replace:!0,templateUrl:"oasp/oasp-ui/html/button-bar/button-bar.html",scope:{buttonDefs:"="},link:function(e){e.onButtonClick=function(e){e&&angular.isFunction(e.onClick)&&e.onClick.apply(void 0,arguments)},e.isButtonDisabled=function(e){return e&&angular.isFunction(e.isActive)?!e.isActive.apply(void 0,arguments):e&&angular.isFunction(e.isNotActive)?e.isNotActive.apply(void 0,arguments):!0}}}}),angular.module("oasp.oaspUi.modal").constant("oaspUiModalDefaults",{backdrop:"static",keyboard:!1}).config(["$provide","oaspUiModalDefaults",function(e,t){"use strict";var n=function(e,n){return{open:function(a){n.show();var o=e.open(angular.extend({},t,a));return o.opened.then(function(){n.hide()},function(){n.hide()}),o}}};n.$inject=["$delegate","globalSpinner"],e.decorator("$modal",n)}]),angular.module("oasp.oaspUi.spinner").factory("globalSpinner",["$rootScope","$q",function(e,t){"use strict";var n={};return n.show=function(){e.globalSpinner=!0},n.hide=function(){e.globalSpinner=!1},n.showOnRouteChangeStartAndHideWhenComplete=function(){e.$on("$routeChangeStart",function(e,t){t.resolve&&n.show()}),e.$on("$routeChangeSuccess",function(){n.hide()}),e.$on("$routeChangeError",function(){n.hide()})},n.decorateCallOfFunctionReturningPromise=function(e){return n.show(),e().then(function(e){return n.hide(),e},function(e){return n.hide(),t.reject(e)})},n}]),angular.module("oasp.oaspUi.spinner").constant("spinnerOptions",{lines:13,length:20,width:4,radius:16,corners:1,rotate:0,color:"#ffffff",speed:1.2,trail:54,shadow:!1,hwaccel:!1,zIndex:2e9}).directive("spinner",["spinnerOptions",function(e){"use strict";return{restrict:"A",replace:!0,templateUrl:"oasp/oasp-ui/html/spinner/spinner.html",scope:{spinnerVisible:"=spinner"},link:function(t){t.spinnerOptions=e}}}]),angular.module("oasp.oaspUi.templates",[]).run(["$templateCache",function(e){e.put("oasp/oasp-ui/html/button-bar/button-bar.html",'<div class="btn-group btn-group-sm" role="group"><button data-ng-repeat="buttonDef in buttonDefs" data-ng-click="onButtonClick(buttonDef)" data-ng-disabled="isButtonDisabled(buttonDef)" class="btn btn-sm btn-default"><span data-ng-bind="buttonDef.label"></span></button></div>'),e.put("oasp/oasp-ui/html/spinner/spinner.html",'<div class="spinner-container" data-ng-show="spinnerVisible"><div class="spinner-backdrop"></div><span us-spinner="spinnerOptions" data-spinner-start-active="1"></span></div>')}]),angular.module("oasp.oaspI18n",["pascalprecht.translate","oasp.oaspI18n.templates"],["$translateProvider","$httpProvider",function(e,t){"use strict";t.interceptors.push("templateLoadTranslationInterceptor"),e.useLoader("$translatePartialLoader",{urlTemplate:"{part}/i18n/locale-{lang}.json"})}]).run(["$rootScope","$translate","$translatePartialLoader",function(e,t,n){"use strict";var a=function(e){n.addPart(e),t.refresh()};e.$on("translationPartChange",function(e,t){a(t,e)})}]),angular.module("oasp.oaspI18n").provider("oaspTranslation",["$translatePartialLoaderProvider","$translateProvider",function(e,t){"use strict";var n,a=[],o=[],r=function(){var e,t;if(o&&o.length)for(t=o[0],e=0;e<o.length;e+=1)o[e].default===!0&&(t=o[e]);return t};this.enableTranslationForModule=function(t,o){if(a.indexOf(t)<0&&a.push(t),o===!0){if(n)throw new Error("Default module already specified defaultModule="+n);n=t,e.addPart(n)}},this.setSupportedLanguages=function(e){if(o&&o.length)throw new Error("Supported languages already specified");o=e,r()&&t.preferredLanguage(r().key)},this.$get=[function(){return{moduleHasTranslations:function(e){return a.indexOf(e)>-1},getDefaultTranslationModule:function(){return n},getSupportedLanguages:function(){return o},getDefaultLanguage:r}}]}]),angular.module("oasp.oaspI18n").service("templateLoadTranslationInterceptor",["$rootScope","oaspTranslation",function(e,t){"use strict";var n=new RegExp("/?([^/]+)/html/");return{request:function(a){if(a.url){var o=n.exec(a.url);o&&o.length>1&&t.moduleHasTranslations(o[1])&&e.$emit("translationPartChange",o[1])}return a}}}]),angular.module("oasp.oaspI18n").controller("LanguageChangeCntl",["$scope","$translate","oaspTranslation",function(e,t,n){"use strict";e.supportedLanguages=n.getSupportedLanguages(),e.changeLanguage=function(e){t.use(e)},e.getCurrentLanguage=function(){return t.use()}}]),angular.module("oasp.oaspI18n").directive("languageChange",function(){"use strict";return{restrict:"EA",scope:!0,replace:!0,controller:"LanguageChangeCntl",templateUrl:"oasp/oasp-i18n/html/language-change.html"}}),angular.module("oasp.oaspI18n.templates",[]).run(["$templateCache",function(e){e.put("oasp/oasp-i18n/html/language-change.html",'<li class="dropdown language-dropdown" dropdown=""><a href="" class="dropdown-toggle" dropdown-toggle=""><span class="icon-container"><span class="icon icon-{{getCurrentLanguage()}}-24"></span></span><span translate="">OASP.LANGUAGE</span><span class="caret"></span></a><ul class="dropdown-menu" role="menu"><li ng-repeat="lang in supportedLanguages" ng-show="getCurrentLanguage()!=lang.key"><a ng-click="changeLanguage(lang.key)"><span class="icon icon-{{lang.key}}-24"></span>{{lang.label}}</a></li></ul></li>')}]),angular.module("app.offer-mgmt",["app.main"]),angular.module("app.offer-mgmt").factory("offerManagementRestService",["$http","currentContextPath",function(e,t){"use strict";var n=t.get()+"services/rest/offermanagement/v1";return{getAllOffers:function(){return e.get(n+"/offer")},getAllProducts:function(){return e.get(n+"/product")}}}]),angular.module("app.offer-mgmt").factory("offers",["offerManagementRestService",function(e){"use strict";return{loadAllOffers:function(){return e.getAllOffers().then(function(e){return e.data})},loadAllProducts:function(){return e.getAllProducts().then(function(e){return e.data})}}}]),angular.module("app.sales-mgmt",["app.main","app.offer-mgmt"]).config(["$routeProvider",function(e){"use strict";e.when("/sales-mgmt/cook-positions",{templateUrl:"sales-mgmt/html/cook-positions.html",controller:"CookPositionsCntl",resolve:{currentPositions:["positions",function(e){return e.get()}]}})}]),angular.module("app.sales-mgmt").controller("CookPositionsCntl",["$scope","currentPositions","positions","globalSpinner","positionStateNotification",function(e,t,n,a,o){"use strict";o.connect().then(function(){o.subscribe(function(){n.get()})}),e.positionsAvailableSelected=[],e.positionsAssignedSelected=[],e.positions=t,e.availablePositionSelected=function(){return e.positionsAvailableSelected&&e.positionsAvailableSelected.length>0?!0:!1},e.assignedPositionSelected=function(){return e.positionsAssignedSelected&&e.positionsAssignedSelected.length>0?!0:!1},e.assignCookToPosition=function(){e.availablePositionSelected()&&a.decorateCallOfFunctionReturningPromise(function(){return n.assignCookToPosition(e.positionsAvailableSelected[0].id)})},e.buttonDefs=[{label:"Done",onClick:function(){e.assignedPositionSelected()&&a.decorateCallOfFunctionReturningPromise(function(){return n.setPositionStatusToPrepared(e.positionsAssignedSelected[0].id)})},isActive:function(){return e.assignedPositionSelected()}},{label:"Reject",onClick:function(){e.assignedPositionSelected()&&a.decorateCallOfFunctionReturningPromise(function(){return n.makePositionAvailable(e.positionsAssignedSelected[0].id)})},isActive:function(){return e.assignedPositionSelected()}}]}]),angular.module("app.sales-mgmt").factory("positionStateNotification",["currentContextPath","$q",function(e,t){"use strict";var n;return{connect:function(){var a,o,r,i,s={},l=function(){a.resolve()},u=function(){a.reject()},c=n?!1:!0;return c?(a=t.defer(),o=a.promise,r=e.get()+"websocket/positions",i=new SockJS(r),n=Stomp.over(i),n.connect(s,l,u)):o=t.when(),o},subscribe:function(e){var t=function(t){var n=JSON.parse(t.body);angular.isFunction(e)&&e(n)};n&&n.subscribe("/topic/positionStatusChange",t)},notify:function(e,t){var a={id:e,status:t};n&&n.send("/sample/positions",{},JSON.stringify(a))}}}]),angular.module("app.sales-mgmt").factory("positions",["salesManagementRestService","offers","appContext","$q",function(e,t,n,a){"use strict";var o={},r=function(){var e,t={},n=[],a=[],o=[],r={},i=function(e,t){var n,a;if(e&&t)for(n=0;n<e.length;n+=1)if(a=e[n],a.id&&a.id===t)return a},s=function(e){var t,n,r;return e?(t=i(a,e.offerId),t&&(n=i(o,t.mealId),r=i(o,t.sideDishId)),{id:e.id,orderId:e.orderId,offerName:e.offerName,mealName:n&&n.description,sideDishName:r&&r.description}):void 0};return t.allPositions=function(e){return n=e,t},t.currentUserId=function(n){return e=n,t},t.offers=function(e){return a=e,t},t.products=function(e){return o=e,t},t.assignCookToPosition=function(e,t){var a=i(n,t);return a&&(a.cookId=e),a},t.setStatusOfPosition=function(e,t){var a=i(n,t);return a&&(a.state=e),a},t.makePositionUnassigned=function(e){var t=i(n,e);return t&&(t.cookId=void 0),t},t.getAvailableAndAssignedPositions=function(){var t,a,o,i=[],l=[];if(n)for(t=0;t<n.length;t+=1)a=n[t],o=null===a.cookId||void 0===a.cookId,o?i.push(s(a)):e===a.cookId&&l.push(s(a));return r.availablePositions=i,r.positionsAssignedToCurrentUser=l,r},t}();return o.get=function(){var o,i=a.defer();return n.getCurrentUser().then(function(n){o=n.getUserId(),o?a.all([e.findOrderPositions({state:"ORDERED",mealOrSideDish:!0}),t.loadAllOffers(),t.loadAllProducts()]).then(function(e){r.currentUserId(o).allPositions(e[0].data).offers(e[1]).products(e[2]),i.resolve(r.getAvailableAndAssignedPositions())},function(e){i.reject(e)}):i.reject()}),i.promise},o.assignCookToPosition=function(t){var a;return n.getCurrentUser().then(function(n){return a=n.getUserId(),e.updateOrderPosition(r.assignCookToPosition(a,t)).then(function(){return o.get()})})},o.makePositionAvailable=function(t){return e.updateOrderPosition(r.makePositionUnassigned(t)).then(function(){return o.get()})},o.setPositionStatusToPrepared=function(t){return e.updateOrderPosition(r.setStatusOfPosition("PREPARED",t)).then(function(){return o.get()})},o}]),angular.module("app.sales-mgmt").factory("salesManagementRestService",["$http","currentContextPath",function(e,t){"use strict";var n=t.get()+"services/rest/salesmanagement/v1";return{findOrders:function(t){return e.post(n+"/order/search",t)},updateOrder:function(t,a){return e.put(n+"/order/"+a,t)},createOrder:function(t){return e.post(n+"/order",t)},updateOrderPosition:function(t){return e.post(n+"/orderposition",t)},findOrderPositions:function(t){return e.get(n+"/orderposition",{params:t})}}}]),angular.module("app.sales-mgmt").factory("sales",["salesManagementRestService",function(e){"use strict";return{loadOrderForTable:function(t){var n={state:"OPEN",tableId:t};return e.findOrders(n).then(function(e){return e.data.result&&e.data.result.length?e.data.result[0]:void 0})},saveOrUpdateOrder:function(t){var n;return n=t.order.id?e.updateOrder(t,t.order.id).then(function(e){return e.data}):e.createOrder(t).then(function(e){return e.data})}}}]),angular.module("app.table-mgmt",["ngRoute","app.offer-mgmt","app.sales-mgmt","app.main","app.tableMgmt.templates"],["$routeProvider","oaspTranslationProvider",function(e,t){"use strict";t.enableTranslationForModule("table-mgmt"),e.when("/table-mgmt/table-search",{templateUrl:"table-mgmt/html/table-search.html",controller:"TableSearchCntl",resolve:{paginatedTableList:["tables",function(e){return e.getPaginatedTables(1,4).then(function(e){return e})}]}})}]),angular.module("app.table-mgmt").filter("price",function(){"use strict";return function(e){return e+" EUR"}}),angular.module("app.table-mgmt").controller("TableDetailsCntl",["$scope","$sce","tableDetails","allOffers","currentOrder","sales","globalSpinner","positionStateNotification",function(e,t,n,a,o,r,i,s){"use strict";e.table=n,e.allOffers=a,e.model={},e.model.order=o,e.model.selected=a.length?a[0]:void 0,e.selectedItems=[],e.positionsShown=[],e.totalItems=void 0!==e.model.order?e.model.order.positions.length:0,e.numPerPage=3,e.currentPage=1,e.maxSize=4,e.$watch("totalItems + currentPage + numPerPage + model.order + model.order.positions",function(){if(void 0!==e.model.order){var t=(e.currentPage-1)*e.numPerPage,n=t+e.numPerPage;e.positionsShown=e.model.order.positions.slice(t,n),e.totalItems=void 0!==e.model.order.positions?e.model.order.positions.length:0}}),e.trustAsHtml=function(e){return t.trustAsHtml(e)},e.noOrderAssigned=function(){return!e.model.order},e.orderAssigned=function(){return!e.noOrderAssigned()},e.assignNewOrder=function(){e.model.order={order:{tableId:e.table.id,state:"OPEN"},positions:[]}},e.forms={},e.submit=function(){i.decorateCallOfFunctionReturningPromise(function(){return r.saveOrUpdateOrder(e.model.order)}).then(function(){s.connect().then(function(){var t=e.model.order.positions[0];s.notify(t.id,t.status)}),e.$close()})},e.addPosition=function(t){e.model.order.positions.push({revision:null,orderId:e.model.order.order.id,offerId:t.id,offerName:t.description,state:"ORDERED",price:t.price,comment:""}),e.totalItems=e.model.order.positions.length},e.buttonDefs=[{label:"Remove",onClick:function(){e.model.order.positions.splice(e.model.order.positions.indexOf(e.selectedItems[0]),1),e.selectedItems.length=0},isActive:function(){return e.selectedItems.length}}]}]),angular.module("app.table-mgmt").factory("tableManagementRestService",["$http","currentContextPath",function(e,t){"use strict";var n=t.get()+"services/rest/tablemanagement/v1";return{getTable:function(t){return e.get(n+"/table/"+t)},getPaginatedTables:function(t,a){var o={pagination:{size:a,page:t,total:!0}};return e.post(n+"/table/search",o)},createTable:function(t,a){return e.put(n+"/table/"+t,a)},deleteTable:function(t){return e.delete(n+"/table/"+t)},saveTable:function(t){return e.post(n+"/table/",t)},isTableReleasable:function(t){return e.get(n+"/table/"+t+"/istablereleasable/")}}}]),angular.module("app.table-mgmt").controller("TableSearchCntl",["$scope","tables","paginatedTableList","$modal","globalSpinner","offers","sales",function(e,t,n,a,o,r,i){"use strict";var s=function(){return e.selectedItems&&e.selectedItems.length?e.selectedItems[0]:void 0};e.openEditDialog=function(e){a.open({templateUrl:"table-mgmt/html/table-details.html",controller:"TableDetailsCntl",resolve:{tableDetails:function(){return t.loadTable(e.id)},allOffers:function(){return r.loadAllOffers()},currentOrder:function(){return i.loadOrderForTable(e.id)}}})},e.selectedItems=[],e.maxSize=5,e.totalItems=n.pagination.total,e.numPerPage=n.pagination.size,e.currentPage=n.pagination.page,e.gridOptions={data:n.result},e.reloadTables=function(){t.getPaginatedTables(e.currentPage,e.numPerPage).then(function(e){return e}).then(function(t){n=t,e.gridOptions.data=n.result})},e.$watch("currentPage",function(){e.reloadTables()}),e.buttonDefs=[{label:"Edit...",onClick:function(){e.openEditDialog(s())},isActive:function(){return s()}},{label:"Reserve",onClick:function(){o.decorateCallOfFunctionReturningPromise(function(){return t.reserve(s()).then(e.reloadTables)})},isActive:function(){return s()&&"FREE"===s().state}},{label:"Cancel Reservation",onClick:function(){o.decorateCallOfFunctionReturningPromise(function(){return t.cancelReservation(s()).then(e.reloadTables)})},isActive:function(){return s()&&"RESERVED"===s().state}},{label:"Occupy",onClick:function(){o.decorateCallOfFunctionReturningPromise(function(){return t.occupy(s()).then(e.reloadTables)})},isActive:function(){return s()&&("RESERVED"===s().state||"FREE"===s().state)}},{label:"Free",onClick:function(){o.decorateCallOfFunctionReturningPromise(function(){return t.free(s()).then(e.reloadTables)})},isActive:function(){return s()&&"OCCUPIED"===s().state}}]}]),angular.module("app.table-mgmt").factory("tables",["tableManagementRestService",function(e){"use strict";var t={};return{getPaginatedTables:function(n,a){return e.getPaginatedTables(n,a).then(function(e){return angular.copy(e.data,t),t})},loadTable:function(t){return e.getTable(t).then(function(e){return e.data})},reserve:function(t){return t.state="RESERVED",e.saveTable(t).then(function(){})},free:function(t){return t.state="FREE",e.saveTable(t).then(function(){})},occupy:function(t){return t.state="OCCUPIED",e.saveTable(t).then(function(){})},cancelReservation:function(t){return t.state="FREE",e.saveTable(t).then(function(){})}}}]),angular.module("app.tableMgmt.templates",[]).run(["$templateCache",function(e){e.put("table-mgmt/html/table-details.html",'<div><div class="modal-header"><h3 class="modal-title">Details for Table #<span data-ng-bind="table.number"></span></h3></div><div class="modal-body"><form name="forms.tableForm" novalidate=""><div class="row"><div class="col-md-12"><label>Status:</label>&nbsp; <span data-ng-bind="table.state"></span></div></div><div class="page-header"><h4>Order</h4></div><div class="row" data-ng-show="noOrderAssigned()"><div class="col-md-12"><div class="alert alert-info" role="alert">No order currently assigned to this table. <a data-ng-click="assignNewOrder()">Assign a new order...</a></div></div></div><div data-ng-show="orderAssigned()"><div class="row"><div class="col-md-12"><label>State:</label>&nbsp; <span data-ng-bind="table.order.orderState"></span></div></div><div class="row"><div class="col-md-6"><label>Order Positions:</label>&nbsp;</div><div class="col-md-4"><ui-select ng-model="model.selected"><ui-select-match placeholder="Select offer...">{{$select.selected.description}}</ui-select-match><ui-select-choices repeat="o in allOffers | filter: $select.search"><div ng-bind-html="trustAsHtml((o.description | highlight: $select.search))"></div></ui-select-choices></ui-select></div><div class="col-md-1"><button class="btn btn-sm btn-primary" data-ng-click="addPosition(model.selected)">+Add</button> &nbsp;</div></div><div class="row"><div class="col-md-12"><table class="raw-table" tr-ng-grid="" selection-mode="SingleRow" enable-filtering="false" selected-items="selectedItems" items="positionsShown"><thead><tr><th field-name="id" display-name="Number" cell-width="7em"></th><th field-name="offerName" display-name="Title"></th><th field-name="state" display-name="Status"></th><th field-name="price" display-name="Price" display-format="price:gridItem"></th><th field-name="comment" display-name="Comment"></th></tr></thead></table><button-bar button-defs="buttonDefs"></button-bar></div></div><pagination total-items="totalItems" items-per-page="numPerPage" ng-model="currentPage" num-pages="numPages" max-size="maxSize" class="pagination-sm" boundary-links="true" rotate="false"></pagination></div></form></div><div class="modal-footer"><button class="btn btn-primary" data-ng-disabled="!model.order" ng-click="submit()" translate="">TABLE_MGMT.SUBMIT</button> <button class="btn btn-warning" ng-click="$close()" translate="">TABLE_MGMT.CANCEL</button></div></div>')}]);