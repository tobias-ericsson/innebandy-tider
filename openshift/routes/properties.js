exports.ipNode = process.env.OPENSHIFT_DIY_IP || "127.0.0.1";
exports.portNode = process.env.OPENSHIFT_DIY_PORT || 8080;
exports.ipES = process.env.OPENSHIFT_DIY_IP || "127.0.0.1";
exports.portES = process.env.ES_PORT || 9200;
exports.interceptPaths = [
    {'path': '/floorball/', 'server': "http://" + exports.ipES + ":" + exports.portES},
    {'path': '/m-ligan', 'server': "http://m-ligan.net:80"},
    {'path': '/stats/', 'server': "http://l-tribe.appspot.com"}
];
