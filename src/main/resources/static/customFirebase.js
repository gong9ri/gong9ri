let firebaseConfig = {
    apiKey: "AIzaSyDLKx_5B8kBCu8QPxLzYsrA9lpXnjdBPB8",
    authDomain: "gong9ri-e7e80.firebaseapp.com",
    projectId: "gong9ri-e7e80",
    storageBucket: "gong9ri-e7e80.appspot.com",
    messagingSenderId: "102830193096",
    appId: "1:102830193096:web:21465d3f428ef17372e8fd",
    measurementId: "G-RVC0TV1BBY"
};

async function requestNotificationPermission(config) {
    firebase.initializeApp(config);

    const messaging = firebase.messaging();

    try {
        await Notification.requestPermission();

        if (Notification.permission === "granted") {
            console.log("Notification permission granted.");
            messaging.getToken().then((currentToken) => {
                if (currentToken) {
                    submitToken(currentToken);
                } else {
                    console.warn("No Instance ID token available. Request permission to generate one.");
                }
            }).catch((err) => {
                console.warn("An error occurred while retrieving token. ", err);
            });

            messaging.onMessage((payload) => {
                console.log("Message received. ", payload);
            });

            messaging.onTokenRefresh(() => {
                messaging.getToken().then((refreshedToken) => {
                    console.log("Token refreshed.", refreshedToken);
                }).catch((err) => {
                    console.warn("Unable to retrieve refreshed token ", err);
                });
            });
        } else {
            console.warn("Unable to get permission to notify.");
        }
    } catch (error) {
        console.error("Error in requestNotificationPermission: ", error);
    }
}

function submitToken(tokenString) {
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const url = '/v1/tokens';
    const requestOptions = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': csrfToken
        },
        body: JSON.stringify({"tokenString": tokenString})
    };

    fetch(url, requestOptions)
        .then((response) => {
            if (response.ok) {
                return response.text();
            } else {
                throw new Error('Error while submitting token. Status code: ' + response.status);
            }
        })
        .then((resultCode) => {
            console.log('Success! Result code:', resultCode);
        })
        .catch((error) => {
            console.error('Error:', error);
        });
}