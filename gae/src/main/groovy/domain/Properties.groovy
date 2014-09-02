package domain

import com.google.appengine.api.utils.SystemProperty

/**
 * Created by Tobias on 2014-03-23.
 */
interface Properties {
    //String TARGET_URL = 'http://innebandy-tider.rhcloud.com/'
    String TARGET_URL = SystemProperty.environment.value() == SystemProperty.Environment.Value.Production ?
            'http://innebandy-tider.rhcloud.com/' : 'http://localhost:8082'
    String TARGET_GAMES_PATH = '/floorball/game/'
    String TARGET_TEAMS_PATH = '/floorball/team/'
    String RESOURCE_URL = 'http://www.m-ligan.net/data/'
    String RESOURCE_SCHEDULE_URL = 'http://www.m-ligan.net/spelht14a.htm'
}
