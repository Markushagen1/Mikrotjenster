import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Heart, X } from 'lucide-react';

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

    useEffect(() => {
        const fetchProfiles = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/profiles');
                setProfiles(response.data);
            } catch (error) {
                console.error('Feil ved henting av profiler', error);
            }
        };
        fetchProfiles();
    }, []);

    const goToNextProfile = () => {
        setCurrentProfileIndex((prevIndex) => (prevIndex + 1) % profiles.length);
    };

    const handleLike = async (likedUserId: number) => {
        try {
            await axios.post('http://localhost:8081/api/match/like', {
                likerId: 1, // Hardkodet for testing; oppdater til innlogget bruker-ID
                likedUserId,
            });
            setStatusMessage(`Du likte ${profiles[currentProfileIndex].name}!`);
            goToNextProfile();
        } catch (error) {
            if (axios.isAxiosError(error)) {
                if (error.response && error.response.status === 409) {
                    setStatusMessage(`Du har allerede likt ${profiles[currentProfileIndex].name}.`);
                } else {
                    console.error('Feil ved å like profilen:', error.message);
                }
            } else {
                console.error('En ukjent feil oppstod:', error);
            }
        } finally {
            clearStatusMessage();
        }
    };

    const handleDislike = () => {
        setStatusMessage(`Du likte ikke ${profiles[currentProfileIndex].name}.`);
        goToNextProfile();
        clearStatusMessage();
    };

    const clearStatusMessage = () => {
        setTimeout(() => {
            setStatusMessage(null);
        }, 2000); // Meldingen forsvinner etter 2 sekunder
    };

    const currentProfile = profiles[currentProfileIndex];

    if (!currentProfile) return <div>Laster profiler...</div>;

    return (
        <div className="flex flex-col items-center min-h-screen bg-gradient-to-b from-purple-50 via-white to-purple-100 p-8">
            <div className="relative max-w-lg w-full bg-white shadow-lg rounded-lg overflow-hidden p-8">
                <div className="absolute top-0 right-0 w-24 h-24 bg-gradient-to-br from-purple-100 to-purple-200 rounded-bl-full opacity-20" />
                <div className="text-center">
                    <h2 className="text-3xl font-bold text-purple-900 mb-2">{currentProfile.name}, {currentProfile.age}</h2>
                    <p className="text-lg text-gray-700 font-medium mb-4">Yrke: {currentProfile.occupation}</p>
                    <p className="text-lg text-gray-700 font-medium mb-4">Interesser: {currentProfile.interests}</p>
                    <p className="text-lg text-gray-700 font-medium mb-4">Budsjett: {currentProfile.budget} NOK</p>
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

            {/* Statusmelding */}
            {statusMessage && (
                <div className="fixed bottom-8 bg-indigo-600 text-white py-2 px-4 rounded shadow-lg transition-opacity duration-300 ease-in-out">
                    {statusMessage}
                </div>
            )}
        </div>
    );
};

export default MatchPage;




