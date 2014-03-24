def targetUrl = domain.Properties.TARGET_URL

log.info("pong " + targetUrl)

println "request "+request.getQueryString()
println "response "+response
println "params"+ params
params.each {
    println it
}


URL url = new URL(targetUrl)

def response = url.head()

log.info("response code: " + response.statusCode)
log.info("response headers: " + response.headersMap)

println("response code: " + response.statusCode)
println("response headers: " + response.headersMap)




