let stompClient = null;
let fromId = 0;
let ChatMessageUl = null;

function connect() {
    var socket = new SockJS('/chats');
    stompClient = Stomp.over(socket);
    let token = "";
    const headers = {
        'X-CSRF-TOKEN': token,
    };

    stompClient.connect(headers, function (frame) {
        console.log('Connected: ' + frame);

        onConnected();
    });
}

function onConnected() {
    getChatMessages(1);
    stompClient.subscribe(`/topic/chats/${chatRoomId}`, function (data) {
        let message = JSON.parse(data.body);
        addChatBubble(true ,`${message.senderId}`, `${message.content}`);
    });
}

function getChatMessages(fromId) {

    console.log("fromId : " + fromId);
    fetch(`/${chatRoomId}/messages?fromId=${fromId}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        }})
        .then(response => response.json())
        .then(body => {
            drawMessages(body);
        })
        .catch(error => {
            console.error(error);
        });
}

function drawMessages(messages) {

    let isOwnChat = true;

    if (messages.length > 0) {
        fromId = messages[messages.length - 1].id;
    }

    messages.forEach((message) => {

        addChatBubble(isOwnChat, `${message.senderId}`, `${message.content}`);
    });
}

function chatWriteMessage(form) {
    form.content.value = form.content.value.trim();

    stompClient.send(`/app/chats/${chatRoomId}`, {}, JSON.stringify({content: form.content.value}));

    form.content.value = '';
    form.content.focus();
}

document.addEventListener("DOMContentLoaded", function() {
    ChatMessageUl = document.querySelector('.chat__message-ul');
    getChatMessages();
    connect();
});

function addChatBubble(isOwnChat, sender, message) {
    const chatContainer = document.createElement("div");
    chatContainer.classList.add("chat", isOwnChat ? "chat-end" : "chat-start");

    const chatHeader = document.createElement("div");
    chatHeader.classList.add("chat-header");
    chatHeader.textContent = sender;

    const chatBubble = document.createElement("div");
    chatBubble.classList.add("chat-bubble");
    chatBubble.textContent = message;

    chatContainer.appendChild(chatHeader);
    chatContainer.appendChild(chatBubble);
    ChatMessageUl.appendChild(chatContainer);
}