import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

interface Profile {
    userId: number;
    name: string;
    age: number;
    interests: string;
    budget: number;
    occupation: string;
}

const MatchPage: React.FC = () => {
    const [profiles, setProfiles] = useState<Profile[]>([]);
    const [currentProfileIndex, setCurrentProfileIndex] = useState(0);
    const [statusMessage, setStatusMessage] = useState<string | null>(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchProfiles = async () => {
            try {
                const response = await axios.get("http://localhost:8080/api/profiles", {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem("token")}`,
                    },
                });
                setProfiles(response.data);
            } catch (error) {
                console.error("Error fetching profiles", error);
            }
        };
        fetchProfiles();
    }, []);

    const handleLike = async (likedUserId: number) => {
        try {
            await axios.post(
                'http://localhost:8081/api/match/like',
                { likedUserId }, // Kun likedUserId
                {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem("token")}`,
                    },
                }
            );
            setStatusMessage(`You liked ${profiles[currentProfileIndex].name}!`);
            goToNextProfile();
        } catch (error: any) {
            if (error.response?.status === 409) {
                setStatusMessage("You cannot like yourself!");
            } else {
                setStatusMessage("An error occurred.");
            }
        } finally {
            setTimeout(() => setStatusMessage(null), 2000);
        }
    };
    const goToNextProfile = () => {
        setCurrentProfileIndex((prevIndex) => (prevIndex + 1) % profiles.length);
    };

    const currentProfile = profiles[currentProfileIndex];

    return (
        <div className="flex flex-col items-center min-h-screen bg-gradient-to-b from-purple-50 via-white to-purple-100 p-8">
            {currentProfile ? (
                <div className="bg-white shadow-lg rounded-lg p-8">
                    <h3 className="text-2xl font-bold text-purple-900">{currentProfile.name}</h3>
                    <p className="text-gray-700">{currentProfile.age} years old</p>
                    <p className="text-gray-700">Interests: {currentProfile.interests}</p>
                </div>
            ) : (
                <p>Loading profiles...</p>
            )}
            <div className="mt-4 flex space-x-4">
                <button
                    onClick={() => handleLike(currentProfile.userId)}
                    className="bg-green-500 text-white px-4 py-2 rounded-lg hover:bg-green-600 transition-all"
                >
                    Like
                </button>
                <button
                    onClick={goToNextProfile}
                    className="bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600 transition-all"
                >
                    Dislike
                </button>
            </div>
            <div className="mt-6 flex gap-4">
                <button
                    onClick={() => navigate("/matches")}
                    className="bg-blue-500 hover:bg-blue-600 text-white py-2 px-4 rounded-lg transition-all"
                >
                    View Matches
                </button>
                <button
                    onClick={() => {
                        localStorage.removeItem("userId");
                        localStorage.removeItem("token");
                        alert("Logged out");
                        navigate("/login");
                    }}
                    className="bg-gray-600 hover:bg-gray-700 text-white py-2 px-4 rounded-lg"
                >
                    Log Out
                </button>
            </div>
            {statusMessage && (
                <p className="fixed bottom-4 text-white bg-purple-600 px-4 py-2 rounded-lg shadow-md">
                    {statusMessage}
                </p>
            )}
        </div>
    );
};

export default MatchPage;
