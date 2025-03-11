var stompClient = null;
var username = "";

// When the join chatroom button is clicked
document.getElementById("joinChatroomButton").addEventListener("click", function() {
    // Fetch the authenticated username from your server endpoint (/username)
    fetch('/username')
        .then(response => response.text())
        .then(data => {
            username = data.trim();
            document.getElementById("displayUsername").textContent = "Logged in as: " + username;
            connect(); // Call the connect function
        })
        .catch(error => console.error('Error fetching username:', error));
});

function connect() {
    // Create a SockJS connection to the /ws endpoint
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        // Subscribe to the public topic to receive messages
        stompClient.subscribe('/topic/public', function(messageOutput) {
            showMessage(JSON.parse(messageOutput.body));
        });
        // Notify the server that the authenticated user has joined the chat
        stompClient.send("/app/chat.register", {}, JSON.stringify({sender: username, type: 'JOIN'}));
    });
}

// Send button event listener remains unchanged
document.getElementById("sendButton").addEventListener("click", function() {
    var messageInput = document.getElementById("message");
    var message = messageInput.value.trim();
    if (message && stompClient) {
        var chatMessage = {
            sender: username, // Use the automatically fetched username
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
