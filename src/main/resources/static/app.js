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
    stompClient.subscribe(`/topic/chats/${chatRoomId}`, function (data) {
        let message = JSON.parse(data.body);
        let isOwnChat = message.senderId === memberId.toString();
        addChatBubble(isOwnChat, `${message.senderName}`, `${message.content}`, `${message.chatType}`);
    });
}

function getChatMessages() {

    fetch(`/${chatRoomId}/messages`, {
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
        })
        .catch(error => {
            console.error(error);
        });
}

function drawMessages(messages, memberId) {
    messages.forEach((message) => {
        let isOwnChat = message.senderId === memberId.toString();

        addChatBubble(isOwnChat, `${message.senderName}`, `${message.content}`, `${message.chatType}`);
    });
}

function chatWriteMessage(form) {
    form.content.value = form.content.value.trim();

    stompClient.send(`/app/chats/${chatRoomId}`, {}, JSON.stringify({chatType: "text", content: form.content.value}));

    form.content.value = '';
    form.content.focus();
}

document.addEventListener("DOMContentLoaded", function() {
    ChatMessageUl = document.querySelector('.chat__message-ul');
    getChatMessages();
    connect();
});

function addChatBubble(isOwnChat, sender, content, chatType) {
    const chatContainer = document.createElement("div");
    chatContainer.classList.add("chat", isOwnChat ? "chat-end" : "chat-start");

    const chatHeader = document.createElement("div");
    chatHeader.classList.add("chat-header");
    chatHeader.textContent = sender;

    const chatBubble = document.createElement("div");
    chatBubble.classList.add("chat-bubble");

    if (chatType === 'img') { // 만약 이미지 타입이면
        const img = document.createElement('img');
        img.src = content;
        img.classList.add("md:max-w-md");
        img.style.width
        chatBubble.appendChild(img);
    } else { // 아닐 경우
        chatBubble.textContent = content;
    }

    chatContainer.append(chatHeader, chatBubble);
    ChatMessageUl.appendChild(chatContainer);
}