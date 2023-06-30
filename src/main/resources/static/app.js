let stompClient = null;
let ChatMessageUl = null;

function connect() {
    var socket = new SockJS('/chats');
    stompClient = Stomp.over(socket);
    const headers = {
        'X-CSRF-TOKEN': token,
    };

    stompClient.connect(headers, function (frame) {
        console.log('Connected: ' + frame);

        onConnected();
    });
}

function onConnected() {
    getChatMessages('no');
    stompClient.subscribe(`/topic/chats/${chatRoomId}`, function (data) {
        let message = JSON.parse(data.body);
        addChatBubble(true, `${message.senderName}`, `${message.content}`);
    });
}

function getChatMessages(isNew) {

    fetch(`/${chatRoomId}/messages?isNew=${isNew}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        }
    })
        .then(response => response.json())
        .then(body => {
            const {messages, memberId} = body;
            drawMessages(messages, memberId);
            return memberId;
        })
        .catch(error => {
            console.error(error);
        });
}

function drawMessages(messages, memberId) {

    let isOwnChat = true;

    messages.forEach((message) => {
        let isOwnChat = message.senderId === memberId;

        addChatBubble(isOwnChat, `${message.senderName}`, `${message.content}`);
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