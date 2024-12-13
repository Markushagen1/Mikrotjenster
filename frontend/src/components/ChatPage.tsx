import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';

interface Message {
    senderId: number;
    receiverId: number;
    messageContent: string;
    timestamp: string;
}

const ChatPage: React.FC = () => {
    const { matchId } = useParams<{ matchId: string }>();
    const navigate = useNavigate();
    const [messages, setMessages] = useState<Message[]>([]);
    const [newMessage, setNewMessage] = useState<string>("");
    const [socket, setSocket] = useState<WebSocket | null>(null);

    // Fetch messages from the backend when the component mounts
    useEffect(() => {
        if (!matchId) {
            console.error("No matchId provided");
            navigate("/matches"); // Redirect to matches page if matchId is missing
            return;
        }

        const fetchMessages = async () => {
            try {
                const senderId = localStorage.getItem("userId");
                if (!senderId) {
                    console.error("User ID is missing");
                    return;
                }

                const response = await axios.get(`http://localhost:8084/api/messages/${senderId}/${matchId}`);
                setMessages(response.data || []); // Fallback to an empty array
            } catch (error) {
                console.error("Error fetching messages:", error);
            }
        };

        fetchMessages();
    }, [matchId, navigate]);

    // WebSocket connection setup
    useEffect(() => {
        const ws = new WebSocket("ws://localhost:8084/ws/messages");

        ws.onopen = () => {
            console.log("WebSocket connected");
        };

        ws.onmessage = (event) => {
            try {
                const receivedMessage: Message = JSON.parse(event.data);
                setMessages((prevMessages) => [...prevMessages, receivedMessage]);
            } catch (error) {
                console.error("Error parsing WebSocket message:", error);
            }
        };

        ws.onerror = (error) => {
            console.error("WebSocket error:", error);
        };

        ws.onclose = () => {
            console.log("WebSocket connection closed");
        };

        setSocket(ws);

        return () => {
            ws.close();
        };
    }, []);

    // Send a message via WebSocket
    const sendMessage = () => {
        if (!socket) {
            console.error("WebSocket connection not established");
            return;
        }

        if (!newMessage.trim()) {
            console.error("Message is empty");
            return;
        }

        const senderId = localStorage.getItem("userId");
        if (!senderId || !matchId) {
            console.error("Sender ID or match ID is missing");
            return;
        }

        const message: Message = {
            senderId: parseInt(senderId),
            receiverId: parseInt(matchId),
            messageContent: newMessage,
            timestamp: new Date().toISOString(),
        };

        try {
            socket.send(JSON.stringify(message));
            setMessages((prevMessages) => [...prevMessages, message]);
            setNewMessage("");
        } catch (error) {
            console.error("Error sending message:", error);
        }
    };

    if (!matchId) {
        return <div>Error: Match ID is not defined. Please try again.</div>;
    }

    return (
        <div className="flex flex-col items-center min-h-screen bg-gradient-to-b from-blue-50 via-white to-blue-100 p-6">
            <h1 className="text-3xl font-bold text-blue-900 mb-6">Chat with Match {matchId}</h1>

            <div className="w-full max-w-2xl flex flex-col space-y-4 bg-white p-6 rounded-lg shadow-lg">
                <div className="flex flex-col space-y-3 overflow-y-scroll max-h-96">
                    {messages.map((msg, index) => (
                        <div
                            key={index}
                            className={`p-3 rounded-lg ${
                                msg.senderId === parseInt(localStorage.getItem("userId") || "0")
                                    ? "bg-blue-100 self-end text-right"
                                    : "bg-gray-200 self-start text-left"
                            }`}
                        >
                            {msg.messageContent}
                        </div>
                    ))}
                </div>

                <div className="flex items-center space-x-3 mt-4">
                    <input
                        type="text"
                        value={newMessage}
                        onChange={(e) => setNewMessage(e.target.value)}
                        placeholder="Type a message..."
                        className="flex-grow border rounded-lg p-2"
                    />
                    <button
                        onClick={sendMessage}
                        className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600"
                    >
                        Send
                    </button>
                </div>
            </div>
        </div>
    );
};

export default ChatPage;
