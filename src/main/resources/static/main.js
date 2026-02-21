var stompClient = null;
let currentUser = null;

async function handleAuth(type) {
    const usernameInput = document.querySelector('#username-input').value.trim();
    const passwordInput = document.querySelector('#password-input').value.trim();

    if (!usernameInput || !passwordInput) {
        alert("Please enter both username and password");
        return;
    }

    const endpoint = type === 'login' ? '/api/login' : '/api/register';

    try {
        const response = await fetch(endpoint, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username: usernameInput, password: passwordInput })
        });

        const data = await response.json();

        if (response.ok) {
            currentUser = data.username;
            document.querySelector('#login-page').style.display = 'none';
            document.querySelector('#chat-page').style.display = 'flex';
            document.querySelector('#welcome-msg').textContent = "Welcome, " + currentUser;
            const savedColor = localStorage.getItem('chatBgColor');
            if (savedColor) {
                changeChatColor(savedColor);
                document.querySelector('#bg-color-picker').value = savedColor;
            }
            await loadHistory();
            connect();
        } else {
            alert("Error: " + (data.error || "Authentication failed"));
        }
    } catch (error) {
        alert("Server connection failed. Is Spring Boot running?");
    }
}

function connect() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function() {
        stompClient.subscribe('/topic/public', onMessageReceived);
        stompClient.send("/app/chat.addUser", {}, JSON.stringify({ sender: currentUser, type: 'JOIN' }));
    }, (err) => { console.error(err); });
}

async function loadHistory() {
    const response = await fetch('/api/messages');
    if (response.ok) {
        const messages = await response.json();
        const messageArea = document.querySelector('#messageArea');
        messageArea.innerHTML = '';
        messages.forEach(msg => displayMessage(msg.sender, msg.content, 'CHAT'));
    }
}

function sendMessage() {
    const messageInput = document.querySelector('#message');
    const content = messageInput.value.trim();
    if (content && stompClient) {
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify({
            sender: currentUser,
            content: content,
            type: 'CHAT'
        }));
        messageInput.value = '';
    }
}

function onMessageReceived(payload) {
    const message = JSON.parse(payload.body);
    displayMessage(message.sender, message.content, message.type);
}

function displayMessage(sender, content, type) {
    const messageArea = document.querySelector('#messageArea');
    const messageElement = document.createElement('li');

    if (type === 'CHAT') {
        messageElement.classList.add('message');
        if (sender === currentUser) {
            messageElement.classList.add('my-message');
            messageElement.innerHTML = `<span>${content}</span>`;
        } else {
            messageElement.classList.add('other-message');
            messageElement.innerHTML = `<strong>${sender}:</strong> <br> <span>${content}</span>`;
        }
    } else {
        messageElement.classList.add('event-message');
        messageElement.textContent = `${sender} ${type === 'JOIN' ? 'joined' : 'left'} the chat`;
    }

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

// Function to change the chat background color
function changeChatColor(color) {
    const messageArea = document.querySelector('#messageArea');
    messageArea.style.backgroundColor = color;
    localStorage.setItem('chatBgColor', color);
}