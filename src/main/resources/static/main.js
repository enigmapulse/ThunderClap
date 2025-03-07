var stompClient = null;


// Connect button event listener
document.getElementById("connectButton").addEventListener("click", function() {
    var username = document.getElementById("username").value.trim();
    if (username) {
        // Create a SockJS connection to the /ws endpoint
        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function(frame) {
            console.log('Connected: ' + frame);
            // Subscribe to the public topic to receive messages
            stompClient.subscribe('/topic/public', function(messageOutput) {
                showMessage(JSON.parse(messageOutput.body));
            });
            // Notify the server that a new user has joined
            stompClient.send("/app/chat.register", {}, JSON.stringify({sender: username, type: 'JOIN'}));
        });
    }
});

// Send button event listener
document.getElementById("sendButton").addEventListener("click", function() {
    var messageInput = document.getElementById("message");
    var message = messageInput.value.trim();
    if (message && stompClient) {
        var chatMessage = {
            sender: document.getElementById("username").value,
            content: message,
            type: 'CHAT'
        };
        // Send the chat message to the server
        stompClient.send("/app/chat.send", {}, JSON.stringify(chatMessage));
        messageInput.value = "";
    }
});

// Function to display messages on the page
function showMessage(message) {
    var messageArea = document.getElementById("messageArea");
    var messageElement = document.createElement("li");
    messageElement.className = "chat-message";
    messageElement.setAttribute('data-type', message.type);

    var senderSpan = document.createElement('span');
    senderSpan.className = 'sender';
    senderSpan.textContent = message.sender;

    var contentSpan = document.createElement('span');
    contentSpan.className = 'message-content';

    if (message.type === "JOIN") {
        contentSpan.textContent = "joined the chat";
    } else {
        contentSpan.textContent = message.content;
    }

    messageElement.appendChild(senderSpan);
    messageElement.appendChild(contentSpan);
    messageArea.appendChild(messageElement);
}
