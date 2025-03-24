var stompClient = null;
var messageArea = document.getElementById("messageArea");
var username = "";
// Global variable to store attached image data
var attachedImage = "";
// Old messages are fetched from the database and shown on the page after refreshing
var attachedAudio = "";
// Add global variable for audio
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
        return fetch('/username');
    })
// Fetch the authenticated username from your server endpoint (/username)
    .then(response => response.text())
    .then(data => {
        username = data.trim();
        document.getElementById("displayUsername").textContent = username;
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



// Modify send button event listener
document.getElementById("sendButton").addEventListener("click", function() {
    var messageInput = document.getElementById("message");
    var messageText = messageInput.value.trim();
    var username = document.getElementById("displayUsername").textContent || "Anonymous";

    // Check if there's text, image, or audio
    if (messageText.length > 0 || attachedImage || attachedAudio) {
        var chatMessage = {
            sender: username,
            content: messageText,
            imageBase64: attachedImage,
            audioBase64: attachedAudio,  // New audio field
            type: 'CHAT'
        };
        stompClient.send("/app/chat.send", {}, JSON.stringify(chatMessage));

        // Clear inputs
        messageInput.value = "";
        attachedImage = "";
        attachedAudio = "";
        document.getElementById("imageInput").value = "";
        document.getElementById("audioInput").value = "";
    }
});

document.getElementById("imageInput").addEventListener("change", function(event){
        var file = event.target.files[0];
         if (file) {
             var reader = new FileReader();
             reader.onload = function(e) {
                 attachedImage = e.target.result; // Store the Base64 encoded image
                 // Optionally, display a preview to the user here
                 console.log("Image attached");
                 };
             reader.readAsDataURL(file);
         }
});

// Add audio file handler
document.getElementById("audioInput").addEventListener("change", function(event) {
    var file = event.target.files[0];
    if (file) {
        var reader = new FileReader();
        reader.onload = function(e) {
            attachedAudio = e.target.result;
            console.log("Audio attached");
        };
        reader.readAsDataURL(file);
    }
});

// Modified showMessage function
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
        // Handle image
        if (message.imageBase64) {
            var img = document.createElement("img");
            img.src = message.imageBase64;
            img.style.maxWidth = "500px";
            contentSpan.appendChild(img);
            contentSpan.appendChild(document.createElement("br"));
        }

        // Handle audio
        if (message.audioBase64) {
            var audio = document.createElement("audio");
            audio.controls = true;
            audio.style.maxWidth = "500px";
            var source = document.createElement("source");
            source.src = message.audioBase64;
            source.type = "audio/wav";
            audio.appendChild(source);
            contentSpan.appendChild(audio);
            contentSpan.appendChild(document.createElement("br"));
        }

        // Handle text
        if (message.content && message.content.trim().length > 0) {
            var textPara = document.createElement("p");
            textPara.textContent = message.content;
            contentSpan.appendChild(textPara);
        }
    }

    messageElement.appendChild(senderSpan);
    messageElement.appendChild(contentSpan);
    messageArea.appendChild(messageElement);
}
