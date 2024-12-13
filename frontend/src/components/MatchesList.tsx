import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

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
                setMatches(response.data);
            } catch (error) {
                console.error('Feil ved henting av matcher', error);
            }
        };
        fetchMatches();
    }, [navigate]);

    return (
        <div className="flex flex-col items-center min-h-screen bg-gradient-to-b from-purple-50 via-white to-purple-100 p-8">
            <h2 className="text-3xl font-bold text-purple-900 mb-8">Dine Matcher</h2>
            <div className="space-y-4">
                {matches.length === 0 ? (
                    <p className="text-lg text-gray-700">Ingen matcher funnet.</p>
                ) : (
                    matches.map((match) => (
                        <div
                            key={match.userId}
                            className="bg-white p-4 rounded-lg shadow-md hover:shadow-lg transition-shadow cursor-pointer"
                            onClick={() => navigate(`/chat/${match.userId}`)}
                        >
                            <h3 className="text-xl font-bold text-purple-800">{match.name}</h3>
                            <p className="text-gray-600">{match.age} år</p>
                            <p className="text-gray-600">Interesser: {match.interests}</p>
                        </div>
                    ))
                )}
            </div>
        </div>
    );
};

export default MatchesList;
