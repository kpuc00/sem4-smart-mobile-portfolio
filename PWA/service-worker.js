const cacheName = "KPUC_V1";
const appFiles = [
  "/manifest.json",
  "/js/scripts.js",
  "/css/style.css",
  "/images/background.jpg",
  "/images/favicon.ico",
  "/images/logo-nav.png",
  "/images/logo.png"
];

self.addEventListener("install", (installing) => {
  installing.waitUntil(
    caches.open(cacheName).then((cache) => {
      console.log("Service Worker: Caching important offline files");
      return cache.addAll(appFiles);
    })
  );
});

self.addEventListener("activate", (activating) => {
  console.log("Service Worker: All systems online, ready to go!");
});

self.addEventListener("fetch", (fetching) => {
  fetching.respondWith(
    caches.match(fetching.request.url).then((response) => {
      console.log("Service Worker: Fetching resource " + fetching.request.url);
      return response || fetch(fetching.request).then((response) => {
        console.log("Service Worker: Resource " + fetching.request.url + " not available in cache");
        return caches.open(cacheName).then((cache) => {
          console.log("Service Worker: Caching (new) resource " + fetching.request.url);
          cache.put(fetching.request, response.clone());
          return response;
        });
      }).catch(function () {
        console.log("Service Worker: Fetching online failed, HAALLPPPP!!!");
        //Do something else with the request (respond with a different cached file)
      })
    })
  );
});

self.addEventListener("push", (pushing) => {
  if(pushing.data){
    pushdata=JSON.parse(pushing.data.text());		
    console.log("Service Worker: I received this:",pushdata);
    if((pushdata["title"]!="")&&(pushdata["message"]!="")){			
      const options={ body:pushdata["message"] }
      self.registration.showNotification(pushdata["title"],options);
      console.log("Service Worker: I made a notification for the user");
    } else {
      console.log("Service Worker: I didn't make a notification for the user, not all the info was there :(");			
    }
  }
});

self.addEventListener("notificationclick",function(clicking){
  const pageToOpen="/";
	const promiseChain=clients.openWindow(pageToOpen);
	event.waitUntil(promiseChain);
});