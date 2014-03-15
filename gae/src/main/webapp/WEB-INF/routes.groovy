
get "/", forward: "/WEB-INF/pages/index.gtpl"
get "/datetime", forward: "/datetime.groovy"

get "/favicon.ico", redirect: "/images/gaelyk-small-favicon.png"
get "/cron/ping", forward: "/ping.groovy"
get "/cron/pong", forward: "/pong.groovy"
get "/cron/parse-scoreboard", forward: "/parseScoreboard.groovy"
get "/cron/parse-schedule", forward: "/parseSchedule.groovy"
