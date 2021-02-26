//See if the browser supports Service Workers, if so try to register one
if ("serviceWorker" in navigator) {
  navigator.serviceWorker.register("service-worker.js").then(function (registering) {
    // Registration was successful
    console.log("Browser: Service Worker registration is successful with the scope", registering.scope);
  }).catch(function (error) {
    //The registration of the service worker failed
    console.log("Browser: Service Worker registration failed with the error", error);
  });
} else {
  //The registration of the service worker failed
  console.log("Browser: I don't support Service Workers :(");
}

//Asking for permission with the Notification API
if (typeof Notification !== typeof undefined) { //First check if the API is available in the browser
  Notification.requestPermission().then(function (result) {
    //If accepted, then save subscriberinfo in database
    if (result === "granted") {
      console.log("Browser: User accepted receiving notifications, save as subscriber data!");
      navigator.serviceWorker.ready.then(function (serviceworker) { //When the Service Worker is ready, generate the subscription with our Serice Worker's pushManager and save it to our list
        const VAPIDPublicKey = "BJOg4KwXT_5P0E0uKkhwMN0-lp7GRK7mJbIF1X7PsRt_aQm6bhGWdtKw788UCS2v8HBkzPT_ji6TA1PRHNFLXXI"; // Fill in your VAPID publicKey here
        const options = { applicationServerKey: VAPIDPublicKey, userVisibleOnly: true } //Option userVisibleOnly is neccesary for Chrome
        serviceworker.pushManager.subscribe(options).then((subscription) => {
          //POST the generated subscription to our saving script (this needs to happen server-side, (client-side) JavaScript can't write files or databases)
          let subscriberFormData = new FormData();
          subscriberFormData.append("json", JSON.stringify(subscription));
          fetch("data/saveSubscription.php", { method: "POST", body: subscriberFormData });
        });
      });
    }
  }).catch((error) => {
    console.log(error);
  });
}