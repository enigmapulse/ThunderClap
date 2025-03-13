var stompClient = null;
var messageArea = document.getElementById("messageArea");
var username = "";
// Old messages are fetched from the database and shown on the page after refreshing
fetch('/old-messages')
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json(); // Assuming the response is JSON
    })
    .then(data => {
        if (Array.isArray(data)) {
            data.slice().reverse().forEach((item) => {
                showMessage(item)
            });
        } else {
            console.log('Fetched data is not an array:', data);
        }
    })
    .catch(error => {
        console.error('Error fetching data:', error);
    });

// Fetch the authenticated username from your server endpoint (/username)
fetch('/username')
    .then(response => response.text())
    .then(data => {
        username = data.trim();
        document.getElementById("displayUsername").textContent = "Logged in as: " + username;
        connect(); // Call the connect function
    })
    .catch(error => console.error('Error fetching username:', error));

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


// Send button event listener
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
