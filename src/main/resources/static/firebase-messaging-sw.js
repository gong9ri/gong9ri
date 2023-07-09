importScripts('https://www.gstatic.com/firebasejs/8.6.5/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/8.6.5/firebase-messaging.js');

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
    apiKey: "AIzaSyDLKx_5B8kBCu8QPxLzYsrA9lpXnjdBPB8",
    authDomain: "gong9ri-e7e80.firebaseapp.com",
    projectId: "gong9ri-e7e80",
    storageBucket: "gong9ri-e7e80.appspot.com",
    messagingSenderId: "102830193096",
    appId: "1:102830193096:web:21465d3f428ef17372e8fd",
    measurementId: "G-RVC0TV1BBY"
};

firebase.initializeApp(firebaseConfig);
const messaging = firebase.messaging();