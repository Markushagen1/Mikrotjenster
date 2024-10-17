import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import {LogOut} from "lucide-react";


interface Profile {
    userId: number;
    name: string;
    age: number;
    interests: string;
    budget: number;
    occupation: string;
}

const MatchesList: React.FC = () => {
    const [matches, setMatches] = useState<Profile[]>([]);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchMatches = async () => {
            const userId = localStorage.getItem('userId');
            if (!userId) {
                alert('Ingen bruker er logget inn');
                navigate('/');
                return;
            }

            try {
                const response = await axios.get(`http://localhost:8087/api/matches/${userId}`);
                setMatches(response.data); // Nå får du en liste av UserProfile-objekter
            } catch (error) {
                console.error('Feil ved henting av matcher', error);
            }
        };
        fetchMatches();
    }, [navigate]);

    const handleLogout = () => {
        localStorage.removeItem('userId');
        alert('Du er nå logget ut!');
        navigate('/');
    };

    return (
        <div className="flex flex-col items-center min-h-screen bg-gradient-to-b from-purple-50 via-white to-purple-100 p-8">
            <h2 className="text-3xl font-bold text-purple-900 mb-8">Dine Matcher</h2>
            <div className="flex flex-col items-center space-y-4">
                {matches.length === 0 ? (
                    <p>Ingen matcher funnet.</p>
                ) : (
                    matches.map((match) => (
                        <div key={match.userId} className="relative max-w-lg w-full bg-white shadow-lg rounded-lg overflow-hidden p-8">
                            <div className="absolute top-0 right-0 w-24 h-24 bg-gradient-to-br from-purple-100 to-purple-200 rounded-bl-full opacity-20" />
                            <div className="text-center">
                                <h2 className="text-3xl font-bold text-purple-900 mb-2">{match.name}, {match.age}</h2>
                                <p className="text-lg text-gray-700 font-medium mb-4">Yrke: {match.occupation}</p>
                                <p className="text-lg text-gray-700 font-medium mb-4">Interesser: {match.interests}</p>
                                <p className="text-lg text-gray-700 font-medium mb-4">Budsjett: {match.budget} NOK</p>
                            </div>
                        </div>
                    ))
                )}
            </div>

            <button
                onClick={handleLogout}
                className="mt-6 bg-gray-600 hover:bg-gray-700 text-white py-2 px-4 rounded-lg transition-all duration-300 shadow-md flex items-center gap-2"
            >
                <LogOut className="w-5 h-5" /> Logout
            </button>
        </div>
    );
};

export default MatchesList;

