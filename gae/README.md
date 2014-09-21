to update configuration modify:

src\main\groovy\domain\Properties.groovy
src\main\webapp\WEB-INF\cron.xml

to deploy:

gradlew
gradlew build

cd build

dev_appserver.sh exploded-app/
appcfg.sh update exploded-app/

to verify:

http://l-tribe.appspot.com/
https://appengine.google.com/deployment?&app_id=s~l-tribe
https://console.developers.google.com/project/apps~l-tribe