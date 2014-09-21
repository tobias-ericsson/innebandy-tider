#for debug, see all data
http://innebandy-tider.rhcloud.com/stats.html
http://innebandy-tider.rhcloud.com/search/
http://innebandy-tider.rhcloud.com/search/?search_field=

#middleware
http://l-tribe.appspot.com/
https://appengine.google.com/deployment?&app_id=s~l-tribe
https://console.developers.google.com/project/apps~l-tribe




#openshift stuff

Get started
===========
1. Add framework of choice to your repo.
2. Modify .openshift/action_hooks/start to start your application.
   The application is required to bind to $OPENSHIFT_INTERNAL_IP:8080.
3. Modify .openshift/action_hooks/stop to stop your application.
4. Commit and push your changes.

Repo layout
===========
static/ - Externally exposed static content goes here
.openshift/action_hooks/start - Script that gets run to start your application
.openshift/action_hooks/stop - Script that gets run to stop your application
.openshift/action_hooks/pre_build - Script that gets run every git push before the build
.openshift/action_hooks/build - Script that gets run every git push as part of the build process (on the CI system if available)
.openshift/action_hooks/deploy - Script that gets run every git push after build but before the app is restarted
.openshift/action_hooks/post_deploy - Script that gets run every git push after the app is restarted

Notes about layout
==================
Please leave the static directory in place (alter but do not delete) but feel
free to create additional directories if needed.

Note: Every time you push, everything in your remote repo dir gets recreated
      please store long term items (like an sqlite database) in the OpenShift
      data directory, which will persist between pushes of your repo.
      The OpenShift data directory is accessible relative to the remote repo
      directory (../data) or via an environment variable OPENSHIFT_DATA_DIR.


Environment Variables
=====================

OpenShift provides several environment variables to reference for ease
of use.  The following list are some common variables but far from exhaustive:

    $_ENV['OPENSHIFT_INTERNAL_IP']  - IP Address assigned to the application
    $_ENV['OPENSHIFT_GEAR_NAME']  - Application name
    $_ENV['OPENSHIFT_GEAR_DIR']   - Application dir
    $_ENV['OPENSHIFT_DATA_DIR']  - For persistent storage (between pushes)
    $_ENV['OPENSHIFT_TMP_DIR']   - Temp storage (unmodified files deleted after 10 days)

To get a full list of environment variables, simply add a line in your
.openshift/action_hooks/build script that says "export" and push.


openshift-diy-nodejs-elasticsearch
==========================

Thanks for the great work by [razorinc](https://github.com/razorinc/elasticsearch-openshift-example) and [creationix](https://github.com/creationix/nvm/), this repo let you test Node.js (v0.8 and above) with ElasticSearch in a OpenShift DIY application. For Node.js, it will first check for pre-compiled linux version, then compile from source if not found.

[node-supervisor](https://github.com/isaacs/node-supervisor) is used to automatically restart the node.js app if somehow crashed.

Usage
-----

Create an DIY app

    rhc app create -t diy-0.1 -a yourapp

Add this repository

    cd yourapp
    git remote add nodejsES -m master git://github.com/eddie168/openshift-diy-nodejs-elasticsearch.git
    git pull -s recursive -X theirs nodejsES master

Then push the repo to openshift

    git push

If pre-compiled node.js binary is not available, first push will take a while to finish.

You can specify the node.js script to start with in `package.json` as described [here](https://openshift.redhat.com/community/kb/kb-e1048-how-can-i-run-my-own-nodejs-script).

Check the end of the message for Node.js and ElastisSearch version:

    remote: Starting application...
    remote: Node Version:
    remote: { http_parser: '1.0',
    remote:   node: '0.10.10',
    remote:   v8: '3.14.5.9',
    remote:   ares: '1.9.0-DEV',
    remote:   uv: '0.10.10',
    remote:   zlib: '1.2.3',
    remote:   modules: '11',
    remote:   openssl: '1.0.1e' }
    remote: ElasticSearch Version: 0.90.1, JVM: 1.7.0_19(23.7-b01)
    remote: nohup supervisor server.js >/var/lib/stickshift/xxxxxxxxxxxxxxxxxx/diy-0.1/logs/server.log 2>&1 &
    remote: Done

In this case it is node `v0.10.10` and elasticsearch `0.90.1`

You can find node.js app's log at `$OPENSHIFT_DIY_LOG_DIR/server.log`. Subsequent `push` will rename the log file with a time stamp before overwritten. The same goes to ElasticSearch log file and can be found at `$OPENSHIFT_DIY_LOG_DIR/elasticsearch.log`. 
You should be able to see these log files with `rhc tail -a yourapp`.

Now open your openshift app in browser and you should see the standard openshift sample page. Enjoy!!

Settings
--------

Edit `config_diy.json`

    {
      "nodejs": {
        "version": "v0.10.10",
        "removeOld": true
      },
      "elasticsearch": {
        "version": "0.90.1",
        "port": 19200,
        "ES_HEAP_SIZE": "256m",
        "removeOld": true
      }
    }

- `nodejs.version`: change node.js version (keep the `v` letter in front)
- `nodejs.removeOld`: delete previous installed node.js binarys
- `elasticsearch.version`: change elasticsearch version
- `elasticsearch.port`: port used by elasticsearch (Refer to [here](https://openshift.redhat.com/community/kb/kb-e1038-i-cant-bind-to-a-port))
- `elasticsearch.ES_HEAP_SIZE`: Refer to [here](http://www.elasticsearch.org/guide/reference/setup/installation.html)
- `elasticsearch.removeOld`: delete previous installed elasticsearch binarys

`commit` and then `push` to reflect the changes to the OpenShift app.

**Note that `v0.6.x` won't work with this method.**

Use ElasticSearch in Node.js
----------------------------

An environment variable `ES_PORT` is defined. Simply make a http connection to ElasticSearch server with `ES_PORT`. For example:

    var esServer = "http://"+process.env.OPENSHIFT_DIY_IP+":"+process.env.ES_PORT+"/";
    http.get(esServer, function (res) {
      res.setEncoding('utf8');
      res.on('data', function (data) {
        console.log('BODY: %s',data);
      });
    }).on('error', function (err) {
      console.log('ES ERROR: %s', err);
    });

