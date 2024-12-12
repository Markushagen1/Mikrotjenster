import React, { useState, useEffect } from 'react';
import axios from 'axios';
import {Heart, LogOut, X} from 'lucide-react';
import { useNavigate } from 'react-router-dom';

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
                const response = await axios.get('http://localhost:8080/api/profiles');
                setProfiles(response.data);
            } catch (error) {
                console.error('Error fetching profiles', error);
            }
        };
        fetchProfiles();
    }, []);

    const goToNextProfile = () => {
        setCurrentProfileIndex((prevIndex) => (prevIndex + 1) % profiles.length);
    };

    const handleLike = async (likedUserId: number) => {
        const likerId = localStorage.getItem("userId");
        if (!likerId) {
            alert('No user logged in');
            return;
        }

        try {
            await axios.post('http://localhost:8081/api/match/like', {
                likerId: parseInt(likerId), // Konverter til tall
                likedUserId,
            });
            setStatusMessage(`You liked ${profiles[currentProfileIndex].name}!`);
            goToNextProfile();
        } catch (error) {
            if (axios.isAxiosError(error) && error.response?.status === 409) {
                setStatusMessage(`You have already liked ${profiles[currentProfileIndex].name}.`);
            } else {
                console.error('Error liking profile:', error);
            }
        } finally {
            clearStatusMessage();
        }
    };



    const handleDislike = () => {
        setStatusMessage(`You disliked ${profiles[currentProfileIndex].name}.`);
        goToNextProfile();
        clearStatusMessage();
    };

    const clearStatusMessage = () => {
        setTimeout(() => {
            setStatusMessage(null);
        }, 2000);
    };

    const handleLogout = () => {
        localStorage.removeItem('userId');
        alert('You are now logged out!');
        navigate('/');
    };

    const currentProfile = profiles[currentProfileIndex];

    if (!currentProfile) return <div>Loading profiles...</div>;

    return (
        <div className="flex flex-col items-center min-h-screen bg-gradient-to-b from-purple-50 via-white to-purple-100 p-8">
            <div className="relative max-w-lg w-full bg-white shadow-lg rounded-lg overflow-hidden p-8">
                <div className="absolute top-0 right-0 w-24 h-24 bg-gradient-to-br from-purple-100 to-purple-200 rounded-bl-full opacity-20" />
                <div className="text-center">
                    <h2 className="text-3xl font-bold text-purple-900 mb-2">{currentProfile.name}, {currentProfile.age}</h2>
                    <p className="text-lg text-gray-700 font-medium mb-4">Occupation: {currentProfile.occupation}</p>
                    <p className="text-lg text-gray-700 font-medium mb-4">Interests: {currentProfile.interests}</p>
                    <p className="text-lg text-gray-700 font-medium mb-4">Budget: {currentProfile.budget} NOK</p>
                </div>
                <div className="flex justify-around mt-8">
                    <button
                        onClick={handleDislike}
                        className="bg-red-500 text-white p-4 rounded-full shadow-lg transform hover:scale-110 transition-transform duration-200"
                    >
                        <X className="w-8 h-8" />
                    </button>
                    <button
                        onClick={() => handleLike(currentProfile.userId)}
                        className="bg-green-500 text-white p-4 rounded-full shadow-lg transform hover:scale-110 transition-transform duration-200"
                    >
                        <Heart className="w-8 h-8" />
                    </button>
                </div>
            </div>

            {statusMessage && (
                <div className="fixed bottom-8 bg-indigo-600 text-white py-2 px-4 rounded shadow-lg transition-opacity duration-300 ease-in-out">
                    {statusMessage}
                </div>
            )}

            <div className="mt-6 flex gap-4">
                <button
                    onClick={handleLogout}
                    className="bg-gray-600 hover:bg-gray-700 text-white py-2 px-4 rounded-lg transition-all duration-300 shadow-md flex items-center gap-2"
                >
                    <LogOut className="w-5 h-5" /> Logout
                </button>
                <button
                    onClick={() => navigate('/matches')}
                    className="bg-blue-600 hover:bg-blue-700 text-white py-2 px-4 rounded-lg transition-all duration-300 shadow-md"
                >
                    View Matches
                </button>
            </div>
        </div>
    );
};

export default MatchPage;






