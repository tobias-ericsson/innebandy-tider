def targetUrl = 'http://innebandy-tider.rhcloud.com/?sportig=ping'

log.info("ping "+targetUrl)

URL url = new URL(targetUrl)

def response = url.head()

log.info("response code: "+response.statusCode)
log.info("response headers: "+response.headersMap)

println("response code: "+response.statusCode)
println("response headers: "+response.headersMap)





